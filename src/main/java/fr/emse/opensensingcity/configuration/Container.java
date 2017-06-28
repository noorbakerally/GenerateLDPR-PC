package fr.emse.opensensingcity.configuration;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bakerally on 5/29/17.
 */
public abstract class Container extends LDPRS {

    Map<String,RDFSourceMap> rdfSourceMaps = new HashMap<String, RDFSourceMap>();
    Map<String,ContainerMap> containerMaps = new HashMap<String, ContainerMap>();

    public Container(String containerIRI) {
        super(containerIRI);
        graph = ModelFactory.createDefaultModel();
    }

    public void addResourceMap(RDFSourceMap rdfSourceMap){
        rdfSourceMaps.put(rdfSourceMap.getIRI(),rdfSourceMap);
    }








}
