package fr.emse.opensensingcity.configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bakerally on 5/29/17.
 */
public class Configuration {
    String baseURI;
    List<Container> containers;
    private Map<String,Container> containerMap = new HashMap<String, Container>();

    public String getBaseURI() {
        return baseURI;
    }

    public void setBaseURI(String baseURI) {
        this.baseURI = baseURI;
    }

    public Map<String, Container> getContainerMap() {
        return containerMap;
    }

    public void setContainerMap(Map<String, Container> containerMap) {
        this.containerMap = containerMap;
    }
}
