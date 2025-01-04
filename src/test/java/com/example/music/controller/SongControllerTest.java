package com.example.music.controller;

import com.example.music.domain.Song;
import com.example.music.service.SongService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(SongController.class)
public class SongControllerTest {

    @MockBean
    private SongService songService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldSaveSong() throws Exception {
        Song song1 = new Song(1L, "Song 1", "Artist 1", "Genre 1");
        when(songService.saveSong(any(Song.class))).thenReturn(song1);

        mockMvc.perform(post("/songs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Song 1\",\"artist\":\"Artist 1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Song 1"))
                .andExpect(jsonPath("$.artist").value("Artist 1"));

        verify(songService, times(1)).saveSong(any(Song.class));
    }

    @Test
    public void shouldGetSongByTitle_Found() throws Exception {
        Song song1 = new Song(1L, "Song 1", "Artist 1", "Genre 1");
        when(songService.getSongByTitle("Song 1")).thenReturn(Optional.of(song1));

        mockMvc.perform(get("/songs/title/Song 1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Song 1"))
                .andExpect(jsonPath("$.artist").value("Artist 1"));

        verify(songService, times(1)).getSongByTitle("Song 1");
    }

    @Test
    public void shouldGetSongByTitle_NotFound() throws Exception {
        when(songService.getSongByTitle("Nonexistent Song")).thenReturn(Optional.empty());

        mockMvc.perform(get("/songs/title/Nonexistent Song"))
                .andExpect(status().isNotFound());

        verify(songService, times(1)).getSongByTitle("Nonexistent Song");
    }

    @Test
    public void shouldGetAllSongs() throws Exception {
        Song song1 = new Song(1L, "Song 1", "Artist 1", "Genre 1");
        Song song2 = new Song(2L, "Song 2", "Artist 2", "Genre 2");
        when(songService.getSongs()).thenReturn(List.of(song1, song2));

        mockMvc.perform(get("/songs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(songService, times(1)).getSongs();
    }
}
