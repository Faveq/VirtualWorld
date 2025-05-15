package Plants;
import General.*;

public class NightshadeBerries extends Plant {
    public NightshadeBerries(Point position) {
        super(Constants.NIGHTSHADE_BERRIES_STRENGTH, position);
    }

    public NightshadeBerries(Point position, int age, World world) {
        super(Constants.NIGHTSHADE_BERRIES_STRENGTH, position, age, world);
    }


    @Override
    public String getGraphicRepresentation() {
        return Constants.NIGHTSHADE_BERRIES_GRAPHIC_REPRESENTATION;
    }

    @Override
    public String toString() {
        return "NightshadeBerries";
    }

    public void addEffect(Organism organism) {
        organism.kill();
    }

    @Override
    public Organism copy() {
        return new NightshadeBerries(this.getPosition(), this.age, this.world);
    }
}