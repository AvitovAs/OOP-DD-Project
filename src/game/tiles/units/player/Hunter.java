package game.tiles.units.player;

import game.board.GameBoard;
import game.callbacks.MessageCallback;
import game.tiles.Tile;
import game.tiles.units.enemies.Enemy;

public class Hunter extends Player {

    private int HUNTER_ATTACK_BONUS = 2;
    private int HUNTER_DEFENSE_BONUS = 1;
    private int BONUS_ARROWS = 10;
    private int range;
    private int arrows;
    private int game_ticks;

    public Hunter(String name, int attack, int defence, int amount, int cap, int x, int y, MessageCallback msg, int range) {
        super(name, attack, defence, amount, cap, x, y, msg);
        this.range = range;
        this.arrows = 10;
    }

    @Override
    public void addExperience(int exp) {
        this.experience += exp;
        while(experience >= REQ_EXP * getLevel()){
            addLevel( 0, HUNTER_ATTACK_BONUS, HUNTER_DEFENSE_BONUS);
            msg.send(String.format("Additional bonuses for Hunters: +%d arrows", BONUS_ARROWS * getLevel()));
            arrows += (BONUS_ARROWS * getLevel());
        }
    }

    @Override
    public void onTurn(GameBoard board) {
        super.onTurn(board);
        if (game_ticks == 10){
            arrows += getLevel();
            game_ticks = 0;
        }
        else {
            game_ticks += 1;
        }
    }

    @Override
    public void specialAbility(GameBoard board) { // TODO: test to see if it works
        if (arrows < 1) {
            msg.send(String.format("%s ran out of arrows so he cannot Shoot.", getName()));
            return;
        }

        int hunterX = getPosition().getX();
        int hunterY = getPosition().getY();
        Tile closest = null;
        double bestDist = Double.MAX_VALUE;

        // scan only rows [hunterX - range .. hunterX + range]
        for (int i = Math.max(0, hunterX - range);
             i <= Math.min(board.getHeight() - 1, hunterX + range); i++) { // first loop
            for (int j = Math.max(0, hunterY - range);
                 j <= Math.min(board.getWidth() - 1, hunterY + range); j++) { // second loop

                if (i == hunterX && j == hunterY) { // skip the hunters tile
                    continue;
                }

                Tile candidate = board.getTile(i, j);
                double dist = candidate.getPosition().distance(getPosition());

                if (candidate.getCharacter() != '.' && candidate.getCharacter() != '#') {
                    if (dist < bestDist) {
                        bestDist = dist;
                        closest = candidate;
                    }
                }
            }
        }

        if (closest == null) {
            msg.send(String.format("%s tried to use Shoot but no enemy was found in range.", getName()));
            return;
        }

        arrows -= 1;
        closest.specialHunterHit(this, board, getAttack());
    }

    @Override
    public void status(){
        msg.send(String.format("%s - Health: %d/%d  Attack: %d   Defense: %d    Arrows: %d    Level: %d   Experience: %d/%d",
                getName(), getHp().getAmount(), getHp().getCapacity(), getAttack(), getDefense(), arrows, getLevel(), getExperience(), getReqExp() * getLevel()));
    }
}
