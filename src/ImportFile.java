/**
 *
 * @author HEIDY2016
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import journal.Display;
import journal.Entry;
import journal.createInstances;
import journal.manageFiles;
import journal.scripture;
import journal.xmlDocument;

public class ImportFile implements Runnable {
    
    private Updater updater;
    private String fileName;
    private String fileType;
    
    public ImportFile(Updater updater, String fileName, String fileType ) {
        this.updater = updater;
        this.fileName = fileName;
        this.fileType = fileType;
    }
    
    @Override
    public void run() {       
        
        if (fileType.equals("txt")) {
            readFileTxt(updater, fileName);            
        }       
    }
    
    /*********************************************************************
     * @param updater
     * @param file 
     ********************************************************************/
    
    public void readFileTxt(Updater updater, String file) {
        ///////////  Counters   //////////////
        int countEntries = 1;
        int countScrip = 0;
        int countTopics = 0;
        ////////   Declare instances  ////////
        List<Entry> entries = new ArrayList<>();
        manageFiles files = new manageFiles();
        createInstances myEntries = new createInstances(); 
        xmlDocument doc = new xmlDocument();
        Display display = new Display();
        
        String propFile = "/resources/journal.properties";
        String termsFile = files.getProperties("terms", propFile);
        String booksFile = files.getProperties("books", propFile);
        
        Map <String, List<String>> termsMap = files.readTopicsList(termsFile);
        Map <String, List<String>> booksMap = files.readBooksList(booksFile);
        
        String date = null;
        String content = "";
        String[] blocks;
        List<scripture> scriptures = new ArrayList<>();
        List<String> topics = new ArrayList<>();
        
        /////  Read the whole file and put it in a String /////
        try {
            BufferedReader bi = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bi.readLine()) != null) {
                content = content + line;
            }
            
           blocks = content.split("-----");
           for (int i = 1; i < blocks.length; i++) {
               
                try {
                  Thread.sleep(1000); // wait for 1 second

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                
                Entry entry = new Entry(date, content, scriptures, topics);

                date = findDate(blocks[i]);   
                content = blocks[i].replace(date, "");
                entry.setDate(date);
                entry.setContent(content);
                
                // Gives a report of how many entries have been read
                updater.update("Entries entered: " + countEntries++);
                System.out.println("Entries entered: " + countEntries++);
                
                List<String> newTopics = searchForTopic(blocks[i], termsMap);
                for (String t: newTopics) {
                    if (!entry.getTopics().contains(t)) {
                        entry.addTopics(t);
                        countTopics++;
                    }
                }
                
                // Wait some time before displaying the next report
                try {
                  Thread.sleep(1000); // wait for 1 second

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                
                // Gives a report of how many topics have been read
                updater.update("Topics entered: " + countTopics);
                System.out.println("Topics entered: " + countTopics);
                
                List<scripture> newScrip = searchForScrip(blocks[i], booksMap);
                for (scripture s: newScrip) {
                    if (!entry.getScriptures().contains(s)){
                        entry.addScriptures(s);
                        countScrip++;
                    }
                }
                
                // Wait some time before displaying the next report
                try {
                  Thread.sleep(1000); // wait for 1 second

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                
                // Gives a report of how many scriptures have been read
                updater.update("Scriptures entered: " + countScrip);
                System.out.println("Scriptures entered: " + countScrip);                   
           }
        }catch (IOException ex) {//(FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    
    /**************************************************************************
    * Find the date in a line
    **************************************************************************/
    public String findDate(String line) {
         // Define a reg-exp for date
        String date;
        String datePattern = "\\d{4}-\\d{2}-\\d{2}";
        Pattern patternForDate = Pattern.compile(datePattern);
        Matcher dateP = patternForDate.matcher(line);
        if (dateP.find()) {
            date = dateP.group();
            return date;
        }else {
            return null;
        }
    }
    
    /**************************************************************************
    * Takes a line and try to find the topics stored in a map and creates
    * a list with the topics that are found.
    **************************************************************************/
    public List<String> searchForTopic(String line, Map<String, List<String>> map) {
        List<String> topics = new ArrayList<>();
        line = line.toLowerCase();
        for (String key: map.keySet()) {
            List<String> topicsList = map.get(key);
            for (String t: topicsList) {
                t = t.toLowerCase();    // Compare everything as lower case
                if (line.contains(t)) {
                    if (!topics.contains(key)) {
                        topics.add(key);
                    }
                }
            }
        }
        
        return topics;
    }
    
    /**************************************************************************
    * Takes a line and try to find different patterns of scriptures and creates
    * a list with the scriptures that are found in the line.
    **************************************************************************/
    public List<scripture> searchForScrip(String line, Map<String, List<String>> map) {
        List<scripture> scriptures = new ArrayList<>();
        String temp = "";
        String book;
        int chapter;
        int firstVerse = 0;
        int lastVerse = 0;
        
        line = line.replace("(", "");
        line = line.replace(")", "");
        
        String scripturePattern = "(\\d*)\\s*([a-zA-Z&]{3,}+)\\s*(\\d+)(?::(\\d+))?(\\s*-\\s*(\\d+)(?:\\s*([a-zA-Z&]{3,}+)\\s*(\\d+))?(?::(\\d+))?)?|(\\S+ chapter \\d)";
        Pattern pattern1 = Pattern.compile(scripturePattern);
        Matcher matchPattern1 = pattern1.matcher(line);
        
        while(matchPattern1.find()) {
              
              temp = matchPattern1.group(0);
              
              book = "";
              chapter = 0;
              firstVerse = 0;
              lastVerse = 0;     
              
              if (matchPattern1.group(1) == null){
                  book = matchPattern1.group(0);
                  book = book.replace(" chapter", "");
                  String[] parts = book.split(" ");
                  book = parts[0];
                  chapter = Integer.parseInt(parts[1]);
              }
              
              if (matchPattern1.group(1) != null) {
                  if (matchPattern1.group(1).isEmpty()) {
                    book = matchPattern1.group(2);
                    book = book.replace("\\s+", "");
                  } else {
                  book = matchPattern1.group(1) + " " + matchPattern1.group(2);
                }
              }
              
              if (matchPattern1.group(3) != null) {
                  chapter = Integer.parseInt(matchPattern1.group(3));
              }
              
              if (matchPattern1.group(4) != null) {
                  firstVerse = Integer.parseInt(matchPattern1.group(4));
              }
              
              if (matchPattern1.group(6) != null) {
                  lastVerse = Integer.parseInt(matchPattern1.group(6));
              }
              
              scripture thisScripture = new scripture(book, chapter, firstVerse, lastVerse);
              
              if (isScriptureValid(book,map) && !scriptures.contains(thisScripture)) {
                scriptures.add(thisScripture);
              }
        }
       
        return scriptures;
    }
    
    /****************************************************************************
    * Check if the scripture is a valid scripture. It compares the scripture with
    * the map that has all the possible valid scripture books.
    *****************************************************************************/
    public boolean isScriptureValid(String book, Map<String,List<String>> map) {
        
        for (String key:map.keySet()){
            if (book.equals(key)) {
                return true;
            }
        }
        return false;    
    }
}

