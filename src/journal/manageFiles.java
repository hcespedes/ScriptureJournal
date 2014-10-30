/*************************************************************************
 * Author: Heidy Cespedes
 * CS246
 * The class is used to manage different tasks related with files
 *************************************************************************/
package journal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/****************************************************************************
 * Define the manageFiles class
 **************************************************************************/
public class manageFiles {
    
/****************************************************************************
 * Get the location of the different files used in a program through a
 * properties file. 
 * Return a string.
 *****************************************************************************/
    
    public String getProperties(String property, String propFile) {
        
        Properties prop = new Properties();
        String propertyValue = null;

        try {
            // load properties file
            prop.load(getClass().getResourceAsStream(propFile));
            propertyValue = prop.getProperty(property);
        } catch (IOException ex){
            ex.printStackTrace();
        } 
        return propertyValue;
    }
    
/**************************************************************************
 * Create a TXT file and copy the journal entries from a list of entries
 * into the file
 **************************************************************************/
    public void writeToFile(List<Entry> entries, String file) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(file);
        String content = "";
        System.out.println("Saving document " + file);
        for (Entry ent: entries) {
            writer.println("-----");
            writer.println(ent.getDate());
            content = ent.getContent();
            content = content.trim();
            content = content.replaceAll("\\t+", "\n");
            writer.println( content + "\n");
        }
        writer.close();
    }   
    
    
/**************************************************************************
 * Append an entry to a TXT file
 **************************************************************************/
    public void appendToTxtFile(String content, String date, String file) throws FileNotFoundException, IOException {
        PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
        System.out.println("Saving entry in " + file);
        content = content.trim();
        content = content.replaceAll ("\\n", " ");
        content = content.replaceAll("\\t+", "\\n");
        
        writer.println("-----");
        writer.println(date);
        writer.println(content);
        writer.close();
    }       
    
/***********************************************************************
 * Reads a file with the list of books and chapters and create a map.
 * The map contains a key which is the book and the values are the 
 * different chapters of the book, which are stored in a list.
 **********************************************************************/  
    
    public Map<String, List<String>> readBooksList(String bFile) {
        Map<String, List<String>> map = new HashMap<>();
        String line = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(bFile));
             while ((line = reader.readLine()) != null) {
                 
                 List<String> list = new ArrayList<>();
                 String[] parts = line.split(":");
                 String key = parts[0];
                 
                 String valueList = parts[1];
                 
                 if(map.containsKey(key)) {
                     list = map.get(key);
                     List<String> newList = new ArrayList<>();
                     newList.add(valueList);
                     list.addAll(newList);
                 } else {
                     list.add(valueList);
                 }
                 map.put(key, list);
            }
            reader.close();
        } catch (IOException exe){
            exe.printStackTrace();
        }
        
        return map;
    }

/***********************************************************************
 * Reads a file with a list of topics and create a map
 * The map contains a key which is the topic and the values are the 
 * different words that define that topic. 
 * The list of words (values) is stored in a Array list.
 **********************************************************************/     
    
    public Map<String, List<String>> readTopicsList(String tFile) {
       Map<String, List<String>> map = new HashMap<>();
       String line = "";
       
        try {
            BufferedReader reader = new BufferedReader(new FileReader(tFile));
            while ((line = reader.readLine()) != null) {
                
                String[] parts = line.split(":");
                String key = parts[0];
                 
                String valueList = parts[1];
                
                String[] terms = valueList.split(",");
                 
                List<String> list = new ArrayList<>();
                list.addAll(Arrays.asList(terms));
                 
                map.put(key, list);
            }
            
            reader.close();
             
        }catch (IOException exe) {
            exe.printStackTrace();
        }
        return map; 
    }
    
}