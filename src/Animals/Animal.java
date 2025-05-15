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
        randomMove();
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

        //if (!getGraphicRepresentation().equals(WOLF_GRAPHIC_REPRESENTATION)) {
        do {
            Random random = new Random();
            int randomX = (random.nextInt(2 * range + 1)) - range;
            int randomY = (random.nextInt(2 * range + 1)) - range;
            shift = new Point(randomX, randomY);
            collidingOrganism = getWorld().getOrganismAt(getPosition().add(shift));

            if (getWorld().isEveryoneStronger(this) && hasGoodSmell()) {
                shift = new Point(0, 0);
                break;
            }

        } while ((shift.x == 0 && shift.y == 0) ||
                (hasGoodSmell() && collidingOrganism != null && collidingOrganism.compareTo(this) > 0));

        move(shift);
        //}
    }

    protected void randomMove() {
        randomMove(1);
    }

    protected void move(Point shift) {
        Point newPosition = getPosition().add(shift);
        if (!newPosition.beyondBorders(getWorld().getWidth(), getWorld().getHeight())) {
            prevPosition = new Point(getPosition().x, getPosition().y);
            setPosition(newPosition);
        }
    }

    private void fight(Organism enemy) {
        if (enemy instanceof Animal) {
            Animal enemyAnimal = (Animal) enemy;
            if (escaped() || enemyAnimal.escaped()) {
                return;
            }
        }

        if (this.isAlive() && enemy.isAlive()) {
            // strength comparison
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

                int thisAge = getAge();
                int enemyAge = enemy.getAge();
                getWorld().addLog("Round: " + getWorld().getRoundNumber() + "-> organism " + this.toString() + " age " + thisAge + " was killed by " + enemy.toString() + " age " + enemyAge);
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

                int thisAge = getAge();
                int enemyAge = enemy.getAge();
                getWorld().addLog("Round: " + getWorld().getRoundNumber() + "-> organism " + enemy.toString() + " age " + enemyAge + " was killed by " + this.toString() + " age " + thisAge);
                enemy.addEffect(this);
                enemy.kill();
            } else {
                if (this.getAge() > enemy.getAge()) {
                    if (enemy.didDeflectAttack(this)) {
                        moveBack();
                        return;
                    }
                    if (enemy.isInvincible()) {
                        Point freeTile = getWorld().getFreeTile(enemy.getPosition());
                        enemy.setPosition(freeTile);
                        return;
                    }
                    int thisAge = getAge();
                    int enemyAge = enemy.getAge();
                    getWorld().addLog("Round: " + getWorld().getRoundNumber() + "-> organism " + enemy.toString() + " age " + enemyAge + " was killed by " + this.toString() + " age " + thisAge);
                    enemy.addEffect(this);
                    enemy.kill();
                } else {
                    if (this.didDeflectAttack(enemy)) {
                        moveBack();
                        return;
                    }
                    if (this.isInvincible()) {
                        Point freeTile = getWorld().getFreeTile(getPosition());
                        setPosition(freeTile);
                        return;
                    }
                    int thisAge = getAge();
                    int enemyAge = enemy.getAge();
                    getWorld().addLog("Round: " + getWorld().getRoundNumber() + "-> organism " + this.toString() + " age " + thisAge + " was killed by " + enemy.toString() + " age " + enemyAge);
                    this.addEffect(enemy);
                    this.kill();
                }
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