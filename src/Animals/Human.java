package Animals;
import General.*;

public class Human extends Animal {

    private boolean invincible = false;

    public Human(Point position) {
        super(Constants.HUMAN_STRENGTH, Constants.HUMAN_INITIATIVE, position);
    }

    public Human(Point position, int age, World world) {
        super(Constants.HUMAN_STRENGTH, Constants.HUMAN_INITIATIVE, position, age, world);
        this.world.setIsHumanAlive(true);
    }

    @Override
    public void action() {
        checkSpecial();

        Point moveDirection = null;

        switch (world.getHumanNextMove()) {
            case UP:
                moveDirection = new Point(0, -1);
                break;
            case DOWN:
                moveDirection = new Point(0, 1);
                break;
            case UP_LEFT:
                if (position.y % 2 == 0) {
                    moveDirection = new Point(-1, -1);
                } else {
                    moveDirection = new Point(0, -1);
                }
                break;
            case UP_RIGHT:
                if (position.y % 2 == 0) {
                    moveDirection = new Point(0, -1);
                } else {
                    moveDirection = new Point(1, -1);
                }
                break;
            case DOWN_LEFT:
                if (position.y % 2 == 0) {
                    moveDirection = new Point(-1, 1);
                } else {
                    moveDirection = new Point(0, 1);
                }
                break;
            case DOWN_RIGHT:
                if (position.y % 2 == 0) {
                    moveDirection = new Point(0, 1);
                } else {
                    moveDirection = new Point(1, 1);
                }
                break;
            case LEFT:
                moveDirection = new Point(-1, 0);
                break;
            case RIGHT:
                moveDirection = new Point(1, 0);
                break;
            case SPECIAL:
                useSpecial();
                break;
            case NONE:
                break;
            default:
                break;
        }

        if (moveDirection != null) {
            move(moveDirection);
        }
    }

    private void checkSpecial() {
        if (isInvincible() && world.getRoundNumber() - world.getSpecialMoveRound() >= Constants.HUMAN_SPECIAL_MOVE_COOLDOWN) {
            setIsInvincible(false);
            world.setIsSpecialActive(false);
            world.setSpecialMoveRound(world.getRoundNumber());
        }
        if (!isInvincible() && world.getRoundNumber() - world.getSpecialMoveRound() >= Constants.HUMAN_SPECIAL_MOVE_COOLDOWN) {
            world.setIsSpecialReady(true);
        }
    }

    private void useSpecial() {
        if (world.getIsSpecialReady()) {
            world.setSpecialMoveRound(world.getRoundNumber());
            setIsInvincible(true);
            world.setIsSpecialActive(true);
            world.setIsSpecialReady(false);
            world.addLog("You used the special ability!");
        }
        else {
            world.addLog("Special ability is not ready yet!");
        }
    }

    @Override
    public boolean isInvincible() {
        return invincible;
    }

    public void setIsInvincible(boolean isInvincible) {
        this.invincible = isInvincible;
        world.setIsSpecialActive(isInvincible);
    }

    @Override
    public String getGraphicRepresentation() {
        return Constants.HUMAN_GRAPHIC_REPRESENTATION;
    }

    @Override
    public String toString() {
        return "Human";
    }

    @Override
    public Human copy() {
        return new Human(this.position, this.age, this.world);
    }
}