package fr.emse.opensensingcity;

import fr.emse.opensensingcity.configuration.*;
import fr.emse.opensensingcity.slugtemplate.IRIGenerator;
import fr.emse.opensensingcity.tests.TestSlug;

import java.io.File;

/**
 * Created by bakerally on 5/29/17.
 */
public class main {
    public static void main(String args []) throws Exception {
        ClassLoader classLoader = main.class.getClassLoader();
        File file = new File(classLoader.getResource("Configuration.ttl").getFile());

        //Configuration configuration = ConfigurationFactory.createConfiguration(file.getAbsolutePath());
        //configuration.print();
        //configuration.execute();

        //LDPRGenerator.sendRequest(configuration);

        TestSlug.test2();


    }
}
