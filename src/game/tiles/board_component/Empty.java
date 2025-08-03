package game.tiles.board_component;

import game.board.GameBoard;
import game.tiles.Tile;
import game.tiles.Visitor;
import game.tiles.units.enemies.Enemy;
import game.tiles.units.enemies.Monster;
import game.tiles.units.enemies.Trap;
import game.tiles.units.player.Player;

public class Empty extends Tile implements Visitor {

    public Empty(int x, int y) {
        super('.', x, y);
    }

    @Override
    public void visit(Empty empty, GameBoard board) {

    }

    @Override
    public void visit(Wall wall, GameBoard board) {

    }

    @Override
    public void visit(Player player, GameBoard board) {

    }

    @Override
    public void visit(Monster monster, GameBoard board) {

    }

    @Override
    public void visit(Trap trap, GameBoard board) {

    }

    public boolean isDead(){
        return false;
    }

    @Override
    public void accept(Visitor visitor, GameBoard board) {
        visitor.visit(this, board);
    }
}
