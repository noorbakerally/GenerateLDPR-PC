package fr.emse.opensensingcity.configuration;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

/**
 * Created by bakerally on 5/29/17.
 */
public abstract class LDPResource {
    String iri;
    Model graph;

    public LDPResource() {
        graph = ModelFactory.createDefaultModel();
    }

    public String getIRI() {
        return iri;
    }

    public void setIRI(String iri) {
        this.iri = iri;
    }
}
