package fuseki;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Resource;

/**
 * Created by hdhamee on 12/30/15.
 */
public class FusekiDemo {
    public static void main(String[] args) {
        String queryString = "SELECT ?x WHERE { ?x  <http://www.w3.org/2001/vcard-rdf/3.0#FN>  \"John Smith\" }";
        String endPoint = "http://localhost:3030/person/query";

        QueryExecution qexec = QueryExecutionFactory.sparqlService(endPoint, queryString);
        ResultSet results  = qexec.execSelect() ;

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
