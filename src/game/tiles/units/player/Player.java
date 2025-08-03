package game.tiles.units.player;

import game.board.GameBoard;
import game.callbacks.MessageCallback;
import game.tiles.Tile;
import game.tiles.Visitor;
import game.tiles.board_component.Empty;
import game.tiles.board_component.Wall;
import game.tiles.units.Unit;
import game.tiles.units.enemies.Enemy;
import game.tiles.units.enemies.Monster;
import game.tiles.units.enemies.Trap;

import java.util.Random;
import java.util.Scanner;

public class Player extends Unit implements Visitor {
    public static final char PLAYER_CHAR = '@';
    protected static final int REQ_EXP = 50;
    protected static final int ATTACK_BONUS = 4;
    protected static final int DEFENSE_BONUS = 1;
    protected static final int HEALTH_BONUS = 10;

    protected int experience;
    private int level;


    public Player(String name, int attack, int defence, int amount, int cap, int x, int y, MessageCallback msg) {
        super(name, attack, defence, amount, cap, PLAYER_CHAR, x, y, msg);
        this.experience = 0;
        this.level = 1;
    }

    public void addExperience(int exp){
        this.experience += exp;
        while(experience >= REQ_EXP * level){
            addLevel(0, 0, 0);
        }
    }

    public void addLevel(int additional_HP, int additional_ATT, int additional_DEF){
        experience -= REQ_EXP * level;
        this.level++;
        int addHP = (HEALTH_BONUS + additional_HP) * level;
        int addATT = (ATTACK_BONUS + additional_ATT) * level;
        int addDEF = (DEFENSE_BONUS + additional_DEF) * level;
        getHp().addCapacity(addHP);
        getHp().setAmount(getHp().getCapacity());
        addAttack(addATT);
        addDefense(addDEF);
        msg.send(String.format("%s gained a level:  +%d health  +%d attack  +%d defense", getName(), addHP, addATT, addDEF));
    }

    public int getLevel(){
        return level;
    }

    public int getExperience(){
        return experience;
    }

    public int getReqExp(){
        return REQ_EXP;
    }

    @Override
    public void visit(Empty empty, GameBoard board) {
        board.swap(this, empty);
    }

    @Override
    public void visit(Wall wall, GameBoard board) {
        msg.send(String.format("%s cant go over walls.", getName()));
    }

    @Override
    public void visit(Player player, GameBoard board) {

    }

    @Override
    public void visit(Monster monster, GameBoard board) {
        attackEnemy(monster, board);
        if (!monster.isAlive()) {
            board.enemyDied(monster);
        }
    }

    @Override
    public void visit(Trap trap, GameBoard board) {
        attackEnemy(trap, board);
        if (!trap.isAlive()) {
            board.enemyDied(trap);
        }
    }

    @Override
    public void accept(Visitor visitor, GameBoard board) {
        visitor.visit(this, board);
    }

    public void onTurn(GameBoard board) {
        Scanner sc = new Scanner(System.in);
        status();
        msg.send("Enter move (w/a/s/d,e for special ability or q to do nothing):");
        String cmd = sc.nextLine();

        int dRow = 0, dCol = 0;
        switch (cmd) {
            case "w" -> dRow = -1;       // up
            case "s" -> dRow =  1;       // down
            case "a" -> dCol = -1;       // left
            case "d" -> dCol =  1;       // right
            case "e" -> specialAbility(board); // calls the special ability of the class
            case "q" -> System.exit(0);
            default  -> {
                msg.send("Invalid input.");
                return;
            }
        }

        int currentRow = getPosition().getX();
        int currentCol = getPosition().getY();
        int newRow = currentRow + dRow;
        int newCol = currentCol + dCol;

        Tile dest = board.getTile(newRow, newCol);
        dest.accept(this, board);
    }


    public void specialAbility(GameBoard board) {}

    /// Player death
    public boolean isDead() {
        if (getHp().getAmount() <= 0) {
            this.character = 'X';
            return true;
        }
        else  {
            return false;
        }
    }

    /// Player attacks enemy through visitor pattern
    public void attackEnemy(Enemy enemy, GameBoard board){
        msg.send(String.format("%s engaged in combat with %s", getName(), enemy.getName()));
        enemy.status();
        Random rand = new Random();
        int attackDMG = rand.nextInt(getAttack() + 1); // player roll attackDMG
        msg.send(String.format("%s rolled %d attack.", getName(), attackDMG));
        int enemyDefense = enemy.defend(attackDMG); // enemy roll defense
        msg.send(String.format("%s rolled %d defense.", enemy.getName(), enemyDefense));
        enemyDeathCheck(enemy, board, true);
    }

    /// Player defense from enemy incoming attack
    public int defend(int dmg) {
        Random rand = new Random();
        int defense = rand.nextInt(getDefense() + 1);
        if (dmg > defense){
            this.getHp().setAmount(getHp().getAmount() - (dmg - defense));
            if (this.getHp().getAmount() <= 0) { // health cannot drop below 0
                this.getHp().setAmount(0);
            }
        }

        return defense;
    }

    /// Checks if an enemy died and handles all the enemy death logic
    public void enemyDeathCheck(Enemy enemy, GameBoard board, boolean b) {
        if (!enemy.isAlive()){
            dth.deathAnnoucement(this, enemy);
            addExperience(enemy.getExperienceValue());
            if (b) {
                board.swap(this, enemy);
            }
            board.enemyDied(enemy);
        }
    }

    /// Printing status of the player to the announcements
    public void status(){
        msg.send(String.format("%s - Health: %d/%d  Attack: %d   Defense: %d    Level: %d   Experience: %d/%d",
                getName(), getHp().getAmount(), getHp().getCapacity(), getAttack(), getDefense(), getLevel(), getExperience(), getReqExp() * getLevel()));
    }
}
