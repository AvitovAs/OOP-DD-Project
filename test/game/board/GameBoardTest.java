package game.board;

import game.tiles.board_component.Empty;
import game.tiles.board_component.Wall;
import game.utils.Position;
import game.tiles.Tile;
import org.junit.Test;
import static org.junit.Assert.*;

public class GameBoardTest {

    @Test
    public void testDimensions() {
        Tile[][] arr = new Tile[3][4];
        GameBoard board = new GameBoard(arr);
        assertEquals(3, board.getHeight());
        assertEquals(4, board.getWidth());
    }

    @Test
    public void testGetTileByCoordinatesAndPosition() {
        Empty e = new Empty(1, 2);
        Tile[][] arr = new Tile[2][3];
        arr[1][2] = e;
        GameBoard board = new GameBoard(arr);

        assertSame(e, board.getTile(1, 2));

        Position pos = new Position(1, 2);
        assertSame(e, board.getTile(pos));
    }

    @Test
    public void testSetTile() {
        Empty original = new Empty(0, 0);
        Empty replacement = new Empty(0, 0);
        Tile[][] arr = new Tile[1][1];
        arr[0][0] = original;
        GameBoard board = new GameBoard(arr);

        board.setTile(0, 0, replacement);
        assertSame(replacement, board.getTile(0, 0));

        board.setTile(0, 0, null);
        assertSame(replacement, board.getTile(0, 0));
    }

    @Test
    public void testSwapUpdatesBoardAndPositions() {
        Empty e = new Empty(0, 0);
        Wall w = new Wall(1, 1);
        Tile[][] arr = new Tile[2][2];
        arr[0][0] = e;
        arr[1][1] = w;
        GameBoard board = new GameBoard(arr);

        board.swap(e, w);

        assertSame(w, board.getTile(0, 0));
        assertSame(e, board.getTile(1, 1));

        assertEquals(0, w.getPosition().getX());
        assertEquals(0, w.getPosition().getY());
        assertEquals(1, e.getPosition().getX());
        assertEquals(1, e.getPosition().getY());
    }

    @Test
    public void testFindPlayer() {
        int rows = 4, cols = 5;
        Tile[][] arr = new Tile[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                arr[i][j] = new Empty(i, j);
            }
        }

        Empty playerTile = new Empty(2, 3);
        playerTile.setCharacter('@');
        arr[2][3] = playerTile;

        GameBoard board = new GameBoard(arr);
        Tile found = board.findPlayer();

        assertNotNull("findPlayer should not return null when '@' is present", found);
        assertSame("findPlayer must return the exact tile instance", playerTile, found);
    }

    @Test
    public void testToStringShowsBoard() {
        Tile[][] arr = new Tile[1][2];
        arr[0][0] = new Empty(0, 0);
        arr[0][1] = new Wall(0, 1);

        GameBoard board = new GameBoard(arr);
        assertEquals(".#\n", board.toString());
    }
}
