package fr.emse.opensensingcity.configuration;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by bakerally on 6/14/17.
 */
public class DataSource {
    String IRI;
    String location;

    public DataSource(String dataSourceIRI) {
        this.IRI = dataSourceIRI;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getIRI() {
        return IRI;
    }

    public void setIRI(String IRI) {
        this.IRI = IRI;
    }


    public String toString(int level) {
        String str="";
        String tab= StringUtils.repeat("\t", level);
        String atttab= StringUtils.repeat("\t", level);
        str += tab+"\t\t\tLocation: "+getLocation()+"\n";
        return str;
    }
}
