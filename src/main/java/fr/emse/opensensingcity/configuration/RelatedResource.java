package fr.emse.opensensingcity.configuration;

import fr.emse.opensensingcity.RDF.Resource;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by noor on 28/06/17.
 */
public class RelatedResource extends Resource {

    Model model;
    public RelatedResource(String iri){
        super(iri);

    }

    Map<String,ResourceMap> resourceMaps = new HashMap<>();

    public Map<String, ResourceMap> getResourceMaps() {
        return resourceMaps;
    }

    public void setResourceMaps(Map<String, ResourceMap> resourceMaps) {
        this.resourceMaps = resourceMaps;
    }

    public void addResourceMap(ResourceMap rs){
        resourceMaps.put(rs.getIRI(),rs);
    }

    public Model getFinalGraph(){
        if (model == null){
            loadGraph();
        }
        return model;
    }

    public Model loadGraph(){
        model = ModelFactory.createDefaultModel();
        for (Map.Entry <String,ResourceMap>resourceMapEntry:resourceMaps.entrySet()){
            ResourceMap cResourceMap = resourceMapEntry.getValue();
            Model newModel = cResourceMap.getResourceGraph(getIRI());
            model.add(newModel);
        }
        return model;
    }
}
