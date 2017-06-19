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

        Configuration configuration = ConfigurationFactory.createConfiguration(file.getAbsolutePath());
        LDPRGenerator.sendRequest(configuration.containerMap);


        /*HttpClient client = HttpClientBuilder.create().build();
        String content = "@prefix dcterms: <http://purl.org/dc/terms/> . @prefix ex: <http://example.com/> .";
        content = content + "<> dcterms:title \"Photos of Alice\" .";
        content = content + "<> ex:show ex:Alice .";
        content = content + "ex:Sam ex:isShownIn <> .";
        HttpPost httpPost = new HttpPost("http://localhost:8888/");
        httpPost.addHeader("Content-Type","text/turtle");
        //httpPost.addHeader("Link","<http://www.w3.org/ns/ldp#Resource>; rel='type'");
        //httpPost.addHeader("Link","<http://www.w3.org/ns/ldp#RDFSource>; rel='type'");
        httpPost.addHeader("Link","<http://www.w3.org/ns/ldp#BasicContainer>; rel='type'");
        httpPost.addHeader("Slug","NewResource1");
        //httpPost.addHeader("Authorization","Basic bm9vcmFuaS5iYWtlcmFsbHlAZ21haWwuY29tOm11NDA2MDA3");
        httpPost.setEntity(new StringEntity(content));
        HttpResponse response = client.execute(httpPost);
        System.out.println(response.getStatusLine());*/


        //creating a container
        /*HttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost("http://localhost:8888/");

        String content = "@prefix dcterms: <http://purl.org/dc/terms/> . @prefix ex: <http://example.com/> ." +
                "@prefix ldp:<http://www.w3.org/ns/ldp#> .";
        content = content + "<> dcterms:title 'Photos of Alice' .";
        content = content + "<> ex:shows ex:Alice .";
        //content = content + "<> a ldp:Container, ldp:BasicContainer .";


        httpPost.addHeader("Content-Type","text/turtle");
        httpPost.addHeader("Link","<http://www.w3.org/ns/ldp#Resource>; rel='type'");
        httpPost.addHeader("Link","<http://www.w3.org/ns/ldp#RDFSource>; rel='type'");
        httpPost.addHeader("Link","<http://www.w3.org/ns/ldp#BasicContainer>; rel=\"type\"");
        httpPost.addHeader("Slug","photos10");
        httpPost.setEntity(new StringEntity(content));
        HttpResponse response = client.execute(httpPost);
        System.out.println(response.getStatusLine());*/

    }
}
