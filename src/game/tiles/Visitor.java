package game.tiles;

import game.board.GameBoard;
import game.tiles.board_component.Empty;
import game.tiles.board_component.Wall;
import game.tiles.units.enemies.Monster;
import game.tiles.units.enemies.Trap;
import game.tiles.units.player.Player;

public interface Visitor {
    void visit(Empty empty, GameBoard board);
    void visit(Wall wall, GameBoard board);
    void visit(Player player, GameBoard board);
    void visit(Monster monster, GameBoard board);
    void visit(Trap trap, GameBoard board);
}
