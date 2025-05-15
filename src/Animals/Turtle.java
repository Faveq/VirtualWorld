package Animals;
import General.*;


public class Turtle extends Animal {

    public Turtle(Point position) {
        super(Constants.TURTLE_STRENGTH, Constants.TURTLE_INITIATIVE, position);
    }

    public Turtle(Point position, int age, World world) {
        super(Constants.TURTLE_STRENGTH, Constants.TURTLE_INITIATIVE, position, age, world);
    }

    @Override
    public String getGraphicRepresentation() {
        return Constants.TURTLE_GRAPHIC_REPRESENTATION;
    }

    @Override
    public String toString() {
        return "Turtle";
    }

    @Override
    public void action() {
        if (Math.random() * 100 < Constants.TURTLE_MOVE_CHANCE) {
            randomMove();
        }
    }

    @Override
    public boolean didDeflectAttack(Organism enemy) {
        return enemy.getStrength() < Constants.TURTLE_DEFLECT_POWER;
    }

    @Override
    public Organism copy() {
        return new Turtle(this.position, this.age, this.world);
    }
}