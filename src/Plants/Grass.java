package Plants;
import General.*;

public class Grass extends Plant {
    public Grass(Point position) {
        super(Constants.GRASS_STRENGTH, position);
    }

    public Grass(Point position, int age, World world) {
        super(Constants.GRASS_STRENGTH, position, age, world);
    }

    @Override
    public String getGraphicRepresentation() {
        return Constants.GRASS_GRAPHIC_REPRESENTATION;
    }

    @Override
    public String toString() {
        return "Grass";
    }

    @Override
    public Organism copy() {
        return new Grass(this.getPosition(), this.age, this.world);
    }
}