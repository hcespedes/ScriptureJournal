/**************************************************************************
* Author: Heidy Cespedes
* Collaborators: Justin Whistine, and Adam Quinton
* User Interface:
*   Manage the different events depending on what options the user has 
*   selected. 
**************************************************************************/


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import journal.Display;
import journal.Entry;
import journal.Journal;

/***********************************************************************
* Declare the User Interface class. JournalFX
***********************************************************************/
public class JournalFX extends Application {
    
    File file;
    String fileName;
    String fileType;
    Journal myJournal;
    
    @Override
    public void start(final Stage primaryStage) {
        //final BorderPane border = new BorderPane();
        final GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        
        VBox vbox1 = new VBox();
        // setPadding (top, right, bottom, left)
        vbox1.setPadding(new Insets(20, 10, 20, 10));
        vbox1.setSpacing(5);
        
        VBox vbox2 = new VBox();
        // setPadding (top, right, bottom, left)
        vbox2.setPadding(new Insets(20, 10, 20, 10));
        vbox2.setSpacing(5);
        
        VBox vbox3 = new VBox();
        // setPadding (top, right, bottom, left)
        vbox3.setPadding(new Insets(20, 10, 20, 10));
        vbox3.setSpacing(5);
        
        HBox hbox1 = new HBox();
        hbox1.setSpacing(5);
        HBox hbox2 = new HBox();
        
        final Label date = new Label("Date (yyyy-mm-dd)"); 
        final TextField info = new TextField();
        info.setPrefColumnCount(25);
        final TextField dateField = new TextField(); // Enable only when "Add
                                                     // Entry" is clicked
        dateField.setDisable(true);
        hbox1.getChildren().addAll(date, dateField);      

        final TextArea txtContent = new TextArea(); 
        txtContent.setPrefColumnCount(25);          
        txtContent.setPrefRowCount(15);  
        txtContent.setWrapText(true);
        vbox2.getChildren().addAll(hbox1, info, txtContent);      
                
        
        Button openJournal = new Button();  
        openJournal.setText("Open Journal");
        
        Button displayE = new Button();
        displayE.setText("Display Entries");
        
        Button addEntry = new Button();  
        addEntry.setText("Add New Entry");
        
        Button save = new Button();  
        save.setText("Save");
        
        Button searchB = new Button();
        searchB.setText("Search");
        
        vbox1.getChildren().addAll(openJournal, displayE, addEntry, save); ////////////////
        
        final Label search = new Label("Browse entry by:"); 
        final ComboBox browse = new ComboBox();
        browse.getItems().addAll("Date", "Topic", "Scripture");
        
        final Label book = new Label("Book:");
        final TextField bookField = new TextField();
        final Label chapter = new Label("Chapter:");
        final TextField chapField = new TextField();
        final Label verse1 = new Label("First Verse:");
        final TextField fVerseField = new TextField();       
        final Label verse2 = new Label("Last Verse:");       
        final TextField lVerseField = new TextField();       
        final Label dateS = new Label("Date (yyyy-mm-dd):");
        final TextField dateSField = new TextField();       
        final Label topic = new Label("Topic:");       
        final TextField topicField = new TextField();   
        
        vbox3.getChildren().addAll(search, browse, dateS, dateSField, topic, topicField, book, bookField, chapter, chapField, verse1, fVerseField, verse2, lVerseField, searchB);
        
        hbox2.getChildren().addAll(vbox1, vbox2, vbox3);
        
        
/****************************************************************************
 * Action Event Handler for "Open Journal" button
 ***************************************************************************/         
        
        openJournal.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                
                try {
                    FileChooser chooser = new FileChooser(); 
                    file = chooser.showOpenDialog(primaryStage);
                    fileName = file.getPath();  
                    
                    if (file != null) {
                        info.setText("Opening Journal...\n");

                        if (fileName.contains(".xml") || fileName.contains(".txt")) {
                            if (fileName.contains(".xml")) {
                                fileType = "xml";
                            } else {
                                fileType = "txt";
                            }
                            
                            ///////  Code to set up the thread  ////////
                            // First set up our updater
                            TextFieldUpdater updater = new TextFieldUpdater();
                            updater.text = info;

                            // Now set up our openFile                           
                            ImportFile openFile = new ImportFile(updater, fileName, fileType);

                            // Now start the thread to run the openFie;
                            Thread t1 = new Thread(openFile);
                            t1.start();
                            
                            info.setText("Done Opening Journal\n");
                        } else {
                            info.setText("The file is not valid, choose a xml or txt file\n");
                        }
                    } else {
                        info.setText("File is null, choose another file\n");
                    }
                } catch (Exception e) {
                    // It does nothing when the cancel button is clicked
                    info.setText("Open file has been cancelled\n");
                }
            }
        });

/****************************************************************************
 * Action Event Handler for "Display Entries" button
 ***************************************************************************/ 
        
        displayE.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
               myJournal = new Journal();
               Display display = new Display();
               List<Entry> journalEntries = new ArrayList<>();
               Thread t = new Thread();
               t.start();
                if (file != null) {
                    info.setText("Displaying Entries...\n");  
                    journalEntries = myJournal.createList(fileName, fileType);                                  
                    display.textFieldDisplay(journalEntries, txtContent);
                } else {
                    info.setText("File is null. Open a file first.\n");
                }
            }
        });

