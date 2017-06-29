package fr.emse.opensensingcity.configuration;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

/**
 * Created by noor on 28/06/17.
 */
public abstract class LDPR extends Resource {
    String relatedResourceIRI;
    String slug;
    Container container;
    public LDPR(String iri) {
        super(iri);
    }

    public String getRelatedResourceIRI() {
        return relatedResourceIRI;
    }

    public void setRelatedResourceIRI(String relatedResourceIRI) {
        this.relatedResourceIRI = relatedResourceIRI;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    @Override
    public String getIRI(){
        String baseURI = Global.baseURI;
        if (container != null){
            iri = container.getIRI()+iri+"";
        } else {
            iri = Global.baseURI+iri+"";
        }
        return iri;
    }

}
