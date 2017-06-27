package fr.emse.opensensingcity.configuration;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by noor on 26/06/17.
 */
public class RDFSourceMap {
    String IRI;
    String resourceSelector;
    String iriTemplate;
    String constant;
    Map<String,ResourceMap> resourceMaps = new HashMap<String, ResourceMap>();

    public RDFSourceMap(String RDFSourceMapIRI) {
        this.IRI = RDFSourceMapIRI;
    }

    public String getIRI() {
        return IRI;
    }

    public void setIRI(String iri) {
        this.IRI = iri;
    }

    public String getResourceSelector() {
        return resourceSelector;
    }

    public void setResourceSelector(String resourceSelector) {
        this.resourceSelector = resourceSelector;
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
        str += tab+"\t\tResourceSelector: "+getResourceSelector();

        for (Map.Entry <String,ResourceMap> resourceMapEntry:resourceMaps.entrySet()){
            str = str + "\n"+resourceMapEntry.getValue().toString(level+2);
        }

        return str;
    }


}
