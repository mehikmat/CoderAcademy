package ontology;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;

import java.util.Iterator;

/**
 * Created by hdhamee on 12/29/15.
 */
public class OntologyDemo {
    public static void main(String[] args) {
        // create the base model
        String SOURCE = "data/ontology.rdf";
        String NS = SOURCE + "#";
        OntModel base = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM );
        base.read( SOURCE, "RDF/XML" );

        // create the reasoning model using the base
        OntModel inf = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF, base);

        // create a dummy paper for this example
        OntClass paper = inf.getOntClass( NS + "Paper" );
        Individual p1 = inf.createIndividual( NS + "paper1", paper );

        // list the asserted types
        for (Iterator<Resource> i = p1.listRDFTypes(false); i.hasNext(); ) {
            System.out.println( p1.getURI() + " is asserted in class " + i.next() );
        }

        // list the inferred types
        /*p1 = inf.getIndividual( NS + "paper1" );
        for (Iterator<Resource> i = p1.listRDFTypes(false); i.hasNext(); ) {
            System.out.println( p1.getURI() + " is inferred to be in class " + i.next() );
        }*/
    }
}
