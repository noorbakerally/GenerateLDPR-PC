package fr.emse.opensensingcity.configuration;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

/**
 * Created by bakerally on 5/29/17.
 */
public abstract  class Resource {
    String iri;
    Model graph;

    public Resource() {
        graph = ModelFactory.createDefaultModel();
    }
}
