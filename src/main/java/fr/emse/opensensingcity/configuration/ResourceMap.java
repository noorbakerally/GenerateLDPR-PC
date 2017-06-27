package fr.emse.opensensingcity.configuration;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Created by noor on 26/06/17.
 */
public class ResourceMap {
    String resourceSelector;
    String graphTemplate;
    String linkToSource;
    String linkFromSource;
    String constant;
    String IRI;
    public ResourceMap(String iri) {
        this.IRI = iri;
    }

    public String getIRI() {
        return IRI;
    }

    public void setIRI(String IRI) {
        this.IRI = IRI;
    }

    public String getResourceSelector() {
        return resourceSelector;
    }

    public void setResourceSelector(String resourceSelector) {
        this.resourceSelector = resourceSelector;
    }

    public String getGraphTemplate() {
        return graphTemplate;
    }

    public void setGraphTemplate(String graphTemplate) {
        this.graphTemplate = graphTemplate;
    }

    public String getLinkToSource() {
        return linkToSource;
    }

    public void setLinkToSource(String linkToSource) {
        this.linkToSource = linkToSource;
    }

    public String getLinkFromSource() {
        return linkFromSource;
    }

    public void setLinkFromSource(String linkFromSource) {
        this.linkFromSource = linkFromSource;
    }

    public String getConstant() {
        return constant;
    }

    public void setConstant(String constant) {
        this.constant = constant;
    }

    public String toString(int level) {
        String str = "";

        String tab= StringUtils.repeat("\t", level);

        String title = "ResourceMap:";

       String titleUnderline = StringUtils.repeat("", title.length());

        str = tab+title + "\n";

        str += tab+"\t\tIRI: "+getIRI()+"\n";
        str += tab+"\t\tGraphTemplate: "+getGraphTemplate()+"\n";
        str += tab+"\t\tLinkToSource: "+getLinkToSource()+"\n";

        return str;
    }
}
