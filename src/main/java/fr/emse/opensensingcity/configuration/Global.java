package fr.emse.opensensingcity.configuration;

/**
 * Created by bakerally on 6/14/17.
 */
public class Global {
    public static String prefixes = "PREFIX ldp: <http://www.w3.org/ns/ldp#> " +
            "PREFIX data: <http://opensensingcity.emse.fr/LDPDesign/data/> " +
            "PREFIX on:     <http://opensensingcity.emse.fr/LDPDesign/> ";

    public enum TripleType {SubjectTriples,ObjectTriples}
}
