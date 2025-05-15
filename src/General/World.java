package General;
import Plants.*;
import Animals.*;
import java.io.*;
import java.util.*;

public class World {
    private int width;
    private int height;
    private HumanMove humanNextMove = HumanMove.UP;
    private int turnNumber = 0;
    private boolean isSpecialActive = false;
    private boolean isSpecialReady = true;
    private int specialMoveRound = 0;
    private ArrayList<String> logs;
    private ArrayList<Organism> organisms;

    public World(int width, int height) {
        this.width = width;
        this.height = height;
        this.logs = new ArrayList<>();
        this.organisms = new ArrayList<>();

        // Java equivalent of srand
        Random random = new Random(System.currentTimeMillis());
    }

    public World(int width, int height, ArrayList<Organism> organisms) {
        this.width = width;
        this.height = height;
        this.logs = new ArrayList<>();
        this.organisms = new ArrayList<>(organisms);

        for (Organism org : this.organisms) {
            org.setWorld(this);
        }

        // Java equivalent of srand
        Random random = new Random(System.currentTimeMillis());
    }

    // No need for destructor in Java as it has garbage collection

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getRoundNumber() {
        return turnNumber;
    }

    public HumanMove getHumanNextMove() {
        return humanNextMove;
    }

    public int getSpecialMoveRound() {
        return specialMoveRound;
    }

    public Organism getColliding(Organism organism) {
        for (Organism org : organisms) {
            if (org != organism && org.getPosition().x == organism.getPosition().x && org.getPosition().y == organism.getPosition().y) {
                return org;
            }
        }
        return null;
    }

    public Organism getOrganismAt(Point position) {
        for (Organism organism : organisms) {
            if (organism.getPosition().equals(position)) {
                return organism;
            }
        }
        return null;
    }

    public ArrayList<String> getLogs() {
        return new ArrayList<>(logs);
    }

    public ArrayList<Organism> getOrganisms() {
        return new ArrayList<>(organisms);
    }

    public boolean getIsSpecialActive() {
        return isSpecialActive;
    }

    public boolean getIsSpecialReady() {
        return isSpecialReady;
    }

    public void setRoundNumber(int roundNumber) {
        this.turnNumber = roundNumber;
    }

