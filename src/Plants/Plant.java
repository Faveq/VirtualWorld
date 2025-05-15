package Plants;
import General.*;



public class Plant extends Organism {
    public Plant(int strength, Point position) {
        super(strength, 0, position);
    }

    public Plant(int strength, Point position, int age, World world) {
        super(strength, 0, position, age, world);
    }

    @Override
    public String getGraphicRepresentation() {
        return "P";
    }

    @Override
    public String toString() {
        return "Plant";
    }

    @Override
    public void action() { // spread
        spread();
    }

    @Override
    public void collision() {
        // Empty implementation as in C++ version
    }

    @Override
    public void resetReproduction() {
        // Empty implementation as in C++ version
    }

    @Override
    public Organism copy() {
        return new Plant(this.getStrength(), this.getPosition(), this.age, this.world);
    }

    protected void spread() {
        if (Math.random() * 100 < Constants.SPREAD_PROBABILITY && this.getAge() > 0) {
            java.util.List<Point> freeTiles = getWorld().getAllFreeTiles(getPosition());

            if (freeTiles.isEmpty()) {
                return;
            }
            int randomIndex = (int)(Math.random() * freeTiles.size());

            Point newPosition = freeTiles.get(randomIndex);
            Organism newPlant = this.copy();
            newPlant.setPosition(newPosition);

            getWorld().addLog("Round: " + getWorld().getRoundNumber() + "->" + " new " + newPlant.toString() +
                    " was born at " + newPosition.x + ", " + newPosition.y);
            newPlant.setAge(0);
            getWorld().addOrganism(newPlant);
        }
    }
}