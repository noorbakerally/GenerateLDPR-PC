package fr.emse.opensensingcity.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bakerally on 5/29/17.
 */
public class Configuration {
    String baseURI;
    public Map<String,ContainerMap> containerMaps = new HashMap<String, ContainerMap>();
    public Map<String,DataSource> dataSources = new HashMap<String, DataSource>();

   List <String> topContainers = new ArrayList<>();

    public Configuration(){
        containerMaps = new HashMap<String, ContainerMap>();
    }

    public String getBaseURI() {
        return baseURI;
    }

    public void setBaseURI(String baseURI) {
        this.baseURI = baseURI;
    }

    public Map<String, ContainerMap> getContainerMap() {
        return containerMaps;
    }

    public void setContainerMap(Map<String, ContainerMap> containerMap) {
        this.containerMaps = containerMap;
    }

    public void print() {
        for (Map.Entry <String,ContainerMap> entry :containerMaps.entrySet()){
            ContainerMap containerMap = entry.getValue();
            if (containerMap.getParentContainerMap() !=null) continue;
            System.out.println(containerMap.toString(0));
        }
    }
    public void addDataSource(DataSource ds){
        dataSources.put(ds.getIRI(),ds);
    }
    public DataSource getDataSource(String dataSourceIRI){
        return dataSources.get(dataSourceIRI);
    }


    public void execute(){
        for (Map.Entry <String,ContainerMap> entry :containerMaps.entrySet()){
            ContainerMap containerMap = entry.getValue();
            if (containerMap.getParentContainerMap() !=null) continue;

            for (Map.Entry <String,RelatedResource> rrEntry:containerMap.getRelatedResources().entrySet()){
                RelatedResource rr = rrEntry.getValue();

                String cURI = getFragmentURI(rr.getIRI());
                Container c = new BasicContainer(cURI);
                c.setRelatedResource(rr);
                c.generateGraph();
            }
        }
    }

    String getFragmentURI(String fragmentPart){
        return baseURI+"#"+fragmentPart;
    }

}
