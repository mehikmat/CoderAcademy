package rdf;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.VCARD;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
        System.out.println("Resource.toString() >>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("\nResource: " + johnSmith.toString() + "\n");

        // list the statements in the Model
        StmtIterator iter = model.listStatements();
        // print out the predicate, subject and object of each statement
        System.out.println("Statements >>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
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
        System.out.printf("\n\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>> RDF model as XML:\n\n");
        model.write(System.out);

        // now write the model in XML form to a file
        System.out.println("RDF to File>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        File file=null;
        try {
            file = new File("RDF-XML-ABBREV.rdf");
            model.write(new FileOutputStream(file), "RDF/XML-ABBREV");
        } catch (IOException e) {
            e.printStackTrace();
        }


        // now write the model in N-TRIPLES form to a file
        System.out.println("RDF N-tripels to File>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        File file1;
        try {
            file1 = new File("N-TRIPLES.rdf");
            model.write(new FileOutputStream(file1), "N-TRIPLES");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
