package game.tiles.units.enemies;

import game.board.GameBoard;
import game.callbacks.MessageCallback;
import game.tiles.board_component.Empty;
import game.tiles.board_component.Wall;
import game.tiles.Tile;
import game.tiles.units.player.Player;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class MonsterTest {

    private Monster monster;
    private DummyMsg msg;
    private GameBoard board;
    private Player player;

    private static class DummyMsg implements MessageCallback {
        List<String> msgs = new ArrayList<>();
        public void send(String s) { msgs.add(s); }
    }

    @Before
    public void setup() {
        msg = new DummyMsg();
        monster = new Monster("Orc",10,5,50,50,'O',0,0,3,100,msg);
        player = new Player("Hero",10,5,100,100,2,0,msg);
        Tile[][] arr = new Tile[3][1];
        for(int i=0;i<3;i++) arr[i][0]=new Empty(i,0);
        arr[0][0]=monster;
        arr[2][0]=player;
        board = new GameBoard(arr);
    }

    @Test
    public void visitEmptySwapsTiles() {
        Empty empty = new Empty(1,0);
        board.setTile(1,0,empty);
        board.getTile(1,0).accept(monster,board);
        assertSame(monster,board.getTile(1,0));
    }

    @Test
    public void visitWallDoesNotMove() {
        Wall wall = new Wall(1,0);
        board.setTile(1,0,wall);
        board.getTile(1,0).accept(monster,board);
        assertSame(monster,board.getTile(0,0));
    }

    @Test
    public void visitPlayerEngagesCombat() {
        Tile pt = board.getTile(2,0);
        pt.accept(monster,board);
        assertTrue(msg.msgs.get(0).contains("Orc engaged in combat with Hero"));
    }

    @Test
    public void testChaseMovement() {
        Tile pt = board.getTile(2,0);
        monster.onTurn(pt,board);
        assertSame(monster,board.getTile(1,0));
    }

    @Test
    public void testOnTurnDeadMonsterDoesNothing() {
        monster.getHp().setAmount(0);
        Tile pt = board.getTile(2,0);
        monster.onTurn(pt,board);
        assertSame(monster,board.getTile(0,0));
    }

    @Test
    public void testDefendRandomRange() {
        for(int i=0;i<1000;i++){
            int before = monster.getHp().getAmount();
            int def = monster.defend(0);
            assertTrue(def>=0 && def<=monster.getDefense());
            assertEquals(before,monster.getHp().getAmount());
        }
    }

    @Test(expected=ArrayIndexOutOfBoundsException.class)
    public void testOnTurnAtBorderThrows() {
        Tile[][] arr = {{monster}};
        GameBoard small = new GameBoard(arr);
        monster.onTurn(monster,small);
    }
}
