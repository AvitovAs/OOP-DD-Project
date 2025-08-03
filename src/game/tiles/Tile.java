package game.tiles;
import game.board.GameBoard;
import game.tiles.board_component.Empty;
import game.tiles.board_component.Wall;
import game.tiles.units.enemies.Monster;
import game.tiles.units.enemies.Trap;
import game.tiles.units.player.Player;
import game.utils.Position;

public abstract class Tile implements Visitor {
    protected char character;
    private Position position;

    public Tile(char c,  int x, int y) {
        this.character = c;
        this.position = new Position(x, y);
    }

    public abstract void accept(Visitor visitor, GameBoard board); // Its a must for the visitor pattern

    public void visit(Empty empty, GameBoard board){}

    public void visit(Wall wall, GameBoard board){}

    public void visit(Player player, GameBoard board){}

    public void visit(Monster monster, GameBoard board){}

    public void visit(Trap trap, GameBoard board){}

    public void setX(int x) {
        this.position.setX(x);
    }

    public void setY(int y) {
        this.position.setY(y);
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(int x, int y) {
        position.setX(x);
        position.setY(y);
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void onTurn(Tile player, GameBoard board) {}

    public void onTurn(GameBoard board) {}

    public void specialWarriorHit(Player player, GameBoard board, int dmg) {}

    public void specialMageHit(Player player, GameBoard board, int dmg) {}

    public void specialHunterHit(Player player, GameBoard board, int dmg) {}

    public char getCharacter() {
        return character;
    }

    public void setCharacter(char character) {
        this.character = character;
    }

    public abstract boolean isDead();

    public String toString() {
        return String.valueOf(character);
    }
}
