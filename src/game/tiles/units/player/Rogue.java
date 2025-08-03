package game.tiles.units.player;

import game.board.GameBoard;
import game.callbacks.MessageCallback;
import game.tiles.Tile;
import game.utils.Resource;

import java.util.ArrayList;
import java.util.List;

public class Rogue extends Player {
    private Resource ENERGY;
    private int ABILITY_COST;
    private int ENERGY_AMOUNT = 100;
    private int ENERGY_REGENRATION = 10;
    private int ROGUE_ATTACK_BONUS = 3;

    public Rogue(String name, int attack, int defence, int amount, int cap, int x, int y, MessageCallback msg, int ability_cost) {
        super(name, attack, defence, amount, cap, x, y, msg);
        this.ENERGY = new Resource(ENERGY_AMOUNT, ENERGY_AMOUNT);
        this.ABILITY_COST = ability_cost;
    }

    @Override
    public void addExperience(int exp) {
        this.experience += exp;
        while(experience >= REQ_EXP * getLevel()){
            addLevel( 0, ROGUE_ATTACK_BONUS, 0);
            ENERGY.setAmount(ENERGY_AMOUNT);
        }
    }

    @Override
    public void onTurn(GameBoard board) {
        super.onTurn(board);
        ENERGY.addAmount(ENERGY_REGENRATION);
    }

    @Override
    public void specialAbility(GameBoard board) {
        if (ENERGY.getAmount() < ABILITY_COST) {
            msg.send(getName() + " tried to cast Fan of Knives but has only "
                    + ENERGY.getAmount() + "/"+ ABILITY_COST + " energy.");
            return;
        }

        msg.send(getName() + " has used its special ability Fan of Knives.");
        ENERGY.addAmount(-ABILITY_COST); // its negative as to remove the neccessary energy needed for the ability
        int rogueX = getPosition().getX();
        int rogueY = getPosition().getY();

        for (int i = Math.max(0, rogueX - 1); i <= Math.min(board.getHeight() - 1, rogueX + 1); i++) {
            for (int j = Math.max(0, rogueY - 1); j <= Math.min(board.getWidth() - 1, rogueY + 1); j++) {
                if (i == rogueX && j == rogueY) { continue;} // skip Rogue itself

                Tile nearbyTile = board.getTile(i, j);
                if (nearbyTile.getCharacter() != '#' && nearbyTile.getCharacter() != '.') {
                    nearbyTile.accept(this, board); // attack the enemy
                }
            }
        }
    }

    @Override
    public void status(){
        msg.send(String.format("%s - Health: %d/%d  Attack: %d   Defense: %d    Energy: %d/%d    Level: %d   Experience: %d/%d",
                getName(), getHp().getAmount(), getHp().getCapacity(), getAttack(), getDefense(), ENERGY.getAmount(), ENERGY.getCapacity(), getLevel(), getExperience(), getReqExp() * getLevel()));
    }
}
