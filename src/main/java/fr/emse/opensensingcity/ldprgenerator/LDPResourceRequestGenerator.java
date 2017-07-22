package fr.emse.opensensingcity.ldprgenerator;

import fr.emse.opensensingcity.LDP.RDFSource;
import fr.emse.opensensingcity.LDP.Resource;
import fr.emse.opensensingcity.LDP.Container;
import fr.emse.opensensingcity.configuration.Configuration;
import fr.emse.opensensingcity.configuration.ContainerMap;
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
import java.util.Map;

/**
 * Created by noor on 15/06/17.
 */
public class LDPResourceRequestGenerator {
    public void sendRequest(Resource resource) throws IOException {

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = null;
        if (resource instanceof RDFSource){
            request = getResourceRequest((RDFSource)resource);
        } else if (resource instanceof Container){
            request = getResourceRequest((Container)resource);
        }

        HttpResponse response = null;
        System.out.println("LDPResourceRequestGenerator.java "+"Request:"+request);
        response = client.execute(request);
        String location = response.getHeaders("Location")[0].getValue();
        resource.setIRI(location);
    }

    public HttpPost getResourceRequest(Resource resource){
        Container container= resource.getContainer();
        String baseIRI = Global.baseURI;
        if (container != null){
            baseIRI = container.getIRI();
        }
        HttpPost httpPost = new HttpPost(baseIRI);
        httpPost.addHeader("Link","<http://www.w3.org/ns/ldp#Resource>; rel=\"type\"");
        if (resource instanceof RDFSource){

            httpPost.addHeader("Content-Type","text/turtle");
            httpPost.addHeader("Link","<http://www.w3.org/ns/ldp#RDFSource>; rel=\"type\"");

            //generate Graph for RDFSource
            Model model = ((RDFSource)resource).getGraph();
            StringWriter out = new StringWriter();
            model.write(out, "TTL");
            /*model.write(System.out, "TTL");*/
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

    public void sendRequests(Configuration configuration) throws IOException {
        Map<String, ContainerMap> containerMaps = configuration.getContainerMap();
        for (Map.Entry<String, ContainerMap> entry : containerMaps.entrySet()) {
            ContainerMap containerMap = entry.getValue();
            if (containerMap.getParentContainerMap() !=null) continue;
            for (Resource resource:containerMap.getResources()){
                sendRequest(resource);
            }
        }
    }
}