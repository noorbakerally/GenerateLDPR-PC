import fr.emse.opensensingcity.configuration.ConfigurationFactory;;import java.io.File;

/**
 * Created by bakerally on 5/29/17.
 */
public class main {
    public static void main(String args []){
        ClassLoader classLoader = main.class.getClassLoader();
        File file = new File(classLoader.getResource("Configuration.ttl").getFile());

        //ConfigurationFactory.createConfiguration("main/resources/Configuration.ttl");
        ConfigurationFactory.createConfiguration(file.getAbsolutePath());



    }
}