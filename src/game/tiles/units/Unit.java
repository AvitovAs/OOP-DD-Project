package game.tiles.units;
import game.board.GameBoard;
import game.callbacks.DeathCallback;
import game.callbacks.MessageCallback;
import game.tiles.Tile;
import game.tiles.Visitor;
import game.utils.Resource;

abstract public class Unit extends Tile {
    protected String name;
    private Resource hp;
    protected int attack;
    protected int defense;
    protected final MessageCallback msg;
    protected final DeathCallback dth;

    public Unit(String name, int attack, int defense, int amount, int cap, char c, int x, int y, MessageCallback msg) {
        super(c, x, y);
        this.name = name;
        this.hp = new  Resource(amount, cap);
        this.attack = attack;
        this.defense = defense;
        this.msg = msg;
        this.dth = (killer, dead) -> msg.send(killer.getName() + " killed " + dead.getName());
    }

    @Override
    public abstract void accept(Visitor visitor, GameBoard board);

    public String getName() {
        return name;
    }

    public Resource getHp() {
        return hp;
    }

    public int getAttack() {
        return attack;
    }

    public void addAttack(int attAddition) {
        this.attack += attAddition;
    }

    public int getDefense() {
        return defense;
    }

    public void addDefense(int defense) {
        this.defense += defense;
    }

    public boolean isAlive() {
        return getHp().getAmount() > 0;
    }
}
