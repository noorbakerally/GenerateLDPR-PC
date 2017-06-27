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
    private static Map <String,ContainerMap> containerMaps = new HashMap<String, ContainerMap>();

    public  static Configuration createConfiguration(String confLocation){
        Configuration configuration = new Configuration();

        model = RDFDataMgr.loadModel(confLocation);


        //load initial container maps
        String containerMapQuery="SELECT DISTINCT ?containerMap " +
                "WHERE { ?containerMap a :ContainerMap .}";
        ResultSet containerMapRs = Global.exeQuery(containerMapQuery, model);
        while (containerMapRs.hasNext()){
            String containerMapIRI = containerMapRs.next().get("?containerMap").toString();
            ContainerMap containerMap = new ContainerMap(containerMapIRI);
            containerMaps.put(containerMapIRI,containerMap);
        }

        for (Map.Entry <String,ContainerMap> containerMapEntry:containerMaps.entrySet()){
            String containerMapIRI = containerMapEntry.getKey();
            ContainerMap containerMap = containerMapEntry.getValue();
            loadContainerMap(containerMap);
        }

        //load initial container maps
        String containerParentMapQuery="SELECT DISTINCT ?childContainerMap ?parentContainerMap " +
                "WHERE { ?parentContainerMap a :ContainerMap; :containerMap ?childContainerMap . }";
        ResultSet hierarchicalContainerMap = Global.exeQuery(containerParentMapQuery, model);
        while (hierarchicalContainerMap.hasNext()){
            QuerySolution qs = hierarchicalContainerMap.next();
            String parent = qs.get("?parentContainerMap").toString();
            String child = qs.get("?childContainerMap").toString();

            ContainerMap parentContainerMap = containerMaps.get(parent);
            ContainerMap childContainerMap = containerMaps.get(child);
            parentContainerMap.addChildContainerMap(childContainerMap);
        }





        configuration.setContainerMap(containerMaps);
        return configuration;
    }

    static void loadContainerMap(ContainerMap containerMap){

        //get container type
        processContainerType(containerMap.getIRI());

        //get RDFSourceMaps
        processContainerRDFSourceMaps(containerMap.getIRI());

        //load as RDFSourceMap
        loadRDFSourceMaps(containerMap);



    }

    private static void processContainerType(String containerIRI) {
        ContainerMap containerMap = containerMaps.get(containerIRI);
        String containerTypeQuery="SELECT DISTINCT * \n" +
                "WHERE { " +
                "<containerMapIRI> :containerType ?containerType ." +
                "}";
        containerTypeQuery = containerTypeQuery.replace("containerMapIRI",containerIRI);
        ResultSet rs = Global.exeQuery(containerTypeQuery, model);
        while (rs.hasNext()){
            String containerType = rs.next().get("?containerType").toString();
            if (containerType.equals(" http://www.w3.org/ns/ldp#BasicContainer")){
                containerMap.setContainerType(Global.ContainerType.Basic);
            } else if (containerType.equals(" http://www.w3.org/ns/ldp#DirectContainer")){
                containerMap.setContainerType(Global.ContainerType.Direct);
            } else {
                containerMap.setContainerType(Global.ContainerType.Indirect);
            }
        }
    }

    private static void processContainerRDFSourceMaps(String containerIRI) {
        ContainerMap containerMap = containerMaps.get(containerIRI);

        //get RDFSource Maps
        String RDFSourceMapQuery="SELECT DISTINCT * \n" +
                "WHERE { " +
                "<containerMapIRI> :rdfSourceMap ?rdfSourceMap ." +
                "}";
        RDFSourceMapQuery = RDFSourceMapQuery.replace("containerMapIRI",containerIRI);
        ResultSet rs = Global.exeQuery(RDFSourceMapQuery, model);
        while (rs.hasNext()){
            String rdfSourceMapIRI = rs.next().get("?rdfSourceMap").toString();
            containerMap.addRDFSourceMap(rdfSourceMapIRI);
        }
        for (Map.Entry <String,RDFSourceMap> rdfSourceMap:containerMap.getRdfSourceMaps().entrySet()){
            String rdfSourceMapIRI = rdfSourceMap.getKey();
            RDFSourceMap currentRDFSourceMap = containerMap.getRdfSourceMaps().get(rdfSourceMapIRI);
            loadRDFSourceMaps(currentRDFSourceMap);
        }

    }

    private static void loadRDFSourceMaps(RDFSourceMap rdfSourceMap){
        //load all RDFSourceMap

            String rdfSourceMapIRI = rdfSourceMap.getIRI();

            String RDFSourceMapQuery="SELECT DISTINCT * \n" +
                    "WHERE { " +
                    "<rdfSourceMapIRI> ?p ?o ." +
                    "}";

            RDFSourceMapQuery = RDFSourceMapQuery.replace("rdfSourceMapIRI",rdfSourceMapIRI);

        ResultSet rs = Global.exeQuery(RDFSourceMapQuery, model);
            while (rs.hasNext()){
                QuerySolution qs = rs.next();
                String p = qs.get("?p").toString();
                String o = qs.get("?o").toString();


                if (Global.getVTerm("resourceMap").equals(p)){
                    processResourceMap(rdfSourceMap,o);
                }
            }
    }


    private static void processResourceMap(RDFSourceMap currentRDFSourceMap, String resourceMapIRI) {
        ResourceMap resourceMap = currentRDFSourceMap.addResourceMap(resourceMapIRI);

        String ResourceMapQuery = "SELECT DISTINCT * \n" +
                "WHERE { " +
                "<resourceMapIRI> ?p ?o ." +
                "}";

        ResourceMapQuery = ResourceMapQuery.replace("resourceMapIRI", resourceMapIRI);
        ResultSet rs = Global.exeQuery(ResourceMapQuery, model);
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            String p = qs.get("?p").toString();
            String o = qs.get("?o").toString();

            if (Global.getVTerm("graphTemplate").equals(p)) {
                resourceMap.setGraphTemplate(o);
            }

            if (Global.getVTerm("linkToSource").equals(p)) {
                resourceMap.setLinkToSource(o);
            }

            if (Global.getVTerm("resourceQuery").equals(p)) {
                resourceMap.setResourceQuery(o);
            }
        }
    }
}
