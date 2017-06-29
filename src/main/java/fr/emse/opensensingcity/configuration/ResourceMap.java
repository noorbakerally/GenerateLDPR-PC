package fr.emse.opensensingcity.configuration;

import fr.emse.opensensingcity.Exceptions.VariableException;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import javax.xml.crypto.Data;
import java.beans.ExceptionListener;
import java.util.*;

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

    public List<String> getResources(Container parent){
        List<String> resources = new ArrayList<>();

        //iterating through all the datasoure and execute the resourceQuery
        //to get all the resources for which the corresponding LDPR has to be created
        for (Map.Entry <String,DataSource> dataSourceEntry:dataSources.entrySet()){
            DataSource ds = dataSourceEntry.getValue();

            if (resourceQuery.contains("?_resource")){
                resourceQuery = resourceQuery.replace("?_resource","<"+parent.getRelatedResource().getIRI()+">");
            }

            //parent bindings will need to be added here
            ResultSet rs = ds.executeResourceQuery(resourceQuery);

            //iterating through all the solutions and
            //get the resourse
            while (rs.hasNext()){
                QuerySolution qs = rs.next();

                //verify the number of variables obtained
                //if more than one then error
                Iterator<String> vars = qs.varNames();
                String varName = "";
                int numVariables = 0;
                while (vars.hasNext()){
                    varName = vars.next();
                    numVariables++;
                }
                if (numVariables > 1){
                    //throw exception here
                    try {
                        throw new VariableException("More than one variable Error");
                    } catch (VariableException e) {
                        e.printStackTrace();
                    }
                }

                String resourceIRI = qs.get(varName).toString();

                if (!resources.contains(resourceIRI)){
                    resources.add(resourceIRI);
                }
            }
        }
        return resources;
    }

    public Model getResourceGraph(String resourceIRI) {
        Model model = ModelFactory.createDefaultModel();
        for (Map.Entry <String,DataSource> dataSourceEntry:dataSources.entrySet()){
            DataSource ds = dataSourceEntry.getValue();

            String resourceGraphQuery = getResourceGraphQuery(resourceIRI);
            //System.out.println("ResourceMap.java getResourceGraph "+resourceGraphQuery);

            model.add(ds.executeGraphQuery(resourceGraphQuery));
        }
        //model.write(System.out,"TTL");
        return model;
    }

    public String getResourceGraphQuery(String resourceIRI){

        //for now only subject object triple
        String query = "CONSTRUCT {\n" +
                "  <resourceIRI> ?p ?o.\n" +
                "  ?s ?p1 <resourceIRI>\n" +
                "} WHERE {\n" +
                "  {<resourceIRI> ?p ?o.} UNION \n" +
                "  {?s ?p1 <resourceIRI> .}\n" +
                "} ";
        //query = query.replace("resourceIRI",resourceIRI);
        return query;
    }
}
