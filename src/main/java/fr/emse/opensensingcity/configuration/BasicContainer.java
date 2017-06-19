package fr.emse.opensensingcity.configuration;

import org.apache.jena.rdf.model.*;
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
        Resource container =graph.createResource("");
        /*for (Member member:members){
            Resource memberResource = ResourceFactory.createResource(member.getIRI());
            container.addProperty(Global.getLDPContains(),memberResource);
        }*/
        container.addProperty(RDF.type,Global.getLDPBC());
        return graph;
    }
}
