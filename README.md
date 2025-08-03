# 🐉 Dungeons and Dragons – CLI Game

A single-player, multi-level Dungeons & Dragons-inspired board game implemented in Java. This project was developed as part of an Object-Oriented Software Design course assignment.

## 📜 Overview

You play as a brave adventurer trapped in a dungeon filled with enemies and traps. Your goal: survive, defeat enemies, and make it through all the levels.

## 🎮 Gameplay

- The game is played on an ASCII board.
- You choose a player class (Warrior, Mage, Rogue, or Hunter).
- Navigate the dungeon using keyboard controls.
- Battle enemies, level up, and use special abilities.

## 🧩 Game Features

### ✅ Player Classes
- **Warrior** – Tanky fighter with healing shield ability.
- **Mage** – Casts powerful spells using mana.
- **Rogue** – Fast attacker with energy-based area damage.
- **Hunter** *(Bonus)* – Long-range attacks using arrows.

Each class levels up and gains stronger stats and abilities over time.

### 💀 Enemies
- **Monsters** – Roaming AI enemies with pathfinding behavior.
- **Traps** – Stationary enemies with visibility cycles.
- **Bosses** *(Bonus)* – Special monsters with abilities.

### ⚔️ Combat System
- Turn-based combat with attack and defense rolls.
- Supports leveling up, experience gain, and special abilities.

### 🧱 Map & CLI
- Map is loaded from text files (one per level).
- Fully interactive Command Line Interface with live board updates and combat logs.
- Uses **Observer/Callback pattern** to decouple UI from logic.

## ⌨️ Controls

| Key | Action            |
|-----|-------------------|
| w   | Move up           |
| s   | Move down         |
| a   | Move left         |
| d   | Move right        |
| e   | Use special ability |
| q   | Do nothing        |

## 🗂️ Directory Structure

The game loads levels from a directory of `.txt` files passed as a command-line argument.  
Example:
```bash
java -jar hw3.jar ./levels
