
import fr.emse.opensensingcity.configuration.Configuration;
import fr.emse.opensensingcity.configuration.ConfigurationFactory;
import fr.emse.opensensingcity.ldprgenerator.LDPRGenerator;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by bakerally on 5/29/17.
 */
public class main {
    public static void main(String args []) throws Exception {
        ClassLoader classLoader = main.class.getClassLoader();
        File file = new File(classLoader.getResource("Configuration.ttl").getFile());

        //Configuration configuration = ConfigurationFactory.createConfiguration(file.getAbsolutePath());

        //LDPRGenerator.sendRequest(configuration.containerMap);

        HttpClient client = HttpClientBuilder.create().build();
        String content = "<> a <http://example.com/ParkingFacility> .";
        HttpPost httpPost = new HttpPost("http://localhost:8080/rest/");
        httpPost.addHeader("Content-Type","text/turtle");
        httpPost.addHeader("Link","<http://www.w3.org/ns/ldp#Resource>; rel='type'");
        httpPost.addHeader("Slug","newresource1");
        httpPost.setEntity(new StringEntity(content));
        HttpResponse response = client.execute(httpPost);

        System.out.println(response.getStatusLine());



    }
}
