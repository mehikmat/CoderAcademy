package rdf;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.VCARD;

/**
 * Created by hdhamee on 12/21/15.
 */
public class RDFAPIDemo {
    public static void main(String[] args) {
        RDFAPIDemo rdfapiDemo = new RDFAPIDemo();
        rdfapiDemo.rdfDemo();
    }

    private void rdfDemo() {
        // some definitions
        String personURI = "http://somewhere/JohnSmith";
        String givenName = "John";
        String familyName = "Smith";
        String fullName = givenName + " " + familyName;

        // create an empty Model
        Model model = ModelFactory.createDefaultModel();

        // create the resource
        // and add the properties cascading style
        Resource johnSmith = model.createResource(personURI)
                .addProperty(VCARD.FN, fullName)
                .addProperty(VCARD.N,model.createResource()
                                .addProperty(VCARD.Given, givenName)
                                .addProperty(VCARD.Family, familyName));
        System.out.println("\nResource: " + johnSmith.toString() + "\n");



        // list the statements in the Model
        StmtIterator iter = model.listStatements();
        // print out the predicate, subject and object of each statement
        while (iter.hasNext()) {
            Statement stmt      = iter.nextStatement();  // get next statement
            Resource  subject   = stmt.getSubject();     // get the subject
            Property predicate = stmt.getPredicate();   // get the predicate
            RDFNode object    = stmt.getObject();      // get the object

            System.out.print(subject.toString());
            System.out.print(" " + predicate.toString() + " ");
            if (object instanceof Resource) {
                System.out.print(object.toString());
            } else {
                // object is a literal
                System.out.print(" \"" + object.toString() + "\"");
            }

            System.out.println(" .");
        }

        // Writing model to output stream
        // now write the model in XML form to a file
        System.out.printf("\n\nRDF model as XML:\n\n");
        model.write(System.out);
    }
}
