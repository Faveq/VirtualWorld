package Animals;
import General.*;


public class Sheep extends Animal {

    public Sheep(Point position) {
        super(Constants.SHEEP_STRENGTH, Constants.SHEEP_INITIATIVE, position);
    }

    public Sheep(Point position, int age, World world) {
        super(Constants.SHEEP_STRENGTH, Constants.SHEEP_INITIATIVE, position, age, world);
    }

    @Override
    public String getGraphicRepresentation() {
        return Constants.SHEEP_GRAPHIC_REPRESENTATION;
    }

    @Override
    public String toString() {
        return "Sheep";
    }

    @Override
    public Organism copy() {
        return new Sheep(this.position, this.age, this.world);
    }
}