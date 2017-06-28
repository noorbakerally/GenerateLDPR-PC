package fr.emse.opensensingcity.configuration;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

/**
 * Created by bakerally on 5/29/17.
 */
public abstract class LDPRS extends LDPR {

    Model graph;
    RelatedResource relatedResource;

    public LDPRS(String iri) {
        super(iri);
        graph = ModelFactory.createDefaultModel();
    }


    public abstract Model generateGraph();

    public Model getGraph() {
        return graph;
    }

    public void setGraph(Model graph) {
        this.graph = graph;
    }

    public RelatedResource getRelatedResource() {
        return relatedResource;
    }

    public void setRelatedResource(RelatedResource relatedResource) {
        this.relatedResource = relatedResource;
    }
}
