package view.parser;

import game.Level;
import game.board.GameBoard;
import game.callbacks.MessageCallback;
import game.tiles.Tile;
import game.tiles.board_component.Empty;
import game.tiles.board_component.Wall;
import game.tiles.units.enemies.Monster;
import game.tiles.units.enemies.Trap;
import game.tiles.units.player.Player;
import game.utils.Position;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class FileParser {
    private final TileFactory tileFactory;
    private final MessageCallback messageCallback;
    private final InputStream inputProvider;

    public FileParser(TileFactory tileFactory, MessageCallback messageCallback, InputStream inputProvider) {
        this.tileFactory = tileFactory;
        this.messageCallback = messageCallback;
        this.inputProvider = inputProvider;
    }

    public Level parseLevel(File file) {
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            int rows = lines.size();
            int cols = lines.get(0).length();
            Tile[][] board = new Tile[rows][cols];
            List<Tile> monsters = new ArrayList<>();
            List<Tile>  traps = new ArrayList<>();
            Tile player = null;

            for (int i = 0; i < rows; i++) {
                String line = lines.get(i);
                for (int j = 0; j < cols; j++) {
                    char c = line.charAt(j);
                    Tile newTile = tileFactory.create(c, i, j);
                    if (c == '@') {
                        newTile.setCharacter('@');
                        player = newTile;
                    }
                    if (c == 'Q' || c == 'B' || c == 'D') {
                        traps.add(newTile);
                    }
                    if (c == 's' || c == 'k' || c == 'q' || c == 'b' || c == 'z' || c == 'g' || c == 'w' || c == 'M' || c == 'C' || c == 'K') {
                        monsters.add(newTile);
                    }
                    board[i][j] = newTile;
                }
            }

            GameBoard gameBoard = new GameBoard(board);
            messageCallback.send("Loaded level: " + file.getName());
            return new Level(gameBoard, messageCallback, inputProvider, player, monsters, traps);

        } catch (IOException e) {
            messageCallback.send("Failed to load level: " + file.getName());
            return null;
        }
    }
}
