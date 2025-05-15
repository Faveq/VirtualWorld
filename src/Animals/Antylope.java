package Animals;
import General.*;

public class Antylope extends Animal {
    public Antylope(Point position) {
        super(Constants.ANTYLOPE_STRENGTH, Constants.ANTYLOPE_INITIATIVE, position);
    }

    public Antylope(Point position, int age, World world) {
        super(Constants.ANTYLOPE_STRENGTH, Constants.ANTYLOPE_INITIATIVE, position, age, world);
    }

    @Override
    public String getGraphicRepresentation() {
        return Constants.ANTYLOPE_GRAPHIC_REPRESENTATION;
    }

    @Override
    public Organism copy() {
        return new Antylope(this.position, this.age, this.world);
    }

    @Override
    public String toString() {
        return "Antylope";
    }

    @Override
    public void action() {
        randomMove(Constants.ANTYLOPE_MOVE_RANGE);
    }

    @Override
    public boolean didEscape() {
        return Math.random() * 100 < Constants.ANTYLOPE_ESCAPE_CHANCE;
    }
}