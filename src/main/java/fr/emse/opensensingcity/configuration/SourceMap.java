package fr.emse.opensensingcity.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by noor on 15/07/17.
 */
public abstract class SourceMap {
    Container container;
    String IRI;
    String slugTemplate;;
    String constant;
    Map<String,ResourceMap> resourceMaps = new HashMap<String, ResourceMap>();
    Map <String,RelatedResource> relatedResources = new HashMap<>();
    List<LDPR> resources = new ArrayList<>();


    public SourceMap(String IRI){
        this.IRI = IRI;
    }
    public List<LDPR> getResources() {
        return resources;
    }

    public void setResources(List<LDPR> resources) {
        this.resources = resources;
    }

    public ResourceMap addResourceMap(String iri) {
        ResourceMap newResourceMap = new ResourceMap(iri);
        resourceMaps.put(iri,newResourceMap);
        return newResourceMap;
    }

    public abstract SourceMap copy();
}
