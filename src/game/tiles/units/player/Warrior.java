package game.tiles.units.player;

import game.board.GameBoard;
import game.callbacks.MessageCallback;
import game.tiles.Tile;
import game.tiles.Visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Warrior extends Player {
    protected static final int WARRIOR_ATTACK_BONUS = 2;
    protected static final int WARRIOR_DEFENSE_BONUS = 1;
    protected static final int WARRIOR_HEALTH_BONUS = 5;
    protected static final int WARRIOR_HEAL_MULTI = 10;
    protected static final double WARRIOR_SPECIAL_ABILITY_MULTI = 0.1;
    private int cooldown;
    private int remaining_CD;

    public Warrior(String name, int attack, int defence, int amount, int cap, int x, int y, MessageCallback msg, int cd) {
        super(name, attack, defence, amount, cap, x, y, msg);
        this.cooldown = cd;
        this.remaining_CD = 0;
    }

    @Override
    public void addExperience(int exp) {
        this.experience += exp;
        while(experience >= REQ_EXP * getLevel()){
            addLevel(WARRIOR_HEALTH_BONUS, WARRIOR_ATTACK_BONUS, WARRIOR_DEFENSE_BONUS);
        }
    }

    @Override
    public void onTurn(GameBoard board) {
        super.onTurn(board);
        remaining_CD -= 1;
    }

    @Override
    public void specialAbility(GameBoard board) {
        if (remaining_CD > 0) {
            msg.send(getName() + " tried to cast Avenger’s Shield but ability is on cooldown: " +
                    remaining_CD + " turns left.");
            return;
        }

        msg.send(String.format("%s used his special ability!", getName()));
        remaining_CD = cooldown;
        heal();
        List<Tile> enemyCandidate = new ArrayList<>();
        int warriorX = getPosition().getX();
        int warriorY = getPosition().getY();

        for (int i = Math.max(0, warriorX - 2); i <= Math.min(board.getHeight() - 1, warriorX + 2); i++) {
            for (int j = Math.max(0, warriorY - 2); j <= Math.min(board.getWidth() - 1, warriorY + 2); j++) {
                Tile  tile = board.getTile(i, j);
                if (tile.getCharacter() != '@' && tile.getCharacter() != '.' && tile.getCharacter() != '#') {
                    enemyCandidate.add(board.getTile(i, j));
                }
            }
        }

        if (!enemyCandidate.isEmpty()) { // randomize the enemy hit around the warrior
            Random rand = new Random();
            int randomEnemy = rand.nextInt(enemyCandidate.size());
            Tile enemyToHit = enemyCandidate.get(randomEnemy);
            int dmg = (int) (WARRIOR_SPECIAL_ABILITY_MULTI * getHp().getCapacity());
            enemyToHit.specialWarriorHit(this, board, dmg);
        } else {
            msg.send(String.format("%s special ability hit nothing, amazing!", getName()));
        }
    }

    private void heal() {
        getHp().addAmount(getDefense() * WARRIOR_HEAL_MULTI);
    }

    public int getRemaining_CD() {
        return  remaining_CD;
    }
}
