import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.util.FileManager;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by hdhamee on 1/4/16.
 */
public class PlantInferenceServlet extends HttpServlet {
    static final String rel = "http://jacobnibu.info/ont/garden#";
    static Model tdb;
    static String owlFile = "input/garden.owl";
    static String dataFile = "input/plants.rdf";
    static String logFile = "logs/log4j.properties";
    static InfModel infmodel;
    static String directory = "tdb";
    static ResultSet results;
    static QueryExecution qexec;
    static String endPoint = "http://localhost:3030/plants/query";

    @Override
    public void init(ServletConfig config) throws ServletException {
        PropertyConfigurator.configure(logFile);
        /**
         * These needed if we are creating in-app TDB store
         */

        /*
        infmodel = createInfModel();
        tdb = createDBModel(directory,infmodel);*/
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        String queryProperties = request.getParameter("queryProperties");
        if (queryProperties != null) {
            queryPlantProperties();
            out.println("<h3>Properties of plants (from RDF dataset)</h3>");
            while (results.hasNext()) {
                QuerySolution row = results.next();
                RDFNode thing = row.get("Properties_of_Plant");
                out.println(thing.toString() + "<br>");
            }
            qexec.close();
        }
        String querySunPref = request.getParameter("querySunPref");
        if (querySunPref != null) {
            queryPlantSunPreference();
            out.println("<h3>Minimum sun preference of plants (inferred property)</h3>");
            while (results.hasNext()) {
                QuerySolution row = results.next();
                RDFNode thing = row.get("Plant");
                Literal label = row.getLiteral("Sun_Preference_Min");
                out.println(thing.toString() + " ... " + "<b><font color=\"blue\">" + label.getString() + "</font></b><br>");
            }
            qexec.close();
        }
    }


    private static void queryPlantProperties() {
        String q = "PREFIX garden: <"+rel+"> " +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
                "SELECT distinct ?Properties_of_Plant "+
                "WHERE {" +
                "?Properties_of_Plant rdfs:domain garden:Plant"+
                "     }";
        executeQuery(q);
    }

    private static void queryPlantSunPreference() {
        String q = "PREFIX garden: <"+rel+"> " +
                "SELECT ?Plant ?Sun_Preference_Min "+
                "WHERE {" +
                "?Plant garden:inFamily ?Family ."+
                "?Family garden:sunMin ?Sun_Preference_Min ."+
                "     }";
        executeQuery(q);
    }

    private static void executeQuery(String q){
        /**
         * For file based model, if the TDB file is on same server
         */

       /*
       Query query = QueryFactory.create(q);
        qexec = QueryExecutionFactory.create(query, tdb);
        */

        /**
         * For SPARQL end point, if the TDB end point is on separate server
         */
        qexec = QueryExecutionFactory.sparqlService(endPoint, q);

        // result of both style
        results = qexec.execSelect();
        System.out.println("\nQuery executed successfully!");
    }

    /* Read ontology from filesystem and store it into database */
    public static Model createDBModel(String directory, InfModel model) {
        // connect store to dataset
        Dataset dataset = TDBFactory.createDataset(directory);
        Model tdb = dataset.getDefaultModel();
        // add the model into the dataset
        tdb.add(model);
        return tdb;

    }

    // this method reads ontology file and RDF file from disk and creates an inference model
    private static InfModel createInfModel(){
        Model schema = FileManager.get().loadModel(owlFile);
        if (schema == null) {
            throw new IllegalArgumentException( "File: " + owlFile + " not found");
        }
        Model data = FileManager.get().loadModel(dataFile);
        if (data == null) {
            throw new IllegalArgumentException( "File: " + dataFile + " not found");
        }
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        reasoner = reasoner.bindSchema(schema);
        InfModel infmodel = ModelFactory.createInfModel(reasoner, data);
        System.out.println("\nInference model created successfully!");
        return infmodel;

    }
}
