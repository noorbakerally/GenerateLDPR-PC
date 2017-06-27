package fr.emse.opensensingcity.configuration;

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
    Map<String,ContainerMap> containerMaps = new HashMap<String, ContainerMap>();
    ContainerMap parentContainerMap;

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

    public void setInsertedContentRelation(String insertedContentRelation) {
        this.insertedContentRelation = insertedContentRelation;
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

    public Map<String, RDFSourceMap> getRdfSourceMaps() {
        return rdfSourceMaps;
    }

    public void setRdfSourceMaps(Map<String, RDFSourceMap> rdfSourceMaps) {
        this.rdfSourceMaps = rdfSourceMaps;
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
                str+=containerMapEntry.getValue().toString(4);
            }
        }

        return str;
    }

    public void addChildContainerMap(ContainerMap containerMap){
        containerMaps.put(containerMap.getIRI(),containerMap);
        containerMap.setParentContainerMap(this);
    }

    public void setParentContainerMap(ContainerMap parentContainerMap) {
        this.parentContainerMap = parentContainerMap;
    }

    public ContainerMap getParentContainerMap() {
        return parentContainerMap;
    }
}