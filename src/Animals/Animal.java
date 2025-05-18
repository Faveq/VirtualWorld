package Animals;
import General.*;
import java.util.Random;

public class Animal extends Organism {
    private boolean reproduced = false;
    private Point prevPosition;

    public Animal(int strength, int initiative, Point position) {
        super(strength, initiative, position);
        this.prevPosition = new Point(position.x, position.y);
    }

    public Animal(int strength, int initiative, Point position, int age, World world) {
        super(strength, initiative, position, age, world);
        this.prevPosition = new Point(position.x, position.y);
    }

    @Override
    public String getGraphicRepresentation() {
        return "A";
    }

    @Override
    public String toString() {
        return "Animal";
    }

    public boolean escaped() {
        if (didEscape()) {
            Point newPosition = getWorld().getFreeTile(getPosition());
            if (newPosition.equals(prevPosition)) {
                return false;
            }
            setPosition(newPosition);
            return true;
        }
        return false;
    }

    @Override
    public void action() {
        randomMove(1);
    }

    @Override
    public void collision() {
        Organism other = getWorld().getColliding(this);
        if (other != null) {
            if (this.getGraphicRepresentation().equals(other.getGraphicRepresentation())) {
                reproduce((Animal) other);
            } else {
                fight(other);
            }
        }
    }

    @Override
    public Organism copy() {
        return new Animal(getStrength(), getInitiative(), getPosition());
    }

    @Override
    public void resetReproduction() {
        reproduced = false;
    }

    protected void randomMove(int range) {
        Point shift = new Point(0, 0);
        Organism collidingOrganism;
        int[][] directions = world.getDirections(this.getPosition().y);

        if (getWorld().isEveryoneStronger(this) && hasGoodSmell()) {
            return;
        }

        if (!getGraphicRepresentation().equals(Constants.WOLF_GRAPHIC_REPRESENTATION)) {
            do {
                Random random = new Random();


//                int randomX = (random.nextInt(2 * range + 1)) - range;
//                int randomY = (random.nextInt(2 * range + 1)) - range;

                int[] randDirection = directions[random.nextInt(directions.length)];

//                shift = new Point(randomX, randomY);
                shift = new Point(randDirection[0], randDirection[1]);
                collidingOrganism = getWorld().getOrganismAt(getPosition().add(shift));

            } while ((shift.x == 0 && shift.y == 0) ||
                    (hasGoodSmell() && collidingOrganism != null && collidingOrganism.compareTo(this) > 0));

            move(shift);
        }
    }

    protected void move(Point shift) {
        Point newPosition = getPosition().add(shift);
        if (!newPosition.beyondBorders(getWorld().getWidth(), getWorld().getHeight())) {
            prevPosition = new Point(getPosition().x, getPosition().y);
            setPosition(newPosition);
        }
    }

//    private void fight(Organism enemy) {
//        if (enemy instanceof Animal) {
//            Animal enemyAnimal = (Animal) enemy;
//            if (escaped() || enemyAnimal.escaped()) {
//                return;
//            }
//        }
//
//        if (this instanceof Animal && this.getGraphicRepresentation() == Constants.CYBER_SHEEP_GRAPHIC_REPRESENTATION) {
//            if (enemy.getGraphicRepresentation() == Constants.HOGEWEED_OF_PINE_GRAPHIC_REPRESENTATION)
//            {
//                enemy.kill();
//                return;
//            }
//        }
//
//        if (this.isAlive() && enemy.isAlive()) {
//            // strength comparison
//            if (this.compareTo(enemy) < 0) {
//                if (this.didDeflectAttack(enemy)) {
//                    moveBack();
//                    return;
//                }
//                if (this.isInvincible()) {
//                    Point freeTile = getWorld().getFreeTile(getPosition());
//                    setPosition(freeTile);
//                    return;
//                }
//
//                int thisAge = getAge();
//                int enemyAge = enemy.getAge();
//                getWorld().addLog("Round: " + getWorld().getRoundNumber() + "-> organism " + this.toString() + " age " + this.getAge() + " strength "+ this.getStrength() + " was killed by " + enemy.toString() + " age " + enemy.getAge() + " strength "+ enemy.getStrength());
//                addEffect(enemy);
//                this.kill();
//            } else if (this.compareTo(enemy) > 0) {
//                if (enemy.didDeflectAttack(this)) {
//                    moveBack();
//                    return;
//                }
//                if (enemy.isInvincible()) {
//                    Point freeTile = getWorld().getFreeTile(enemy.getPosition());
//                    enemy.setPosition(freeTile);
//                    return;
//                }
//
//                int thisAge = getAge();
//                int enemyAge = enemy.getAge();
//                getWorld().addLog("Round: " + getWorld().getRoundNumber() + "-> organism " + enemy.toString() + " age "+ enemy.getAge() + " strength " + enemy.getStrength()  + " was killed by " + this.toString() + " age " + this.getAge()+ " strength "+ this.getStrength() );
//                enemy.addEffect(this);
//                enemy.kill();
//            } else {
//                if (this.getAge() > enemy.getAge()) {
//                    if (enemy.didDeflectAttack(this)) {
//                        moveBack();
//                        return;
//                    }
//                    if (enemy.isInvincible()) {
//                        Point freeTile = getWorld().getFreeTile(enemy.getPosition());
//                        enemy.setPosition(freeTile);
//                        return;
//                    }
//                    int thisAge = getAge();
//                    int enemyAge = enemy.getAge();
//                    getWorld().addLog("Round: " + getWorld().getRoundNumber() + "-> organism " + enemy.toString() + " age "+  enemyAge + " strength " + enemy.getStrength() +" was killed by " + this.toString() + " age " + thisAge+ " strength " + this.getStrength());
//                    enemy.addEffect(this);
//                    enemy.kill();
//                } else {
//                    if (this.didDeflectAttack(enemy)) {
//                        moveBack();
//                        return;
//                    }
//                    if (this.isInvincible()) {
//                        Point freeTile = getWorld().getFreeTile(getPosition());
//                        setPosition(freeTile);
//                        return;
//                    }
//                    int thisAge = getAge();
//                    int enemyAge = enemy.getAge();
//                    getWorld().addLog("Round: " + getWorld().getRoundNumber() + "-> organism " + this.toString() + " age " + thisAge + " strength " + this.getStrength() + " was killed by " + enemy.toString() + " age " + enemyAge+ " strength " + enemy.getStrength());
//                    this.addEffect(enemy);
//                    this.kill();
//                }
//            }
//        }
//    }

