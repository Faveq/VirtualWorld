import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import Animals.*;
import General.*;
import Plants.*;

public class Main {
    public static void main(String[] args) {
        int width = 20;
        int height = 15;
        int maxRounds = 2000;

        // Zastępuje std::srand(std::time(nullptr))
        Random random = new Random(System.currentTimeMillis());

        // Tworzenie listy organizmów
        ArrayList<Organism> organisms = new ArrayList<>();

        //organisms.add(new CyberSheep(new Point(0, 0)));
        organisms.add(new Sheep(new Point(0, 5)));
        organisms.add(new Sheep(new Point(0, 4)));
        organisms.add(new Sheep(new Point(0, 3)));
        organisms.add(new Sheep(new Point(2, 0)));
        //organisms.add(new HogweedOfPine(new Point(8, 8)));
        organisms.add(new Grass(new Point(9, 9)));
        organisms.add(new Wolf(new Point(9, 100)));
        organisms.add(new Grass(new Point(5, 9)));
        organisms.add(new Grass(new Point(9, 8)));
        organisms.add(new Grass(new Point(12, 9)));
        organisms.add(new Grass(new Point(15, 9)));
//        organisms.add(new Human(new Point(5, 9)));

        // Zakomentowany fragment kodu dodający losowe organizmy
        /*
        for (int i = 0; i < 3; ++i) {
            organisms.add(new Fox(new Point(random.nextInt(width), random.nextInt(height))));
            organisms.add(new Turtle(new Point(random.nextInt(width), random.nextInt(height))));
            organisms.add(new Antylope(new Point(random.nextInt(width), random.nextInt(height))));
            organisms.add(new Grass(new Point(random.nextInt(width), random.nextInt(height))));
            organisms.add(new Guarana(new Point(random.nextInt(width), random.nextInt(height))));
            organisms.add(new MilkWeed(new Point(random.nextInt(width), random.nextInt(height))));
            organisms.add(new NightshadeBerries(new Point(random.nextInt(width), random.nextInt(height))));
        }
        */

        // Tworzenie świata i uruchomienie symulacji
        World world = new World(width, height, organisms);
        world.drawWorld();

        for (int i = 0; i <= maxRounds; i++) {
            world.handleNextTurn();
        }
    }
}