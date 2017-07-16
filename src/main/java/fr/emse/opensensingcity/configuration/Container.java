package fr.emse.opensensingcity.configuration;

import fr.emse.opensensingcity.slugtemplate.IRIGenerator;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bakerally on 5/29/17.
 */
public class Container extends LDPRS {

    Map<String,RDFSourceMap> rdfSourceMaps = new HashMap<String, RDFSourceMap>();
    Map<String,ContainerMap> containerMaps = new HashMap<String, ContainerMap>();
    Map<String,NonRDFSourceMap> nonrdfSourceMaps = new HashMap<String, NonRDFSourceMap>();

    public Container(String containerIRI) {
        super(containerIRI);
        graph = ModelFactory.createDefaultModel();
    }

    public void addResourceMap(RDFSourceMap rdfSourceMap){
        rdfSourceMaps.put(rdfSourceMap.getIRI(),rdfSourceMap);
    }

    @Override
    public Model generateGraph() {
        return super.generateGraph();
    }

    public Map<String, RDFSourceMap> getRdfSourceMaps() {
        return rdfSourceMaps;
    }

    public void setRdfSourceMaps(Map<String, RDFSourceMap> rdfSourceMaps) {
        this.rdfSourceMaps = rdfSourceMaps;
    }

    public Map<String, ContainerMap> getContainerMaps() {
        return containerMaps;
    }

    public void setContainerMaps(Map<String, ContainerMap> containerMaps) {
        this.containerMaps = containerMaps;
    }

    public void processRDFSourceMaps() {
        for (Map.Entry <String,RDFSourceMap> rdfSourceMapEntry:rdfSourceMaps.entrySet()){
            RDFSourceMap rdfSourceMap = rdfSourceMapEntry.getValue();
            rdfSourceMap.setContainer(this);
            rdfSourceMap.generateResources();
        }
    }

    public void processNonRDFSourceMaps() {
        for (Map.Entry <String,NonRDFSourceMap> nonRdfSourceMapEntry:nonrdfSourceMaps.entrySet()){
            NonRDFSourceMap nonRdfSourceMap = nonRdfSourceMapEntry.getValue();
            nonRdfSourceMap.setContainer(this);
            nonRdfSourceMap.generateResources();
        }
    }

    public void sendRequestForRDFSourceMaps() throws IOException {
        for (Map.Entry <String,RDFSourceMap> rdfSourceMapEntry:rdfSourceMaps.entrySet()){
            RDFSourceMap rdfSourceMap = rdfSourceMapEntry.getValue();
            for (LDPR ldprs:rdfSourceMap.getResources()){
                ldprs = (LDPRS)ldprs;
                ldprs.setContainer(this);
                ((LDPRS)ldprs).sendRequest();
            }
        }
    }

    public void sendRequestForNonRDFSourceMaps() throws IOException {
        for (Map.Entry <String,NonRDFSourceMap> nonRdfSourceMapEntry:nonrdfSourceMaps.entrySet()){
            NonRDFSourceMap nonRdfSourceMap = nonRdfSourceMapEntry.getValue();
            for (LDPR ldpnr:nonRdfSourceMap.getResources()){
                ldpnr = (LDPNR)ldpnr;
                ldpnr.setContainer(this);
                ((LDPNR)ldpnr).sendRequest();
            }
        }
    }

    public HttpPost getResourceRequest(){
        String baseIRI = Global.baseURI;
        if (container != null){
            baseIRI = container.getIRI();
        }
        HttpPost httpPost = new HttpPost(baseIRI);

        httpPost.addHeader("Content-Type","text/turtle");

        httpPost.addHeader("Link","<http://www.w3.org/ns/ldp#BasicContainer>; rel=\"type\"");

        httpPost.addHeader("Slug",getSlug());

        Model model = generateGraph();
        StringWriter out = new StringWriter();
        model.write(out,"TTL");
        try {
            httpPost.setEntity(new StringEntity(out.toString()));
            //System.out.println(out.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return httpPost;
    }


    public void processContainerMaps() throws IOException {

        for (Map.Entry <String,ContainerMap> containerMapEntry:containerMaps.entrySet()){
            ContainerMap containerMap = containerMapEntry.getValue();
            containerMap.setContainer(this);

            if (containerMap.getResourceMaps().size() > 0){
                containerMap.generateResources();
                for (LDPR container:containerMap.getResources()){
                    ((Container)container).setContainer(this);
                    ((Container)container).sendRequest();
                    ((Container)container).processRDFSourceMaps();
                    ((Container)container).processNonRDFSourceMaps();
                    ((Container)container).sendRequestForRDFSourceMaps();
                    ((Container)container).sendRequestForNonRDFSourceMaps();
                    ((Container)container).processContainerMaps();
                }
            } else {
                //zombie container
                Container c = null;

                c = new BasicContainer("");
                String uri = IRIGenerator.getSlug(c,containerMap.getSlugTemplate());
                c.setSlug(uri);
                c.setContainer(this);

                c.setRdfSourceMaps(containerMap.getRdfSourceMaps());
                c.setContainerMaps(containerMap.getContainerMaps());

                c.sendRequest();
                c.processContainerMaps();
                c.processRDFSourceMaps();

            }
        }
    }

    public Map<String, NonRDFSourceMap> getNonrdfSourceMaps() {
        return nonrdfSourceMaps;
    }

    public void setNonrdfSourceMaps(Map<String, NonRDFSourceMap> nonrdfSourceMaps) {
        this.nonrdfSourceMaps = nonrdfSourceMaps;
    }

    public void addNonRDFSourceMap(NonRDFSourceMap nonRDFSourceMap){
        nonRDFSourceMap.setContainer(this);
        nonrdfSourceMaps.put(nonRDFSourceMap.getIRI(),nonRDFSourceMap);
    }


}
