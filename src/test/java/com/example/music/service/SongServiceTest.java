package com.example.music.service;

import com.example.music.domain.Song;
import com.example.music.repository.SongRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SongServiceTest {

    @Mock
    private SongRepository songRepository;

    @InjectMocks
    private SongService songService;

    @Test
    public void shouldSaveSong() {
        Song song1 = new Song(1L, "Song 1", "Artist 1", "Genre 1");
        when(songRepository.save(song1)).thenReturn(song1);

        Song savedSong = songService.saveSong(song1);

        assertThat(savedSong).isNotNull();
        assertThat(savedSong.getId()).isEqualTo(1L);
        assertThat(savedSong.getTitle()).isEqualTo("Song 1");
        assertThat(savedSong.getArtist()).isEqualTo("Artist 1");

        verify(songRepository, times(1)).save(song1);
    }

    @Test
    public void shouldGetSongByTitle_Found() {
        Song song1 = new Song(1L, "Song 1", "Artist 1", "Genre 1");
        when(songRepository.findByTitle("Song 1")).thenReturn(Optional.of(song1));

        Optional<Song> foundSong = songService.getSongByTitle("Song 1");

        assertThat(foundSong).isPresent();
        assertThat(foundSong.get()).isEqualTo(song1);

        verify(songRepository, times(1)).findByTitle("Song 1");
    }

    @Test
    public void shouldGetSongByTitle_NotFound() {
        when(songRepository.findByTitle("Song 3")).thenReturn(Optional.empty());

        Optional<Song> foundSong = songService.getSongByTitle("Song 3");

        assertThat(foundSong).isNotPresent();

        verify(songRepository, times(1)).findByTitle("Song 3");
    }

    @Test
    public void shouldGetSongs() {
        Song song1 = new Song(1L, "Song 1", "Artist 1", "Genre 1");
        Song song2 = new Song(2L, "Song 2", "Artist 2", "Genre 2");
        when(songRepository.findAll()).thenReturn(List.of(song1, song2));

        List<Song> allSongs = songService.getSongs();

        assertThat(allSongs).hasSize(2);
        assertThat(allSongs).containsExactly(song1, song2);

        verify(songRepository, times(1)).findAll();
    }
}
