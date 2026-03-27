package com.pao.laboratory05.playlist;

import java.util.Comparator;

public class SongDurationComparator implements Comparator<Song> {
    @Override
    public int compare(Song o1, Song o2) {
        return o1.durationSeconds() - o2.durationSeconds();
    }
}
