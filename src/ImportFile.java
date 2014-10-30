/**
 *
 * @author HEIDY2016
 */


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import journal.Display;
import journal.Entry;
import journal.createInstances;
import journal.manageFiles;
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
        List<Entry> journalEntries = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000); // wait for 1 second

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            
            // call the update method. Note that this could be anything
            // that the updater wants to do, it doesn't _have_ to be
            // updating a text field, so there is nothing coupling this to
            // the GUI            
            updater.update(i);
            
            System.out.println(i);            
        }
        
        if (fileType.equals("txt")) {
            
            
        } else {
            
            
            
        }
        
        
    }
}

