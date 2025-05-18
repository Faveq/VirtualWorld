import java.util.ArrayList;
import java.util.Random;
import Animals.*;
import General.*;
import Plants.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;

public class Main {
    public static void main(String[] args) {



        int width = 25;
        int height = 15;

        Random random = new Random(System.currentTimeMillis());

        ArrayList<Organism> organisms = new ArrayList<>();

        //organisms.add(new CyberSheep(new Point(0, 0)));
//        organisms.add(new Fox(new Point(0, 0)));
//        organisms.add(new Wolf(new Point(1, 1)));
//        organisms.add(new Wolf(new Point(1, 0)));
//        organisms.add(new Wolf(new Point(0, 1)));
//
//        //organisms.add(new HogweedOfPine(new Point(8, 8)));
//        organisms.add(new Grass(new Point(9, 9)));
//        organisms.add(new Wolf(new Point(9, 100)));
//        organisms.add(new Grass(new Point(5, 9)));
//        organisms.add(new Fox(new Point(9, 8)));
//        organisms.add(new Fox(new Point(12, 9)));
//        organisms.add(new Grass(new Point(15, 9)));
//        organisms.add(new Human(new Point(5, 9)));
//        organisms.add(new Fox(new Point(0, 1)));
//        organisms.add(new Fox(new Point(11, 11)));
//        organisms.add(new Human(new Point(15, 2)));
//       organisms.add(new Fox(new Point(1, 0)));
//       organisms.add(new Fox(new Point(1, 0)));
//       organisms.add(new Antylope(new Point(10, 0)));
//       organisms.add(new Antylope(new Point(11, 0)));
//       organisms.add(new Antylope(new Point(10, 1)));
//       organisms.add(new Antylope(new Point(11, 1)));


//        for (int i = 0; i < 3; ++i) {
//            organisms.add(new Fox(new Point(random.nextInt(width), random.nextInt(height))));
//            organisms.add(new Turtle(new Point(random.nextInt(width), random.nextInt(height))));
//            organisms.add(new Antylope(new Point(random.nextInt(width), random.nextInt(height))));
//            organisms.add(new Grass(new Point(random.nextInt(width), random.nextInt(height))));
//            organisms.add(new Guarana(new Point(random.nextInt(width), random.nextInt(height))));
//            organisms.add(new MilkWeed(new Point(random.nextInt(width), random.nextInt(height))));
//            organisms.add(new NightshadeBerries(new Point(random.nextInt(width), random.nextInt(height))));
//        }


        // Tworzenie świata i uruchomienie symulacji
        World world = new World(width, height, organisms);
//        world.drawWorld();

        JFrame frame = new JFrame("Symulacja świata");
        WorldPanel panel = new WorldPanel(world);
        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

//        for (int i = 0; i <= maxRounds; i++) {
//            world.handleNextTurn();
//        }
    }
}