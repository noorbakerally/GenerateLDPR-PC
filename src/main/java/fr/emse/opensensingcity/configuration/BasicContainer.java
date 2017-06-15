package fr.emse.opensensingcity.configuration;

import org.apache.jena.rdf.model.*;

/**
 * Created by bakerally on 5/29/17.
 */
public class BasicContainer extends Container {

    public BasicContainer(String containerIRI) {
        super(containerIRI);
    }

    public void addMember(Member member) {
        members.add(member);
    }

    @Override
    public Model generateGraph() {
        Resource container =model.createResource("");
        for (Member member:members){
            Resource memberResource = ResourceFactory.createResource(member.getIRI());
            container.addProperty(Global.getLDPContains(),memberResource);
        }
        return model;
    }
}
