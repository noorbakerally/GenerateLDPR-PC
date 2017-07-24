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
    String contentType;
    public NonRDFSource(String iri) {
        super(iri);
    }
    public byte[] getBinary() {return binary;}
    public void setBinary(byte[] binary) {
        this.binary = binary;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
