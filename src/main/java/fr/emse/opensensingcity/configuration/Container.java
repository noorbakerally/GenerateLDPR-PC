package fr.emse.opensensingcity.configuration;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

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

    public HttpPost getResourceRequest(){
        System.out.println("Container.java test");
        String baseIRI = Global.baseURI;
        if (container != null){
            baseIRI = container.getIRI();
        }
        HttpPost httpPost = new HttpPost(baseIRI);

        httpPost.addHeader("Content-Type","text/turtle");
        httpPost.addHeader("Link","<http://www.w3.org/ns/ldp#Resource>; rel='type'");
        httpPost.addHeader("Link","<http://www.w3.org/ns/ldp#RDFSource>; rel='type'");
        httpPost.addHeader("Link","<http://www.w3.org/ns/ldp#BasicContainer>; rel='type'");

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

    public HttpPost getResourceRequests() {
        HttpPost httpPost = new HttpPost(Global.baseURI);

        String content = "@prefix dcterms: <http://purl.org/dc/terms/> . @prefix ex: <http://example.com/> ." +
                "@prefix ldp:<http://www.w3.org/ns/ldp#> .";
        content = content + "<> dcterms:title 'Photos of Alice' .";
        content = content + "<> ex:shows ex:Alice .";

        httpPost.addHeader("Content-Type","text/turtle");
        httpPost.addHeader("Link","<http://www.w3.org/ns/ldp#Resource>; rel='type'");
        httpPost.addHeader("Link","<http://www.w3.org/ns/ldp#RDFSource>; rel='type'");
        httpPost.addHeader("Link","<http://www.w3.org/ns/ldp#BasicContainer>; rel='type'");
        httpPost.addHeader("Slug","tests");

        try {
            httpPost.setEntity(new StringEntity(content));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return httpPost;
    }
}
