package UI;

import static Main.GameStates.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import Helps.Constants.Towers;
import Helps.LoadSave;
import Main.Game;
import Object.Tower;
import Scenes.Playing;

import javax.imageio.ImageIO;

public class ActionBar extends Bar {

    private final Playing playing;
    private MyButton bMenu, bPause, bReplay;
    private static Game game;
    private MyButton[] towerButtons;
    private Tower selectedTower;
    private Tower displayedTower;
    private MyButton sellTower, upgradeTower;
    private DecimalFormat formatter;
    private int gold = 100;
    private boolean showTowerCost;
    private int towerCostType;

    private int lives = 25;

    public ActionBar(int x, int y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        formatter = new DecimalFormat("0.0");
        initButtons();
    }

    public void resetEverything() {
        lives = 25;
        towerCostType = 0;
        showTowerCost = false;
        gold = 150;
        selectedTower = null;
        displayedTower = null;
    }

    private void initButtons() {

        bMenu = new MyButton("Menu", 2, 645, 100, 30);
        bReplay = new MyButton("Replay", 2, 680, 100, 30);
        bPause = new MyButton("Pause", 2, 715, 100, 30);

        towerButtons = new MyButton[3];

        int w = 50;
        int h = 50;
        int xStart = 110;
        int yStart = 650;
        int xOffset = (int) (w * 1.1f);

        for (int i = 0; i < towerButtons.length; i++)
            towerButtons[i] = new MyButton("", xStart + xOffset * i, yStart, w, h, i);

//		sellTower, upgradeTower;

        sellTower = new MyButton("Sell", 420, 702, 80, 25);
        upgradeTower = new MyButton("Upgrade", 535, 702, 90, 25);

    }

    public void removeOneLife() {
        lives--;
        if (lives <= 0)
            SetGameState(GAME_OVER);
    }

    private void drawButtons(Graphics g) {
        g.setFont(new Font("Minecraftia", Font.PLAIN, 15));
        bMenu.draw(g);
        bPause.draw(g);
        bReplay.draw(g);

        for (MyButton b : towerButtons) {
            g.setColor(Color.gray);
            g.fillRect(b.x, b.y, b.width, b.height);
            g.drawImage(playing.getTowerManager().getTowerImgs()[b.getId()], b.x, b.y, b.width, b.height, null);
            drawButtonFeedback(g, b);
        }
    }

    public void draw(Graphics g) {

        // Background
        drawBackground(g);

        // Buttons
        drawButtons(g);

        // DisplayedTower
        drawDisplayedTower(g);

        // Wave info
        drawWaveInfo(g);

        // Gold + Lives info
        drawInfo(g);


        // Seperate rod
        drawSeperateRod(g);

        // Draw Game Win
        showGameWon(g);

        // Draw Tower Cost
        if (showTowerCost)
            drawTowerCost(g);


        // Game paused text
        if (playing.isGamePaused()) {
            g.setColor(Color.black);
            g.drawRect(240, 235, 160, 45);
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(240, 235, 160, 45);
            g.setFont(new Font("Minecraftia", Font.PLAIN, 30));
            g.setColor(Color.red);
            g.drawString("PAUSED!", 250 , 290);
        }
    }

    private void drawTowerCost(Graphics g) {
        g.setColor(Color.gray);
        g.fillRect(280, 650, 120, 50);
        g.setColor(Color.black);
        g.drawRect(280, 650, 120, 50);

        g.drawString("" + getTowerCostName(), 285, 670);
        g.drawString("Cost: " + getTowerCostCost() + "g", 285, 695);

        // Show this if player lacks gold for the selected tower.
        if (isTowerCostMoreThanCurrentGold()) {
            g.setFont(new Font("LucidaSans", Font.BOLD, 20));
            g.setColor(Color.RED);
            g.drawString("Can't Afford", 150, 640);

        }

    }

    private boolean isTowerCostMoreThanCurrentGold() {
        return getTowerCostCost() > gold;
    }

    private String getTowerCostName() {
        return Towers.GetName(towerCostType);
    }

    private int getTowerCostCost() {
        return Towers.GetTowerCost(towerCostType);
    }

    private void drawInfo(Graphics g) {
        g.setColor(Color.ORANGE);
        g.drawString("Gold: " + gold + "g", 110, 725);
        g.drawString("Lives: " + lives, 110, 745);
    }

    private void drawSeperateRod(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(217, 705, 3,40);
    }

    private void drawWaveInfo(Graphics g) {
        g.setColor(Color.ORANGE);
        g.setFont(new Font("LucidaSans", Font.BOLD, 20));
        drawWaveTimerInfo(g);
        drawEnemiesLeftInfo(g);
        drawWavesLeftInfo(g);

    }

