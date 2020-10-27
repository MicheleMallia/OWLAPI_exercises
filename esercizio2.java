/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package owlapi.tutorial.esercizio2;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import static org.apache.http.client.fluent.Form.form;
import org.coode.owlapi.turtle.TurtleOntologyFormat;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

/**
 *
 * @author michele
 */
public class esercizio2 {
    private static Path testFile;
    public static void main(String[] args) throws OWLOntologyCreationException, IOException, OWLOntologyStorageException{
        
        //create ontology manager
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        
        //create data factory
        OWLDataFactory factory = manager.getOWLDataFactory();
        
        //creo la mia ontologia
        IRI ontologyIRI = IRI.create("http://owlapi.tutorial.mylexicon");
        OWLOntology ontology = manager.createOntology(ontologyIRI);
        
        IRI ontolexIri = IRI.create("http://www.w3.org/ns/lemon/ontolex");
        
        
        DefaultPrefixManager pm = new DefaultPrefixManager();
        pm.setDefaultPrefix(ontologyIRI + "#");
        pm.setPrefix("ontolex:", "http://www.w3.org/ns/lemon/ontolex#");                        
        pm.setPrefix("skos:", "http://www.w3.org/2004/02/skos/core#");  
        pm.setPrefix("lexinfo:", "https://lexinfo.net/ontology/3.0/lexinfo#");
        pm.setPrefix("lime:", "http://www.w3.org/ns/lemon/lime#");
        pm.setPrefix("vartrans:", "http://www.w3.org/ns/lemon/vartrans#");
        pm.setPrefix("NS_TR:", "http://purl.org/net/translation-categories#");
        pm.setPrefix("synsem:", "http://www.w3.org/ns/lemon/synsem#");
        
        //Crea un lessico di nome lexicon_it
        OWLClass lexicon = factory.getOWLClass("lime:Lexicon", pm);
        OWLIndividual lexicon_it = factory.getOWLNamedIndividual(ontologyIRI + "#lexicon_it");
        manager.addAxiom(ontology, 
                factory.getOWLClassAssertionAxiom(lexicon, lexicon_it));               
        
        //Assegna la stringa “it” alla proprietà lime:language
        OWLDataProperty language = factory.getOWLDataProperty("lime:language", pm);
        OWLLiteral l_it = factory.getOWLLiteral("it");
        OWLDataPropertyAssertionAxiom label_it = factory.getOWLDataPropertyAssertionAxiom(language, lexicon_it, l_it);
        manager.addAxiom(ontology, label_it);
        
        //Crea un lessico di nome lexicon_en
        OWLIndividual lexicon_en = factory.getOWLNamedIndividual(ontologyIRI + "#lexicon_en");
        manager.addAxiom(ontology, 
                factory.getOWLClassAssertionAxiom(lexicon, lexicon_en));
        
        //Assegna la stringa “en” alla proprietà lime:language
        OWLLiteral l_en = factory.getOWLLiteral("en");
        OWLDataPropertyAssertionAxiom label_en = factory.getOWLDataPropertyAssertionAxiom(language, lexicon_en, l_en);
        manager.addAxiom(ontology, label_en);
        
        //Assegna la stringa “http://www.lexinfo.net/ontologies/3.0/lexinfo” alla proprità lime:linguisticCatalog
        OWLDataProperty linguisticCatalog = factory.getOWLDataProperty("lime:linguisticCatalog", pm);
        OWLLiteral l_lexinfo = factory.getOWLLiteral("http://www.lexinfo.net/ontologies/3.0/lexinfo");
        OWLDataPropertyAssertionAxiom label_lexinfo_it = factory.getOWLDataPropertyAssertionAxiom(linguisticCatalog, lexicon_it, l_lexinfo);
        OWLDataPropertyAssertionAxiom label_lexinfo_en = factory.getOWLDataPropertyAssertionAxiom(linguisticCatalog, lexicon_en, l_lexinfo);
        manager.addAxiom(ontology, label_lexinfo_it);
        manager.addAxiom(ontology, label_lexinfo_en);
        
        //Crea una lexical entry pazzo (regola nome istanza: [lemma]_pos_lang_entry es: pazzo_adjective_it_entry)
        OWLClass lexicalEntry = factory.getOWLClass("ontolex:LexicalEntry", pm);
        OWLIndividual pazzo_adjective_it_entry = factory.getOWLNamedIndividual(ontologyIRI + "#pazzo_adjective_it_entry");
        manager.addAxiom(ontology, 
                factory.getOWLClassAssertionAxiom(lexicalEntry, pazzo_adjective_it_entry)
        );
        
        //Crea una lexical entry crazy (regola nome istanza: [lemma]_pos_lang_entry es: crazy_adjective_it_entry)
        OWLIndividual crazy_adjective_en_entry = factory.getOWLNamedIndividual(ontologyIRI + "#crazy_adjective_en_entry");
        manager.addAxiom(ontology, 
                factory.getOWLClassAssertionAxiom(lexicalEntry, crazy_adjective_en_entry)
        );
        
        //Crea una lexical entry matto (regola nome istanza: [lemma]_pos_lang_entry es: matto_adjective_it_entry)
        OWLIndividual matto_adjective_it_entry = factory.getOWLNamedIndividual(ontologyIRI + "#matto_adjective_it_entry");
        manager.addAxiom(ontology, 
                factory.getOWLClassAssertionAxiom(lexicalEntry, matto_adjective_it_entry));
        
        //Crea una lexical entry madre (regola nome istanza: [lemma]_pos_lang_entry es: madre_noun_it_entry)
        OWLIndividual madre_noun_it_entry = factory.getOWLNamedIndividual(ontologyIRI + "#madre_noun_it_entry");
        manager.addAxiom(ontology, 
                factory.getOWLClassAssertionAxiom(lexicalEntry, madre_noun_it_entry));
        
        //Crea la proprietà lime:entry che collega il lessico all’entrata
        OWLObjectProperty entry = factory.getOWLObjectProperty("lime:entry", pm);
        OWLObjectPropertyAssertionAxiom pazzo_to_lexicon_it = factory.getOWLObjectPropertyAssertionAxiom(entry, lexicon_it, pazzo_adjective_it_entry);
        manager.addAxiom(ontology, pazzo_to_lexicon_it);
        OWLObjectPropertyAssertionAxiom crazy_to_lexicon_en = factory.getOWLObjectPropertyAssertionAxiom(entry, lexicon_en, crazy_adjective_en_entry);
        manager.addAxiom(ontology, crazy_to_lexicon_en);
        OWLObjectPropertyAssertionAxiom matto_to_lexicon = factory.getOWLObjectPropertyAssertionAxiom(entry, lexicon_it, matto_adjective_it_entry);
        manager.addAxiom(ontology, matto_to_lexicon);
        OWLObjectPropertyAssertionAxiom madre_to_lexicon = factory.getOWLObjectPropertyAssertionAxiom(entry, lexicon_it, madre_noun_it_entry);
        manager.addAxiom(ontology, madre_to_lexicon);
        
        //Crea il lemma matto 
        //(regola nome istanza: [lemma]_pos_lang_lemma es: matto_adjective_it_lemma)
        OWLDataProperty writtenRep = factory.getOWLDataProperty("ontolex:writtenRep", pm);
        OWLClass word = factory.getOWLClass("ontolex:Word", pm); 
        OWLObjectProperty canonicalForm = factory.getOWLObjectProperty("ontolex:canonicalForm", pm);
        OWLIndividual matto_adjective_it_lemma = factory.getOWLNamedIndividual(ontologyIRI + "#matto_adjective_it_lemma");        
        manager.addAxiom(ontology, 
                factory.getOWLClassAssertionAxiom(word, matto_adjective_it_lemma));
        OWLLiteral l_matto_lemma = factory.getOWLLiteral("matto");
        OWLDataPropertyAssertionAxiom label_matto_lemma = factory.getOWLDataPropertyAssertionAxiom(writtenRep, matto_adjective_it_lemma, l_matto_lemma);
        manager.addAxiom(ontology, label_matto_lemma);
           
        //Crea la forma matta 
        //(regola nome istanza: [lemma]_pos_lang_[form]_form es: matto_adjective_it_matta_form)
        
        OWLIndividual matto_adjective_it_matta_form = factory.getOWLNamedIndividual(ontologyIRI + "#matto_adjective_it_matta_form");      
        manager.addAxiom(ontology, 
                factory.getOWLClassAssertionAxiom(word, matto_adjective_it_matta_form));
        OWLLiteral l_matta_form = factory.getOWLLiteral("matta");
        OWLDataPropertyAssertionAxiom label_matta_lemma = factory.getOWLDataPropertyAssertionAxiom(writtenRep, matto_adjective_it_matta_form, l_matta_form);
        manager.addAxiom(ontology, label_matta_lemma);
                
        //Crea la forma matti 
        //(regola nome istanza: [lemma]_pos_lang_[form]_form es: matto_adjective_it_matti_form)
        OWLIndividual matto_adjective_it_matti_form = factory.getOWLNamedIndividual(ontologyIRI + "#matto_adjective_it_matti_form");
        manager.addAxiom(ontology, 
                factory.getOWLClassAssertionAxiom(word, matto_adjective_it_matti_form));
        OWLLiteral l_matti_form = factory.getOWLLiteral("matti");
        OWLDataPropertyAssertionAxiom label_matti_form = factory.getOWLDataPropertyAssertionAxiom(writtenRep, matto_adjective_it_matti_form, l_matti_form);
        manager.addAxiom(ontology, label_matti_form);
        
        //Crea la forma matte 
        //(regola nome istanza: [lemma]_pos_lang_[form]_form es: matto_adjective_it_matte_form)
        OWLIndividual matto_adjective_it_matte_form = factory.getOWLNamedIndividual(ontologyIRI + "#matto_adjective_it_matte_form");
        manager.addAxiom(ontology, 
                factory.getOWLClassAssertionAxiom(word, matto_adjective_it_matte_form));
        OWLLiteral l_matte_form = factory.getOWLLiteral("matte");
        OWLDataPropertyAssertionAxiom label_matte_form = factory.getOWLDataPropertyAssertionAxiom(writtenRep, matto_adjective_it_matte_form, l_matte_form);
        manager.addAxiom(ontology, label_matte_form);
        
        OWLObjectProperty otherForm = factory.getOWLObjectProperty("ontolex:otherForm", pm);
        OWLObjectPropertyAssertionAxiom matto_lemma_canonicalForm_matto_lex = factory.getOWLObjectPropertyAssertionAxiom(canonicalForm, matto_adjective_it_entry, matto_adjective_it_lemma);
        manager.addAxiom(ontology, matto_lemma_canonicalForm_matto_lex);
        
        OWLObjectPropertyAssertionAxiom matta_otherForm_matto_lex = factory.getOWLObjectPropertyAssertionAxiom(otherForm, matto_adjective_it_entry, matto_adjective_it_matta_form);
        OWLObjectPropertyAssertionAxiom matti_otherForm_matto_lex = factory.getOWLObjectPropertyAssertionAxiom(otherForm, matto_adjective_it_entry, matto_adjective_it_matti_form);
        OWLObjectPropertyAssertionAxiom matte_otherForm_matto_lex = factory.getOWLObjectPropertyAssertionAxiom(otherForm, matto_adjective_it_entry, matto_adjective_it_matte_form);
        manager.addAxiom(ontology, matta_otherForm_matto_lex);
        manager.addAxiom(ontology, matti_otherForm_matto_lex);
        manager.addAxiom(ontology, matte_otherForm_matto_lex);
        
        //Crea il lemma pazzo (regola nome istanza: [lemma]_pos_lang_lemma es: pazzo_adjective_it_lemma)                      
        OWLIndividual pazzo_adjective_it_lemma = factory.getOWLNamedIndividual(ontologyIRI+"#pazzo_adjective_it_lemma");
        manager.addAxiom(ontology, 
                factory.getOWLClassAssertionAxiom(word, pazzo_adjective_it_lemma)
        );
        OWLLiteral l_pazzo_lemma = factory.getOWLLiteral("pazzo");
        OWLDataPropertyAssertionAxiom label_pazzo_lemma = factory.getOWLDataPropertyAssertionAxiom(writtenRep, pazzo_adjective_it_lemma, l_pazzo_lemma);
        manager.addAxiom(ontology, label_pazzo_lemma);
        
        OWLObjectPropertyAssertionAxiom pazzo_lemma_canonicalForm_pazzo_lex = factory.getOWLObjectPropertyAssertionAxiom(canonicalForm, pazzo_adjective_it_entry, pazzo_adjective_it_lemma);
        manager.addAxiom(ontology, pazzo_lemma_canonicalForm_pazzo_lex);
        
        
        //Crea il lemma crazy (regola nome istanza: [lemma]_pos_lang_lemma es: crazy_adjective_it_lemma)
        OWLIndividual crazy_adjective_en_lemma = factory.getOWLNamedIndividual(ontologyIRI+"#crazy_adjective_en_lemma");
        manager.addAxiom(ontology, 
                factory.getOWLClassAssertionAxiom(word, crazy_adjective_en_lemma)
        );    
        
        OWLLiteral l_crazy_lemma = factory.getOWLLiteral("crazy");
        OWLDataPropertyAssertionAxiom label_crazy_lemma = factory.getOWLDataPropertyAssertionAxiom(writtenRep, crazy_adjective_en_lemma, l_crazy_lemma);
        manager.addAxiom(ontology, label_crazy_lemma);
        
        OWLObjectPropertyAssertionAxiom crazy_lemma_canonicalForm_crazy_lex = factory.getOWLObjectPropertyAssertionAxiom(canonicalForm, crazy_adjective_en_entry, crazy_adjective_en_lemma);
        manager.addAxiom(ontology, crazy_lemma_canonicalForm_crazy_lex);
        
        //Crea il lemma madre (regola nome istanza: [lemma]_pos_lang_lemma es: madre_noun_it_lemma)
        OWLIndividual madre_noun_it_lemma = factory.getOWLNamedIndividual(ontologyIRI+"#madre_noun_it_lemma");
        manager.addAxiom(ontology, 
                factory.getOWLClassAssertionAxiom(word, madre_noun_it_lemma)
        );
        
        OWLLiteral l_madre_lemma = factory.getOWLLiteral("madre");
        OWLDataPropertyAssertionAxiom label_madre_lemma = factory.getOWLDataPropertyAssertionAxiom(writtenRep, madre_noun_it_lemma, l_madre_lemma);
        manager.addAxiom(ontology, label_madre_lemma);
        
        OWLObjectPropertyAssertionAxiom madre_lemma_canonicalForm_madre_lex = factory.getOWLObjectPropertyAssertionAxiom(canonicalForm, madre_noun_it_entry, madre_noun_it_lemma);
        manager.addAxiom(ontology, madre_lemma_canonicalForm_madre_lex);
        
        //Crea due sensi di pazzo (regola [lemma]_pos_lang_sensen es: pazzo_adjective_it_sense1 e pazzo_adjective_it_sense2 
        //Crea le opportune proprietà ontolex:canonicalForm, ontolex:sense (all’individuo del senso aggiungere la dataproperty skos:definition con la stringa di definizione del senso)
        OWLClass lexicalSense = factory.getOWLClass("ontolex:LexicalSense", pm);
        OWLObjectProperty sense = factory.getOWLObjectProperty("ontolex:sense", pm);
        OWLDataProperty definition = factory.getOWLDataProperty("skos:definition", pm);
        
        OWLIndividual pazzo_adjective_it_sense1 = factory.getOWLNamedIndividual(ontologyIRI + "#pazzo_adjective_it_sense1");
        manager.addAxiom(ontology, 
                factory.getOWLClassAssertionAxiom(lexicalSense, pazzo_adjective_it_sense1));
        OWLLiteral pazzo_adjective_it_sense1_definition = factory.getOWLLiteral("Malato di mente; è, come pazzia, termine generico e non tecnico");
        OWLObjectPropertyAssertionAxiom sense1_to_pazzo_entry = factory.getOWLObjectPropertyAssertionAxiom(sense, pazzo_adjective_it_entry, pazzo_adjective_it_sense1);
        OWLDataPropertyAssertionAxiom sense_def_to_pazzo_sense_1 = factory.getOWLDataPropertyAssertionAxiom(definition, pazzo_adjective_it_sense1, pazzo_adjective_it_sense1_definition);
        manager.addAxiom(ontology, sense_def_to_pazzo_sense_1);
        manager.addAxiom(ontology, sense1_to_pazzo_entry);
        
        OWLIndividual pazzo_adjective_it_sense2 = factory.getOWLNamedIndividual(ontologyIRI + "#pazzo_adjective_it_sense2");
        OWLLiteral pazzo_adjective_it_sense2_definition = factory.getOWLLiteral("Riferito alle condizioni atmosferiche, capriccioso, incostante, mutevole");
        manager.addAxiom(ontology, 
                factory.getOWLClassAssertionAxiom(lexicalSense, pazzo_adjective_it_sense2));
        OWLObjectPropertyAssertionAxiom sense2_to_pazzo_entry = factory.getOWLObjectPropertyAssertionAxiom(sense, pazzo_adjective_it_entry, pazzo_adjective_it_sense2);
        OWLDataPropertyAssertionAxiom sense_def_to_pazzo_sense_2 = factory.getOWLDataPropertyAssertionAxiom(definition, pazzo_adjective_it_sense2, pazzo_adjective_it_sense2_definition);
        manager.addAxiom(ontology, sense_def_to_pazzo_sense_2);
        manager.addAxiom(ontology, sense2_to_pazzo_entry);
        
        
        //Crea due sensi di crazy (regola [lemma]_pos_lang_sensen es: crazy_adjective_it_sense1 e crazy_adjective_it_sense2
        OWLIndividual crazy_adjective_en_sense1 = factory.getOWLNamedIndividual(ontologyIRI + "#crazy_adjective_en_sense1");
        manager.addAxiom(ontology, 
                factory.getOWLClassAssertionAxiom(lexicalSense, crazy_adjective_en_sense1));
        OWLLiteral crazy_adjective_en_sense1_definition = factory.getOWLLiteral("mad, especially as manifested in wild or aggressive behaviour");
        OWLObjectPropertyAssertionAxiom sense1_to_crazy_entry = factory.getOWLObjectPropertyAssertionAxiom(sense, crazy_adjective_en_entry, crazy_adjective_en_sense1);
        OWLDataPropertyAssertionAxiom sense_def_to_crazy_sense_1 = factory.getOWLDataPropertyAssertionAxiom(definition, crazy_adjective_en_sense1, crazy_adjective_en_sense1_definition);
        manager.addAxiom(ontology, sense_def_to_crazy_sense_1);
        manager.addAxiom(ontology, sense1_to_crazy_entry);
        
        OWLIndividual crazy_adjective_en_sense2 = factory.getOWLNamedIndividual(ontologyIRI + "#crazy_adjective_en_sense2");
        manager.addAxiom(ontology, 
                factory.getOWLClassAssertionAxiom(lexicalSense, crazy_adjective_en_sense2));
        OWLLiteral crazy_adjective_en_sense2_definition = factory.getOWLLiteral("extremely enthusiastic");
        OWLObjectPropertyAssertionAxiom sense2_to_crazy_entry = factory.getOWLObjectPropertyAssertionAxiom(sense, crazy_adjective_en_entry, crazy_adjective_en_sense2);
        OWLDataPropertyAssertionAxiom sense_def_to_crazy_sense_2 = factory.getOWLDataPropertyAssertionAxiom(definition, crazy_adjective_en_sense2, crazy_adjective_en_sense2_definition);
        manager.addAxiom(ontology, sense_def_to_crazy_sense_2);
        manager.addAxiom(ontology, sense2_to_crazy_entry);
        
        
        //Aggiungere definizione (skos:definition) al senso 1 di matto : “darsi alla più sfrenata allegria”
        OWLIndividual matto_adjective_it_sense1 = factory.getOWLNamedIndividual(ontologyIRI + "#matto_adjective_it_sense1");
        manager.addAxiom(ontology, 
                factory.getOWLClassAssertionAxiom(lexicalSense, matto_adjective_it_sense1));
        OWLLiteral sense_1 = factory.getOWLLiteral("darsi alla più sfrenata allegria");
        OWLDataPropertyAssertionAxiom sense_def_to_matto_sense_1 = factory.getOWLDataPropertyAssertionAxiom(definition, matto_adjective_it_sense1, sense_1);
        OWLObjectPropertyAssertionAxiom sense1_to_matto_entry = factory.getOWLObjectPropertyAssertionAxiom(sense, matto_adjective_it_entry, matto_adjective_it_sense1);
        manager.addAxiom(ontology, sense_def_to_matto_sense_1);
        manager.addAxiom(ontology, sense1_to_matto_entry);
        
        
        //Aggiungere definizione (skos:definition) al senso 2 di matto : “Nel gioco degli scacchi, si dice che il re è matto”
        OWLIndividual matto_adjective_it_sense2 = factory.getOWLNamedIndividual(ontologyIRI + "#matto_adjective_it_sense2");
        manager.addAxiom(ontology, 
                factory.getOWLClassAssertionAxiom(lexicalSense, matto_adjective_it_sense2));
        OWLLiteral sense_2 = factory.getOWLLiteral("Nel gioco degli scacchi, si dice che il re è matto");
        OWLDataPropertyAssertionAxiom sense_def_to_matto_sense_2 = factory.getOWLDataPropertyAssertionAxiom(definition, matto_adjective_it_sense2, sense_2);
        OWLObjectPropertyAssertionAxiom sense2_to_matto_entry = factory.getOWLObjectPropertyAssertionAxiom(sense, matto_adjective_it_entry, matto_adjective_it_sense2);
        manager.addAxiom(ontology, sense_def_to_matto_sense_2);
        manager.addAxiom(ontology, sense2_to_matto_entry);
       
        
        //Aggiungere definizione (skos:definition) al senso 3 di matto : “Opaco, non lucido”
        OWLIndividual matto_adjective_it_sense3 = factory.getOWLNamedIndividual(ontologyIRI + "#matto_adjective_it_sense3");
        manager.addAxiom(ontology, 
                factory.getOWLClassAssertionAxiom(lexicalSense, matto_adjective_it_sense3));
        OWLLiteral sense_3 = factory.getOWLLiteral("Opaco, non lucido");
        OWLDataPropertyAssertionAxiom sense_def_to_matto_sense_3 = factory.getOWLDataPropertyAssertionAxiom(definition, matto_adjective_it_sense3, sense_3);
        OWLObjectPropertyAssertionAxiom sense3_to_matto_entry = factory.getOWLObjectPropertyAssertionAxiom(sense, matto_adjective_it_entry, matto_adjective_it_sense3);
        manager.addAxiom(ontology, sense_def_to_matto_sense_3);
        manager.addAxiom(ontology, sense3_to_matto_entry);
        
        
        //Crea due sensi di craz,adrey (regola [lemma]_pos_lang_sensen es: madre_noun_it_sense1 e madre_noun_it_sense2 
        OWLIndividual madre_noun_it_sense1 = factory.getOWLNamedIndividual(ontologyIRI + "#madre_noun_it_sense1");
        manager.addAxiom(ontology, 
                factory.getOWLClassAssertionAxiom(lexicalSense, madre_noun_it_sense1));
        OWLLiteral madre_noun_it_sense1_definition = factory.getOWLLiteral("genitore di sesso femminile");
        OWLObjectPropertyAssertionAxiom sense1_to_madre_entry = factory.getOWLObjectPropertyAssertionAxiom(sense, madre_noun_it_entry, madre_noun_it_sense1);
        OWLDataPropertyAssertionAxiom sense_def_to_madre_sense_1 = factory.getOWLDataPropertyAssertionAxiom(definition, madre_noun_it_sense1, madre_noun_it_sense1_definition);        
        manager.addAxiom(ontology, sense_def_to_madre_sense_1);
        manager.addAxiom(ontology, sense1_to_madre_entry);
        
        OWLIndividual madre_noun_it_sense2 = factory.getOWLNamedIndividual(ontologyIRI + "#madre_noun_it_sense2");
         manager.addAxiom(ontology, 
                factory.getOWLClassAssertionAxiom(lexicalSense, madre_noun_it_sense2));
        OWLLiteral madre_noun_it_sense2_definition = factory.getOWLLiteral("titolo ecclesiastico");
        OWLObjectPropertyAssertionAxiom sense2_to_madre_entry = factory.getOWLObjectPropertyAssertionAxiom(sense, madre_noun_it_entry, madre_noun_it_sense2);
        OWLDataPropertyAssertionAxiom sense_def_to_madre_sense_2 = factory.getOWLDataPropertyAssertionAxiom(definition, madre_noun_it_sense2, madre_noun_it_sense2_definition);
        manager.addAxiom(ontology, sense_def_to_madre_sense_2);
        manager.addAxiom(ontology, sense2_to_madre_entry);
        
        //Collegare il senso 1 all’entità ontologica http://dbpedia.org/property/mother
        OWLClass ontomap = factory.getOWLClass("synsem:OntoMap", pm);
        OWLObjectProperty ontoMapping = factory.getOWLObjectProperty("synsem:ontoMapping", pm);
        
        OWLIndividual mother_sense_ontomap = factory.getOWLNamedIndividual(ontologyIRI + "#mother_sense_ontomap");
        manager.addAxiom(ontology, 
                factory.getOWLClassAssertionAxiom(ontomap, mother_sense_ontomap));
        manager.addAxiom(ontology, 
                factory.getOWLClassAssertionAxiom(lexicalSense, mother_sense_ontomap));
        
        OWLObjectPropertyAssertionAxiom madre_sense1_to_madre_ontomap = factory.getOWLObjectPropertyAssertionAxiom(sense, madre_noun_it_sense1, mother_sense_ontomap);
        manager.addAxiom(ontology, madre_sense1_to_madre_ontomap);
        
        OWLObjectPropertyAssertionAxiom mother_sense_ontomapping = factory.getOWLObjectPropertyAssertionAxiom(ontoMapping, mother_sense_ontomap, mother_sense_ontomap);
        manager.addAxiom(ontology, mother_sense_ontomapping);
        
        OWLObjectProperty reference = factory.getOWLObjectProperty("ontolex:reference", pm);
        OWLIndividual mother_sense = factory.getOWLNamedIndividual("http://dbpedia.org/property/mother");
        OWLObjectPropertyAssertionAxiom mother_sense_reference_mother_ontomap = factory.getOWLObjectPropertyAssertionAxiom(reference, mother_sense_ontomap, mother_sense);
        manager.addAxiom(ontology, mother_sense_reference_mother_ontomap);
        
        
        OWLObjectProperty subjOfProp = factory.getOWLObjectProperty("synsem:subjOfProp", pm);
        OWLObjectProperty objOfProp = factory.getOWLObjectProperty("synsem:objOfProp", pm);
        
        
        //Crea una relazione di sinonimia (lexinfo:synonym) tra il senso 1 di matto e il senso 1 di pazzo (relazione diretta tra sensi)        
        OWLObjectProperty synonym = factory.getOWLObjectProperty("lexinfo:synonym", pm);
        OWLObjectPropertyAssertionAxiom matto_sense1_synonym_pazzo_sense_1 = factory.getOWLObjectPropertyAssertionAxiom(synonym, matto_adjective_it_sense1, pazzo_adjective_it_sense1);
        manager.addAxiom(ontology, matto_sense1_synonym_pazzo_sense_1);
        
        //Crea una relazione di dating moderno (lexinfo:modern) tra pazzo e matto (relazione indiretta tra entrate lessicali)
        OWLClass lexicalRelation = factory.getOWLClass("vartrans:LexicalRelation", pm);       
        OWLIndividual pazzo_lexicalRel_matto = factory.getOWLNamedIndividual(ontologyIRI + "#pazzo_lexicalRel_matto");
        manager.addAxiom(ontology, 
                factory.getOWLClassAssertionAxiom(lexicalRelation, pazzo_lexicalRel_matto));
        
        OWLObjectProperty source = factory.getOWLObjectProperty("vartrans:source", pm);
        OWLObjectPropertyAssertionAxiom source_pazzo = factory.getOWLObjectPropertyAssertionAxiom(source, pazzo_lexicalRel_matto, pazzo_adjective_it_entry);
        manager.addAxiom(ontology, source_pazzo);
        
        OWLObjectProperty target = factory.getOWLObjectProperty("vartrans:target", pm);
        OWLObjectPropertyAssertionAxiom target_matto = factory.getOWLObjectPropertyAssertionAxiom(target, pazzo_lexicalRel_matto, matto_adjective_it_entry);
        manager.addAxiom(ontology, target_matto);
        
        OWLObjectProperty category = factory.getOWLObjectProperty("vartrans:category", pm);        
        OWLIndividual modern = factory.getOWLNamedIndividual("lexinfo:modern", pm);
        OWLObjectPropertyAssertionAxiom pazzo_modern_matto = factory.getOWLObjectPropertyAssertionAxiom(category, pazzo_lexicalRel_matto, modern);
        manager.addAxiom(ontology, pazzo_modern_matto);
        
        
        //Crea una relazione di traduzione (directEquivalent) tra pazzo (senso 1) e crazy (senso 1) con confidence 0,95
        OWLClass terminologicalRelation = factory.getOWLClass("vartrans:TerminologicalRelation", pm);       
        OWLIndividual pazzo_termRel_matto = factory.getOWLNamedIndividual(ontologyIRI + "#pazzo_termRel_matto");
        manager.addAxiom(ontology, 
                factory.getOWLClassAssertionAxiom(terminologicalRelation, pazzo_termRel_matto));
        
        
        OWLObjectPropertyAssertionAxiom source_pazzo_1 = factory.getOWLObjectPropertyAssertionAxiom(source, pazzo_termRel_matto, pazzo_adjective_it_sense1);
        manager.addAxiom(ontology, source_pazzo_1);
        
        OWLObjectPropertyAssertionAxiom target_matto_1 = factory.getOWLObjectPropertyAssertionAxiom(target, pazzo_termRel_matto, matto_adjective_it_sense1);
        manager.addAxiom(ontology, target_matto_1);
        
        OWLDataProperty translationConfidence = factory.getOWLDataProperty("lexinfo:translationConfidence", pm);
        OWLLiteral confidenze_ratio = factory.getOWLLiteral(0.95);
        OWLDataPropertyAssertionAxiom trans_conf_ratio = factory.getOWLDataPropertyAssertionAxiom(translationConfidence, pazzo_termRel_matto, confidenze_ratio);
        manager.addAxiom(ontology, trans_conf_ratio);
        OWLIndividual directEquivalent = factory.getOWLNamedIndividual("NS_TR:directEquivalent", pm);
        OWLObjectPropertyAssertionAxiom termRel_category_directEquivalent = factory.getOWLObjectPropertyAssertionAxiom(category, pazzo_termRel_matto, directEquivalent);
        manager.addAxiom(ontology, termRel_category_directEquivalent);
        
        //Crea un frame (X è madre di Y) di tipo lexinfo:nounFrame per l’entrata madre. 
        OWLClass nounFrame = factory.getOWLClass("lexinfo:NounFrame", pm);
        OWLIndividual madre_nounFrame = factory.getOWLNamedIndividual(ontologyIRI + "#madre_nounFrame");
        manager.addAxiom(ontology,
                factory.getOWLClassAssertionAxiom(nounFrame, madre_nounFrame));
        
        OWLObjectProperty synBehavior = factory.getOWLObjectProperty("synsem:synBehavior", pm);
        OWLObjectPropertyAssertionAxiom madre_synBehavior_madre_nounFrame = factory.getOWLObjectPropertyAssertionAxiom(synBehavior, madre_noun_it_entry, madre_nounFrame);
        manager.addAxiom(ontology, madre_synBehavior_madre_nounFrame);
        
        //Crea il primo argomento X, soggetto        
        OWLIndividual arg_X = factory.getOWLNamedIndividual(ontologyIRI+"#arg_X");
                       
        //Crea il secondo argomento Y, argomento preposizionale.
        OWLIndividual arg_Y = factory.getOWLNamedIndividual(ontologyIRI+"#arg_Y");
        
        //Far corrispondere il primo argomento al soggetto della proprietà mother di dbpedia a cui il senso si riferisce
        OWLObjectProperty subject = factory.getOWLObjectProperty("lexinfo:subject", pm);
        OWLObjectProperty prepositionalArg = factory.getOWLObjectProperty("lexinfo:prepositionalArg", pm);
        OWLObjectPropertyAssertionAxiom arg_X_subject_madre_nounFrame = factory.getOWLObjectPropertyAssertionAxiom(subject, madre_nounFrame, arg_X);
        OWLObjectPropertyAssertionAxiom arg_Y_directObject_madre_nounFrame = factory.getOWLObjectPropertyAssertionAxiom(prepositionalArg, madre_nounFrame, arg_Y);
        manager.addAxiom(ontology, arg_X_subject_madre_nounFrame);
        manager.addAxiom(ontology, arg_Y_directObject_madre_nounFrame);
        
        OWLObjectPropertyAssertionAxiom arg_X_subjOfProp_mother_sense_ontomap = factory.getOWLObjectPropertyAssertionAxiom(subjOfProp, mother_sense_ontomap, arg_X);
        OWLObjectPropertyAssertionAxiom arg_Y_objOfProp_mother_sense_ontomap = factory.getOWLObjectPropertyAssertionAxiom(objOfProp, mother_sense_ontomap, arg_Y);
        manager.addAxiom(ontology, arg_X_subjOfProp_mother_sense_ontomap);
        manager.addAxiom(ontology, arg_Y_objOfProp_mother_sense_ontomap);
        
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
        System.out.println("\nNomi delle entrate lessicali con annessa lingua:\n");
        for(OWLIndividual i : EntitySearcher.getIndividuals(lexicon, ontology).collect(Collectors.toList())){                                    
            for(OWLIndividual j : EntitySearcher.getObjectPropertyValues(i, entry, ontology).collect(Collectors.toList())){                
                String entry_name = j.toStringID().replace(pm.getDefaultPrefix(), "");
                String lang = EntitySearcher.getDataPropertyValues(i, language, ontology).map(e -> e.toString()).reduce("", String::concat);
                System.out.println(entry_name + " - " + lang);
            }
        }
        
        System.out.println("\nCanonical form e writtenRepresentation:\n");
        for(OWLIndividual i : EntitySearcher.getIndividuals(lexicalEntry, ontology).collect(Collectors.toList())){            
            String entry_name = i.toStringID().replace(pm.getDefaultPrefix(), "");            
            for(OWLIndividual k : EntitySearcher.getObjectPropertyValues(i, canonicalForm, ontology).collect(Collectors.toList())){               
                String lemma_name = k.toStringID().replace(pm.getDefaultPrefix(), "");
                String lemma_writtenRep = EntitySearcher.getDataPropertyValues(k, writtenRep, ontology).map(e -> e.toString()).reduce("", String::concat);                    
                System.out.println(entry_name+" --------> "+lemma_name + " - " + lemma_writtenRep);
            }
        }
        
        
        System.out.println("\nOtherForm form e writtenRepresentation:\n");
        for(OWLIndividual i : EntitySearcher.getIndividuals(lexicalEntry, ontology).collect(Collectors.toList())){            
            String entry_name = i.toStringID().replace(pm.getDefaultPrefix(), "");            
            for(OWLIndividual k : EntitySearcher.getObjectPropertyValues(i, otherForm, ontology).collect(Collectors.toList())){                
                String other_form_lemma_name = k.toStringID().replace(pm.getDefaultPrefix(), "");
                String other_form_lemma_writtenRep = EntitySearcher.getDataPropertyValues(k, writtenRep, ontology).map(e -> e.toString()).reduce("", String::concat);                    
                System.out.println(entry_name+" --------> "+other_form_lemma_name + " - " + other_form_lemma_writtenRep);
            }
        }
               
        System.out.println("\nNomi degli individuali di senso:\n");
        for(OWLIndividual i : EntitySearcher.getIndividuals(lexicalEntry, ontology).collect(Collectors.toList())){           
            String entry_name = i.toStringID().replace(pm.getDefaultPrefix(), "");            
            for(OWLIndividual k : EntitySearcher.getObjectPropertyValues(i, sense, ontology).collect(Collectors.toList())){               
                String entry_sense = k.toStringID().replace(pm.getDefaultPrefix(), "");                
                System.out.println(entry_name+" --------> "+entry_sense);
            }
        }
        
        System.out.println("\nNomi delle relazioni tra sensi:\n");
        for(OWLIndividual i : EntitySearcher.getIndividuals(lexicalSense, ontology).collect(Collectors.toList())){            
            String sense_name = i.toStringID().replace(pm.getDefaultPrefix(), "");            
            for(OWLIndividual k : EntitySearcher.getObjectPropertyValues(i, synonym, ontology).collect(Collectors.toList())){                
                String synonym_sense = k.toStringID().replace(pm.getDefaultPrefix(), "");                
                System.out.println(sense_name+" ----- synonym of -----> "+synonym_sense);
            }
            
            for(OWLIndividual k : EntitySearcher.getObjectPropertyValues(i, sense, ontology).collect(Collectors.toList())){                
                String onto_sense = k.toStringID().replace(pm.getDefaultPrefix(), "");                
                System.out.println(sense_name+" ----- riferimento ontologico ----->"+onto_sense);
            }
        }
        
        for(OWLIndividual i : EntitySearcher.getIndividuals(terminologicalRelation, ontology).collect(Collectors.toList())){
            String termRel_name = "";
            termRel_name = i.toStringID().replace(pm.getDefaultPrefix(), "");            
            for(OWLIndividual k : EntitySearcher.getObjectPropertyValues(i, synonym, ontology).collect(Collectors.toList())){                
                String category_sense = EntitySearcher.getObjectPropertyValues(k, category, ontology).map(e -> e.toString()).reduce("", String::concat);
                String source_sense = EntitySearcher.getObjectPropertyValues(k, source, ontology).map(e -> e.toString()).reduce("", String::concat);
                String target_sense = EntitySearcher.getObjectPropertyValues(k, target, ontology).map(e -> e.toString()).reduce("", String::concat);                
                System.out.println(source_sense+" ---- "+category_sense+" ----> "+ target_sense);
            }
        }
        
        System.out.println("\nNomi delle relazioni tra entrate lessicali:\n");
        for(OWLIndividual i : EntitySearcher.getIndividuals(lexicalRelation, ontology).collect(Collectors.toList())){
            String lexRel_name = "";
            lexRel_name = i.toStringID().replace(pm.getDefaultPrefix(), "");                        
            String category_sense = "";
            String source_sense = "";
            String target_sense = "";
            category_sense = EntitySearcher.getObjectPropertyValues(i, category, ontology).map(e -> e.toString()).reduce("", String::concat).replace(pm.getPrefix("lexinfo:"), "");
            source_sense = EntitySearcher.getObjectPropertyValues(i, source, ontology).map(e -> e.toString()).reduce("", String::concat).replace(pm.getDefaultPrefix(), "");
            target_sense = EntitySearcher.getObjectPropertyValues(i, target, ontology).map(e -> e.toString()).reduce("", String::concat).replace(pm.getDefaultPrefix(), "");                     
            System.out.println(source_sense+" ---- "+category_sense+" ----> "+ target_sense);
            
        }
        
        
        System.out.println("\nNomi dei frame:\n");
        for(OWLIndividual i : EntitySearcher.getIndividuals(nounFrame, ontology).collect(Collectors.toList())){
            String frameName = i.toStringID().replace(pm.getDefaultPrefix(), "");;
            String arg_1 = EntitySearcher.getObjectPropertyValues(i, subject, ontology).map(e -> e.toString()).reduce("", String::concat).replace(pm.getDefaultPrefix(), "");
            String arg_2 = EntitySearcher.getObjectPropertyValues(i, prepositionalArg, ontology).map(e -> e.toString()).reduce("", String::concat).replace(pm.getDefaultPrefix(), "");
            System.out.println(frameName + " is a "+ nounFrame.toStringID().replace(pm.getPrefix("lexinfo:"), ""));
            System.out.println("Subject: "+ arg_1);
            System.out.println("PrepositionalArgs" + arg_2);
            
        }
        System.out.println("");
    }                       
}
