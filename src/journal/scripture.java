/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package journal;

/**
 *
 * @author HEIDY2016
 */
public class scripture {
    private String book;
    private int chapter;
    private int firstVerse;
    private int lastVerse;
    
    public scripture() {
        this.book = "";
        this.chapter = 0;
        this.firstVerse = 0;
        this.lastVerse = 0;
    }
    
    public scripture(String book, int chapter, int firstVerse, int lastVerse){
        this.book = book;
        this.chapter = chapter;
        this.firstVerse = firstVerse;
        this.lastVerse = lastVerse;
    }
    
    public String getBook() {
        return book;
    }

    public int getChapter() {
        return chapter;
    }

    public int getFirstVerse() {
        return firstVerse;
    }

    public int getLastVerse() {
        return lastVerse;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    public void setFirstVerse(int firstVerse) {
        this.firstVerse = firstVerse;
    }

    public void setLastVerse(int lastVerse) {
        this.lastVerse = lastVerse;
    }
}