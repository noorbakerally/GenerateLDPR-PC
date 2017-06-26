package fr.emse.opensensingcity.ldprgenerator;

import fr.emse.opensensingcity.configuration.BasicContainer;
import fr.emse.opensensingcity.configuration.Container;
import fr.emse.opensensingcity.configuration.Member;
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
    static String baseURI = "http://localhost:8888/";
    public static void sendRequest(Map<String,Container> containerMap) throws IOException {
        for (Map.Entry<String,Container> containerEntry:containerMap.entrySet()){
            HttpClient client = HttpClientBuilder.create().build();

            BasicContainer container = (BasicContainer) containerEntry.getValue();
            HttpPost request = getResourceRequest(container);
            HttpResponse response = null;
            response = client.execute(request);
            System.out.println(response);

            for (Member member:container.getMembers()){
                HttpPost memberRequest = getResourceRequest(member,container.getIRI());
                response = client.execute(memberRequest);
                System.out.println(response);
            }
        }
    }

    public static HttpPost getResourceRequest(BasicContainer container){
        HttpPost httpPost = new HttpPost(baseURI);

        httpPost.addHeader("Content-Type","text/turtle");
        httpPost.addHeader("Link","<http://www.w3.org/ns/ldp#Resource>; rel='type'");
        httpPost.addHeader("Link","<http://www.w3.org/ns/ldp#RDFSource>; rel='type'");
        httpPost.addHeader("Link","<http://www.w3.org/ns/ldp#BasicContainer>; rel=\"type\"");

        System.out.println(container.getIRI().replace(baseURI,""));
        httpPost.addHeader("Slug",container.getIRI().replace(baseURI,""));
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

    public static HttpPost getResourceRequest(Member member,String containerIRI){
        HttpPost httpPost = new HttpPost(containerIRI);
        httpPost.addHeader("Content-Type","text/turtle");
        httpPost.addHeader("Slug",member.getIRI().replace(baseURI,""));

        System.out.println("creating "+member.getIRI());

        Model model = member.generateGraph();
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
