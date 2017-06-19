package fr.emse.opensensingcity.configuration;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

/**
 * Created by bakerally on 6/14/17.
 */
public class Global {
    public static String prefixes = "PREFIX ldp: <http://www.w3.org/ns/ldp#> " +
            "PREFIX data: <http://opensensingcity.emse.fr/LDPDesign/data/> " +
            "PREFIX on:     <http://opensensingcity.emse.fr/LDPDesign/> ";

    public enum TripleType {SubjectTriples,ObjectTriples}

    public static Property getLDPContains(){
        return ResourceFactory.createProperty("http://www.w3.org/ns/ldp#contains");
    }

    public static Resource getLDPBC(){
        return ResourceFactory.createResource("http://www.w3.org/ns/ldp#BasicContainer");
    }
}
