package com.pao.laboratory05.playlist;

import java.util.ArrayList;
import java.util.Arrays;

public class Playlist {
    private String name;
    private Song[] songs = new Song[0];

    public Playlist(String name) {
        this.name = name;
    }

    public  String getName() {
        return name;
    }

    void addSong(Song song) {
        Song[] temp =  new Song[songs.length + 1];
        System.arraycopy(songs, 0, temp, 0, songs.length);
        temp[temp.length - 1] = song;
        this.songs = temp;
    }

    void printSortedByTitle() {
        Song[] temp  = new Song[songs.length];
        System.arraycopy(songs, 0, temp, 0, songs.length);
        Arrays.sort(temp);
        for (int i = 0; i < temp.length; i++) {
            System.out.println(temp[i]);
        }
    }

    void printSortedByDuration() {
        Song[] temp  = new Song[songs.length];
        System.arraycopy(songs, 0, temp, 0, songs.length);
        Arrays.sort(temp, new SongDurationComparator());
        for (int i = 0; i < temp.length; i++) {
            System.out.println(temp[i]);
        }
    }

    int getTotalDuration() {
        int duration = 0;
        for (Song song : songs) {
            duration += song.durationSeconds();
        }
        return duration;
    }
}