/**************************************************************************
* Author: Heidy Cespedes
* This class takes care of the different displays in the program  
**************************************************************************/
package journal;

import java.util.List;
import java.util.Map;
import javafx.scene.control.TextArea;

/****************************************************************************
 * Define the Display class
 ***************************************************************************/
public class Display {
 /***********************************************************************
 * The following are the display methods for the different type of objects.
 * Display Entries: Display the info stored in each entry, which is the 
 * date the entry was done, the scriptures mentioned in the entry, the 
 * topics mentioned and the content of the entry.
 **********************************************************************/      
    
    public void displayEntries(List<Entry> entryList) {
        String scripture;
        for (Entry e: entryList) {
            
            System.out.println("Entry:");
            System.out.println("   Date: " + e.getDate());
            System.out.println("   Topics:");
            
            List<String> myListTop = e.getTopics();
            displayList(myListTop);
            System.out.print("   Scriptures:\n");
            
            List<scripture> myListScrip = e.getScriptures();
            for (scripture s: myListScrip) {
                scripture = s.getBook() + " " + Integer.toString(s.getChapter());
                if (s.getFirstVerse() != 0) {
                    scripture = scripture + ":" + Integer.toString(s.getFirstVerse());
                }
                if (s.getLastVerse() != 0){
                    scripture = scripture + "-" + Integer.toString(s.getLastVerse());
                }
                
                System.out.println("\t   " + scripture);
            }
            System.out.print("\n");
            
            System.out.println("   Content: \n" + "\t   " + e.getContent() + "\n");
        }
    }

/***********************************************************************
 * What it needs to be displayed at the beginning of the screen
 **********************************************************************/   
    public void displayHeader(String file) {
        System.out.println("Loading file \"" + file + "\"...");
        System.out.println("Journal:\n");
    }
    
/***********************************************************************
 * Display Map. If key has not values then don't display key
 **********************************************************************/      
    void displayMap(Map<String,List<String>> map, String header) {
        System.out.println(header);
        for (String key: map.keySet()) {
            if (map.get(key) != null) {
                System.out.println(key + ":");
                List<String> dates = map.get(key);
                for (String s: dates) {
                    System.out.println("\t   " + s);
                }
            }
        }
    }
    
/***********************************************************************
 * Display a list of Strings
 **********************************************************************/      
    void displayList (List<String> list) {
        for (String node: list) {
            System.out.println("\t   " + node);
        }
    }    
    
    
    
     public void textFieldDisplay(List<Entry> entryList, TextArea txtContent) {
        if (!entryList.isEmpty()) {
            txtContent.clear();
            for (Entry e: entryList) {                             
                txtContent.appendText("-----\n");
                txtContent.appendText("Date: ");
                txtContent.appendText(e.getDate());
                txtContent.appendText("\n");
                txtContent.appendText("Content: \n"); 
                txtContent.appendText(e.getContent());
                txtContent.appendText("\n\n");
            }
        } else {
            txtContent.setText("No entry match the date\n");
        }
    }
}
