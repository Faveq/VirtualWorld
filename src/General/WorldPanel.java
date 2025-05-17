package General;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class WorldPanel extends JPanel {
    private final World world;
    private final int cellSize = 45;
    private final int padding = 15;
    private final JLabel turnLabel;
    private Point selectedCell = null;

    // Nowe pola klasowe
    private JPanel buttonPanel;
    private JPanel dpadPanel;
    private JButton nextTurnButton;
    private JButton saveButton;
    private JButton loadButton;
    private JButton specialButton;
    private JLabel specialCooldownLabel = new JLabel();

    public WorldPanel(World world) {
        this.world = world;
        setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGrid(g);
                drawOrganisms(g);
                if (selectedCell != null) {
                    g.setColor(new Color(173, 216, 230, 128));
                    g.fillRect(selectedCell.x * cellSize, selectedCell.y * cellSize, cellSize, cellSize);
                }
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(world.getWidth() * cellSize, world.getHeight() * cellSize + 70);
            }
        };

        // Obsługa kliknięcia na pole
        gridPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX() / cellSize;
                int y = e.getY() / cellSize;
                if (x >= 0 && x < world.getWidth() && y >= 0 && y < world.getHeight()) {
                    selectedCell = new Point(x, y);
                    repaint();
                    showOrganismModal(x, y);
                }
            }
        });

        turnLabel = new JLabel("Tura: " + world.getRoundNumber());

        // Inicjalizacja paneli i przycisków
        buttonPanel = new JPanel();
        nextTurnButton = new JButton("Następna tura");
        saveButton = new JButton("Zapis");
        loadButton = new JButton("Wczytanie");
        dpadPanel = createDPadPanel();
        specialButton = new JButton("Specjalna umiejętność"); // Inicjalizacja przycisku

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(turnLabel);

        nextTurnButton.addActionListener(e -> {
            world.handleNextTurn();
            turnLabel.setText("Tura: " + world.getRoundNumber());
            updateControlPanel();
            repaint();
        });

        saveButton.addActionListener(e -> {
            world.saveGame();
            JOptionPane.showMessageDialog(this, "Gra została zapisana!", "Zapis gry", JOptionPane.INFORMATION_MESSAGE);
        });
        loadButton.addActionListener(e -> {
            world.loadGame();
            turnLabel.setText("Tura: " + world.getRoundNumber());
            updateControlPanel();
            repaint();
            JOptionPane.showMessageDialog(this, "Gra została wczytana!", "Wczytanie gry", JOptionPane.INFORMATION_MESSAGE);
        });

        // Obsługa przycisku specjalnej umiejętności
        specialButton.addActionListener(e -> {
            world.setHumanNextMove(HumanMove.SPECIAL);
            world.handleNextTurn();
            turnLabel.setText("Tura: " + world.getRoundNumber());
            updateControlPanel();
            repaint();
        });

        // Ustaw początkowy stan panelu sterowania
        updateControlPanel();

        add(gridPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Nowa metoda do dynamicznej aktualizacji panelu sterowania
    private void updateControlPanel() {
        buttonPanel.remove(nextTurnButton);
        buttonPanel.remove(dpadPanel);
        buttonPanel.remove(specialButton);
        buttonPanel.remove(specialCooldownLabel);

        if (!world.isHumanAlive()) {
            buttonPanel.add(nextTurnButton, 1); // po turnLabel
        } else {
            buttonPanel.add(dpadPanel, 1); // po turnLabel
            buttonPanel.add(specialButton, 2); // po dpadPanel
            buttonPanel.add(specialCooldownLabel, 3); // licznik cooldownu

            // Aktualizacja stanu przycisku i licznika
            if (world.getIsSpecialActive()) {
                specialButton.setEnabled(false);
                specialButton.setText("Specjalna aktywna!");
                specialButton.setBackground(Color.ORANGE);
                int left = Constants.HUMAN_SPECIAL_MOVE_COOLDOWN - (world.getRoundNumber() - world.getSpecialMoveRound());
                if (left < 0) left = 0;
                specialCooldownLabel.setText("Pozostało: " + left + " tur");
            } else if (!world.getIsSpecialReady()) {
                specialButton.setEnabled(false);
                specialButton.setText("Specjalna (cooldown)");
                specialButton.setBackground(Color.LIGHT_GRAY);
                int left = Constants.HUMAN_SPECIAL_MOVE_COOLDOWN - (world.getRoundNumber() - world.getSpecialMoveRound());
                if (left < 0) left = 0;
                specialCooldownLabel.setText("Pozostało: " + left + " tur");
            } else {
                specialButton.setEnabled(true);
                specialButton.setText("Specjalna umiejętność");
                specialButton.setBackground(null);
                specialCooldownLabel.setText("");
            }
        }
        if (saveButton.getParent() != buttonPanel) buttonPanel.add(saveButton);
        if (loadButton.getParent() != buttonPanel) buttonPanel.add(loadButton);

        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    private void drawGrid(Graphics g) {
        for (int i = 0; i < world.getWidth(); i++) {
            for (int j = 0; j < world.getHeight(); j++) {
                int x = i * cellSize;
                int y = j * cellSize;
                g.drawRect(x, y, cellSize, cellSize);
            }
        }
    }

    private JPanel createDPadPanel() {
        JPanel dpad = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(2, 2, 2, 2);

        JButton up = new JButton("↑");
        JButton down = new JButton("↓");
        JButton left = new JButton("←");
        JButton right = new JButton("→");

        up.addActionListener(e -> {
            world.setHumanNextMove(HumanMove.UP);
            world.handleNextTurn();
            turnLabel.setText("Tura: " + world.getRoundNumber());
            updateControlPanel();
            repaint();
        });
        down.addActionListener(e -> {
            world.setHumanNextMove(HumanMove.DOWN);
            world.handleNextTurn();
            turnLabel.setText("Tura: " + world.getRoundNumber());
            updateControlPanel();
            repaint();
        });
        left.addActionListener(e -> {
            world.setHumanNextMove(HumanMove.LEFT);
            world.handleNextTurn();
            turnLabel.setText("Tura: " + world.getRoundNumber());
            updateControlPanel();
            repaint();
        });
        right.addActionListener(e -> {
            world.setHumanNextMove(HumanMove.RIGHT);
            world.handleNextTurn();
            turnLabel.setText("Tura: " + world.getRoundNumber());
            updateControlPanel();
            repaint();
        });

        c.gridx = 1; c.gridy = 0; dpad.add(up, c);
        c.gridx = 0; c.gridy = 1; dpad.add(left, c);
        c.gridx = 2; c.gridy = 1; dpad.add(right, c);
        c.gridx = 1; c.gridy = 2; dpad.add(down, c);

        return dpad;
    }

    private void drawOrganisms(Graphics g) {
        List<Organism> organisms = world.getOrganisms();
        for (Organism org : organisms) {
            int x = org.getPosition().x * cellSize;
            int y = org.getPosition().y * cellSize;
            g.setColor(Color.BLACK);

            Image img = null;
            java.net.URL imgUrl = getClass().getResource("/resources/" + org.toString() + ".png");
            if (imgUrl != null) {
                img = new ImageIcon(imgUrl).getImage();
            }
            if (img != null) {
                g.drawImage(img, x + 2, y + 2, cellSize - 4, cellSize - 4, this);
            }
        }
    }

    private void showOrganismModal(int x, int y) {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Dodaj organizm", true);
        dialog.setLayout(new GridLayout(0, 3, 5, 5)); // Siatka z 3 kolumnami
        dialog.getContentPane().setBackground(Color.WHITE);

        String[] organismNames = {
                "Human","Fox", "Antylope", "Wolf", "Turtle", "Sheep", "CyberSheep",
                "Grass", "Guarana", "MilkWeed", "NightshadeBerries", "HogweedOfPine",
        };

        for (String name : organismNames) {
            JPanel organismPanel = new JPanel(new BorderLayout());
            organismPanel.setBackground(Color.WHITE);
            organismPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            String imgPath = "/resources/" + name + ".png";
            ImageIcon icon = null;
            java.net.URL imgUrl = getClass().getResource(imgPath);
            if (imgUrl != null) {
                icon = new ImageIcon(imgUrl);
                Image image = icon.getImage();
                Image scaledImage = image.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                icon = new ImageIcon(scaledImage);
            } else {
                icon = new ImageIcon();
            }

            JButton btn = new JButton(icon);
            btn.setToolTipText(name);
            btn.setPreferredSize(new Dimension(60, 60));
            btn.setBackground(Color.WHITE);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);

            JLabel nameLabel = new JLabel(name, JLabel.CENTER);
            nameLabel.setFont(new Font("Arial", Font.PLAIN, 10));

            organismPanel.add(btn, BorderLayout.CENTER);
            organismPanel.add(nameLabel, BorderLayout.SOUTH);

            btn.addActionListener(e -> {
                Organism org = createOrganismByName(name, new Point(x, y));
                if (org != null) {
                    world.addOrganism(org);
                    repaint();
                }
                selectedCell = null;
                dialog.dispose();
            });

            dialog.add(organismPanel);
        }

        JPanel closePanel = new JPanel(new BorderLayout());
        closePanel.setBackground(Color.WHITE);
        JButton closeButton = new JButton("Anuluj");
        closeButton.addActionListener(e -> {
            selectedCell = null;
            dialog.dispose();
        });
        closePanel.add(closeButton, BorderLayout.CENTER);
        dialog.add(closePanel);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private Organism createOrganismByName(String name, Point pos) {
        Organism org = null;
        org = world.getOrganismAt(pos);

        if (org != null) {
            world.removeOrganism(org);
        }

        switch (name) {
            case "Human":
                if (!world.isHumanAlive()) {
                    return new Animals.Human(pos, 0, world);
                } else {
                    JOptionPane.showMessageDialog(this, "W świecie znajduje się już człowiek", "Błąd", JOptionPane.INFORMATION_MESSAGE);
                    return null;
                }
            case "Fox": return new Animals.Fox(pos, 0 ,world);
            case "Antylope": return new Animals.Antylope(pos, 0 ,world);
            case "Grass": return new Plants.Grass(pos, 0 ,world);
            case "Guarana": return new Plants.Guarana(pos, 0 ,world);
            case "MilkWeed": return new Plants.MilkWeed(pos, 0 ,world);
            case "NightshadeBerries": return new Plants.NightshadeBerries(pos, 0 ,world);
            case "Wolf": return new Animals.Wolf(pos, 0 ,world);
            case "Turtle": return new Animals.Turtle(pos, 0 ,world);
            case "Sheep": return new Animals.Sheep(pos, 0 ,world);
            case "CyberSheep": return new Animals.CyberSheep(pos, 0 ,world);
            case "HogweedOfPine": return new Plants.HogweedOfPine(pos, 0 ,world);

            default: return null;
        }
    }
}