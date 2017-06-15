package fr.emse.opensensingcity.ldprgenerator;

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
import java.util.Map;

/**
 * Created by noor on 15/06/17.
 */
public class LDPRGenerator {
    public static void sendRequest(Map<String,Container> containerMap){
        for (Map.Entry<String,Container> container:containerMap.entrySet()){
            //System.out.println(container.getKey() + " " + container.getValue());
            Model model = container.getValue().generateGraph();

            StringWriter out = new StringWriter();
            model.write(out, "TTL");

            HttpClient client = HttpClientBuilder.create().build();

            //String content = out.toString();
            String content = "<> a <http://example.com/ParkingFacility>";

            HttpPost httpPost = new HttpPost("http://localhost:8080/rest/");
            httpPost.addHeader("Content-Type","text/turtle");

            //httpPost.addHeader("Slug",container.getValue().getIRI().replace("","http://example.org/"));
            httpPost.addHeader("Slug","noortest");
            try {
                httpPost.setEntity(new StringEntity(content));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            HttpResponse response = null;
            try {
                response = client.execute(httpPost);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(response.getStatusLine());
        }
    }
}
