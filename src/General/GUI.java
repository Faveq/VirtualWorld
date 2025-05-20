package General;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class GUI extends JPanel {
    private static final int CELL_SIZE = 45;
    private static final int PADDING = 45;

    private final World world;
    private final JLabel turnLabel;
    private Point selectedCell = null;

    private JPanel buttonPanel;
    private JPanel dpadPanel;

    private JButton nextTurnButton;
    private JButton specialButton;
    private JLabel specialCooldownLabel;

    // Constructor for GUI panel
    public GUI(World world) {
        this.world = world;
        setLayout(new BorderLayout());

        turnLabel = new JLabel("Turn: " + world.getRoundNumber());
        buttonPanel = new JPanel();
        nextTurnButton = new JButton("Next turn");
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

    // Creates the grid panel for board rendering
    private JPanel createGridPanel() {
        JPanel gridPanel = new JPanel() {

            // Paints the board and organisms
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
                            int py = y * vertDist + PADDING;
                            Polygon hex = createHex(px, py, hexHeight / 2);
                            g.setColor(Color.LIGHT_GRAY);
                            g.drawPolygon(hex);
                        }
                    }
                    drawOrganismsHex(g, hexWidth, vertDist, hexHeight / 2);
                    if (selectedCell != null) {
                        int px = selectedCell.x * hexWidth + (selectedCell.y % 2) * (hexWidth / 2) + PADDING;
                        int py = selectedCell.y * vertDist + PADDING;
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

            // Creates a hexagon polygon at given position and radius
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

            // Draws organisms on a hexagonal board
            private void drawOrganismsHex(Graphics g, int hexWidth, int vertDist, int r) {
                List<Organism> organisms = world.getOrganisms();
                for (Organism organism : organisms) {
                    int x = organism.getPosition().x * hexWidth + (organism.getPosition().y % 2) * (hexWidth / 2) + PADDING;
                    int y = organism.getPosition().y * vertDist + PADDING;

                    Polygon hex = createHex(x, y, r - 2);

                    drawHexImage(g, organism, x, y, hex, r);
                }
            }

            // Draws an organism image clipped to a hexagon
            private void drawHexImage(Graphics g, Organism organism, int x, int y, Polygon hex, int r) {
                Graphics2D g2d = (Graphics2D) g.create();

                try {
                    Image originalImage = loadOrganismImage(organism);
                    if (originalImage == null) return;

                    BufferedImage bufferedImage = new BufferedImage(
                            originalImage.getWidth(null),
                            originalImage.getHeight(null),
                            BufferedImage.TYPE_INT_ARGB);

                    Graphics2D bImageGraphics = bufferedImage.createGraphics();
                    bImageGraphics.drawImage(originalImage, 0, 0, null);
                    bImageGraphics.dispose();

                    g2d.setClip(hex);

                    int imageSize = r * 2 - 8;
                    g2d.drawImage(bufferedImage,
                            x - imageSize/2,
                            y - imageSize/2,
                            imageSize,
                            imageSize,
                            null);
                } finally {
                    g2d.dispose();
                }
            }

            // Returns preferred size of the grid panel
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

        // Handles mouse click on the grid
        gridPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleGridClick(e);
            }
        });

        return gridPanel;
    }

    // Handles click event on the board grid
    private void handleGridClick(MouseEvent e) {
        if (world.getBoardType() == BoardType.HEX) {
            int hexHeight = CELL_SIZE;
            int hexWidth = (int) (Math.sqrt(3) / 2 * hexHeight);
            int vertDist = hexHeight * 3 / 4;

            int mouseX = e.getX() - PADDING;
            int mouseY = e.getY() - PADDING;

            int closestX = -1;
            int closestY = -1;
            double minDistance = Double.MAX_VALUE;

            for (int y = 0; y < world.getHeight(); y++) {
                for (int x = 0; x < world.getWidth(); x++) {
                    int px = x * hexWidth + (y % 2) * (hexWidth / 2);
                    int py = y * vertDist;

                    double distance = Math.sqrt(Math.pow(mouseX - px, 2) + Math.pow(mouseY - py, 2));

                    if (distance < minDistance) {
                        minDistance = distance;
                        closestX = x;
                        closestY = y;
                    }
                }
            }

            if (closestX >= 0 && closestX < world.getWidth() && closestY < world.getHeight()) {
                selectedCell = new Point(closestX, closestY);
                repaint();
                showOrganismModal(closestX, closestY);
            }
        } else {
            int x = e.getX() / CELL_SIZE;
            int y = e.getY() / CELL_SIZE;

            if (x >= 0 && x < world.getWidth() && y >= 0 && y < world.getHeight()) {
                selectedCell = new Point(x, y);
                repaint();
                showOrganismModal(x, y);
            }
        }
    }

    // Configures actions for main control buttons
    private void configureButtonActions() {
        nextTurnButton.addActionListener(e -> {
            world.handleNextTurn();
            updateAfterTurn();
        });

        specialButton.addActionListener(e -> {
            world.setHumanNextMove(HumanMove.SPECIAL);
            world.handleNextTurn();
            updateAfterTurn();
        });
    }

    // Updates GUI after a turn is made
    public void updateAfterTurn() {
        turnLabel.setText("Turn: " + world.getRoundNumber());
        updateControlPanel();
        repaint();
    }

    // Creates the D-Pad panel for movement controls
    private JPanel createDPadPanel() {
        JPanel dpad = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(2, 2, 2, 2);

        JButton upLeft = createDirectionButton("↖", HumanMove.UP_LEFT);
        JButton upRight = createDirectionButton("↗", HumanMove.UP_RIGHT);
        JButton left = createDirectionButton("←", HumanMove.LEFT);
        JButton right = createDirectionButton("→", HumanMove.RIGHT);
        JButton downLeft = createDirectionButton("↙", HumanMove.DOWN_LEFT);
        JButton downRight = createDirectionButton("↘", HumanMove.DOWN_RIGHT);

        c.gridx = 1; c.gridy = 0; dpad.add(upLeft, c);
        c.gridx = 2; c.gridy = 0; dpad.add(upRight, c);
        c.gridx = 1; c.gridy = 1; dpad.add(left, c);
        c.gridx = 2; c.gridy = 1; dpad.add(right, c);
        c.gridx = 1; c.gridy = 2; dpad.add(downLeft, c);
        c.gridx = 2; c.gridy = 2; dpad.add(downRight, c);

        return dpad;
    }

    // Updates the control panel with buttons and labels
    private void updateControlPanel() {
        buttonPanel.remove(nextTurnButton);
        buttonPanel.remove(dpadPanel);
        buttonPanel.remove(specialButton);
        buttonPanel.remove(specialCooldownLabel);

        if (!world.getIsHumanAlive()) {
            buttonPanel.add(nextTurnButton, 1);
        } else {
            if (world.getBoardType() == BoardType.HEX) {
                buttonPanel.add(dpadPanel, 1);
            }
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

        buttonPanel.revalidate();
        buttonPanel.repaint();
    }

    // Configures the special ability button
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

    // Creates a button for a movement direction
    private JButton createDirectionButton(String label, HumanMove move) {
        JButton button = new JButton(label);
        button.addActionListener(e -> {
            world.setHumanNextMove(move);
            world.handleNextTurn();
            updateAfterTurn();
        });
        return button;
    }

    // Draws organisms on a square board
    private void drawOrganisms(Graphics g) {
        List<Organism> organisms = world.getOrganisms();
        for (Organism organism : organisms) {
            int x = organism.getPosition().x * CELL_SIZE;
            int y = organism.getPosition().y * CELL_SIZE;
            g.setColor(Color.BLACK);

            drawOrganismImage(g, organism, x, y);
        }
    }

    // Draws an organism image on the board
    private void drawOrganismImage(Graphics g, Organism organism, int x, int y) {
        Image image = loadOrganismImage(organism);
        if (image != null) {
            g.drawImage(image, x + 2, y + 2, CELL_SIZE - 4, CELL_SIZE - 4, this);
        }
    }

    // Loads an image for a given organism
    private Image loadOrganismImage(Organism organism) {
        String resourcePath = "/resources/" + organism.toString() + ".png";
        java.net.URL imageUrl = getClass().getResource(resourcePath);

        if (imageUrl != null) {
            return new ImageIcon(imageUrl).getImage();
        }
        return null;
    }

    // Shows a modal dialog to add or delete an organism
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

    // Creates a panel for a single organism in the modal dialog
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

    // Loads an icon for a given organism name
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

    // Creates an organism instance by name and position
    private Organism createOrganismByName(String name, Point position) {
        Organism existingOrganism = world.getOrganismAt(position);
        if (existingOrganism != null) {
            world.removeOrganism(existingOrganism);
        }

        switch (name) {
            case "Human":
                if (!world.getIsHumanAlive()) {
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