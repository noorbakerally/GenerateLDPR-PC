package fr.emse.opensensingcity.configuration;

/**
 * Created by noor on 28/06/17.
 */
public abstract class LDPR extends Resource {
    String relatedResourceIRI;

    public LDPR(String iri) {
        super(iri);
    }

    public String getRelatedResourceIRI() {
        return relatedResourceIRI;
    }

    public void setRelatedResourceIRI(String relatedResourceIRI) {
        this.relatedResourceIRI = relatedResourceIRI;
    }
}
