package fr.emse.opensensingcity.configuration;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bakerally on 5/29/17.
 */
public abstract class Container extends LDPRS {

    List<Member> members;
    public Container(String containerIRI) {
        setIRI(containerIRI);
        members = new ArrayList<Member>();
        graph = ModelFactory.createDefaultModel();
    }

    public void  addMember(Member member){
        members.add(member);
        member.addContainer(this);
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }


}
