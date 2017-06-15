
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

        //ConfigurationFactory.createConfiguration("main/resources/Configuration.ttl");
        //Configuration configuration = ConfigurationFactory.createConfiguration(file.getAbsolutePath());


        HttpClient client = HttpClientBuilder.create().build();
        String content = "<> a <http://example.com/ParkingFacility>";
        HttpPost httpPost = new HttpPost("http://localhost:8081/rest/");
        httpPost.addHeader("Content-Type","text/turtle");
        httpPost.addHeader("Slug","noorparking17");
        httpPost.setEntity(new StringEntity(content));
        HttpResponse response = client.execute(httpPost);

        System.out.println(response.getStatusLine());



    }
}
