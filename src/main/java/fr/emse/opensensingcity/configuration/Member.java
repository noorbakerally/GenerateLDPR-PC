package fr.emse.opensensingcity.configuration;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bakerally on 5/29/17.
 */
public class Member extends LDPRS {
    ContentGenerator topicGenerator;
    List<String> topics ;
    Logger logger;
    List <Container> containers;
    public Member(String memberIRI) {
        super();
        setIRI(memberIRI);
        topics = new ArrayList<String>();
        logger = LoggerFactory.getLogger(getClass());
        containers = new ArrayList<Container>();
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

    public void addContainer(Container container){
        containers.add(container);
    }

    @Override
    public Model generateGraph() {
        return graph;
    }

    public List<Container> getContainers() {
        return containers;
    }

    public void setContainers(List<Container> containers) {
        this.containers = containers;
    }
}
