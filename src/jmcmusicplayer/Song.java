/*
Copyright 2020 John Renald Garcines
Diploma of Software Development
South Metropolitan TAFE Murdoch Campus
*/
/*
    Created on : 2020/04/25, 18:25:16
    Author     : John
*/
package jmcmusicplayer;

public class Song {

    private String title;
    private String path;
    private String artist;

    //default constructor for opencsv to create song object
    public Song() {
    }

    public Song(String title) {
        this.title = title;
    }

    public Song(String title, String path) {
        this.title = title;
        this.path = path;
    }

    public Song(String title, String path, String artist) {
        this.title = title;
        this.path = path;
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
