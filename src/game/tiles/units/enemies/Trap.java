package game.tiles.units.enemies;

import game.board.GameBoard;
import game.callbacks.MessageCallback;
import game.tiles.Tile;
import game.tiles.Visitor;
import game.tiles.board_component.Empty;
import game.tiles.board_component.Wall;
import game.tiles.units.player.Player;

public class Trap extends Enemy implements Visitor {
    private int visibilityTime;
    private int invisibleTime;
    private int tickCount;
    private boolean visible;

    public Trap(String name, int attack, int defense, int amount, int cap, char c, int x, int y,int visibilityTime, int invisibleTime, int experienceValue,  MessageCallback msg) {
        super(name, attack, defense, amount, cap, c, x, y, experienceValue, msg);
        this.visibilityTime = visibilityTime;
        this.invisibleTime = invisibleTime;
        this.tickCount = 0;
        this.visible = true;
    }

    @Override
    public void visit(Empty empty, GameBoard board) {}

    @Override
    public void visit(Wall wall, GameBoard board) {}

    @Override
    public void visit(Player player, GameBoard board) {
        attackPlayer(player);
    }

    @Override
    public void visit(Monster monster, GameBoard board) {}

    @Override
    public void visit(Trap trap, GameBoard board) {}

    @Override
    public void accept(Visitor visitor, GameBoard board) {
        if (visible) {
            visitor.visit(this, board);
        }
    }

    @Override
    public void onTurn(Tile player, GameBoard board) {
        if (getHp().getAmount() > 0) {
            if (visible) {
                if (visibilityTime == tickCount) {
                    visible = false;
                    tickCount = 0;
                }
            } else {
                if (invisibleTime == tickCount) {
                    visible = true;
                    tickCount = 0;
                }
            }

            if (getPosition().distance(player.getPosition()) < 2) {
                visible = true;
                player.accept(this, board);
            }
            else {
                visible = false;
            }

            tickCount += 1;
        }
    }

    @Override
    public void specialWarriorHit(Player player, GameBoard board, int dmg) {
        if (visible) {
            super.specialWarriorHit(player, board, dmg);
        }
    }

    @Override
    public void specialMageHit(Player player, GameBoard board, int dmg) {
        if (visible) {
            super.specialMageHit(player, board, dmg);
        }
    }

    @Override
    public void specialHunterHit(Player player, GameBoard board, int dmg) {
        if (visible) {
            super.specialHunterHit(player, board, dmg);
        }
    }

    @Override
    public String toString() {
        if (this.visible) {
            return String.valueOf(getCharacter());
        }
        else {
            return ".";
        }
    }

}
