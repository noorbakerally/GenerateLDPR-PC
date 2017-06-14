package fr.emse.opensensingcity.configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bakerally on 5/29/17.
 */
public abstract class Container extends Resource {
    List<Member> members;

    public Container(String containerIRI) {
        members = new ArrayList<Member>();
    }

    public abstract void  addMember(Member member);

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }
}