    public void setHumanNextMove(HumanMove move) {
        humanNextMove = move;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setSpecialMoveRound(int specialMoveRound) {
        this.specialMoveRound = specialMoveRound;
    }

    public void setIsSpecialActive(boolean isSpecialActive) {
        this.isSpecialActive = isSpecialActive;
    }

    public void setIsSpecialReady(boolean isSpecialReady) {
        this.isSpecialReady = isSpecialReady;
    }

    public void setTurn(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public void addOrganism(Organism organism) {
        organisms.add(organism);
    }

    public void removeOrganism(Organism organism) {
        organisms.remove(organism);
        // Note: In Java, we don't need to explicitly delete the organism
    }

    public void handleHumanMovement() {
        // Java doesn't have direct access to Windows API like C++
        // We would need to use Java's KeyListener or similar input handling
        // Below is a conceptual adaptation that would need to be implemented
        // with proper Java input handling mechanisms

        Scanner scanner = new Scanner(System.in);
        HumanMove arrow = HumanMove.NONE;

        System.out.println("Enter move (W=UP, S=DOWN, A=LEFT, D=RIGHT, SPACE=SPECIAL, S=SAVE, L=LOAD): ");
        String input = scanner.nextLine().toUpperCase();

        switch (input) {
            case "W":
                arrow = HumanMove.UP;
                break;
            case "S":
                arrow = HumanMove.DOWN;
                break;
            case "A":
                arrow = HumanMove.LEFT;
                break;
            case "D":
                arrow = HumanMove.RIGHT;
                break;
            case " ":
                arrow = HumanMove.SPECIAL;
                break;
            case "SAVE":
                saveGame();
                break;
            case "LOAD":
                loadGame();
                break;
            default:
                break;
        }

        setHumanNextMove(arrow);
    }

    public Organism allocateOrganismByName(String name) {
        Point defaultPosition = new Point(0, 0);

        switch (name) {
            case "Human":
                return new Human(defaultPosition);
            case "Wolf":
                return new Wolf(defaultPosition);
            case "Sheep":
                return new Sheep(defaultPosition);
            case "Fox":
                return new Fox(defaultPosition);
            case "Turtle":
                return new Turtle(defaultPosition);
            case "Antylope":
                return new Antylope(defaultPosition);
            case "Grass":
                return new Grass(defaultPosition);
            case "MilkWeed":
                return new MilkWeed(defaultPosition);
            case "Guarana":
                return new Guarana(defaultPosition);
            case "NightshadeBerries":
                return new NightshadeBerries(defaultPosition);
            case "HogweedOfPine":
                return new HogweedOfPine(defaultPosition);
            default:
                return null;
        }
    }

    public Point getFreeTile(Point position) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                Point newPosition = new Point(position.x + x, position.y + y);

                if (!newPosition.beyondBorders(width, height) && getOrganismAt(newPosition) == null && !newPosition.equals(position)) {
                    return newPosition;
                }
            }
        }
        return position;
    }

    public ArrayList<Point> getAllFreeTiles(Point position) {
        ArrayList<Point> freeTiles = new ArrayList<>();

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                Point newPosition = new Point(position.x + x, position.y + y);

                if (!newPosition.beyondBorders(width, height) &&
                        getOrganismAt(newPosition) == null &&
                        !newPosition.equals(position)) {
                    freeTiles.add(newPosition);
                }
            }
        }

        return freeTiles;
    }

    public boolean isEveryoneStronger(Organism organism) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                Point newPosition = new Point(organism.getPosition().x + x, organism.getPosition().y + y);

                Organism collidingOrganism = getOrganismAt(newPosition);

                if (collidingOrganism != null && collidingOrganism != organism) {
                    if (collidingOrganism.compareTo(organism) < 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public Point getClosestHogweed(Point position) {
        Point closestHogweed = new Point(-1, -1);
        int minDistance = Integer.MAX_VALUE;

        for (Organism organism : organisms) {
            if (organism.toString().equals("HogweedOfPine")) {
                int distance = Math.abs(organism.getPosition().x - position.x) + Math.abs(organism.getPosition().y - position.y);

                if (distance < minDistance) {
                    minDistance = distance;
                    closestHogweed = organism.getPosition();
                }
            }
        }
        return closestHogweed;
    }

    public void addLog(String log) {
        logs.add(log);
    }

    public void clearLogs() {
        logs.clear();
    }

    public void printLogs() {
        System.out.println("Logs:");
        for (String log : logs) {
            System.out.println(log);
        }
    }

    public void handleNextTurn() {
        clearLogs();

        updateSpecialState();

        for (Organism organism : organisms) {
            organism.resetReproduction();
        }

        turnNumber++;

        moveOrganisms();

        removeBodies();

        drawWorld();
        handleHumanMovement();

        // Sleep equivalent in Java
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void updateSpecialState() {
        if (turnNumber - specialMoveRound >= Constants.HUMAN_SPECIAL_MOVE_COOLDOWN) {
            setIsSpecialActive(false);
            setIsSpecialReady(true);
        }
    }

    public void moveOrganisms() {
        // Sort organisms by initiative and age
        Collections.sort(organisms, new Comparator<Organism>() {
            @Override
            public int compare(Organism org1, Organism org2) {
                if (org1.getInitiative() == org2.getInitiative()) {
                    return Integer.compare(org2.getAge(), org1.getAge());
                }
                return Integer.compare(org2.getInitiative(), org1.getInitiative());
            }
        });

        for (int i = 0; i < organisms.size(); i++) {
            Organism organism = organisms.get(i);

            if (organism.isAlive() && organism.getAge() != 0) {
                organism.action();
                organism.collision();
            }

            organism.increaseAge();
        }
    }

    public void removeBodies() {
        Iterator<Organism> iterator = organisms.iterator();
        while (iterator.hasNext()) {
            Organism organism = iterator.next();
            if (!organism.isAlive()) {
                iterator.remove();
            }
        }
    }

    public void drawBorder() {
        System.out.print("+");
        for (int i = 0; i < width; i++) {
            System.out.print("-");
        }
        System.out.println("+");
    }

    public void drawWorld() {
        // Clear screen in Java (not perfect but close)
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }

        System.out.println("Bartosz Swierczynski 203825");
        System.out.println("\nTurn #" + turnNumber + "\n");

        if (isSpecialActive) {
            System.out.println("\nSpecial ability is active!");
        } else if (isSpecialReady) {
            System.out.println("\nSpecial ability is ready to use!");
        } else {
            System.out.println("\nSpecial ability is not ready to use!");
        }

        drawBorder();

        for (int y = 0; y < height; y++) {
            System.out.print("|");
            for (int x = 0; x < width; x++) {
                Point position = new Point(x, y);
                Organism organism = getOrganismAt(position);
                if (organism != null) {
                    System.out.print(organism.getGraphicRepresentation());
                } else {
                    System.out.print(".");
                }
            }
            System.out.println("|");
        }

        drawBorder();
        printLogs();
    }

    public void clearGame() {
        clearLogs();
        organisms.clear();
    }

    public void saveGame() {
        try (PrintWriter outFile = new PrintWriter(new FileWriter("save.txt"))) {
            outFile.println(turnNumber + " " + width + " " + height);

            for (Organism organism : getOrganisms()) {
                // NAME AGE POSITION.X POSITION.Y [OPTIONAL: ISINVINCIBLE SPECIALMOVEROUND]
                outFile.print(organism.toString() + " "
                        + organism.getAge() + " "
                        + organism.getPosition().x + " "
                        + organism.getPosition().y);

                if (organism.toString().equals("Human")) {
                    if (organism instanceof Human) {
                        Human human = (Human) organism;
                        outFile.print(" " + human.isInvincible() + " "
                                + specialMoveRound);
                    }
                }

                outFile.println();
            }

            outFile.println("Logs");
            for (String log : logs) {
                outFile.println(log);
            }
        } catch (IOException e) {
            System.out.println("Error: Could not open save file for writing.");
            e.printStackTrace();
        }
    }

    public void loadGame() {
        try (Scanner inFile = new Scanner(new File("save.txt"))) {
            clearGame();

            int turnNum = inFile.nextInt();
            int width = inFile.nextInt();
            int height = inFile.nextInt();

            setTurn(turnNum);
            setWidth(width);
            setHeight(height);

            inFile.nextLine(); // Consume the newline

            String line;
            while (inFile.hasNextLine()) {
                line = inFile.nextLine();
                Scanner lineScanner = new Scanner(line);

                if (!lineScanner.hasNext()) {
                    continue;
                }

                String organismType = lineScanner.next();

                if (organismType.equals("Logs")) {
                    break;
                }

                int age = lineScanner.nextInt();
                int posX = lineScanner.nextInt();
                int posY = lineScanner.nextInt();

                Organism organism = allocateOrganismByName(organismType);
                if (organism != null) {
                    organism.setAge(age);
                    organism.setPosition(new Point(posX, posY));
                    organism.setWorld(this);

                    if (organismType.equals("Human")) {
                        boolean isInvincible = lineScanner.nextBoolean();
                        int specialMoveRound = lineScanner.nextInt();

                        if (organism instanceof Human) {
                            Human human = (Human) organism;
                            human.setIsInvincible(isInvincible);
                            setSpecialMoveRound(specialMoveRound);
                        }
                    }

                    addOrganism(organism);
                } else {
                    System.out.println("Error: Unknown organism type: " + organismType);
                }

                lineScanner.close();
            }

            // Read logs
            while (inFile.hasNextLine()) {
                line = inFile.nextLine();
                if (!line.isEmpty()) {
                    logs.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: Could not open save file.");
        } catch (Exception e) {
            System.out.println("Error while loading game: " + e.getMessage());
            e.printStackTrace();
        }

        drawWorld();
    }
}