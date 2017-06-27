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
}
