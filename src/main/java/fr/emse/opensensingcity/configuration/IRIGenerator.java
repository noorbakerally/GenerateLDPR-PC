package fr.emse.opensensingcity.configuration;

/**
 * Created by noor on 28/06/17.
 */
public class IRIGenerator {
    public static String getSlug(RelatedResource rr){
        if (rr.getIRI().equals("https://opendata.paris.fr/api/v2/catalog/exports/ttl")){
            return "catalog";
        } else {
            int i = rr.getIRI().lastIndexOf("/");
            return rr.getIRI().substring(i+1);
        }
    }

}
