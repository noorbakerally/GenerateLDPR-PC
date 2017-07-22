package fr.emse.opensensingcity.configuration;

import fr.emse.opensensingcity.LDP.BasicContainer;
import fr.emse.opensensingcity.LDP.Container;
import fr.emse.opensensingcity.slugtemplate.IRIGenerator;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by noor on 26/06/17.
 */
public class ContainerMap extends RDFSourceMap{
    String membershipResource;
    String memberRelation;
    String insertedContentRelation;
    Global.ContainerType containerType;
    Map<String,RDFSourceMap> rdfSourceMaps = new HashMap<String, RDFSourceMap>();
    Map<String,NonRDFSourceMap> nonrdfSourceMaps = new HashMap<String, NonRDFSourceMap>();
    Map<String,ContainerMap> containerMaps = new HashMap<String, ContainerMap>();
    ContainerMap parentContainerMap;


    /*Core Methods*/

    //generate containers for the container maps
    @Override
    public void generateResources(){


        super.generateRelatedResources();

        //if no resource maps
        //Create Zombie Container
        //it's a dummy container
        if (getResourceMaps().size() == 0){
            Container container = new Container("temp");
            container.setSourceMaps(this);
            resources.add(container);
            return;
        }

        for (Map.Entry <String,RelatedResource> rrEntry:getRelatedResources().entrySet()){
            RelatedResource rr = rrEntry.getValue();
            Container c = new BasicContainer("");
            c.setRelatedResource(rr);
            c.setGraph(rr.getGraph());
            String uri = IRIGenerator.getSlug(c,getSlugTemplate());
            c.setSlug(uri);
            c.setSourceMaps(this);
            c.processSourceMaps();
            resources.add(c);
        }


    }

    /*General Methods*/
    public void addChildContainerMap(ContainerMap containerMap){
        containerMaps.put(containerMap.getIRI(),containerMap);
        containerMap.setParentContainerMap(this);
    }
    public void setInsertedContentRelation(String insertedContentRelation) {
        this.insertedContentRelation = insertedContentRelation;
    }
    public ContainerMap(String containerMapIRI) {
        super(containerMapIRI);
    }
    public String getMembershipResource() {
        return membershipResource;
    }
    public void setMembershipResource(String membershipResource) {
        this.membershipResource = membershipResource;
    }
    public String getMemberRelation() {
        return memberRelation;
    }
    public void setMemberRelation(String memberRelation) {
        this.memberRelation = memberRelation;
    }
    public String getInsertedContentRelation() {
        return insertedContentRelation;
    }
    public Global.ContainerType getContainerType() {
        return containerType;
    }
    public void setContainerType(Global.ContainerType containerType) {
        this.containerType = containerType;
    }
    public void addRDFSourceMap(String iri){
        rdfSourceMaps.put(iri,new RDFSourceMap(iri));
    }
    public void addNonRDFSourceMap(String iri){
        nonrdfSourceMaps.put(iri,new NonRDFSourceMap(iri));
    }
    public Map<String, RDFSourceMap> getRdfSourceMaps() {
        return rdfSourceMaps;
    }
    public Map<String, NonRDFSourceMap> getNonRdfSourceMaps() {
        return nonrdfSourceMaps;
    }
    public void setRdfSourceMaps(Map<String, RDFSourceMap> rdfSourceMaps) {
        this.rdfSourceMaps = rdfSourceMaps;
    }
    public void setParentContainerMap(ContainerMap parentContainerMap) {
        this.parentContainerMap = parentContainerMap;
    }
    public ContainerMap getParentContainerMap() {
        return parentContainerMap;
    }
    public Map<String, ContainerMap> getContainerMaps() {
        return containerMaps;
    }
    public void setContainerMaps(Map<String, ContainerMap> containerMaps) {
        this.containerMaps = containerMaps;
    }
    public String toString(int level){
        String str = "";
        String tab= StringUtils.repeat("\t", level);

        str = str + super.toString(level);
        str += tab+"\t\tContainerType: "+getContainerType()+"\n";
        for (Map.Entry <String,RDFSourceMap> rdfSourceMapEntry:rdfSourceMaps.entrySet()){
            str = str + "\n"+rdfSourceMapEntry.getValue().toString(level+2);
        }

        //print child container map
        if (containerMaps.size() > 0){
            str += tab+"\t\tChild Containers: \n";
            for (Map.Entry <String,ContainerMap> containerMapEntry:containerMaps.entrySet()){
                str+=containerMapEntry.getValue().toString(level+4);
            }
        }
        return str;
    }
}
