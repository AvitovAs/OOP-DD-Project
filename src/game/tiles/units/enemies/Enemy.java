package game.tiles.units.enemies;

import game.board.GameBoard;
import game.callbacks.MessageCallback;
import game.tiles.Tile;
import game.tiles.units.Unit;
import game.tiles.units.player.Player;

import java.util.Random;

public abstract class Enemy extends Unit {

    private int experienceValue;

    public Enemy(String name, int attack, int defense, int amount, int cap, char c, int x, int y, int experienceValue,  MessageCallback msg) {
        super(name, attack, defense, amount, cap, c, x, y, msg);
        this.experienceValue = experienceValue;
    }

    public int getExperienceValue() {
        return experienceValue;
    }

    @Override
    public void onTurn(Tile player, GameBoard board){}

    public void attackPlayer(Player player) {
        msg.send(String.format("%s engaged in combat with %s", getName(), player.getName()));
        status();
        player.status();
        Random rand = new Random();
        int attackDMG = rand.nextInt(getAttack() + 1);
        msg.send(String.format("%s rolled %d attack.", getName(), attackDMG));
        int playerDefense = player.defend(attackDMG);
        msg.send(String.format("%s rolled %d defense.", player.getName(), playerDefense));
        if (player.isDead()){
            dth.deathAnnoucement(this, player);
        }
    }

    public int defend(int attackDMG) {
        Random rand = new Random();
        int defense = rand.nextInt(getDefense() + 1);
        if (attackDMG > defense){ // see if the defense rolled can block the attackDMG
            this.getHp().setAmount(getHp().getAmount() - (attackDMG - defense));
            if (this.getHp().getAmount() <= 0) { // health cannot drop below 0
                this.getHp().setAmount(0);
            }
        }

        return defense;
    }

    @Override
    public void specialWarriorHit(Player player, GameBoard board, int dmg) {
        this.getHp().setAmount(getHp().getAmount() - dmg);
        msg.send(String.format("%s dealt %d damage to %s", player.getName(), dmg, getName()));
        status();
        player.enemyDeathCheck(this, board, true);
    }

    @Override
    public void specialMageHit(Player player, GameBoard board, int dmg) {
        msg.send(String.format("%s used Blizzard on %s.", player.getName(), getName()));
        msg.send(String.format("%s rolled %d defense.", this.getName(), defend(dmg)));
        status();
        player.enemyDeathCheck(this, board, false);
    }

    @Override
    public void specialHunterHit(Player player, GameBoard board, int dmg) {
        msg.send(String.format("%s shot %s with an arrow.", player.getName(), getName()));
        msg.send(String.format("%s rolled %d defense.", this.getName(), defend(dmg)));
        status();
        player.enemyDeathCheck(this, board, false);
    }


    public boolean isDead(){
        if (getHp().getAmount() <= 0){
            this.setCharacter('.');
            return true;
        }
        else {
            return false;
        }
    }

    public void status(){
        msg.send(String.format("%s - Health: %d/%d  Attack: %d   Defense: %d    Experience Value: %d",
                getName(), getHp().getAmount(), getHp().getCapacity(), getAttack(), getDefense(), getExperienceValue()));
    }
}
