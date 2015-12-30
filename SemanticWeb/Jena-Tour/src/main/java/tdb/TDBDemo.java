package tdb;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;

/**
 * Created by hdhamee on 12/30/15.
 */
public class TDBDemo {
    public static void main(String[] args) {
        // Make a TDB-backed dataset/model store
        String directory = "data/MyTDBDatabases/Dataset1" ;
        Dataset dataset = TDBFactory.createDataset(directory) ;
        dataset.begin(ReadWrite.READ) ;

        // Get model inside the transaction
        Model model = dataset.getDefaultModel() ;
        dataset.end() ;
        dataset.begin(ReadWrite.WRITE) ;
        model = dataset.getDefaultModel() ;
        dataset.end() ;
    }
}
