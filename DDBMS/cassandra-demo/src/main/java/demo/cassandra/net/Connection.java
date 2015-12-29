package demo.cassandra.net;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Connection {
    private static Cluster cluster;
    private static Properties clusterConfig;
    private static final String configFileName = "cluster_config.properties";

    public Connection() {
        clusterConfig = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(configFileName);
        if (inputStream != null){
            try {
                clusterConfig.load(inputStream);
                System.out.println("ClusterConfig: " + clusterConfig.toString());
            } catch (IOException e) {
                throw  new RuntimeException(e.getLocalizedMessage());
            }
        }
    }

    public Session getConnection() {
        // Connection to the cluster and keyspace "demo"
        cluster = Cluster.builder()
                .addContactPoint(clusterConfig.getProperty("cluster.url"))
                .withPort(Integer.parseInt(clusterConfig.getProperty("cluster.port")))
                .withCredentials(clusterConfig.getProperty("user.name"), clusterConfig.getProperty("user.password"))
                .build();
        return cluster.connect(clusterConfig.getProperty("keyspace.name"));
    }
}