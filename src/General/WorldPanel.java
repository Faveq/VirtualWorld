package General;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class WorldPanel extends JPanel {
    private static final int CELL_SIZE = 45;
    private static final int PADDING = 45;

    private final World world;
    private final JLabel turnLabel;
    private Point selectedCell = null;

    private JPanel buttonPanel;
    private JPanel dpadPanel;

    private JButton nextTurnButton;
    private JButton saveButton;
    private JButton loadButton;
    private JButton specialButton;
    private JLabel specialCooldownLabel;

    public WorldPanel(World world) {
        this.world = world;
        setLayout(new BorderLayout());

        turnLabel = new JLabel("Turn: " + world.getRoundNumber());
        buttonPanel = new JPanel();
        nextTurnButton = new JButton("Next turn");
        saveButton = new JButton("Save");
        loadButton = new JButton("Load");
        dpadPanel = createDPadPanel();
        specialButton = new JButton("Special ability");
        specialCooldownLabel = new JLabel();

        JPanel gridPanel = createGridPanel();

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(turnLabel);

        configureButtonActions();

        updateControlPanel();

        add(gridPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createGridPanel() {
        JPanel gridPanel = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (world.getBoardType() == BoardType.HEX) {
                    int hexHeight = CELL_SIZE;
                    int hexWidth = (int) (Math.sqrt(3) / 2 * hexHeight);
                    int vertDist = hexHeight * 3 / 4;

                    for (int y = 0; y < world.getHeight(); y++) {
                        for (int x = 0; x < world.getWidth(); x++) {
                            int px = x * hexWidth + (y % 2) * (hexWidth / 2) + PADDING;
                            int py = y * vertDist+ PADDING;
                            Polygon hex = createHex(px, py, hexHeight / 2);
                            g.setColor(Color.LIGHT_GRAY);
                            g.drawPolygon(hex);
                        }
                    }
                    drawOrganismsHex(g, hexWidth, vertDist, hexHeight / 2);
                    if (selectedCell != null) {
                        int px = selectedCell.x * hexWidth + (selectedCell.y % 2) * (hexWidth / 2);
                        int py = selectedCell.y * vertDist;
                        Polygon hex = createHex(px, py, hexHeight / 2);
                        g.setColor(new Color(173, 216, 230, 128));
                        g.fillPolygon(hex);
                    }
                } else {
                    for (int i = 0; i < world.getWidth(); i++) {
                        for (int j = 0; j < world.getHeight(); j++) {
                            int x = i * CELL_SIZE;
                            int y = j * CELL_SIZE;
                            g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                        }
                    }
                    drawOrganisms(g);
                    if (selectedCell != null) {
                        g.setColor(new Color(173, 216, 230, 128));
                        g.fillRect(selectedCell.x * CELL_SIZE, selectedCell.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    }
                }
            }

            private Polygon createHex(int x, int y, int r) {
                Polygon hex = new Polygon();
                for (int i = 0; i < 6; i++) {
                    double angle = Math.PI / 3 * i + Math.PI / 6;
                    int dx = (int) (x + r * Math.cos(angle));
                    int dy = (int) (y + r * Math.sin(angle));
                    hex.addPoint(dx, dy);
                }
                return hex;
            }

            private void drawOrganismsHex(Graphics g, int hexWidth, int vertDist, int r) {
                List<Organism> organisms = world.getOrganisms();
                for (Organism organism : organisms) {
                    int x = organism.getPosition().x * hexWidth + (organism.getPosition().y % 2) * (hexWidth / 2) + PADDING;
                    int y = organism.getPosition().y * vertDist+ PADDING;
                    drawOrganismImage(g, organism, x - r / 2, y - r / 2);
                }
            }

            @Override
            public Dimension getPreferredSize() {
                if (world.getBoardType() == BoardType.HEX) {
                    int hexWidth = (int) (Math.sqrt(3) / 2 * CELL_SIZE);
                    int width = world.getWidth() * hexWidth + hexWidth / 2 + 2 * PADDING;
                    int height = world.getHeight() * CELL_SIZE * 3 / 4 + CELL_SIZE / 2 + 70 + 2 * PADDING;
                    return new Dimension(width, height);
                } else {
                    return new Dimension(world.getWidth() * CELL_SIZE, world.getHeight() * CELL_SIZE + 70);
                }
            }
        };

        gridPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleGridClick(e);
            }
        });

        return gridPanel;
    }

    private void handleGridClick(MouseEvent e) {
        if (world.getBoardType() == BoardType.HEX) {
            int hexHeight = CELL_SIZE;
            int hexWidth = (int) (Math.sqrt(3) / 2 * hexHeight);
            int vertDist = hexHeight * 3 / 4;

            // Przekształcenie współrzędnych kliknięcia na współrzędne siatki hex
            int mouseX = e.getX();
            int mouseY = e.getY();

            // Znajdź najbliższy heksagon
            int closestX = -1;
            int closestY = -1;
            double minDistance = Double.MAX_VALUE;

            for (int y = 0; y < world.getHeight(); y++) {
                for (int x = 0; x < world.getWidth(); x++) {
                    int px = x * hexWidth + (y % 2) * (hexWidth / 2);
                    int py = y * vertDist;

                    // Odległość od środka heksagonu
                    double distance = Math.sqrt(Math.pow(mouseX - px, 2) + Math.pow(mouseY - py, 2));

                    if (distance < minDistance) {
                        minDistance = distance;
                        closestX = x;
                        closestY = y;
                    }
                }
            }

            // Sprawdź czy kliknięcie jest w granicach planszy
            if (closestX >= 0 && closestX < world.getWidth() && closestY >= 0 && closestY < world.getHeight()) {
                selectedCell = new Point(closestX, closestY);
                repaint();
                showOrganismModal(closestX, closestY);
            }
        } else {
            // Oryginalna obsługa dla planszy kwadratowej
            int x = e.getX() / CELL_SIZE;
            int y = e.getY() / CELL_SIZE;

            if (x >= 0 && x < world.getWidth() && y >= 0 && y < world.getHeight()) {
                selectedCell = new Point(x, y);
                repaint();
                showOrganismModal(x, y);
            }
        }
    }

    private void configureButtonActions() {
        nextTurnButton.addActionListener(e -> {
            world.handleNextTurn();
            updateAfterTurn();
        });

        saveButton.addActionListener(e -> {
            world.saveGame();
            JOptionPane.showMessageDialog(this, "Game has been saved!", "Game save", JOptionPane.INFORMATION_MESSAGE);
        });

        loadButton.addActionListener(e -> {
            world.loadGame();
            updateAfterTurn();
            JOptionPane.showMessageDialog(this, "Game has been loaded!", "Game load", JOptionPane.INFORMATION_MESSAGE);
        });

        specialButton.addActionListener(e -> {
            world.setHumanNextMove(HumanMove.SPECIAL);
            world.handleNextTurn();
            updateAfterTurn();
        });
    }

    private void updateAfterTurn() {
        turnLabel.setText("Turn: " + world.getRoundNumber());
        updateControlPanel();
        repaint();
    }

    private void updateControlPanel() {
        buttonPanel.remove(nextTurnButton);
        buttonPanel.remove(dpadPanel);
        buttonPanel.remove(specialButton);
        buttonPanel.remove(specialCooldownLabel);

        if (!world.isHumanAlive()) {
            buttonPanel.add(nextTurnButton, 1);
        } else {
            buttonPanel.add(dpadPanel, 1);
            buttonPanel.add(specialButton, 2);
            buttonPanel.add(specialCooldownLabel, 3);

            int cooldownRemaining = Constants.HUMAN_SPECIAL_MOVE_COOLDOWN - (world.getRoundNumber() - world.getSpecialMoveRound());
            cooldownRemaining = Math.max(cooldownRemaining, 0);

            if (world.getIsSpecialActive()) {
                configureSpecialButton(false, "Special active!", Color.ORANGE, cooldownRemaining);
            } else if (!world.getIsSpecialReady()) {
                configureSpecialButton(false, "Special (cooldown)", Color.LIGHT_GRAY, cooldownRemaining);
            } else {
                configureSpecialButton(true, "Special ability", null, 0);
            }
        }

        if (saveButton.getParent() != buttonPanel) {
            buttonPanel.add(saveButton);
        }

        if (loadButton.getParent() != buttonPanel) {
            buttonPanel.add(loadButton);
        }

        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    private void configureSpecialButton(boolean enabled, String text, Color color, int cooldown) {
        specialButton.setEnabled(enabled);
        specialButton.setText(text);
        specialButton.setBackground(color);

        if (cooldown > 0) {
            specialCooldownLabel.setText("Remaining: " + cooldown + " turns");
        } else {
            specialCooldownLabel.setText("");
        }
    }

    private JPanel createDPadPanel() {
        JPanel dpad = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(2, 2, 2, 2);

        JButton upButton = createDirectionButton("↑", HumanMove.UP);
        JButton downButton = createDirectionButton("↓", HumanMove.DOWN);
        JButton leftButton = createDirectionButton("←", HumanMove.LEFT);
        JButton rightButton = createDirectionButton("→", HumanMove.RIGHT);

        c.gridx = 1; c.gridy = 0; dpad.add(upButton, c);
        c.gridx = 0; c.gridy = 1; dpad.add(leftButton, c);
        c.gridx = 2; c.gridy = 1; dpad.add(rightButton, c);
        c.gridx = 1; c.gridy = 2; dpad.add(downButton, c);

        return dpad;
    }

    private JButton createDirectionButton(String label, HumanMove move) {
        JButton button = new JButton(label);
        button.addActionListener(e -> {
            world.setHumanNextMove(move);
            world.handleNextTurn();
            updateAfterTurn();
        });
        return button;
    }

    private void drawOrganisms(Graphics g) {
        List<Organism> organisms = world.getOrganisms();
        for (Organism organism : organisms) {
            int x = organism.getPosition().x * CELL_SIZE;
            int y = organism.getPosition().y * CELL_SIZE;
            g.setColor(Color.BLACK);

            drawOrganismImage(g, organism, x, y);
        }
    }

    private void drawOrganismImage(Graphics g, Organism organism, int x, int y) {
        Image image = loadOrganismImage(organism);
        if (image != null) {
            g.drawImage(image, x + 2, y + 2, CELL_SIZE - 4, CELL_SIZE - 4, this);
        }
    }

    private Image loadOrganismImage(Organism organism) {
        String resourcePath = "/resources/" + organism.toString() + ".png";
        java.net.URL imageUrl = getClass().getResource(resourcePath);

        if (imageUrl != null) {
            return new ImageIcon(imageUrl).getImage();
        }
        return null;
    }

    private void showOrganismModal(int x, int y) {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Add organism", true);
        dialog.setLayout(new GridLayout(0, 3, 5, 5));
        dialog.getContentPane().setBackground(Color.WHITE);

        String[] organismNames = {
                "Human", "Fox", "Antylope", "Wolf", "Turtle", "Sheep", "CyberSheep",
                "Grass", "Guarana", "MilkWeed", "NightshadeBerries", "HogweedOfPine", "Delete"
        };

        for (String organismName : organismNames) {
            JPanel organismPanel = createOrganismPanel(organismName, x, y, dialog);
            dialog.add(organismPanel);
        }

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private JPanel createOrganismPanel(String organismName, int x, int y, JDialog dialog) {
        JPanel organismPanel = new JPanel(new BorderLayout());
        organismPanel.setBackground(Color.WHITE);
        organismPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        ImageIcon icon = loadOrganismIcon(organismName);

        JButton button = new JButton(icon);
        button.setToolTipText(organismName);
        button.setPreferredSize(new Dimension(60, 60));
        button.setBackground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        JLabel nameLabel = new JLabel(organismName, JLabel.CENTER);
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 10));

        button.addActionListener(e -> {
            Organism organism = createOrganismByName(organismName, new Point(x, y));
            if (organism != null) {
                world.addOrganism(organism);
                repaint();
            }
            selectedCell = null;
            dialog.dispose();
        });

        organismPanel.add(button, BorderLayout.CENTER);
        organismPanel.add(nameLabel, BorderLayout.SOUTH);

        return organismPanel;
    }

    private ImageIcon loadOrganismIcon(String organismName) {
        String imagePath = "/resources/" + organismName + ".png";
        java.net.URL imageUrl = getClass().getResource(imagePath);

        if (imageUrl != null) {
            ImageIcon icon = new ImageIcon(imageUrl);
            Image image = icon.getImage();
            Image scaledImage = image.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        }
        return new ImageIcon();
    }

    private Organism createOrganismByName(String name, Point position) {
        Organism existingOrganism = world.getOrganismAt(position);
        if (existingOrganism != null) {
            world.removeOrganism(existingOrganism);
        }

        switch (name) {
            case "Human":
                if (!world.isHumanAlive()) {
                    return new Animals.Human(position, 0, world);
                } else {
                    JOptionPane.showMessageDialog(this, "There is already a human in the world",
                            "Error", JOptionPane.INFORMATION_MESSAGE);
                    return null;
                }
            case "Fox": return new Animals.Fox(position, 0, world);
            case "Antylope": return new Animals.Antylope(position, 0, world);
            case "Grass": return new Plants.Grass(position, 0, world);
            case "Guarana": return new Plants.Guarana(position, 0, world);
            case "MilkWeed": return new Plants.MilkWeed(position, 0, world);
            case "NightshadeBerries": return new Plants.NightshadeBerries(position, 0, world);
            case "Wolf": return new Animals.Wolf(position, 0, world);
            case "Turtle": return new Animals.Turtle(position, 0, world);
            case "Sheep": return new Animals.Sheep(position, 0, world);
            case "CyberSheep": return new Animals.CyberSheep(position, 0, world);
            case "HogweedOfPine": return new Plants.HogweedOfPine(position, 0, world);
            case "Delete":
                if (existingOrganism != null) {
                    world.removeOrganism(existingOrganism);
                    repaint();
                } else {
                    JOptionPane.showMessageDialog(this, "Cannot delete a non-existent organism",
                            "Error", JOptionPane.INFORMATION_MESSAGE);
                }
                return null;
            default: return null;
        }
    }
}