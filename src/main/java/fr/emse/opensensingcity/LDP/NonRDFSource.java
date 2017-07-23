package fr.emse.opensensingcity.LDP;

import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;

/**
 * Created by noor on 28/06/17.
 */
public class NonRDFSource extends Resource {
    byte[] binary;
    public NonRDFSource(String iri) {
        super(iri);
    }

    public HttpPost getResourceRequest(){
        String baseURI = container.getIRI();
        HttpPost httpPost = new HttpPost(baseURI);

        URI fileURL = URI.create(getRelatedResource().getIRI());


        HttpURLConnection connection = null;
        String contentType = null;
        try {
            connection = (HttpURLConnection)  fileURL.toURL().openConnection();
            connection.setRequestMethod("HEAD");
            connection.connect();
            contentType = connection.getContentType();
        } catch (IOException e) {
            e.printStackTrace();
        }



        httpPost.addHeader("Content-Type",contentType);
        httpPost.addHeader("Link","<http://www.w3.org/ns/ldp#Resource>; rel=\"type\"");


        File file = new File("DownloadedFile");

        try {
            FileUtils.copyURLToFile(fileURL.toURL(),file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] data = new byte[0];
        try {
            data = FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        httpPost.addHeader("Slug",getSlug());
        httpPost.setEntity(new ByteArrayEntity(data));

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

    public byte[] getBinary() {
        return binary;
    }

    public void setBinary(byte[] binary) {
        this.binary = binary;
    }
}
