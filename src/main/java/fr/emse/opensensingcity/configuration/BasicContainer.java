package fr.emse.opensensingcity.configuration;

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
}
