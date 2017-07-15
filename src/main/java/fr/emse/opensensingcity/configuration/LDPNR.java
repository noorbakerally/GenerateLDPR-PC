package fr.emse.opensensingcity.configuration;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

/**
 * Created by noor on 28/06/17.
 */
public class LDPNR extends LDPR {

    public LDPNR(String iri) {
        super(iri);
    }

    public HttpPost getResourceRequest(){
        String baseURI = container.getIRI();
        HttpPost httpPost = new HttpPost(baseURI);

        httpPost.addHeader("Content-Type","text/turtle");
        httpPost.addHeader("Link","<http://www.w3.org/ns/ldp#Resource>; rel=\"type\"");



        httpPost.addHeader("Slug",getSlug());
        Model model = ModelFactory.createDefaultModel();
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
        System.out.println("LDPNR.java Request:"+request+" Reply:"+response);
        String location = response.getHeaders("Location")[0].getValue();
        setIRI(location);
    }
}
