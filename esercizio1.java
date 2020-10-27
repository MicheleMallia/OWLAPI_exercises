/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package owlapi.tutorial.esercizio1;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.coode.owlapi.turtle.TurtleOntologyFormat;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDatatypeDefinitionAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

/**
 *
 * @author michele
 */
public class esercizio1 {
    private static Path testFile;
    public static void main(String[] args) throws OWLOntologyCreationException, IOException, OWLOntologyStorageException{
        
        //create ontology manager
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        
        //create data factory
        OWLDataFactory factory = manager.getOWLDataFactory();
        
        //creo la mia ontologia
        IRI ontologyIRI = IRI.create("http://owlapi.tutorial.mylexicon");
        OWLOntology ontology = manager.createOntology(ontologyIRI);
        
        //creo il riferimento a ontolex e carico l'ontologia nel manager
        IRI ontolexIri = IRI.create("http://www.w3.org/ns/lemon/ontolex");
        IRI lexinfoIri = IRI.create("https://lexinfo.net/ontology/3.0/lexinfo");
        IRI skosIri = IRI.create("http://www.w3.org/2004/02/skos/core");
        IRI limeIri = IRI.create("http://www.w3.org/ns/lemon/lime");
        
        DefaultPrefixManager pm = new DefaultPrefixManager();
        pm.setDefaultPrefix(ontologyIRI + "#");
        pm.setPrefix("ontolex:", "http://www.w3.org/ns/lemon/ontolex#");                        
        pm.setPrefix("skos:", "http://www.w3.org/2004/02/skos/core#");  
        pm.setPrefix("lexinfo:", "https://lexinfo.net/ontology/3.0/lexinfo#");
        pm.setPrefix("lime:", "http://www.w3.org/ns/lemon/lime#");
        
        //Crea un lessico di nome lexicon_it
        OWLClass lexicon = factory.getOWLClass("lime:Lexicon", pm);
        OWLIndividual lexicon_it = factory.getOWLNamedIndividual(ontologyIRI + "#lexicon_it");
        manager.addAxiom(ontology, 
                factory.getOWLClassAssertionAxiom(lexicon, lexicon_it));
        
        //Assegna la stringa “it” alla proprietà lime:language
        OWLDataProperty language = factory.getOWLDataProperty("lime:language", pm);
        OWLLiteral l_it = factory.getOWLLiteral("it", "");
        OWLDataPropertyAssertionAxiom label_it = factory.getOWLDataPropertyAssertionAxiom(language, lexicon_it, l_it);
        manager.addAxiom(ontology, label_it);
        
        //Assegna la stringa “http://www.lexinfo.net/ontologies/3.0/lexinfo” 
        //alla proprità lime:linguisticCatalog
        OWLDataProperty linguisticCatalog = factory.getOWLDataProperty("lime:linguisticCatalog", pm);
        OWLLiteral l_lexinfo = factory.getOWLLiteral("http://www.lexinfo.net/ontologies/3.0/lexinfo", "");
        OWLDataPropertyAssertionAxiom label_lexinfo = factory.getOWLDataPropertyAssertionAxiom(linguisticCatalog, lexicon_it, l_lexinfo);
        manager.addAxiom(ontology, label_lexinfo);
        
        //Crea una lexical entry matto 
        //(regola nome istanza: [lemma]_pos_lang_entry es: matto_adjective_it_entry)
        OWLClass lexicalEntry = factory.getOWLClass("ontolex:LexicalEntry", pm);
        OWLIndividual matto_adjective_it_entry = factory.getOWLNamedIndividual(ontologyIRI + "#matto_adjective_it_entry");
        manager.addAxiom(ontology, 
                factory.getOWLClassAssertionAxiom(lexicalEntry, matto_adjective_it_entry));
        
        //Crea la proprietà lime:entry che collega il lessico all’entrata
        OWLObjectProperty entry = factory.getOWLObjectProperty("lime:entry", pm);
        OWLObjectPropertyAssertionAxiom entry_to_lexicon = factory.getOWLObjectPropertyAssertionAxiom(entry, lexicon_it, matto_adjective_it_entry);
        manager.addAxiom(ontology, entry_to_lexicon);
        
        //Crea il lemma matto 
        //(regola nome istanza: [lemma]_pos_lang_lemma es: matto_adjective_it_lemma)
        OWLDataProperty writtenRep = factory.getOWLDataProperty("ontolex:writtenRep", pm);
        OWLClass form = factory.getOWLClass("ontolex:Form", pm);               
        OWLIndividual matto_adjective_it_lemma = factory.getOWLNamedIndividual(ontologyIRI + "#matto_adjective_it_lemma");        
        manager.addAxiom(ontology, 
                factory.getOWLClassAssertionAxiom(form, matto_adjective_it_lemma));
        OWLLiteral l_matto_lemma = factory.getOWLLiteral("matto");
        OWLDataPropertyAssertionAxiom label_matto_lemma = factory.getOWLDataPropertyAssertionAxiom(writtenRep, matto_adjective_it_lemma, l_matto_lemma);
        manager.addAxiom(ontology, label_matto_lemma);
        
        //Crea la forma matta 
        //(regola nome istanza: [lemma]_pos_lang_[form]_form es: matto_adjective_it_matta_form)
        
        OWLIndividual matto_adjective_it_matta_form = factory.getOWLNamedIndividual(ontologyIRI + "#matto_adjective_it_matta_form");      
        manager.addAxiom(ontology, 
                factory.getOWLClassAssertionAxiom(form, matto_adjective_it_matta_form));
        OWLLiteral l_matta_form = factory.getOWLLiteral("matta");
        OWLDataPropertyAssertionAxiom label_matta_lemma = factory.getOWLDataPropertyAssertionAxiom(writtenRep, matto_adjective_it_matta_form, l_matta_form);
        manager.addAxiom(ontology, label_matta_lemma);
                
        //Crea la forma matti 
        //(regola nome istanza: [lemma]_pos_lang_[form]_form es: matto_adjective_it_matti_form)
        OWLIndividual matto_adjective_it_matti_form = factory.getOWLNamedIndividual(ontologyIRI + "#matto_adjective_it_matti_form");
        manager.addAxiom(ontology, 
                factory.getOWLClassAssertionAxiom(form, matto_adjective_it_matti_form));
        OWLLiteral l_matti_form = factory.getOWLLiteral("matti");
        OWLDataPropertyAssertionAxiom label_matti_form = factory.getOWLDataPropertyAssertionAxiom(writtenRep, matto_adjective_it_matti_form, l_matti_form);
        manager.addAxiom(ontology, label_matti_form);
        
        //Crea la forma matte 
        //(regola nome istanza: [lemma]_pos_lang_[form]_form es: matto_adjective_it_matte_form)
        OWLIndividual matto_adjective_it_matte_form = factory.getOWLNamedIndividual(ontologyIRI + "#matto_adjective_it_matte_form");
        manager.addAxiom(ontology, 
                factory.getOWLClassAssertionAxiom(form, matto_adjective_it_matte_form));
        OWLLiteral l_matte_form = factory.getOWLLiteral("matte");
        OWLDataPropertyAssertionAxiom label_matte_form = factory.getOWLDataPropertyAssertionAxiom(writtenRep, matto_adjective_it_matte_form, l_matte_form);
        manager.addAxiom(ontology, label_matte_form);
        
        //Crea due sensi di matto 
        //(regola [lemma]_pos_lang_sensen es: matto_adjective_it_sense1 e matto_adjective_it_sense2
        //(all’individuo del senso aggiungere la dataproperty skos:definition con la stringa di definizione del senso)
        OWLObjectProperty sense = factory.getOWLObjectProperty("ontolex:sense", pm);
        OWLDataProperty definition = factory.getOWLDataProperty("skos:definition", pm);
        
        OWLIndividual matto_adjective_it_sense1 = factory.getOWLNamedIndividual(ontologyIRI + "#matto_adjective_it_sense1");
        OWLLiteral sense_1 = factory.getOWLLiteral("di persona che non possiede, o non possiede interamente, l’uso della ragione");
        OWLDataPropertyAssertionAxiom sense_def_to_matto_sense_1 = factory.getOWLDataPropertyAssertionAxiom(definition, matto_adjective_it_sense1, sense_1);
        OWLObjectPropertyAssertionAxiom sense1_to_matto_entry = factory.getOWLObjectPropertyAssertionAxiom(sense, matto_adjective_it_entry, matto_adjective_it_sense1);
        manager.addAxiom(ontology, sense_def_to_matto_sense_1);
        manager.addAxiom(ontology, sense1_to_matto_entry);
        
        
        OWLIndividual matto_adjective_it_sense2 = factory.getOWLNamedIndividual(ontologyIRI + "#matto_adjective_it_sense2");
        OWLLiteral sense_2 = factory.getOWLLiteral("Di parte del corpo, e in partic. di un arto, che, per non essere del tutto sano, non assolve come dovrebbe la sua funzione");
        OWLDataPropertyAssertionAxiom sense_def_to_matto_sense_2 = factory.getOWLDataPropertyAssertionAxiom(definition, matto_adjective_it_sense2, sense_2);
        OWLObjectPropertyAssertionAxiom sense2_to_matto_entry = factory.getOWLObjectPropertyAssertionAxiom(sense, matto_adjective_it_entry, matto_adjective_it_sense2);
        manager.addAxiom(ontology, sense_def_to_matto_sense_2);
        manager.addAxiom(ontology, sense2_to_matto_entry);
        
        //Crea le opportune proprietà ontolex:
        //canonicalForm, ontolex:otherForm, ontolex:sense                 
        OWLObjectProperty canonicalForm = factory.getOWLObjectProperty("ontolex:canonicalForm", pm);
        OWLObjectProperty otherForm = factory.getOWLObjectProperty("ontolex:otherForm", pm);
        OWLObjectPropertyAssertionAxiom matto_lemma_canonicalForm_matto_lex = factory.getOWLObjectPropertyAssertionAxiom(canonicalForm, matto_adjective_it_entry, matto_adjective_it_lemma);
        manager.addAxiom(ontology, matto_lemma_canonicalForm_matto_lex);
        
        OWLObjectPropertyAssertionAxiom matta_otherForm_matto_lex = factory.getOWLObjectPropertyAssertionAxiom(otherForm, matto_adjective_it_entry, matto_adjective_it_matta_form);
        OWLObjectPropertyAssertionAxiom matti_otherForm_matto_lex = factory.getOWLObjectPropertyAssertionAxiom(otherForm, matto_adjective_it_entry, matto_adjective_it_matti_form);
        OWLObjectPropertyAssertionAxiom matte_otherForm_matto_lex = factory.getOWLObjectPropertyAssertionAxiom(otherForm, matto_adjective_it_entry, matto_adjective_it_matte_form);
        manager.addAxiom(ontology, matta_otherForm_matto_lex);
        manager.addAxiom(ontology, matti_otherForm_matto_lex);
        manager.addAxiom(ontology, matte_otherForm_matto_lex);
        
        testFile = Files.createTempFile("prov", ".ttl");

        TurtleOntologyFormat turtleFormat = new TurtleOntologyFormat();
        turtleFormat.copyPrefixesFrom(pm);
        turtleFormat.setDefaultPrefix(ontologyIRI + "#");

        try (OutputStream outputStream = Files.newOutputStream(testFile)) {
                manager.saveOntology(ontology, turtleFormat,				
                                outputStream);
        }
        System.out.println(testFile);

        //Utilizzando EntitySearcher (vedi esempi in LexiconModel.java) stampare a video:
        //I nomi degli individui delle entrate lessicali con la lingua (es: matto_adjective_it_entry - it)        
        System.out.println("Nome dell'entrata lessicale con annessa lingua:");
        for(OWLIndividual i : EntitySearcher.getIndividuals(lexicalEntry, ontology).collect(Collectors.toList())){
            
            String entry_name = i.toStringID().replace(pm.getDefaultPrefix(), "");                     
            String lang = EntitySearcher.getDataPropertyValues(lexicon_it, language, ontology).map(e -> e.toString()).reduce("", String::concat);
                       
            System.out.println(entry_name + " - " + lang +"\n");
            
            
        }
        
        //Per ognuno di essi stampare i nomi degli individui 
        //dei lemmi con la loro written representation
        //(es: matto_adjective_it_lemma - matto)
        System.out.println("Nome del lemma con annessa rappresentazione scritta:");
        for(OWLIndividual i : EntitySearcher.getIndividuals(form, ontology).collect(Collectors.toList())){
            String lemma_name = i.toStringID().replace(pm.getDefaultPrefix(), "");
            String lemma_writtenRep = EntitySearcher.getDataPropertyValues(i, writtenRep, ontology).map(e -> e.toString()).reduce("", String::concat);
            
            System.out.println(lemma_name + " - " + lemma_writtenRep + "\n");
        } 
        
        //stampare le other forms (es: matto_adjective_it_matti_form - matti, ecc….)
        System.out.println("Nome delle altre forme con annesse rappresentazioni scritte:");
        for(OWLIndividual i : EntitySearcher.getObjectPropertyValues(matto_adjective_it_entry, otherForm, ontology).collect(Collectors.toList())){
            String form_name = i.toStringID().replace(pm.getDefaultPrefix(), "");
            String form_writtenRep = EntitySearcher.getDataPropertyValues(i, writtenRep, ontology).map(e -> e.toString()).reduce("", String::concat);
            
            System.out.println(form_name + " - " + form_writtenRep);
        }        
        System.out.println("\n");
        
        //stampare i nomi degli individui dei sensi
        System.out.println("Nome degli individui dei sensi:");
        for(OWLIndividual i : EntitySearcher.getObjectPropertyValues(matto_adjective_it_entry, sense, ontology).collect(Collectors.toList())){
            String sense_name = i.toStringID().replace(pm.getDefaultPrefix(), "");
            String sense_definition = EntitySearcher.getDataPropertyValues(i, definition, ontology).map(e -> e.toString()).reduce("", String::concat);
            
            System.out.println(sense_name + " - " + sense_definition);
        }        
    }
}
