package fr.emse.opensensingcity.configuration;

import fr.emse.opensensingcity.slugtemplate.IRIGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by noor on 15/07/17.
 */
public class NonRDFSourceMap extends SourceMap {


    public NonRDFSourceMap(String IRI){
        super(IRI);
    }

    public ResourceMap addResourceMap(String iri) {
        ResourceMap newResourceMap = new ResourceMap(iri);
        resourceMaps.put(iri,newResourceMap);
        return newResourceMap;
    }

    public void generateListOfRelatedResources(){
        //iterate through all resource maps
        //and generate the resources for each resource maps
        for (Map.Entry <String,ResourceMap> resourceMap:resourceMaps.entrySet()){
            String resourceMapIRI = resourceMap.getKey();

            ResourceMap cResourceMap = resourceMap.getValue();

            //get the related resource from the ResourceMap
            //add it to the relatedResources for the RDFSourceMap
            for (String relatedResource:cResourceMap.getResources(container)){

                //if relatedResources already contain the relatedResource
                //check if the relatedResource has a link to the cResourceMap
                //if not add it

                if (relatedResources.containsKey(relatedResource)){
                    Map <String, ResourceMap> rResouceMaps = relatedResources.get(relatedResource).getResourceMaps();
                    if (!rResouceMaps.containsKey(cResourceMap.getIRI())){
                        relatedResources.get(relatedResource).addResourceMap(cResourceMap);
                    }
                } else {
                    //create the relatedResource
                    //add it to the relatedResources of the RDFSourceMap
                    RelatedResource rr1 = new RelatedResource(relatedResource);



                    rr1.addResourceMap(cResourceMap);
                    relatedResources.put(relatedResource,rr1);
                }
            }
        }
        //System.out.println("Class:RDFSourceMap RelatedResources:"+relatedResources);
    }

    public void generate(){
        //generate all the list of related resources
        generateListOfRelatedResources();
    }

    public void generateResources(){
        generate();

        for (Map.Entry <String,RelatedResource> rrEntry:getRelatedResources().entrySet()){
            RelatedResource rr = rrEntry.getValue();



            LDPNR nonRdfSource = null;

            nonRdfSource = new LDPNR("temp");
            nonRdfSource.setRelatedResource(rr);


            String slug = IRIGenerator.getSlug(nonRdfSource, getSlugTemplate());
            nonRdfSource.setSlug(slug);

            resources.add(nonRdfSource);
        }
    }

    public Container getContainer() {
        return container;
    }

    public void setContainer(Container container) {
        this.container = container;
    }

    public String getIRI() {
        return IRI;
    }

    public void setIRI(String IRI) {
        this.IRI = IRI;
    }

    public String getSlugTemplate() {
        return slugTemplate;
    }

    public void setSlugTemplate(String slugTemplate) {
        this.slugTemplate = slugTemplate;
    }

    public String getConstant() {
        return constant;
    }

    public void setConstant(String constant) {
        this.constant = constant;
    }

    public Map<String, ResourceMap> getResourceMaps() {
        return resourceMaps;
    }

    public void setResourceMaps(Map<String, ResourceMap> resourceMaps) {
        this.resourceMaps = resourceMaps;
    }

    public Map<String, RelatedResource> getRelatedResources() {
        return relatedResources;
    }

    public void setRelatedResources(Map<String, RelatedResource> relatedResources) {
        this.relatedResources = relatedResources;
    }

    @Override
    public SourceMap copy() {
        NonRDFSourceMap newObject = new NonRDFSourceMap(getIRI());

        newObject.IRI = IRI;
        newObject.slugTemplate = slugTemplate;
        newObject.constant = constant;
        newObject.resourceMaps =resourceMaps;
        newObject.relatedResources = new HashMap<>();
        newObject.resources = new ArrayList<>();
        return newObject;
    }
}
