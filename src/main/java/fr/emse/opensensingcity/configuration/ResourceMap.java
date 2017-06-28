package fr.emse.opensensingcity.configuration;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by noor on 26/06/17.
 */
public class ResourceMap {
    String resourceQuery;
    String graphTemplate;
    String linkToSource;
    String linkFromSource;
    String constant;
    String IRI;
    Map <String,DataSource> dataSources = new HashMap<String,DataSource>();


    public ResourceMap(String iri) {
        this.IRI = iri;
    }

    public String getIRI() {
        return IRI;
    }

    public void setIRI(String IRI) {
        this.IRI = IRI;
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

    public String getResourceQuery() {
        return resourceQuery;
    }

    public void setResourceQuery(String resourceQuery) {
        this.resourceQuery = resourceQuery;
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
        str += tab+"\t\tResourceQuery: "+getResourceQuery()+"\n";

        //print child container map
        if (dataSources.size() > 0){
            str += tab+"\t\tDataSources: \n";
            for (Map.Entry <String,DataSource> dataSourceEntry:dataSources.entrySet()){
                str+=dataSourceEntry.getValue().toString(level);
            }
        }

        return str;
    }

    public void addDataSource(DataSource dataSource) {
        dataSources.put(dataSource.getIRI(),dataSource);
    }
}
