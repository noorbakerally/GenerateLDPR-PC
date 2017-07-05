package fr.emse.opensensingcity.tests;

import fr.emse.opensensingcity.configuration.Container;
import fr.emse.opensensingcity.configuration.LDPRS;
import fr.emse.opensensingcity.configuration.RelatedResource;
import fr.emse.opensensingcity.slugtemplate.IRIGenerator;

/**
 * Created by noor on 05/07/17.
 */
public class TestSlug {

    public static void test1(){
        String slugTemplate = "{_r.iri.scheme}-ans";
        String iri = "https://opendata.paris.fr/api/v2/catalog/exports/ttl";
        LDPRS ldprs = new LDPRS("");
        ldprs.setRelatedResource(new RelatedResource(iri));
        String slug = IRIGenerator.getSlug(ldprs,slugTemplate);
        System.out.println(slug);
    }

    public static void test2(){
        String slugTemplate = "{_r.iri.host}-ans";
        String iri = "";

        iri = "https://opendata.paris.fr/api/v2/catalog/exports/ttl";
        Container c1 = new Container("");
        c1.setRelatedResource(new RelatedResource(iri));

        iri = "https://opendata.paris.fr/api/v2/catalog/datasets/adresse_paris";
        Container c2 = new Container("");
        c2.setRelatedResource(new RelatedResource(iri));
        c2.setContainer(c1);

        iri = "https://opendata.paris.fr/api/v2/catalog/datasets/adresse_paris-csv";
        LDPRS ldprs = new LDPRS("");
        ldprs.setRelatedResource(new RelatedResource(iri));
        ldprs.setContainer(c2);


        String slug = IRIGenerator.getSlug(ldprs,slugTemplate);
        System.out.println(slug);
    }
}
