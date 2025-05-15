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
                    g.setColor(new Color(173, 216, 230, 128)); // Jasnoniebieski z przezroczystością
                    g.fillRect(selectedCell.x * cellSize, selectedCell.y * cellSize, cellSize, cellSize);
                }
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(world.getWidth() * cellSize, world.getHeight() * cellSize);
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

        JPanel buttonPanel = new JPanel();
        JButton nextTurnButton = new JButton("Następna tura");
        JButton saveButton = new JButton("Zapis");
        JButton loadButton = new JButton("Wczytanie");

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(turnLabel);

        nextTurnButton.addActionListener(e -> {
            world.handleNextTurn();
            turnLabel.setText("Tura: " + world.getRoundNumber());
            repaint();
        });

        saveButton.addActionListener(e -> {
            world.saveGame();
            JOptionPane.showMessageDialog(this, "Gra została zapisana!", "Zapis gry", JOptionPane.INFORMATION_MESSAGE);
        });
        loadButton.addActionListener(e -> {
            world.loadGame();
            turnLabel.setText("Tura: " + world.getRoundNumber());
            repaint();
            JOptionPane.showMessageDialog(this, "Gra została wczytana!", "Wczytanie gry", JOptionPane.INFORMATION_MESSAGE);
        });

        buttonPanel.add(nextTurnButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);

        add(gridPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
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

    // Modal z wyborem organizmu

    private void showOrganismModal(int x, int y) {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Dodaj organizm", true);
        dialog.setLayout(new GridLayout(0, 3, 5, 5)); // Siatka z 3 kolumnami
        dialog.getContentPane().setBackground(Color.WHITE);
//        dialog.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Pełna lista wszystkich organizmów
        String[] organismNames = {
                "Fox", "Antylope", "Wolf", "Turtle", "Sheep", "CyberSheep",
                "Grass", "Guarana", "MilkWeed", "NightshadeBerries", "HogweedOfPine"
        };

        for (String name : organismNames) {
            JPanel organismPanel = new JPanel(new BorderLayout());
            organismPanel.setBackground(Color.WHITE);
            organismPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            // Pobieranie obrazka
            String imgPath = "/resources/" + name + ".png";
            ImageIcon icon = null;
            java.net.URL imgUrl = getClass().getResource(imgPath);
            if (imgUrl != null) {
                icon = new ImageIcon(imgUrl);
                // Skalowanie obrazka jeśli potrzeba
                Image image = icon.getImage();
                Image scaledImage = image.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                icon = new ImageIcon(scaledImage);
            } else {
                // Placeholder jeśli brak obrazka
                icon = new ImageIcon();
            }

            JButton btn = new JButton(icon);
            btn.setToolTipText(name);
            btn.setPreferredSize(new Dimension(60, 60));
            btn.setBackground(Color.WHITE);
            btn.setBorderPainted(false);
            btn.setFocusPainted(false);

            // Etykieta z nazwą pod obrazkiem
            JLabel nameLabel = new JLabel(name, JLabel.CENTER);
            nameLabel.setFont(new Font("Arial", Font.PLAIN, 10));

            organismPanel.add(btn, BorderLayout.CENTER);
            organismPanel.add(nameLabel, BorderLayout.SOUTH);

            // Obsługa kliknięcia
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

        // Przycisk do zamknięcia modalu
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
    // Pomocnicza metoda do tworzenia organizmów po nazwie
    private Organism createOrganismByName(String name, Point pos) {

        Organism org = null;
        org = world.getOrganismAt(pos);

        if (org != null) {
            world.removeOrganism(org);
        }

        switch (name) {
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