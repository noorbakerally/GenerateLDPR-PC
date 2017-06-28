package fr.emse.opensensingcity.configuration;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.jena.rdf.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by noor on 26/06/17.
 */
public class RDFSourceMap {



    String IRI;
    String iriTemplate;
    String constant;
    Map<String,ResourceMap> resourceMaps = new HashMap<String, ResourceMap>();

    Map <String,RelatedResource> relatedResources = new HashMap<>();
    List<LDPRS> rdfSources = new ArrayList<>();

    public RDFSourceMap(String RDFSourceMapIRI) {
        this.IRI = RDFSourceMapIRI;
    }

    public String getIRI() {
        return IRI;
    }

    public void setIRI(String iri) {
        this.IRI = iri;
    }


    public String getIriTemplate() {
        return iriTemplate;
    }

    public void setIriTemplate(String iriTemplate) {
        this.iriTemplate = iriTemplate;
    }

    public String getConstant() {
        return constant;
    }

    public void setConstant(String constant) {
        this.constant = constant;
    }

    public ResourceMap addResourceMap(String iri) {
        ResourceMap newResourceMap = new ResourceMap(iri);
        resourceMaps.put(iri,newResourceMap);
        return newResourceMap;
    }

    public String toString(int level) {

        String str = "";

        String tab= StringUtils.repeat("\t", level);

        String title = "RDFSourceMap:";

        if (this instanceof ContainerMap){
            title = "ContainerMap:";
        }
        String titleUnderline = StringUtils.repeat("", title.length());

        str = tab+title + "\n";

        str += tab+"\t\tIRI: "+getIRI()+"\n";

        for (Map.Entry <String,ResourceMap> resourceMapEntry:resourceMaps.entrySet()){
            str = str + "\n"+resourceMapEntry.getValue().toString(level+2);
        }

        return str;
    }

    public void generateListOfRelatedResources(){
        //iterate through all resource maps
        //and generate the resources for each resource maps
        for (Map.Entry <String,ResourceMap> resourceMap:resourceMaps.entrySet()){
            String resourceMapIRI = resourceMap.getKey();
            ResourceMap cResourceMap = resourceMap.getValue();

            //get the related resource from the ResourceMap
            //add it to the relatedResources for the RDFSourceMap
            for (String relatedResource:cResourceMap.getResources()){

                //if relatedResources already contain the relatedResource
                //check if the relatedResource has a link to the cResourceMap
                //if not add it
                if (relatedResources.containsKey(relatedResource)){
                    Map <String, ResourceMap> rResouceMaps = relatedResources.get(relatedResources).
                            getResourceMaps();
                    if (!rResouceMaps.containsKey(cResourceMap.getIRI())){
                        relatedResources.get(relatedResource).addResourceMap(cResourceMap);
                    }
                } else {
                    //create the relatedResource
                    //add it to the relatedResources of the RDFSourceMap
                    RelatedResource rr1 = new RelatedResource(relatedResource);
                    rr1.addResourceMap(cResourceMap);
                    relatedResources.put(relatedResource,rr1);
                }
            }
        }
        //System.out.println("Class:RDFSourceMap RelatedResources:"+relatedResources);
    }

    public void generateRelatedResourcesGraph(){
        for (Map.Entry<String,RelatedResource> relatedResourceEntry:relatedResources.entrySet()){
            RelatedResource cRelatedResource = relatedResourceEntry.getValue();
            cRelatedResource.getFinalGraph();
        }
    }

    public void generate(){

        //generate all the list of related resources
        generateListOfRelatedResources();
        //System.out.println("Class:RDFSourceMap:"+relatedResources);

        //generate graph of all related resources
        generateRelatedResourcesGraph();

    }

    public Map<String, ResourceMap> getResourceMaps() {
        return resourceMaps;
    }

    public void setResourceMaps(Map<String, ResourceMap> resourceMaps) {
        this.resourceMaps = resourceMaps;
    }

    public Map<String, RelatedResource> getRelatedResources() {
        return relatedResources;
    }

    public void setRelatedResources(Map<String, RelatedResource> relatedResources) {
        this.relatedResources = relatedResources;
    }

    public void generateResources(){
        generate();

        for (Map.Entry <String,RelatedResource> rrEntry:getRelatedResources().entrySet()){
            RelatedResource rr = rrEntry.getValue();

            String uri = IRIGenerator.getSlug(rr);

            LDPRS rdfSource = null;

            rdfSource = new LDPRS(uri);

            rdfSource.setRelatedResource(rr);
            rdfSource.generateGraph();

            rdfSources.add(rdfSource);
        }
    }

    public void sendRequest() throws IOException {
        for (LDPRS ldprs:rdfSources){
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost request = getResourceRequest((BasicContainer) ldprs);
            HttpResponse response = null;
            response = client.execute(request);
            System.out.println(response);
        }
    }

    public static HttpPost getResourceRequest(LDPRS ldprs){
        String baseURI = "http://localhost:8888/";
        HttpPost httpPost = new HttpPost(baseURI);

        httpPost.addHeader("Content-Type","text/turtle");
        httpPost.addHeader("Link","<http://www.w3.org/ns/ldp#Resource>; rel='type'");
        httpPost.addHeader("Link","<http://www.w3.org/ns/ldp#RDFSource>; rel='type'");

        httpPost.addHeader("Slug",ldprs.getIRI());

        Model model = ldprs.generateGraph();
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
