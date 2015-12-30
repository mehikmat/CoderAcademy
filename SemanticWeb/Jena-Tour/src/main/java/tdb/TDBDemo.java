package tdb;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.vocabulary.VCARD;

import java.util.Iterator;

/**
 * Created by hdhamee on 12/30/15.
 */
public class TDBDemo {
    public static void main(String[] args) {
        // Make a TDB-backed dataset/model store
        String directory = "data/MyTDBDatabases/Dataset1" ;
        Dataset dataset = TDBFactory.createDataset(directory) ;

        dataset.begin(ReadWrite.WRITE) ;
        Model model = dataset.getNamedModel("person") ;
        createModel(model);
        System.out.println("Writing model to TDB: " + model.toString());
        dataset.commit();
        dataset.end() ;
        model.close();

        // Get model inside the transaction
        dataset.begin(ReadWrite.READ) ;
        model = dataset.getNamedModel("person") ;
        System.out.println("Reading model from TDB: " + model.toString());
        Iterator<String> graphNames = dataset.listNames();
        while (graphNames.hasNext()) {
            String graphName = graphNames.next();
            System.out.println("Model Name: " + graphName);
        }
        dataset.end() ;
        model.close();

        // Assembler way: Make a TDB-back Jena model in the named directory.
        // This way, you can change the model being used without changing the code.
        // The assembler file is a configuration file.
        // The same assembler description will work in Fuseki.
        String assemblerFile = "data/MyTDBDatabases/tdb-assembler.ttl" ;
        Dataset dataset1 = TDBFactory.assembleDataset(assemblerFile);

        //dataset1.begin(ReadWrite.READ) ;
        // Get model inside the transaction
        //Model model1 = dataset1.getDefaultModel() ;
        //dataset1.end() ;

    }

    private static void createModel(Model model){
        // some definitions
        String personURI = "http://somewhere/JohnSmith";
        String givenName = "John";
        String familyName = "Smith";
        String fullName = givenName + " " + familyName;

        // create the resource
        // and add the properties cascading style
        Resource johnSmith = model.createResource(personURI)
                .addProperty(VCARD.FN, fullName)
                .addProperty(VCARD.N,model.createResource()
                        .addProperty(VCARD.Given, givenName)
                        .addProperty(VCARD.Family, familyName));
    }
}
