package fr.emse.opensensingcity.configuration;

/**
 * Created by bakerally on 6/14/17.
 */
public class ContentGenerator {
    String query;
    DataSource dataSource;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
