package Plants;
import General.*;

public class MilkWeed extends Plant {
    public MilkWeed(Point position) {
        super(Constants.MILKWEED_STRENGTH, position);
    }

    public MilkWeed(Point position, int age, World world) {
        super(Constants.MILKWEED_STRENGTH, position, age, world);
    }

    @Override
    public String getGraphicRepresentation() {
        return Constants.MILKWEED_GRAPHIC_REPRESENTATION;
    }

    @Override
    public String toString() {
        return "MilkWeed";
    }

    @Override
    public void action() {
        for (int i = 0; i < Constants.SPREAD_ATTEMPTS; i++) {
            spread();
        }
    }

    @Override
    public Organism copy() {
        return new MilkWeed(this.getPosition(), this.age, this.world);

    }
}