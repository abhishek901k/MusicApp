package com.example.music.service;

import com.example.music.repository.SongRepository;
import com.example.music.domain.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SongService {

    private final SongRepository songRepository;

    @Autowired
    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public Song saveSong(Song song) {
        return songRepository.save(song);
    }

    public Optional<Song> getSongByTitle(String title) {
        return songRepository.findByTitle(title);
    }

    public List<Song> getSongs() {
        return songRepository.findAll();
    }
}
