package game.tiles.units.player;

import game.board.GameBoard;
import game.callbacks.MessageCallback;
import game.tiles.Tile;
import game.utils.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Mage extends Player {
    private Resource mp;
    private int ABILITY_MANA_COST;
    private int ABILITY_DAMAGE;
    private int ABILITY_HIT_COUNT;
    private int ABILITY_RANGE;
    private int MANA_BONUS_PER_LEVEL = 25;
    private int ABILITY_DAMAGE_BONUS = 10;

    public Mage(String name, int attack, int defence, int amount, int cap, int x, int y, MessageCallback msg, int manaPool, int manaCost, int spell_power, int hit_count, int ability_range){
        super(name, attack, defence, amount, cap, x, y, msg);
        this.mp = new Resource(manaPool/4, manaPool);
        this.ABILITY_MANA_COST = manaCost;
        this.ABILITY_DAMAGE = spell_power;
        this.ABILITY_HIT_COUNT = hit_count;
        this.ABILITY_RANGE = ability_range;
    }

    @Override
    public void addExperience(int exp) {
        this.experience += exp;
        while(experience >= REQ_EXP * getLevel()){
            addLevel( 0, 0, 0);
            mp.addCapacity(MANA_BONUS_PER_LEVEL * getLevel());
            mp.addAmount(mp.getCapacity()/4);
            this.ABILITY_DAMAGE += (ABILITY_DAMAGE_BONUS * getLevel());
            msg.send(String.format("Additional mage bonuses - Spell Damage: +%d     Mana Capacity: +%d", ABILITY_DAMAGE_BONUS * getLevel(), MANA_BONUS_PER_LEVEL * getLevel()));
        }
    }

    @Override
    public void onTurn(GameBoard board) {
        super.onTurn(board);
        mp.addAmount(getLevel());
    }

    @Override
    public void specialAbility(GameBoard board) {
        if (mp.getAmount() < ABILITY_MANA_COST) {
            msg.send(getName() + " tried to cast Blizzard but has only "
                    + mp.getAmount() + "/" + ABILITY_MANA_COST + " mana.");
            return;
        }

        status();
        msg.send(getName() + " has used its special ability Blizzard.");
        mp.addAmount(-ABILITY_MANA_COST);
        int hits = 0;

        // Scan enemies around the Mage
        int mageX = getPosition().getX();
        int mageY = getPosition().getY();
        List<Tile> nearbyEnemies = new ArrayList<>();

        for (int i = Math.max(0, mageX - ABILITY_RANGE); i <= Math.min(board.getHeight() - 1, mageX + ABILITY_RANGE); i++) {
            for (int j = Math.max(0, mageY - ABILITY_RANGE); j <= Math.min(board.getWidth() - 1, mageY + ABILITY_RANGE); j++) {
                if (i == mageX && j == mageY) { // Skip Mage itself
                    continue;
                }

                Tile enemyCandidate = board.getTile(i, j);
                double dist = enemyCandidate.getPosition().distance(getPosition());
                if (dist >= ABILITY_RANGE) { // Only include tiles within range < 3
                    continue;
                }

                if (enemyCandidate.getCharacter() != '#' && enemyCandidate.getCharacter() != '.') {
                    nearbyEnemies.add(enemyCandidate);
                }
            }
        }

        // Hit the enemies randomly until hit count was exhausted
        while (hits < ABILITY_HIT_COUNT && nearbyEnemies.size() > 0) {
            Tile enemyToHit = nearbyEnemies.get(randomEnemy(nearbyEnemies.size()));
            enemyToHit.specialMageHit(this, board, ABILITY_DAMAGE);

            if (enemyToHit.getCharacter() == '.') { // checks if the enemy died
                nearbyEnemies.remove(enemyToHit);
            }

            hits += 1;
        }
    }

    private int randomEnemy(int range){
        Random rand = new Random();
        return rand.nextInt(range);
    }

    @Override
    public void status(){
        msg.send(String.format("%s - Health: %d/%d  Attack: %d   Defense: %d    Mana: %d/%d     Spell Damage: %d    Level: %d   Experience: %d/%d",
                getName(), getHp().getAmount(), getHp().getCapacity(), getAttack(), getDefense(), mp.getAmount(), mp.getCapacity(), ABILITY_DAMAGE,
                getLevel(), getExperience(), getReqExp() * getLevel()));
    }
}
