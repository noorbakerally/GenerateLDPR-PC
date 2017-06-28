package fr.emse.opensensingcity.configuration;

import org.apache.jena.rdf.model.*;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.RDF;

/**
 * Created by bakerally on 5/29/17.
 */
public class BasicContainer extends Container {

    public BasicContainer(String containerIRI) {
        super(containerIRI);
    }


    @Override
    public Model generateGraph() {
        Resource container = graph.createResource("");

        //create a resource for the related resource
        Resource rResource = graph.createResource(relatedResource.getIRI());


        graph.add(relatedResource.getGraph());
        graph.createResource("").addProperty(FOAF.primaryTopic,rResource);

        return graph;
    }
}
