/****************************************************************************
 *
 * @author HEIDY2016
 ****************************************************************************/
package journal;

import java.util.List;

public class Entry {
    private String content;
    private String date;
    private String title;
    private List<scripture> scriptures;
    private List<String> topics;
    
    public Entry(String entryDate, String entryContent, List<scripture> scrip, List<String> topic) {
        this.date = entryDate;
        this.content = entryContent;
        this.scriptures = scrip;
        this.topics = topic;
    }
    
    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public void addScriptures(scripture scrip) {
        scriptures.add(scrip);
    }
    
    public void addTopics(String topic) {
        topics.add(topic);
    }
    
    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
    
    public List<String> getTopics() {
        return topics;
    }
    
    public List<scripture> getScriptures() {
        return scriptures;
    }
}