/****************************************************************************
 * Action Event Handler for "AddEntry" button
 ***************************************************************************/         

        addEntry.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
                // Enable the text fields to allow the user to type the Entry
                dateField.clear();          
                dateField.setDisable(false);
                txtContent.clear();
            }
        });
        
/****************************************************************************
 * Action Event Handler for "Save" button
 * Save checks for the different scenarios before saving and entry
 * Ex: Entry and Date fields cannot be entered
 *     The format of the date must be the required
 *     The file to save the entry should be a valid file
 ***************************************************************************/     
        save.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                
                myJournal = new Journal();
                String content = "";
                if ((txtContent.getText() != null) && (!txtContent.getText().isEmpty())) {
                    if ((dateField.getText()) != null && !dateField.getText().isEmpty()) {
                        
                        // Make sure the date follows the format yyyy-mm-dd
                        if (!dateField.getText().matches("\\d{4}-\\d{2}-\\d{2}")) {
                            info.setText("Date must follow the format specified. No spaces\n");
                            dateField.clear();
                        } else {
                        try {
                            FileChooser chooser = new FileChooser(); 
                            file = chooser.showOpenDialog(primaryStage);
                            fileName = file.getPath();

                            if (file != null) {            

                                if (fileName.contains(".xml") || fileName.contains(".txt")) {
                                    if (fileName.contains(".xml")) {
                                        content = dateField.getText() + "\n" + txtContent.getText();
                                        myJournal.addNewEntryXml(content, fileName);
                                    } else {
                                        myJournal.addNewEntryTxt(txtContent.getText(), dateField.getText(), fileName);
                                    }
                                    info.setText("Saved Entry\n");
                                    txtContent.clear();
                                    
                                } else {
                                    info.setText("The file is not valid, choose a xml or txt file\n");
                                }
                            } else {
                                info.setText("File is null, choose another file");
                            }

                        } catch (Exception ex) {
                            // Does nothing when "cancel" is clicked in file chooser
                            info.setText("Saving has been cancelled");
                        }
                        fileType = "";
                        fileName = null;
                        file = null; 
                        }
                    } else {
                        info.setText("Date cannot be empty");
                    }
                } else {
                    info.setText("Entry cannot be empty. Add new entry first");
                }
            }
        });
        
/****************************************************************************
 * Action Event Handler for browsing by topics, date, or scripture
 ***************************************************************************/         
        searchB.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Journal myJournal = new Journal();
                Display display = new Display();
                List<Entry> searchedList = new ArrayList<>();
                
                if (file != null) {
                                                      
                    if (browse.getSelectionModel() != null) {
                        info.setText("Searching...\n");
                        // Search by Date
                        if (browse.getValue() == "Date") {
                            
                            if ((dateSField.getText()) != null && !dateSField.getText().isEmpty()) {
                        
                                // Make sure the date follows the format yyyy-mm-dd
                                if (!dateSField.getText().matches("\\d{4}-\\d{2}-\\d{2}")) {
                                    info.setText("Date must follow the format specified. No spaces.\n");
                                    dateSField.clear();
                                } else {                   
                                    searchedList = myJournal.searchByDate(dateSField.getText(), fileName, fileType);                                  
                                    display.textFieldDisplay(searchedList, txtContent); 
                                    dateSField.clear();
                                    info.setText("Done Searching");
                                }
                            } else {
                                info.setText("Date is empty. Type the date for the search");
                            }
                            dateSField.clear();
                        } else if (browse.getValue() == "Topic") {
                            if ((topicField.getText()) != null && !topicField.getText().isEmpty()) {
                                searchedList = myJournal.searchByTopic(topicField.getText(), fileName, fileType);
                                display.textFieldDisplay(searchedList, txtContent);
                                topicField.clear();
                                info.setText("Done searching");
                            } else {
                                info.setText("Topic is empty. Type a topic for the search");
                            }
                        } else if (browse.getValue() == "Scripture") {
                            if ((bookField.getText()) != null && !bookField.getText().isEmpty()) {
                                searchedList = myJournal.searchByScripture(bookField.getText(), chapField.getText(), fVerseField.getText(), lVerseField.getText(), fileName, fileType);
                                display.textFieldDisplay(searchedList, txtContent);   
                                bookField.clear();
                                chapField.clear();
                                fVerseField.clear();
                                lVerseField.clear();
                                info.setText("Done Searching");
                            } else {
                                info.setText("Book is empty. Type at least a book for the search");
                            }
                        } else {
                            info.setText("Select a criteria for your search");
                        }                      
                    } else {
                        info.setText("Select a criteria for your search");
                    }
                } else {
                    info.setText("File is null. Open a file first.\n");
                }
            }
        });
        
        
        Scene scene = new Scene(hbox2, 700, 450, Color.rgb(240, 248,255));
        
        primaryStage.setTitle("My Scripture Journal");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
    }
    
    @Override
    public void stop() {
        System.out.println("The end of the application");
        System.exit(0);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }    
}
