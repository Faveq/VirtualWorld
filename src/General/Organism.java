package General;

import Animals.Human;

public abstract class Organism {
    protected int strength;
    protected int initiative;
    protected Point position;
    protected boolean alive = true;
    protected int age = 0;
    protected World world;

    public Organism(int strength, int initiative, Point position) {
        this.strength = strength;
        this.initiative = initiative;
        this.position = position;
    }

    public Organism(int strength, int initiative, Point position, int age, World world) {
        this.strength = strength;
        this.initiative = initiative;
        this.position = position;
        this.age = age;
        this.world = world;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getInitiative() {
        return initiative;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void increaseAge() {
        this.age++;
    }

    public boolean isAlive() {
        return alive;
    }

    public void kill() {
        this.alive = false;
        if(this instanceof Human)
        {
            world.setIsHumanAlive(false);
        }
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void addEffect(Organism organism) {
        // default: no effect
    }

    public boolean isInvincible() {
        return false;
    }

    public boolean didEscape() {
        return false;
    }

    public boolean hasGoodSmell() {
        return false;
    }

    public boolean didDeflectAttack(Organism enemy) {
        return false;
    }

    public abstract String getGraphicRepresentation();

    public abstract Organism copy();

    public abstract String toString();

    public abstract void action();

    public abstract void collision();

    public abstract void resetReproduction();


    // -1 if this < other
    // 0 if this == other
    // 1 if this > other
    public int compareTo(Organism other) {
        return Integer.compare(this.strength, other.strength);
    }

    //Add < > overrides as methods
}
