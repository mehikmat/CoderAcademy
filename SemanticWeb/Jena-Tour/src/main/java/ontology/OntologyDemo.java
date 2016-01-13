package ontology;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;

import java.util.Iterator;

/**
 * By default, the file path_name is used for prefix name space for
 * all the relative URIS in rdf document. If there is a xmlns:base
 * namespace is defined then it will be used for relative URIs.
 *
 * xml:base="http://www.eswc2006.org/technologies/ontology
 *
 */
public class OntologyDemo {
    public static void main(String[] args) {

        // create the base model
        String SOURCE = "data/ontology.rdf";
        String basens = "http://www.eswc2006.org/technologies/ontology";
        String NS = basens + "#";
        OntModel base = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM );
        base.read(SOURCE, "RDF/XML" );

        // create the reasoning model using the base
        OntModel inf = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_MICRO_RULE_INF, base);

        // create a dummy paper for this example
        OntClass paper = inf.getOntClass( NS + "Paper" );
        Individual p1 = inf.createIndividual( NS + "paper1", paper );

        // list the asserted types
        Iterator<Resource> i = p1.listRDFTypes(false);
        while (i.hasNext()){
            System.out.println( p1.getURI() + " is asserted in class " + i.next() );
        }

        // list the inferred types
        p1 = inf.getIndividual( NS + "paper1" );
        Iterator<Resource> ii = p1.listRDFTypes(false);
        System.out.println("\n\n");
        while(ii.hasNext()) {
            System.out.println( p1.getURI() + " is inferred to be in class " + ii.next() );
        }
    }
}
