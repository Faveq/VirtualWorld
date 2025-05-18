import java.util.ArrayList;
import java.util.Random;
import Animals.*;
import General.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    private static World world;
    private static JFrame frame;
    private static WorldPanel panel;
    private static KeyAdapter keyAdapter;

    public static void main(String[] args) {
        initializeGame();
        createAndShowGUI();
    }

    private static void initializeGame() {
        int width = 15;
        int height = 15;
        BoardType boardType = BoardType.SQUARE;

        ArrayList<Organism> organisms = new ArrayList<>();
        organisms.add(new Sheep(new Point(7, 7))); // Add human in center of map

        world = new World(width, height, organisms, boardType);
    }

    private static void createAndShowGUI() {
        frame = new JFrame("Symulacja świata");
        panel = new WorldPanel(world);
        frame.add(panel);

        // Create menu bar with save and load options
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");

        JMenuItem saveItem = new JMenuItem("Save Game");
        saveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                world.saveGame();
                JOptionPane.showMessageDialog(frame, "Game saved successfully!");
            }
        });

        JMenuItem loadItem = new JMenuItem("Load Game");
        loadItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                world.loadGame();
                panel.updateAfterTurn();
                frame.requestFocus(); // Request focus after loading
                JOptionPane.showMessageDialog(frame, "Game loaded successfully!");
            }
        });

        gameMenu.add(saveItem);
        gameMenu.add(loadItem);
        menuBar.add(gameMenu);
        frame.setJMenuBar(menuBar);

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        };

        frame.addKeyListener(keyAdapter);
        frame.setFocusable(true);
        frame.requestFocus();
        frame.setVisible(true);
    }

    private static void handleKeyPress(KeyEvent e) {
        if (!world.getIsHumanAlive()) {
            world.handleNextTurn();
            panel.updateAfterTurn();
            return;
        }

        HumanMove move = HumanMove.NONE;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                move = HumanMove.UP;
                break;
            case KeyEvent.VK_DOWN:
                move = HumanMove.DOWN;
                break;
            case KeyEvent.VK_LEFT:
                move = HumanMove.LEFT;
                break;
            case KeyEvent.VK_RIGHT:
                move = HumanMove.RIGHT;
                break;
        }

        if (move != HumanMove.NONE) {
            world.setHumanNextMove(move);

            world.handleNextTurn();
            panel.updateAfterTurn();

            System.out.println("Round: " + world.getRoundNumber());
        }
    }
}