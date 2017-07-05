package fr.emse.opensensingcity.configuration;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.vocabulary.FOAF;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

/**
 * Created by bakerally on 5/29/17.
 */
public class LDPRS extends LDPR {

    Model graph;


    public LDPRS(String iri) {
        super(iri);
        setSlug(iri);
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

    public HttpPost getResourceRequest(){
        String baseURI = container.getIRI();
        System.out.println("LDPRS.java baseIRI:"+baseURI);
        HttpPost httpPost = new HttpPost(baseURI);

        httpPost.addHeader("Content-Type","text/turtle");
        httpPost.addHeader("Link","<http://www.w3.org/ns/ldp#Resource>; rel=\"type\"");
        httpPost.addHeader("Link","<http://www.w3.org/ns/ldp#RDFSource>; rel=\"type\"");


        httpPost.addHeader("Slug",getSlug());
        Model model = generateGraph();
        StringWriter out = new StringWriter();
        model.write(out, "TTL");
        try {
            httpPost.setEntity(new StringEntity(out.toString()));
            //System.out.println(out.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return httpPost;
    }

    public void sendRequest() throws IOException {

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = getResourceRequest();
        HttpResponse response = null;
        response = client.execute(request);
        System.out.println("LDPRS.java Request:"+request+" Reply:"+response);
        String location = response.getHeaders("Location")[0].getValue();
        setIRI(location);
    }

}
