package game.board;
import game.tiles.Tile;
import game.tiles.board_component.Empty;
import game.tiles.board_component.Wall;
import game.tiles.units.enemies.Enemy;
import game.utils.Position;

public class GameBoard { // This is for passive management of the game
    private Tile[][] board;

    public GameBoard(Tile[][] gameboard) {
        this.board = gameboard;
    }

    public Tile getTile(int row, int col) {
        return board[row][col];
    }

    public Tile getTile(Position p){ return board[p.getX()][p.getY()]; }

    public void setTile(int row, int col, Tile tile) {
        if (tile != null) {
            board[row][col] = tile;
        }
    }

    /// Swap t1 and t2 tiles positions on the board
    public void swap(Tile t1, Tile t2) {
        Position p1 = t1.getPosition();
        Position p2 = t2.getPosition();

        board[p1.getX()][p1.getY()] = t2;
        board[p2.getX()][p2.getY()] = t1;

        t1.setPosition(new Position(p2.getX(), p2.getY()));
        t2.setPosition(new Position(p1.getX(), p1.getY()));
    }


    public Tile[][] getBoard() {
        return board;
    }

    public int getHeight() { // Rows length
        return board.length;
    }

    public int getWidth() { // Column length
        return board[0].length;
    }

    /// Finds the player Tile on the board based on its character(@), used mainly for debugging or testing
    public Tile findPlayer() {
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                if (board[i][j].getCharacter() == '@') {
                    return board[i][j];
                }
            }
        }
        return null; // Should never happen if the player is always present!
    }

    public void enemyDied(Enemy enemy) {
        int x = enemy.getPosition().getX();
        int y = enemy.getPosition().getY();
        board[x][y] = new Empty(x, y);
        enemy.setCharacter('.'); // needed for the special abilities
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Tile[] tiles : board) {
            for (Tile tile : tiles) {
                sb.append(tile.toString());
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
