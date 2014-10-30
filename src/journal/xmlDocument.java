/*************************************************************************
 * Author: Heidy Cespedes
 * CS246
 * The class is used to manage different tasks related with XML files. 
 * Such as read from an XML file
 * Create a Document object to create a XML file
 *************************************************************************/
package journal;


import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/****************************************************************************
 * Define the xmlDocument class
 ***************************************************************************/
public class xmlDocument {
      
       public void buildXmlDocument(List<Entry> entries, String file) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        builder = dbf.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element element = doc.createElement("journal");
        doc.appendChild(element);
        //TODO: Put for loop for list reading here
        for(Entry ent: entries){
           
           Element entryElement = doc.createElement("entry");
           entryElement.setAttribute("date", ent.getDate());
           element.appendChild(entryElement);
           
           for (scripture scrip:ent.getScriptures()) {
               Element scripElement = doc.createElement("scripture");
               entryElement.appendChild(scripElement);
               scripElement.setAttribute("book", scrip.getBook());
               scripElement.setAttribute("chapter", Integer.toString(scrip.getChapter()));
               
               if (scrip.getFirstVerse() != 0) {
                scripElement.setAttribute("startverse", Integer.toString(scrip.getFirstVerse()));
               }
               if (scrip.getLastVerse() != 0) {
                scripElement.setAttribute("endverse", Integer.toString(scrip.getLastVerse()));
               }
           }
           
           for (String topic:ent.getTopics()) {
               Element topicElement = doc.createElement("topic");
               Text topicText = doc.createTextNode(topic);
               topicElement.appendChild(topicText);
               entryElement.appendChild(topicElement);
           }
           
           Element contentElement = doc.createElement("content");
           Text contentText = doc.createTextNode(ent.getContent());
           contentElement.appendChild(contentText);
           entryElement.appendChild(contentElement);
        }
        saveDocument(doc, file);
    }    
       
/***********************************************************************
 * Takes the XML file and put it in an object "document" to parse it and 
 * manage the information needed to create the objects for the journal
 * and entry objects
 **********************************************************************/  
    public List<Entry> readXml(String file) {
        List<Entry> entryList = new ArrayList<>();
           
        try {
            File fXmlFile = new File(file);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            
            doc.getDocumentElement().normalize();
            entryList = parseXmlData(doc);               
            
        }catch (ParserConfigurationException | SAXException | IOException | DOMException e){
            e.printStackTrace();
        }
        
        return entryList;
    }
       
/***********************************************************************
 * Takes the document object and divide the document by tags, and children
 * in order to parse it and create the entry objects and fill up the 
 * information in each entry.
 * Finally returns a list of Entries
 **********************************************************************/      
    public List<Entry> parseXmlData(Document doc){
        List<Entry> entryList = new ArrayList<>();
        String date = null;
        String content = null;
        String book;
        int chapter = 0;
        int firstVerse = 0;
        int lastVerse = 0;
        
        NodeList nList = doc.getElementsByTagName("entry");
            
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);       
                    
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    NodeList childNodes = nNode.getChildNodes();
                    
                    List<scripture> scriptures = new ArrayList<>();
                    List<String> topics = new ArrayList<>();
                    
                    for (int i = 0; i < childNodes.getLength(); i++) {
                        Node cNode = childNodes.item(i);
                        
                        if (cNode.getNodeType() == Node.ELEMENT_NODE) {
                            if (cNode.getNodeName() == "scripture" ) {
                                Element cElement = (Element) cNode;
                            
                                book = cElement.getAttribute("book");
                            
                                if (cElement.hasAttribute("chapter")) {
                                    //scrip = scrip + " " + cElement.getAttribute("chapter");
                                    chapter = Integer.parseInt(cElement.getAttribute("chapter"));
                                }
                                if (cElement.hasAttribute("startverse")) {
                                   //scrip = scrip + ":" + cElement.getAttribute("startverse");
                                    firstVerse = Integer.parseInt(cElement.getAttribute("startverse"));

                                }
                                if (cElement.hasAttribute("endverse")) {
                                    //scrip = scrip + "-" + cElement.getAttribute("endverse");
                                    lastVerse = Integer.parseInt(cElement.getAttribute("endverse"));

                                }
                            
                                scriptures.add(new scripture(book, chapter, firstVerse, lastVerse));
                            }
                            
                            if ("topic".equals(cNode.getNodeName())) {
                                topics.add(cNode.getTextContent());
                            }
                        
                            if (cNode.getNodeName() == "content") {
                                content = cNode.getTextContent();
                                content = content.trim();
                                content = content.replaceAll("\\n\\s+", "\n");
                            }
                        }
                    }
                    date = eElement.getAttribute("date");
                    entryList.add(new Entry(date, content, scriptures, topics));
                } 
            }
        
        return entryList;
    }       
       
/**************************************************************************
* Display a document object on the screen
* 
**************************************************************************/       
    public void displayDocument(Document xml) throws Exception {
        Transformer tf = TransformerFactory.newInstance().newTransformer();
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        Writer out = new StringWriter();
        tf.transform(new DOMSource(xml), new StreamResult(out));
        System.out.println(out.toString());
    }

/**************************************************************************
* Save a document object into an XML file
* 
**************************************************************************/
    public void saveDocument(Document doc, String file) throws Exception {
        System.out.println("Saving document " + file);
        
        Transformer trans = TransformerFactory.newInstance().newTransformer();
        trans.setOutputProperty(OutputKeys.INDENT, "yes");
        trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        
        Result output = new StreamResult(new File(file));
        Source input = new DOMSource(doc);
        
        trans.transform(input, output);
    } 
}
