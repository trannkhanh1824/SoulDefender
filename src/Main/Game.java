package Main;

import javax.swing.JFrame;

import Helps.LoadSave;
import Audio.*;
import Manager.TileManager;
import Scenes.*;

public class Game extends JFrame implements Runnable {
    private GameStates currentGameState = null;
    private GameScreen gameScreen;
    private Thread gameThread;

    private final double FPS_SET = 120.0;
    private final double UPS_SET = 60.0;

    // Classes
    private Render render;
    private Menu menu;
    private Playing playing;
    private Settings settings;
    private Editing editing;
    private GameOver gameOver;
    private TileManager tileManager;
    private PlayMusic playMusic;

    public Game() {

        initClasses();
        createDefaultLevel();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(400,0 );
        setResizable(false);
        setTitle("Soul Defender");
        add(gameScreen);
        pack();
        setVisible(true);
    }

    private void createDefaultLevel() {
        int[] arr = new int[400];
        for (int i = 0; i < arr.length; i++)
            arr[i] = 0;

        LoadSave.CreateLevel("new_level", arr);

    }

    private void initClasses() {
        tileManager = new TileManager();
        render = new Render(this);
        gameScreen = new GameScreen(this);
        menu = new Menu(this);
        playing = new Playing(this);
        settings = new Settings(this);
        editing = new Editing(this);
        gameOver = new GameOver(this);
        settings = new Settings(this);
    }

    private void start() {
        gameThread = new Thread(this) {
        };

        gameThread.start();
    }

    private void updateGame() {
        if (GameStates.gameState != currentGameState) {
            currentGameState = GameStates.gameState;
            playSceneMusic(currentGameState); // Play music based on the current scene
        }

        switch (GameStates.gameState) {
            case EDIT:
                editing.update();
                break;
            case MENU:
                break;
            case PLAYING:
                playing.update();
                break;
            case SETTINGS:
                break;
            default:
                break;
        }
    }

    private void playSceneMusic(GameStates state) {
        switch (state) {
            case MENU:
                PlayMusic.playMusic("res/mainmenu.wav");
                break;
            case PLAYING:
                PlayMusic.playMusic("res/play.wav");
                break;
            case GAME_OVER:
                PlayMusic.playMusic("res/gameover.wav");
                break;
            case EDIT:
                PlayMusic.playMusic("res/edit.wav");
                break;
            case SETTINGS:
                PlayMusic.continueMusic();
                break;
            default:
                PlayMusic.stopMusic();
                break;
        }
    }



    public static void main(String[] args) {

        Game game = new Game();
        game.gameScreen.initInputs();
        game.start();

    }

    @Override
    public void run() {

        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;

        long lastFrame = System.nanoTime();
        long lastUpdate = System.nanoTime();
        long lastTimeCheck = System.currentTimeMillis();

        int frames = 0;
        int updates = 0;

        long now;

        while (true) {
            now = System.nanoTime();

            // Render
            if (now - lastFrame >= timePerFrame) {
                repaint();
                lastFrame = now;
                frames++;
            }

            // Update
            if (now - lastUpdate >= timePerUpdate) {
                updateGame();
                lastUpdate = now;
                updates++;
            }

            if (System.currentTimeMillis() - lastTimeCheck >= 1000) {
                System.out.println("FPS: " + frames + " + UPS: " + updates);
                frames = 0;
                updates = 0;
                lastTimeCheck = System.currentTimeMillis();
            }

        }

    }

    // Getters and setters
    public Render getRender() {
        return render;
    }

    public Menu getMenu() {
        return menu;
    }

    public Playing getPlaying() {
        return playing;
    }

    public Settings getSettings() {
        return settings;
    }

    public Editing getEditor() {
        return editing;
    }

    public GameOver getGameOver() {
        return gameOver;
    }


    public TileManager getTileManager() {
        return tileManager;
    }

}