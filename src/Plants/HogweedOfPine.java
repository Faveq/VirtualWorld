package Plants;
import General.*;

public class HogweedOfPine extends Plant {
    public HogweedOfPine(Point position) {
        super(Constants.HOGEWEED_OF_PINE_STRENGTH, position);
    }

    public HogweedOfPine(Point position, int age, World world) {
        super(Constants.HOGEWEED_OF_PINE_STRENGTH, position, age, world);
    }

    @Override
    public String getGraphicRepresentation() {
        return Constants.HOGEWEED_OF_PINE_GRAPHIC_REPRESENTATION;
    }

    @Override
    public String toString() {
        return "HogweedOfPine";
    }

    @Override
    public void action() {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                Organism victim = getWorld().getOrganismAt(this.getPosition().add(new Point(x, y)));
                if (victim != null &&
                        !victim.getGraphicRepresentation().equals(Constants.CYBER_SHEEP_GRAPHIC_REPRESENTATION) &&
                        !(victim instanceof Plant)) {
                    victim.kill();
                }
            }
        }
        spread();
    }

    @Override
    public Organism copy() {
        return new HogweedOfPine(this.getPosition(), this.age, this.world);
    }
}