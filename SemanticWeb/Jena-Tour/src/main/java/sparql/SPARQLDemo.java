package sparql;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.FileManager;

import java.io.InputStream;

/**
 * Created by hdhamee on 12/29/15.
 */
public class SPARQLDemo {
    public static void main(String[] args) {

        // Reading RDF model
        // create an empty model
        Model vcModel = ModelFactory.createDefaultModel();

        // use the FileManager to find the input file
        InputStream in = FileManager.get().open("data/vc-db.rdf");
        if (in == null) {
            throw new IllegalArgumentException("File: " + "vc-db.rdf" + " not found");
        }

        // read the RDF/XML file
        vcModel.read(in, null);

        String queryString = "SELECT ?x WHERE { ?x  <http://www.w3.org/2001/vcard-rdf/3.0#FN>  \"John Smith\" }";
        //String queryString = "SELECT ?x WHERE { ?x  []  \"John Smith\" }";
        Query query = QueryFactory.create(queryString) ;
        QueryExecution qexec = QueryExecutionFactory.create(query, vcModel) ;
        ResultSet results  = qexec.execSelect() ;

        // Formatting result instead of while loop
        System.out.println("Using formatter to display result: ");
        ResultSet results1 = ResultSetFactory.copyResults(results) ;
        ResultSetFormatter.out(System.out, results1, query);

        // OR use while loop
        System.out.println("Using while loop to display result: ");
        while(results.hasNext()){
            QuerySolution soln = results.nextSolution() ;
            Resource r = soln.getResource("x") ; // Get a result variable - must be a resource
            System.out.println(r.toString());
        }

        qexec.close() ;
    }
}