    private void fight(Organism enemy) {
        if (enemy instanceof Animal) {
            Animal enemyAnimal = (Animal) enemy;
            if (escaped() || enemyAnimal.escaped()) {
                return;
            }
        }

        if (this instanceof Animal && this.getGraphicRepresentation() == Constants.CYBER_SHEEP_GRAPHIC_REPRESENTATION) {
            if (enemy.getGraphicRepresentation() == Constants.HOGEWEED_OF_PINE_GRAPHIC_REPRESENTATION) {
                enemy.kill();
                return;
            }
        }

        if (this.isAlive() && enemy.isAlive()) {
            if (this.compareTo(enemy) < 0) {
                if (this.didDeflectAttack(enemy)) {
                    moveBack();
                    return;
                }
                if (this.isInvincible()) {
                    Point freeTile = getWorld().getFreeTile(getPosition());
                    setPosition(freeTile);
                    return;
                }
                getWorld().addLog("Round: " + getWorld().getRoundNumber() + "-> organism " + this.toString() + " age " + this.getAge() + " strength "+ this.getStrength() + " was killed by " + enemy.toString() + " age " + enemy.getAge() + " strength "+ enemy.getStrength());
                addEffect(enemy);
                this.kill();
            } else if (this.compareTo(enemy) > 0) {
                if (enemy.didDeflectAttack(this)) {
                    moveBack();
                    return;
                }
                if (enemy.isInvincible()) {
                    Point freeTile = getWorld().getFreeTile(enemy.getPosition());
                    enemy.setPosition(freeTile);
                    return;
                }
                getWorld().addLog("Round: " + getWorld().getRoundNumber() + "-> organism " + enemy.toString() + " age "+ enemy.getAge() + " strength " + enemy.getStrength()  + " was killed by " + this.toString() + " age " + this.getAge()+ " strength "+ this.getStrength() );
                enemy.addEffect(this);
                enemy.kill();
            } else { // równa siła, wygrywa atakujący (this)
                if (enemy.didDeflectAttack(this)) {
                    moveBack();
                    return;
                }
                if (enemy.isInvincible()) {
                    Point freeTile = getWorld().getFreeTile(enemy.getPosition());
                    enemy.setPosition(freeTile);
                    return;
                }
                getWorld().addLog("Round: " + getWorld().getRoundNumber() + "-> organism " + enemy.toString() + " age "+  enemy.getAge() + " strength " + enemy.getStrength() +" was killed by " + this.toString() + " age " + this.getAge()+ " strength " + this.getStrength());
                enemy.addEffect(this);
                enemy.kill();
            }
        }
    }
    private void reproduce(Animal partner) {
        Animal child = (Animal) this.copy();
        Point birthPosition = getWorld().getFreeTile(partner.getPosition());
        this.moveBack();
        if (birthPosition.equals(partner.getPosition()) || reproduced || partner.reproduced) {
            return;
        }
        child.setPosition(birthPosition);
        child.setAge(0);
        getWorld().addOrganism(child);
        getWorld().addLog("Round: " + getWorld().getRoundNumber() + "-> animal " + this.toString() + " age " + this.getAge() + " reproduced with " + partner.toString() + " age " + partner.getAge());
        reproduced = true;
        partner.reproduced = true;
    }

    private void moveBack() {
        setPosition(prevPosition);
    }
}