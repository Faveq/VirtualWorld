package Animals;
import General.*;

public class Wolf extends Animal {

    public Wolf(Point position) {
        super(Constants.WOLF_STRENGTH, Constants.WOLF_INITIATIVE, position);
    }

    public Wolf(Point position, int age, World world) {
        super(Constants.WOLF_STRENGTH, Constants.WOLF_INITIATIVE, position, age,world);
    }

    @Override
    public String getGraphicRepresentation() {
        return Constants.WOLF_GRAPHIC_REPRESENTATION;
    }

    @Override
    public String toString() {
        return "Wolf";
    }

    @Override
    public Organism copy() {
        return new Wolf(this.position, this.age, this.world);
    }
}