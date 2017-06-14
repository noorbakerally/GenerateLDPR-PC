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
    private static String prefixes="PREFIX ldp: <http://www.w3.org/ns/ldp#>";
    private static Map <String,Container> containerMap = new HashMap<String, Container>();
    public  static Configuration createConfiguration(String confLocation){
        Configuration configuration = null;

        model = RDFDataMgr.loadModel(confLocation);

        loadBasicContainer();
        loadContainerMembers();
        return configuration;
    }

    static void loadBasicContainer(){
        //Get all containers
        String basicContainersRQ="SELECT DISTINCT ?container " +
                "WHERE { ?container a ldp:BasicContainer .}";

        basicContainersRQ = prefixes + basicContainersRQ;

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

            containerMembersRQ = prefixes + containerMembersRQ;

            Query query = QueryFactory.create(containerMembersRQ,Syntax.syntaxARQ);
            QueryExecution qexec = QueryExecutionFactory.create(query, model);
            ResultSet rs = qexec.execSelect() ;

            while (rs.hasNext()){
                String memberIRI = rs.next().get("?member").toString();
                container.addMember(new Member(memberIRI));
            }
        }
    }
}
