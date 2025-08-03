package view;

import game.Level;
import game.callbacks.MessageCallback;
import game.tiles.Tile;
import game.tiles.units.player.*;
import game.utils.Position;
import view.parser.FileParser;
import view.parser.TileFactory;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class GameRunner {
    private final TileFactory tileFactory = new TileFactory(this :: sendMessage);
    private List<Level> levelList;
    private Tile playerUnit;

    public GameRunner() {
        this.playerUnit = null;
    }

    public void initialize(String levelsDirectory) {
        // callback function to show messages
        MessageCallback mcb = this::sendMessage;

        // initialize file parser
        FileParser parser = new FileParser(tileFactory, mcb, System.in);

        File root = new File(levelsDirectory);
        levelList = Arrays.stream(Objects.requireNonNull(root.listFiles()))
                .sorted(Comparator.comparing(File::getName))
                .map(parser::parseLevel)
                .collect(Collectors.toList());

        // After parsing the level and obtaining player position we can choose the player character:
        Level level = levelList.get(0);
        Player player = choosePlayer(level.getPlayerStartPosition(), this::sendMessage);
        level.setPlayer(player);
    }

    public void start() {
        Tile player = null;
        int levelCounter = 0;
        boolean wonGame = false;
        for (Level currentLevel : levelList) {
            currentLevel.start(); // assume Level handles the game loop
            if (levelCounter > 0){ // relevant for after the first level as we need to update the player position
                Position startPos = currentLevel.getPlayerStartPosition();
                player.setPosition(startPos); // Update the player position to the new board
                currentLevel.setPlayer(player);
            }
            while (!currentLevel.won() && !currentLevel.lost()) {
                sendMessage(currentLevel.toString());
                currentLevel.processRound(); // player and enemies act
            }
            if (currentLevel.lost()) { // player lost, exit game
                break;
            }
            player = currentLevel.getPlayerTile();
            levelCounter++;
            if (levelCounter == levelList.size() && !currentLevel.lost()) {
                wonGame = true;
            }
        }

        if (wonGame) {
            sendMessage("Congratulations! You have completed all levels and won the game!");
        }
    }

    public void sendMessage(String msg) {
        System.out.println(msg);
    }

    private Player choosePlayer(Position start, MessageCallback cb) {
        printAvailablePlayers(cb);
        Scanner sc = new Scanner(System.in);
        Player player = null;
        while (player == null) {
            cb.send("Enter a number (1-7):");
            String cmd = sc.nextLine();
            try {
                int choice = Integer.parseInt(cmd);
                switch (choice) {
                    case 1 -> player = new Warrior("Jon Snow",    30, 4, 300, 300, start.getX(), start.getY(), cb, 3);
                    case 2 -> player = new Warrior("The Hound",    20, 6, 400, 400, start.getX(), start.getY(), cb, 5);
                    case 3 -> player = new Mage   ("Melisandre",    5, 1, 100, 100, start.getX(), start.getY(), cb, 300, 30, 15, 5, 6);
                    case 4 -> player = new Mage   ("Thoros of Myr", 25, 4, 250, 250, start.getX(), start.getY(), cb, 150, 20, 20, 3, 4);
                    case 5 -> player = new Rogue  ("Arya Stark",    40, 2, 150, 150, start.getX(), start.getY(), cb, 20);
                    case 6 -> player = new Rogue  ("Bronn",         35, 3, 250, 250, start.getX(), start.getY(), cb, 50);
                    case 7 -> player = new Hunter ("Ygritte",       30, 2, 220, 220, start.getX(), start.getY(), cb, 6);
                    default -> cb.send("Invalid input.");
                }
            } catch (NumberFormatException e) {
                cb.send("Invalid input, try again.");
            }
        }
        return player;
    }

    private void printAvailablePlayers(MessageCallback cb) {
        String msg = """
    1. Jon Snow (Warrior)     - Health: 300, Attack: 30, Defense: 4,  Ability Cooldown: 3
    2. The Hound (Warrior)     - Health: 400, Attack: 20, Defense: 6,  Ability Cooldown: 5
    3. Melisandre (Mage)       - Health: 100, Attack: 5,  Defense: 1,  Mana Pool: 300, Cost: 30, Spell Power: 15, Hits: 5, Range: 6
    4. Thoros of Myr (Mage)    - Health: 250, Attack: 25, Defense: 4,  Mana Pool: 150, Cost: 20, Spell Power: 20, Hits: 3, Range: 4
    5. Arya Stark (Rogue)      - Health: 150, Attack: 40, Defense: 2,  Energy Cost: 20
    6. Bronn (Rogue)           - Health: 250, Attack: 35, Defense: 3,  Energy Cost: 50
    7. Ygritte (Hunter)        - Health: 220, Attack: 30, Defense: 2,  Range: 6
    """;
        cb.send("Please choose from the available characters:\n" + msg);
    }
}
