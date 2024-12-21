package Scenes;

import javax.imageio.ImageIO;

import Audio.PlayMusic;
import Helps.LoadSave;
import Main.Game;
import UI.MyButton;
import static Main.GameStates.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;


public class Settings extends GameScene implements SceneMethods {

    private MyButton bMenu;

    public Settings(Game game) {
        super(game);
        initButtons();
    }

    private void initButtons() {
        bMenu = new MyButton("Menu", 160, 235, 480 -160, 50);
    }

    @Override
    public void render(Graphics g) {
        drawBackground(g);
        drawButtons(g);
    }

    private void drawButtons(Graphics g) {
        g.setFont(new Font("Minecraftia", Font.BOLD, 20));
        bMenu.draw(g);
    }

    public static BufferedImage getBackground() {
        BufferedImage background = null;
        InputStream img = LoadSave.class.getClassLoader().getResourceAsStream("background/setting.png");

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
        if (bMenu.getBounds().contains(x, y))
            SetGameState(MENU);
    }

    @Override
    public void mouseMoved(int x, int y) {
        bMenu.setMouseOver(false);
        if (bMenu.getBounds().contains(x, y))
            bMenu.setMouseOver(true);
    }

    @Override
    public void mousePressed(int x, int y) {
        if (bMenu.getBounds().contains(x, y))
            bMenu.setMousePressed(true);
    }

    @Override
    public void mouseReleased(int x, int y) {
        resetButtons();
    }

    private void resetButtons() {
        bMenu.resetBooleans();
    }

    @Override
    public void mouseDragged(int x, int y) {}

    public void enter() {
        // Continue the previously playing music
        PlayMusic.continueMusic();
    }
}
