package fr.emse.opensensingcity.configuration;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.RDFDataMgr;

/**
 * Created by bakerally on 5/29/17.
 */
public class ConfigurationFactory {
    public  static Configuration createConfiguration(String confLocation){
        Configuration configuration = null;


        Model model = RDFDataMgr.loadModel(confLocation);
        model.write(System.out,"Turtle");

        return configuration;
    }
}
