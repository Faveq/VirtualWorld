package Animals;
import General.*;

public class CyberSheep extends Animal {

    public CyberSheep(Point position) {
        super(Constants.CYBER_SHEEP_STRENGTH, Constants.CYBER_SHEEP_INITIATIVE, position);
    }

    public CyberSheep(Point position, int age, World world) {
        super(Constants.CYBER_SHEEP_STRENGTH, Constants.CYBER_SHEEP_INITIATIVE, position, age, world);
    }

    @Override
    public String getGraphicRepresentation() {
        return Constants.CYBER_SHEEP_GRAPHIC_REPRESENTATION;
    }

    @Override
    public String toString() {
        return "CyberSheep";
    }

    @Override
    public void action() {
        Point closestHogweed = world.getClosestHogweed(position);
        if (!closestHogweed.equals(new Point(-1, -1))) {
            Point shift = closestHogweed.subtract(position);

            if (shift.x != 0) {
                shift.x /= Math.abs(shift.x);
            }
            if (shift.y != 0) {
                shift.y /= Math.abs(shift.y);
            }

            move(shift);
        } else {
            randomMove(1);
        }
    }

    @Override
    public Organism copy() {
        return new CyberSheep(this.position, this.age, this.world);
    }
}