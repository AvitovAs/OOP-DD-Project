package game;

import game.board.GameBoard;
import game.callbacks.MessageCallback;
import game.tiles.Tile;
import game.utils.Position;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Level {
    private final GameBoard board;
    private final MessageCallback callback;
    private final InputStream input;
    private Tile playerTile;
    private List<Tile> monstersPositions;
    private List<Tile> trapsPositions;
    private boolean isDead;

    public Level(GameBoard board, MessageCallback callback, InputStream input, Tile player, List<Tile> monsters, List<Tile> traps) {
        this.board = board;
        this.callback = callback;
        this.input = input;
        this.playerTile = player;
        this.monstersPositions = monsters;
        this.trapsPositions = traps;
        this.isDead = false;
    }

    public void start() {
        callback.send("Starting level...");
    }

    public boolean won() {
        return monstersPositions.isEmpty() && trapsPositions.isEmpty();
    }

    public boolean lost() {
        return isDead;
    }

    public void processRound() {
        playerTurn();
        proccessEnemyDeaths();
        enemyTurn();
        if (playerTile.getCharacter() == 'X') {
            callback.send("You Lost! Game Over");
            isDead = true;
        }
    }

    public void playerTurn() {
        playerTile.onTurn(board);
    }

    public void enemyTurn() {
        for (Tile monsterPos: monstersPositions) {
            monsterPos.onTurn(playerTile, board);
        }

        for (Tile trapPos: trapsPositions) {
            trapPos.onTurn(playerTile, board);
        }
    }

    public void setPlayer(Tile playerTile) {
        this.playerTile = playerTile;
        Position pos = playerTile.getPosition();
        board.setTile(pos.getX(), pos.getY(), playerTile); // place player on the board
    }

    public Tile getPlayerTile() {
        return playerTile;
    }

    public Position getPlayerStartPosition() {
        return playerTile.getPosition();
    }

    public void proccessEnemyDeaths(){
        // Checks if any enemy died this round and removes it from its list.
        List<Tile> monstersToRemove = new ArrayList<Tile>();
        List<Tile> trapsToRemove = new ArrayList<>();

        for (Tile tile : monstersPositions) {
            if (tile.isDead()) {
                monstersToRemove.add(tile);
            }
        }
        for (Tile tile : trapsPositions) {
            if (tile.isDead()) {
                trapsToRemove.add(tile);
            }
        }

        for (Tile tile : monstersToRemove) {
            monstersPositions.remove(tile);
        }
        for (Tile tile : trapsToRemove) {
            trapsPositions.remove(tile);
        }
    }

    @Override
    public String toString() {
        return board.toString();
    }
}
