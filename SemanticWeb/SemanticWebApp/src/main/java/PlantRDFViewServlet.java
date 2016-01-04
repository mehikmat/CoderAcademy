import org.apache.jena.query.*;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.util.FileManager;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * Created by hdhamee on 1/4/16.
 */
public class PlantRDFViewServlet  extends HttpServlet {
    static final String NS = "http://jacobnibu.info/ont/garden/";
    static final String rel = "http://jacobnibu.info/ont/garden#";
    static Model tdb;
    static String owlFile = "input/garden.owl";
    static String dataFile = "input/plants.rdf";
    static String logFile = "logs/log4j.properties";
    static InfModel infmodel;
    static String directory = "tdb";
    static ResultSet results;
    static QueryExecution qexec;
    static String hardinessZone="1";
    static String sunPref="part sun";
    static String harvestDur="1";

    @Override
    public void init() throws ServletException {
        PropertyConfigurator.configure(logFile);
        infmodel = createInfModel();
        tdb = createDBModel(directory,infmodel);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        String search = req.getParameter("find");
        if(search!=null && search.equals("Search")){
            hardinessZone = req.getParameter("hardinessZone");
            sunPref = req.getParameter("sunPref");
            harvestDur = req.getParameter("harvestDur");
            findPlant();
            if(!results.hasNext()){
                out.println("<p class=\"alert\">No plants match your search... please try again!</p>");
            }else{
                out.println("<h3>Best plants for the season:</h3><table border=1 cellpadding=5><tr><th>Plant</th><th>Family</th></tr>");
                while (results.hasNext()) {
                    QuerySolution row= results.next();
                    RDFNode plant= row.get("Plant");
                    RDFNode family= row.get("Family");
                    out.println("<tr><td style=\"min-width:80px\">"+plant.toString().replace("http://jacobnibu.info/garden/"," ")+"</td><td style=\"min-width:100px\">"+
                            family.toString().replace("http://jacobnibu.info/garden/"," ")+"</td></tr>");
                }qexec.close();
                out.println("</table>");
            }
        }
    }

    private static void findPlant() {
        String q = "PREFIX garden: <"+rel+"> " +
                "SELECT ?Plant ?Family "+
                "WHERE {" +
                "?Plant garden:hardinessZoneMin \""+hardinessZone+"\" ."+
                "?Plant garden:harvestDurationMax \""+harvestDur+"\" ."+
                "?Plant garden:inFamily ?Family ."+
                "?Family garden:sunMin \""+sunPref+"\" ."+
                "     }";
        executeQuery(q);
    }

    private static void executeQuery(String q){
        Query query = QueryFactory.create(q);
        qexec = QueryExecutionFactory.create(query, tdb);
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
        InfModel infmodel = ModelFactory.createInfModel(reasoner,data);
        System.out.println("\nInference model created successfully!");
        return infmodel;
    }
}
