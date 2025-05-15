package Animals;
import General.*;

public class Fox extends Animal {

    public Fox(Point position) {
        super(Constants.FOX_STRENGTH, Constants.FOX_INITIATIVE, position);
    }

    public Fox(Point position, int age, World world) {
        super(Constants.FOX_STRENGTH, Constants.FOX_INITIATIVE, position, age, world);
    }

    @Override
    public String getGraphicRepresentation() {
        return Constants.FOX_GRAPHIC_REPRESENTATION;
    }

    @Override
    public String toString() {
        return "Fox";
    }

    @Override
    public boolean hasGoodSmell() {
        return true;
    }

    @Override
    public Organism copy() {
        return new Fox(this.position, this.age, this.world);
    }
}