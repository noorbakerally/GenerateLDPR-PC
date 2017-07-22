package fr.emse.opensensingcity.ldprgenerator;

import fr.emse.opensensingcity.LDP.RDFSource;
import fr.emse.opensensingcity.LDP.Resource;
import fr.emse.opensensingcity.LDP.Container;
import fr.emse.opensensingcity.configuration.Global;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.jena.rdf.model.Model;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by noor on 15/06/17.
 */
public class LDPRGenerator {
    static String baseURI = "http://localhost:8080/";

    public void sendRequest(Resource resource) throws IOException {

        HttpClient client = HttpClientBuilder.create().build();

        resource = (RDFSource)resource;
        HttpPost request = null;
        if (resource instanceof RDFSource){
            request = getResourceRequest((RDFSource)resource);
        } else if (resource instanceof Container){
            request = getResourceRequest((Container)resource);
        }

        HttpResponse response = null;
        response = client.execute(request);
        System.out.println("LDPRS.java Request:"+request+" Reply:"+response);
        String location = response.getHeaders("Location")[0].getValue();
        resource.setIRI(location);
    }

    public HttpPost getResourceRequest(Resource resource){
        Container container= resource.getContainer();
        String baseIRI = Global.baseURI;
        if (container != null){
            baseIRI = container.getIRI();
        }
        HttpPost httpPost = new HttpPost(baseURI);
        httpPost.addHeader("Link","<http://www.w3.org/ns/ldp#Resource>; rel=\"type\"");
        if (resource instanceof RDFSource){
            httpPost.addHeader("Content-Type","text/turtle");
            httpPost.addHeader("Link","<http://www.w3.org/ns/ldp#RDFSource>; rel=\"type\"");

            //generate Graph for RDFSource
            Model model = ((RDFSource)resource).getGraph();
            StringWriter out = new StringWriter();
            model.write(out, "TTL");
            try {
                httpPost.setEntity(new StringEntity(out.toString()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        if (resource instanceof Container){
            httpPost.addHeader("Link","<http://www.w3.org/ns/ldp#BasicContainer>; rel=\"type\"");
        }
        httpPost.addHeader("Slug",resource.getSlug());
        return httpPost;
    }
}