    private void drawWavesLeftInfo(Graphics g) {
        int current = playing.getWaveManager().getWaveIndex();
        int size = playing.getWaveManager().getWaves().size();
        g.drawString("Wave " + (current + 1) + " / " + size, 225, 725);

    }

    private void drawEnemiesLeftInfo(Graphics g) {
        int remaining = playing.getEnemyManger().getAmountOfAliveEnemies();
        g.drawString("Enemies Left: " + remaining, 225, 745);
    }

    private void drawWaveTimerInfo(Graphics g) {
        if (playing.getWaveManager().isWaveTimerStarted()) {

            float timeLeft = playing.getWaveManager().getTimeLeft();
            String formattedText = formatter.format(timeLeft);
            g.drawString("Time Left: " + formattedText, 425, 750);
        }
    }

    private void drawDisplayedTower(Graphics g) {
        if (displayedTower != null) {
            g.setColor(Color.gray);
            g.fillRect(410, 645, 220, 85);
            g.setColor(Color.black);
            g.drawRect(410, 645, 220, 85);
            g.drawRect(420, 650, 50, 50);
            g.drawImage(playing.getTowerManager().getTowerImgs()[displayedTower.getTowerType()], 420, 650, 50, 50, null);
            g.setFont(new Font("Minecraftia", Font.PLAIN, 15));
            g.drawString("" + Towers.GetName(displayedTower.getTowerType()), 480, 675);
            g.drawString("ID: " + displayedTower.getId(), 480, 700);
            g.drawString("| Tier: " + displayedTower.getTier(), 520, 700);
            drawDisplayedTowerBorder(g);
            drawDisplayedTowerRange(g);

            // Sell button
            sellTower.draw(g);
            drawButtonFeedback(g, sellTower);

            // Upgrade Button
            if (displayedTower.getTier() < 3 && gold >= getUpgradeAmount(displayedTower)) {
                upgradeTower.draw(g);
                drawButtonFeedback(g, upgradeTower);
            }

            if (sellTower.isMouseOver()) {
                g.setFont(new Font("LucidaSans", Font.BOLD, 15));
                g.setColor(Color.red);
                g.drawString("Sell for: " + getSellAmount(displayedTower) + "g", 530, 750);
            } else if (upgradeTower.isMouseOver() && gold >= getUpgradeAmount(displayedTower)) {
                g.setFont(new Font("LucidaSans", Font.BOLD, 15));
                g.setColor(Color.blue);
                g.drawString("Upgrade for: " + getUpgradeAmount(displayedTower) + "g", 520, 750);
            }

        }

    }

    private int getUpgradeAmount(Tower displayedTower) {
        return (int) (Towers.GetTowerCost(displayedTower.getTowerType()) * 0.3f);
    }

    private int getSellAmount(Tower displayedTower) {
        int upgradeCost = (displayedTower.getTier() - 1) * getUpgradeAmount(displayedTower);
        upgradeCost *= 0.5f;

        return Towers.GetTowerCost(displayedTower.getTowerType()) / 2 + upgradeCost;
    }

    private void drawDisplayedTowerRange(Graphics g) {
        g.setColor(Color.white);
        g.drawOval(displayedTower.getX() + 16 - (int) (displayedTower.getRange() * 2) / 2, displayedTower.getY() + 16 - (int) (displayedTower.getRange() * 2) / 2, (int) displayedTower.getRange() * 2,
                (int) displayedTower.getRange() * 2);

    }

    private void drawDisplayedTowerBorder(Graphics g) {

        g.setColor(Color.CYAN);
        g.drawRect(displayedTower.getX(), displayedTower.getY(), 32, 32);

    }

    public void displayTower(Tower t) {
        displayedTower = t;
    }

    private void sellTowerClicked() {
        playing.removeTower(displayedTower);
        gold += Towers.GetTowerCost(displayedTower.getTowerType()) / 2;

        int upgradeCost = (displayedTower.getTier() - 1) * getUpgradeAmount(displayedTower);
        upgradeCost *= 0.5f;
        gold += upgradeCost;

        displayedTower = null;

    }

    private void upgradeTowerClicked() {
        playing.upgradeTower(displayedTower);
        gold -= getUpgradeAmount(displayedTower);

    }

    public void togglePause() {
        playing.setGamePaused(!playing.isGamePaused());

        if (playing.isGamePaused())
            bPause.setText("Unpause");
        else
            bPause.setText("Pause");

    }

