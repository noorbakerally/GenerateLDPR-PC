package fr.emse.opensensingcity.LDP;

import fr.emse.opensensingcity.configuration.*;
import org.apache.jena.rdf.model.ModelFactory;
import java.io.IOException;
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

    public void processContainerMaps() throws IOException {
        for (Map.Entry <String,ContainerMap> containerMapEntry:containerMaps.entrySet()){
            ContainerMap containerMap = containerMapEntry.getValue();
            containerMap.generateResources();
        }
    }
    public void processRDFSourceMaps() throws IOException {
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

    //assign parent ContainerMap SourceMaps to Container
    // generated from the current ContainerMap
    public void setSourceMaps(ContainerMap sourceMaps) {
        //copy ContainerMaps
        for (Map.Entry <String,ContainerMap> containerMapEntry:sourceMaps.getContainerMaps().entrySet()){
            ContainerMap newContainerMap = (ContainerMap) containerMapEntry.getValue().copy();
            this.addContainerMap(newContainerMap);
        }

        //this.setRdfSourceMaps(sourceMaps.getRdfSourceMaps());
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

    private void addContainerMap(ContainerMap newContainerMap) {
        newContainerMap.setContainer(this);
        containerMaps.put(newContainerMap.getIRI(),newContainerMap);
    }

    public void processSourceMaps() throws IOException {
        processContainerMaps();
        //processRDFSourceMaps();
        //processNonRDFSourceMaps();

    }



    /*General Methods*/
    public void addNonRDFSourceMap(NonRDFSourceMap nonRDFSourceMap){
        nonRDFSourceMap.setContainer(this);
        nonrdfSourceMaps.put(nonRDFSourceMap.getIRI(),nonRDFSourceMap);
    }
    public Map<String, NonRDFSourceMap> getNonrdfSourceMaps() {
        return nonrdfSourceMaps;
    }
    public void setNonrdfSourceMaps(Map<String, NonRDFSourceMap> nonrdfSourceMaps) {
        this.nonrdfSourceMaps = nonrdfSourceMaps;
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
}
