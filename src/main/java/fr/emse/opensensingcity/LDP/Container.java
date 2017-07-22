package fr.emse.opensensingcity.LDP;

import fr.emse.opensensingcity.configuration.*;
import fr.emse.opensensingcity.slugtemplate.IRIGenerator;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bakerally on 5/29/17.
 */
public class Container extends RDFSource {

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


    public void processContainerMaps() throws IOException {

        for (Map.Entry <String,ContainerMap> containerMapEntry:containerMaps.entrySet()){
            ContainerMap containerMap = containerMapEntry.getValue();
            containerMap.setContainer(this);

            if (containerMap.getResourceMaps().size() > 0){
                containerMap.generateResources();
                for (Resource container:containerMap.getResources()){
                    ((Container)container).setContainer(this);

                    ((Container)container).processRDFSourceMaps();
                    ((Container)container).processNonRDFSourceMaps();

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


    public void setSourceMaps(ContainerMap sourceMaps) {

        this.setRdfSourceMaps(sourceMaps.getRdfSourceMaps());

        //this.setContainerMaps(sourceMaps.getContainerMaps());
        //this.setNonrdfSourceMaps(sourceMaps.getNonRdfSourceMaps());

        /*c.setRdfSourceMaps(rdfSourceMaps);
        c.setContainerMaps(containerMaps);
        c.processRDFSourceMaps();

        //adding nonRDFSourceMaps
        for (Map.Entry <String,NonRDFSourceMap> cNonRDFSourceMap:nonrdfSourceMaps.entrySet()){
            NonRDFSourceMap nonRDFSourceMap = cNonRDFSourceMap.getValue();
            NonRDFSourceMap newNonRDFSourceMap = (NonRDFSourceMap)nonRDFSourceMap.copy();
            c.addNonRDFSourceMap(newNonRDFSourceMap);
        }*/


    }

    public void processSourceMaps() {
        processRDFSourceMaps();
    }
}
