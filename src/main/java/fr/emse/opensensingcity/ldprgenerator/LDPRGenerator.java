package fr.emse.opensensingcity.ldprgenerator;

import fr.emse.opensensingcity.configuration.BasicContainer;
import fr.emse.opensensingcity.configuration.Container;
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
import java.util.Map;

/**
 * Created by noor on 15/06/17.
 */
public class LDPRGenerator {
    static String baseURI = "http://localhost:8080/";

    public static void sendRequest(List<Container> containers) throws IOException {
        for (Container ccontainer:containers){


            HttpClient client = HttpClientBuilder.create().build();
            BasicContainer container = (BasicContainer) ccontainer;
            HttpPost request = getResourceRequest(container);
            HttpResponse response = null;
            response = client.execute(request);
            System.out.println(response);
            //create corresponding LDPRs here*/
        }
    }

    public static HttpPost getResourceRequest(BasicContainer container){
        HttpPost httpPost = new HttpPost(baseURI);

        httpPost.addHeader("Content-Type","text/turtle");
        httpPost.addHeader("Link","<http://www.w3.org/ns/ldp#Resource>; rel='type'");
        httpPost.addHeader("Link","<http://www.w3.org/ns/ldp#RDFSource>; rel='type'");
        httpPost.addHeader("Link","<http://www.w3.org/ns/ldp#BasicContainer>; rel=\"type\"");

        System.out.println(container.getIRI().replace(baseURI,""));
        httpPost.addHeader("Slug","test1");
        System.out.println("creating "+container.getIRI());

        Model model = container.generateGraph();
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
}
