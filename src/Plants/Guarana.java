package Plants;
import General.*;

public class Guarana extends Plant {
    public Guarana(Point position) {
        super(Constants.GUARANA_STRENGTH, position);
    }

    public Guarana(Point position, int age, World world) {
        super(Constants.GUARANA_STRENGTH, position, age, world);
    }

    @Override
    public String getGraphicRepresentation() {
        return Constants.GUARANA_GRAPHIC_REPRESENTATION;
    }

    @Override
    public String toString() {
        return "Guarana";
    }

    @Override
    public void action() {
        for (int i = 0; i < Constants.SPREAD_ATTEMPTS; i++) {
            spread();
        }
    }

    public void addEffect(Organism organism) {
        organism.setStrength(organism.getStrength() + Constants.GUARANA_STRENGTH_BUFF);
    }

    @Override
    public Organism copy() {
        return new Guarana(this.getPosition(), this.age, this.world);
    }
}