    public void mouseClicked(int x, int y) {
        if (bMenu.getBounds().contains(x, y))
            SetGameState(MENU);
        else if (bPause.getBounds().contains(x, y))
            togglePause();
        else if (bReplay.getBounds().contains(x, y))
            playing.resetEverything();
        else {

            if (displayedTower != null) {
                if (sellTower.getBounds().contains(x, y)) {
                    sellTowerClicked();

                    return;
                } else if (upgradeTower.getBounds().contains(x, y) && displayedTower.getTier() < 3 && gold >= getUpgradeAmount(displayedTower)) {
                    upgradeTowerClicked();
                    return;
                }
            }
            for (MyButton b : towerButtons) {
                if (b.getBounds().contains(x, y)) {
                    if (!isGoldEnoughForTower(b.getId()))
                        return;

                    selectedTower = new Tower(0, 0, -1, b.getId());
                    playing.setSelectedTower(selectedTower);
                    return;
                }
            }
        }

    }

    private boolean isGoldEnoughForTower(int towerType) {

        return gold >= Towers.GetTowerCost(towerType);
    }

    public void mouseMoved(int x, int y) {
        bMenu.setMouseOver(false);
        bPause.setMouseOver(false);
        bReplay.setMouseOver(false);
        showTowerCost = false;
        sellTower.setMouseOver(false);
        upgradeTower.setMouseOver(false);

        for (MyButton b : towerButtons)
            b.setMouseOver(false);
        if (bMenu.getBounds().contains(x, y))
            bMenu.setMouseOver(true);
        else if (bPause.getBounds().contains(x, y))
            bPause.setMouseOver(true);
        else if (bReplay.getBounds().contains(x, y))
            bReplay.setMouseOver(true);

        else {

            if (displayedTower != null) {
                if (sellTower.getBounds().contains(x, y)) {
                    sellTower.setMouseOver(true);
                    return;
                } else if (upgradeTower.getBounds().contains(x, y) && displayedTower.getTier() < 3) {
                    upgradeTower.setMouseOver(true);
                    return;
                }
            }

            for (MyButton b : towerButtons)
                if (b.getBounds().contains(x, y)) {
                    b.setMouseOver(true);
                    showTowerCost = true;
                    towerCostType = b.getId();
                    return;
                }
        }
    }

    public void mousePressed(int x, int y) {
        if (bMenu.getBounds().contains(x, y))
            bMenu.setMousePressed(true);
        else if (bPause.getBounds().contains(x, y))
            bPause.setMousePressed(true);
        else if (bReplay.getBounds().contains(x, y))
            bReplay.setMousePressed(true);

        else {

            if (displayedTower != null) {
                if (sellTower.getBounds().contains(x, y)) {
                    sellTower.setMousePressed(true);
                    return;
                } else if (upgradeTower.getBounds().contains(x, y) && displayedTower.getTier() < 3) {
                    upgradeTower.setMousePressed(true);
                    return;
                }
            }

            for (MyButton b : towerButtons)
                if (b.getBounds().contains(x, y)) {
                    b.setMousePressed(true);
                    return;
                }
        }

    }

    public void mouseReleased(int x, int y) {
        bMenu.resetBooleans();
        bPause.resetBooleans();
        bReplay.resetBooleans();

        for (MyButton b : towerButtons)
            b.resetBooleans();
        sellTower.resetBooleans();
        upgradeTower.resetBooleans();

    }

    public void payForTower(int towerType) {
        this.gold -= Towers.GetTowerCost(towerType);

    }

    public void addGold(int getReward) {
        this.gold += getReward;
    }

    public int getLives() {
        return lives;
    }

    public static BufferedImage getBackground() {
        BufferedImage background = null;
        InputStream img = LoadSave.class.getClassLoader().getResourceAsStream("background/play.png");

        try {
            background = ImageIO.read(img);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return background;
    }

    public static void drawBackground(Graphics g) {
        BufferedImage background = getBackground();
        g.drawImage(background, 0, 640, 640, 110, null);
    }

    private void showGameWon(Graphics g) {
        int lives = getLives();
        int current = playing.getWaveManager().getWaveIndex();
        int size = playing.getWaveManager().getWaves().size();
        int remaining = playing.getEnemyManger().getAmountOfAliveEnemies();
        int waveleft = size - (current + 1);
        if (lives > 0 && waveleft == 0 && remaining == 0) {
            BufferedImage background = getWonLogo();
            g.drawImage(background, 160, 80, 320,320, null);
        }
    }

    private BufferedImage getWonLogo() {
        BufferedImage background = null;
        InputStream img = LoadSave.class.getClassLoader().getResourceAsStream("WinLogo.png");

        try {
            background = ImageIO.read(img);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return background;
    }
}
