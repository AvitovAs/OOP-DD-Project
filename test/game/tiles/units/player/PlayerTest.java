package game.tiles.units.player;

import game.board.GameBoard;
import game.callbacks.MessageCallback;
import game.tiles.board_component.Empty;
import game.tiles.board_component.Wall;
import game.tiles.Tile;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class PlayerTest {

    private Player player;
    private DummyMsg msg;
    private GameBoard board;

    private static class DummyMsg implements MessageCallback {
        List<String> msgs = new ArrayList<>();
        public void send(String s) { msgs.add(s); }
    }

    @Before
    public void setup() {
        msg = new DummyMsg();
        player = new Player("Hero",5,3,100,100,0,0,msg);
        Tile[][] arr = new Tile[2][2];
        arr[0][0] = player;
        for(int i=0;i<2;i++) for(int j=0;j<2;j++)
            if(arr[i][j]==null) arr[i][j]=new Empty(i,j);
        board = new GameBoard(arr);
    }

    @Test
    public void testAddExperienceLevelUp() {
        int req = player.getReqExp();
        player.addExperience(req);
        assertEquals(2,player.getLevel());
        assertEquals(0,player.getExperience());
        assertEquals(120,player.getHp().getCapacity());
        assertEquals(120,player.getHp().getAmount());
        assertEquals(5+(4*2),player.getAttack());
        assertEquals(3+(1*2),player.getDefense());
    }

    @Test
    public void testIsDeadAndCharacterChange() {
        player.getHp().setAmount(1);
        assertFalse(player.isDead());
        player.getHp().setAmount(0);
        assertTrue(player.isDead());
        assertEquals('X',player.getCharacter());
    }

    @Test
    public void testDefendZeroDamage() {
        int before = player.getHp().getAmount();
        int def = player.defend(0);
        assertTrue(def>=0 && def<player.getDefense());
        assertEquals(before,player.getHp().getAmount());
    }

    @Test
    public void testVisitEmptySwapsTile() {
        Empty empty = new Empty(1,0);
        board.setTile(1,0,empty);
        board.getTile(1,0).accept(player,board);
        assertSame(player,board.getTile(1,0));
    }

    @Test
    public void testVisitWallShowsMessage() {
        Wall wall = new Wall(1,0);
        board.setTile(1,0,wall);
        board.getTile(1,0).accept(player,board);
        assertEquals("Hero cant go over walls.",msg.msgs.get(0));
        assertSame(player,board.getTile(0,0));
    }

    @Test
    public void testMultipleLevelUps() {
        int req = player.getReqExp();
        player.addExperience(req*3);
        assertEquals(3,player.getLevel());
        assertEquals(0,player.getExperience());
    }

    @Test
    public void testDefendRandomRange() {
        for(int i=0;i<1000;i++){
            int before = player.getHp().getAmount();
            int def = player.defend(0);
            assertTrue(def>=0 && def<player.getDefense());
            assertEquals(before,player.getHp().getAmount());
        }
    }

    @Test(expected=IllegalArgumentException.class)
    public void testDefendWithZeroDefenseThrows() {
        Player weak = new Player("Weak",5,0,10,10,0,0,msg);
        weak.defend(5);
    }
}
