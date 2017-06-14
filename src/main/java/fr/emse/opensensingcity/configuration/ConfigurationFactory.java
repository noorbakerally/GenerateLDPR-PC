package fr.emse.opensensingcity.configuration;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bakerally on 5/29/17.
 */
public class ConfigurationFactory {
    private static Model model;
    private static Map <String,Container> containerMap = new HashMap<String, Container>();

    public  static Configuration createConfiguration(String confLocation){
        Configuration configuration = new Configuration();

        model = RDFDataMgr.loadModel(confLocation);

        loadBasicContainer();
        loadContainerMembers();
        configuration.setContainerMap(containerMap);
        return configuration;
    }

    static void loadBasicContainer(){
        //Get all containers
        String basicContainersRQ="SELECT DISTINCT ?container " +
                "WHERE { ?container a ldp:BasicContainer .}";

        basicContainersRQ = Global.prefixes + basicContainersRQ;

        Query query = QueryFactory.create(basicContainersRQ,Syntax.syntaxARQ);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        ResultSet rs = qexec.execSelect() ;

        while (rs.hasNext()){
            String containerIRI = rs.next().get("?container").toString();
            Container newContainer = new BasicContainer(containerIRI);
            containerMap.put(containerIRI,newContainer);
        }
    }

    static void loadContainerMembers() {
        for (String containerIRI:containerMap.keySet()){

            Container container = containerMap.get(containerIRI);

            String containerMembersRQ="SELECT DISTINCT ?member " +
                    "WHERE { " +
                            "<"+containerIRI+">" + " ldp:contains ?member ." +
                    "}";

            containerMembersRQ = Global.prefixes + containerMembersRQ;

            Query query = QueryFactory.create(containerMembersRQ,Syntax.syntaxARQ);
            QueryExecution qexec = QueryExecutionFactory.create(query, model);
            ResultSet rs = qexec.execSelect() ;

            while (rs.hasNext()){
                String memberIRI = rs.next().get("?member").toString();
                container.addMember(getMember(memberIRI));
            }
        }
    }


    static Member getMember(String memberIRI){
        Member member = new Member(memberIRI);
        ContentGenerator topicGenerator = getContentGenerator(memberIRI,"topicGenerator");
        member.setTopicGenerator(topicGenerator);
        member.loadTopics();
        return member;
    }

    static ContentGenerator getContentGenerator(String resourceIRI,String status){
        ContentGenerator cg = new ContentGenerator();
        String contentGeneratorRQ="SELECT DISTINCT * WHERE {" +
                "    <"+resourceIRI+"> on:"+status+" ?contentGenerator ." +
                "    ?contentGenerator on:query ?query;" +
                "                      on:dataSource ?dataSource ." +
                "    ?dataSource on:location ?location;" +
                "                on:DataSourceType ?dataSourceType ." +
                "}";

        contentGeneratorRQ = Global.prefixes + contentGeneratorRQ;
        Query query = QueryFactory.create(contentGeneratorRQ,Syntax.syntaxARQ);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        ResultSet rs = qexec.execSelect() ;
        while (rs.hasNext()){
            QuerySolution qs = rs.next();
            String cgQuery = qs.get("?query").toString();
            cg.setQuery(cgQuery);
            DataSource ds = new DataSource();
            ds.setLocation(qs.get("?location").toString());
            cg.setDataSource(ds);
        }
        return cg;
    }
}
