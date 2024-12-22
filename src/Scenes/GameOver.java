package Scenes;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import Audio.*;
import Helps.LoadSave;
import Main.Game;
import UI.MyButton;

import javax.imageio.ImageIO;

import static Main.GameStates.*;

public class GameOver extends GameScene implements SceneMethods {

    private MyButton bReplay, bMenu, bEdit;

    public GameOver(Game game) {
        super(game);
        initButtons();
    }

    private void initButtons() {

        int w = 150;
        int h = w / 3;
        int x = 640 / 2 - w / 2;
        int y = 300;
        int yOffset = 100;

        bMenu = new MyButton("Menu", x, y + (2 * yOffset), w, h);
        bReplay = new MyButton("Replay", x, y, w, h);
        bEdit = new MyButton("Edit", x, y+ yOffset, w, h);
    }

    @Override
    public void render(Graphics g) {
        drawBackground(g);

        // buttons
        g.setFont(new Font("Minecraftia", Font.BOLD, 20));
        bMenu.draw(g);
        bReplay.draw(g);
        bEdit.draw(g);
    }

    private void replayGame() {
        // reset everything
        resetAll();

        // change state to playing
        SetGameState(PLAYING);

    }

    private void resetAll() {
        game.getPlaying().resetEverything();
    }

    public static BufferedImage getBackground() {
        BufferedImage background = null;
        InputStream img = LoadSave.class.getClassLoader().getResourceAsStream("background/over.png");

        try {
            background = ImageIO.read(img);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return background;
    }

    public static void drawBackground(Graphics g) {
        BufferedImage background = getBackground();
        g.drawImage(background, 0, 0, 640, 750, null);
    }

    @Override
    public void mouseClicked(int x, int y) {
        if (bMenu.getBounds().contains(x, y)) {
            SetGameState(MENU);
            resetAll();
        } else if (bReplay.getBounds().contains(x, y))
            replayGame();
        else if (bEdit.getBounds().contains(x, y))
            SetGameState(EDIT);

    }

    @Override
    public void mouseMoved(int x, int y) {
        bMenu.setMouseOver(false);
        bReplay.setMouseOver(false);

        if (bMenu.getBounds().contains(x, y))
            bMenu.setMouseOver(true);
        else if (bReplay.getBounds().contains(x, y))
            bReplay.setMouseOver(true);
        else if(bEdit.getBounds().contains(x, y))
            bEdit.setMouseOver(true);

    }

    @Override
    public void mousePressed(int x, int y) {
        if (bMenu.getBounds().contains(x, y))
            bMenu.setMousePressed(true);
        else if (bReplay.getBounds().contains(x, y))
            bReplay.setMousePressed(true);
        else if (bEdit.getBounds().contains(x, y))
            bEdit.setMousePressed(true);

    }

    @Override
    public void mouseReleased(int x, int y) {
        bMenu.resetBooleans();
        bReplay.resetBooleans();
        bEdit.resetBooleans();
    }

    @Override
    public void mouseDragged(int x, int y) {

    }
}
