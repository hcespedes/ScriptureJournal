/*************************************************************************
 * Author: Heidy Cespedes
 * CS246
 * The class is used to create and manage the main instances used in 
 * the Scripture Journal application
 *************************************************************************/
package journal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/****************************************************************************
 * Define the createInstances class
 **************************************************************************/
public class createInstances {
    
    
/**************************************************************************
* Read and Parse a file to obtain the data to create the instances of
* the journal. A list of entries.
**************************************************************************/
    
    public List<Entry> processFile(String file, Map<String,List<String>> topicsMap, Map<String,List<String>> scripMap) {

        List<Entry> entries = new ArrayList<>();

        String content = "";
        String[] blocks;

        try {
            BufferedReader bi = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bi.readLine()) != null) {
                content = content + line;
            }
            
           blocks = content.split("-----");
           for (int i = 1; i < blocks.length; i++) {
               entries.add(createEntry(blocks[i], topicsMap, scripMap));
           }
        }catch (IOException ex) {//(FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return entries;
     }

/****************************************************************************
 * Create an object Entry.
 * Call for Search for Topic and Search for Scripture methods to verify that
 * the scriptures are valid and to obtain the topics contained in the block
 * of text has been passed unto it.
 *****************************************************************************/
    public Entry createEntry(String block, Map<String, List<String>> topicsMap, Map<String, List<String>> scripMap) {
        
        List<scripture> scriptures = new ArrayList<>();
        List<String> topics = new ArrayList<>();
        String content = "";
        String date = null;
        
        Entry entry = new Entry(date, content, scriptures, topics);
        
        date = findDate(block);   
        content = block.replace(date, "");
        entry.setDate(date);
        entry.setContent(content);
       
        List<String> newTopics = searchForTopic(block, topicsMap);
        for (String t: newTopics) {
            if (!entry.getTopics().contains(t)) {
                entry.addTopics(t);
            }
        }
        
        List<scripture> newScrip = searchForScrip(block, scripMap);
        for (scripture s: newScrip) {
            if (!entry.getScriptures().contains(s)){
                entry.addScriptures(s);
            }
        }

        return entry;
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
