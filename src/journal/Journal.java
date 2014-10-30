/**************************************************************************
 * Author: Heidy Cespedes
 * Collaborators: Justin Whistine and Adam Quinton
 * Milestone: Import, Export, and Version Control
 *************************************************************************/
package journal;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*************************************************************************
 * In Journal class we have the classes that are used to run the journal
 * from the user interface
 *************************************************************************/
public class Journal {
    
/*************************************************************************
 * Create a list of Entries from a TXT file
 * 
 *************************************************************************/
    public List<Entry> createListEntries(String file) {    
        List<Entry> journalEntries = new ArrayList<>();
        // Create instances to run the program
        manageFiles files = new manageFiles();
        createInstances myEntries = new createInstances(); 
        xmlDocument doc = new xmlDocument();
        Display display = new Display();
        
        String propFile = "/resources/journal.properties";
        String termsFile = files.getProperties("terms", propFile);
        String booksFile = files.getProperties("books", propFile);
        
        Map <String, List<String>> termsMap = files.readTopicsList(termsFile);
        Map <String, List<String>> booksMap = files.readBooksList(booksFile);
       
        journalEntries = myEntries.processFile(file, termsMap, booksMap);
        
        return journalEntries;
    }

/*************************************************************************
 * Display the journal entries saved in either, a TXT file or a XML file
 * 
 *************************************************************************/
    public void displayEntries(String file, String typeOfFile) {     
        List<Entry> journalEntries = new ArrayList<>();
        Display display = new Display();

        journalEntries = createList(file, typeOfFile);
        display.displayEntries(journalEntries);
    } 

 /*************************************************************************
 * Create a list of journal entries according to either file, XML or
 * TXT file
 *************************************************************************/
    public List<Entry> createList(String file, String typeOfFile) {     
        List<Entry> journalEntries = new ArrayList<>();
        xmlDocument doc = new xmlDocument();
        Display display = new Display();
        
        if (typeOfFile.equals("txt")) {
            journalEntries = createListEntries(file);
            
        } else {
            journalEntries = doc.readXml(file);
        }

        return journalEntries;
    } 
    
/*************************************************************************
 * Add new journal entries to a XML file.
 * First, it gets the entries from the file and put them in a list
 * Second, create the new entry and add it to the list
 * Third, stored the new list in the XML file
 *************************************************************************/
    public void addNewEntryXml(String content, String outFile) throws FileNotFoundException, Exception {
        // Create instances to run the program
        manageFiles files = new manageFiles();
        createInstances myEntries = new createInstances(); 
        xmlDocument doc = new xmlDocument();
        
        String propFile = "/resources/journal.properties";
        String termsFile = files.getProperties("terms", propFile);
        String booksFile = files.getProperties("books", propFile);
        
        List<Entry> journalEntries = new ArrayList<>();        
            
        Map <String, List<String>> termsMap = files.readTopicsList(termsFile);
        Map <String, List<String>> booksMap = files.readBooksList(booksFile);

        journalEntries = doc.readXml(outFile);
        journalEntries.add(myEntries.createEntry(content, termsMap, booksMap));
        try {
            doc.buildXmlDocument(journalEntries, outFile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }

/***************************************************************************
 * Save the new journal entry to a TXT file.
 * The new entry is appended to an existing file
 ***************************************************************************/
    public void addNewEntryTxt(String content, String date, String outFile) {
        manageFiles files = new manageFiles();   
        try {
            files.appendToTxtFile(content, date, outFile);
            } catch (Exception e){
                System.out. println("File cannot be written");
                e.printStackTrace();
            }
    }
    
/***************************************************************************
 * Search entries by topics
 * 
 ***************************************************************************/
    public List<Entry> searchByTopic(String topic, String file, String typeOfFile) {
        List<Entry> journalEntries = new ArrayList<>();
        List<Entry> topicEntries = new ArrayList<>();
        Display display = new Display();
        
        journalEntries = this.createList(file, typeOfFile);
        
        for(Entry e: journalEntries) {
            List<String> tempTopics = new ArrayList<>();
            tempTopics = e.getTopics();
            if (tempTopics.contains(topic)) {
                topicEntries.add(e);
            }
        }
        
        System.out.println("The following entries have the specified topic:");
        return topicEntries;
    }
    
/***************************************************************************
 * Search entries by date
 * 
 ***************************************************************************/
    public List<Entry> searchByDate(String date, String file, String typeOfFile) {
        List<Entry> journalEntries = new ArrayList<>();
        List<Entry> dateEntries = new ArrayList<>();
        Display display = new Display();
        
        journalEntries = this.createList(file, typeOfFile);
        
        for (Entry e: journalEntries) {
            if (e.getDate().equals(date)) {
                dateEntries.add(e);
            }
        }
        
        System.out.println("The following entries were entered in the specified date:");

        return dateEntries;
    }   
    
/***************************************************************************
 * Search entries by scriptures
 * 
 ***************************************************************************/
    public List<Entry> searchByScripture(String book, String chapter, String firstVerse, String lastVerse, String file, String typeOfFile) {
        List<Entry> journalEntries = new ArrayList<>();
        List<Entry> scriptureEntries = new ArrayList<>();
        Display display = new Display();
        int firstV = 0;
        int lastV = 0;
        
        if (!firstVerse.isEmpty()) {
            firstV = Integer.parseInt(firstVerse);
        }
        if (!lastVerse.isEmpty()) {
            lastV = Integer.parseInt(lastVerse);
        }
        
        scripture tmpScrip = new scripture(book, Integer.parseInt(chapter), firstV, lastV);
        journalEntries = this.createList(file, typeOfFile);

        for (Entry e: journalEntries) {
            List<scripture> scriptures = new ArrayList<>();
            scriptures = e.getScriptures();
            
            for(scripture s: scriptures) {
               if (isSameScripture(s, tmpScrip)) { 
                  scriptureEntries.add(e);
               }
            }
        }

        return scriptureEntries;
    }   
    
/***************************************************************************
 * Compare two scriptures. If they are the same, returns true.
 ***************************************************************************/
    boolean isSameScripture(scripture scripOne, scripture scripTwo) {
        if (scripOne.getBook().equals(scripTwo.getBook())) {
            if (scripOne.getChapter() == scripTwo.getChapter()) {
                if (scripOne.getFirstVerse() == scripTwo.getFirstVerse()) {
                    if (scripOne.getLastVerse() == scripTwo.getLastVerse()) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
} 