package fr.emse.opensensingcity.configuration;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bakerally on 5/29/17.
 */
public class Configuration {
    //private static final Logger logger = LoggerFactory.getLogger(Configuration.class);
    String baseURI;
    public Map<String,ContainerMap> containerMaps = new HashMap<String, ContainerMap>();
    public Map<String,DataSource> dataSources = new HashMap<String, DataSource>();


   public List <Container> topContainers = new ArrayList<>();

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


    public void execute() throws IOException {
        for (Map.Entry <String,ContainerMap> entry :containerMaps.entrySet()){
            ContainerMap containerMap = entry.getValue();
            if (containerMap.getParentContainerMap() !=null) continue;
            containerMap.generateResources();

            for (LDPRS container:containerMap.getResources()){
                container = (Container)container;
                container.sendRequest();
            }

        }
    }
}
