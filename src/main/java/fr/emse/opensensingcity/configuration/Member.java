package fr.emse.opensensingcity.configuration;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bakerally on 5/29/17.
 */
public class Member extends Resource {
    ContentGenerator topicGenerator;
    List<String> topics ;

    public Member(String memberIRI) {
        super();
        topics = new ArrayList<String>();
    }

    public ContentGenerator getTopicGenerator() {
        return topicGenerator;
    }

    public void setTopicGenerator(ContentGenerator topicGenerator) {
        this.topicGenerator = topicGenerator;
    }

    public void loadTopics(){
        Model model = ModelFactory.createDefaultModel();
        model.read(topicGenerator.getDataSource().getLocation());

        String topicsRQ= topicGenerator.getQuery();

        topicsRQ = Global.prefixes + topicsRQ;
        Query query = QueryFactory.create(topicsRQ, Syntax.syntaxARQ);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        ResultSet rs = qexec.execSelect();

        while (rs.hasNext()){
            String topicIRI = rs.next().get("?resource").toString();
            topics.add(topicIRI);


            String topicSGraphRQ = "CONSTRUCT {" +
                        "<"+topicIRI+"> ?p ?o ." +
                    "} WHERE {" +
                        "<"+topicIRI+"> ?p ?o ." +
                    "} ";

            qexec = QueryExecutionFactory.create(topicSGraphRQ, model);
            qexec.execConstruct(graph);
        }



    }


}
