package game.tiles.units.enemies;

import game.board.GameBoard;
import game.callbacks.MessageCallback;
import game.tiles.Tile;
import game.tiles.Visitor;
import game.tiles.board_component.Empty;
import game.tiles.board_component.Wall;
import game.tiles.units.player.Player;
import game.utils.Position;

import java.util.Random;

public class Monster extends Enemy implements Visitor {
    private int visionRange;

    public Monster(String name, int attack, int defense, int amount, int cap, char c, int x, int y,int visionRange, int experienceValue,  MessageCallback msg) {
        super(name, attack, defense, amount, cap, c, x, y, experienceValue, msg);
        this.visionRange = visionRange;
    }

    @Override
    public void visit(Empty empty, GameBoard board) {
        board.swap(this, empty);
    }

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
        visitor.visit(this, board);
    }

    @Override
    public void onTurn(Tile player, GameBoard board) {
        if (getHp().getAmount() > 0) {
            if (this.getPosition().distance(player.getPosition()) < this.visionRange) { // Monsters goes towards the player
                int moveX = getPosition().getX() - player.getPosition().getX();
                int moveY = getPosition().getY() - player.getPosition().getY();
                int monsterX = this.getPosition().getX();
                int monsterY = this.getPosition().getY();
                if (Math.abs(moveX) > Math.abs(moveY)) {
                    if (moveX > 0) {
                        board.getTile(monsterX - 1, monsterY).accept(this, board);
                    } else {
                        board.getTile(monsterX + 1, monsterY).accept(this, board);
                    }
                } else {
                    if (moveY > 0) {
                        board.getTile(monsterX, monsterY - 1).accept(this, board);
                    } else {
                        board.getTile(monsterX, monsterY + 1).accept(this, board);
                    }
                }
            } else { // Monster goes randomly
                Random rnd = new Random();
                int direction;
                int dRow = 0, dCol = 0;
                direction = rnd.nextInt(0, 5);
                switch (direction) {
                    case 1 -> dCol = 1;         // Right
                    case 2 -> dRow = -1;        // Up
                    case 3 -> dCol = -1;        // Left
                    case 4 -> dRow = 1;         // Down
                    default -> {
                    }
                }

                int newRow = this.getPosition().getX() + dRow;
                int newCol = this.getPosition().getY() + dCol;
                Tile dest = board.getTile(newRow, newCol);
                dest.accept(this, board);
            }
        }
    }
}
