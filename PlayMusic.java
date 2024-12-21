package Audio;

import Main.GameStates;

import javax.sound.sampled.*;
import java.io.File;

import static Main.GameStates.gameState;

public class PlayMusic {

    private static Clip clip;
    private boolean playMusic;
    // Play music for a given file name
    public static void playMusic(String fileName) {
        try {
            // Stop any currently playing music
            stopMusic();

            File musicFile = new File(fileName);
            if (musicFile.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicFile);
                clip = AudioSystem.getClip();
                clip.open(audioInput);

                // Enable looping
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                clip.start();
            } else {
                System.out.println("File not found: " + fileName);
            }
        } catch (Exception e) {
            System.out.println("Error playing music: " + e.getMessage());
        }
    }


    // Stop the currently playing music
    public static void stopMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close(); // Close the clip to release resources
        }
        clip = null;
    }

    public static void continueMusic() {
        if (clip != null && !clip.isRunning()) {
            clip.start(); // Resume previously paused music
        }
    }

    public static boolean isMusicPlaying() {
        if (clip != null && clip.isRunning()) {
            return true;
        }
        return false;
    }

    public void setMusicPlaying(boolean playMusic) {
        this.playMusic = playMusic;
    }

    public static void playMusicState() {
        // Play the music
        switch (gameState) {
            case MENU:
                PlayMusic.playMusic("res/mainmenu.wav");
                break;

            case EDIT:
                playMusic("res/edit.wav");
                break;

            case PLAYING:
                playMusic("res/play.wav");
                break;

            case GAME_OVER:
                playMusic("res/gameover.wav");
                break;
        }
    }

}
