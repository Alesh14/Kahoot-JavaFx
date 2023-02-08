package com.example.project3;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

class Music {
    String PATH_NAME;
    Media media;
    MediaPlayer mediaPlayer;

    Music() {
        media = new Media(new File("C:\\Users\\User\\IdeaProjects\\project3\\src\\lilnasx.mp3").toURI().toString());
        mediaPlayer = new MediaPlayer(media);
    }

    Music(String s) {
        media = new Media(new File(s).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
    }

    void play() {
        mediaPlayer.play();
    }

    void pause() {
        mediaPlayer.pause();
    }

    void stop() {
        mediaPlayer.stop();
    }
}
