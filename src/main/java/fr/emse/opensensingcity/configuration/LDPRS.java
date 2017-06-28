package fr.emse.opensensingcity.configuration;

import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.vocabulary.FOAF;

/**
 * Created by bakerally on 5/29/17.
 */
public class LDPRS extends LDPR {

    Model graph;
    RelatedResource relatedResource;

    public LDPRS(String iri) {
        super(iri);
        graph = ModelFactory.createDefaultModel();
    }


    public Model generateGraph(){
        org.apache.jena.rdf.model.Resource container = graph.createResource("");

        //create a resource for the related resource
        org.apache.jena.rdf.model.Resource rResource = graph.createResource(relatedResource.getIRI());


        graph.add(relatedResource.getFinalGraph());
        graph.createResource("").addProperty(FOAF.primaryTopic,rResource);

        return graph;
    }

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
