# 2048 Game

A desktop recreation of the classic **2048 puzzle game**, built in Java using the Swing graphical user interface library.

The goal is to combine matching numbered tiles until you create the **2048 tile**. The game includes score tracking, saving and loading, restarting, and win or game-over detection.

## Features

* Classic 4×4 2048 game board
* Keyboard controls using the arrow keys
* Automatic tile movement and merging
* Score tracking
* Random generation of 2 and 4 tiles
* Win detection when the 2048 tile is created
* Game-over detection when no moves remain
* Save the current game to a file
* Load a previously saved game
* Restart the game at any time
* Graphical interface created with Java Swing

## Controls

| Key or Button | Action                           |
| ------------- | -------------------------------- |
| Up Arrow      | Move tiles upward                |
| Down Arrow    | Move tiles downward              |
| Left Arrow    | Move tiles left                  |
| Right Arrow   | Move tiles right                 |
| Save Game     | Save the current board and score |
| Load Game     | Load a saved board and score     |
| Restart Game  | Begin a new game                 |
| Exit          | Close the application            |

## How to Play

1. Use the arrow keys to move every tile on the board.
2. When two tiles with the same number touch, they merge into one tile.
3. The merged tile is added to your score.
4. A new tile appears after each valid move.
5. Continue combining tiles until you create the 2048 tile.
6. The game ends when the board is full and no more moves are possible.

## Requirements

* Java Development Kit, JDK 8 or newer
* A Java IDE such as IntelliJ IDEA, Eclipse, or Visual Studio Code

## Project Structure

```text
2048-Game/
├── images/
│   ├── iconLogo.jpg
│   ├── icon2.jpg
│   ├── icon4.jpg
│   ├── icon8.jpg
│   ├── ...
│   └── icon2048.jpg
├── Game2048.java
├── Game2048GUI.java
├── Game2048Listener.java
└── README.md
```

### Main Files

* `Game2048.java` contains the game board, movement, merging, scoring, win detection, game-over detection, and save/load logic.
* `Game2048GUI.java` creates and updates the Swing graphical interface.
* `Game2048Listener.java` handles keyboard controls and button actions.
* `images/` contains the logo and numbered tile images used by the interface.

## Running the Game

### Using IntelliJ IDEA

1. Open the project folder in IntelliJ IDEA.
2. Make sure a Java JDK is selected for the project.
3. Open `Game2048GUI.java`.
4. Run the `main` method inside `Game2048GUI`.

### Using the Command Line

Open a terminal inside the project folder and compile the files:

```bash
javac Game2048.java Game2048GUI.java Game2048Listener.java
```

Run the game:

```bash
java Game2048GUI
```

The `images` folder must remain in the same directory as the compiled program so the tile and logo images can be loaded correctly.

## Saving and Loading

Select **Save Game** and enter a file name to save the current score and board.

To continue a saved game, select **Load Game** and enter the same file name. The saved file must be accessible from the directory where the program is running.

## Technologies Used

* Java
* Java Swing
* Java AWT
* Object-oriented programming
* File input and output
* Event listeners
* Two-dimensional arrays

## Possible Future Improvements

* Add movement animations
* Store and display a high score
* Add an undo button
* Add customizable board sizes
* Add keyboard shortcuts for saving and restarting
* Improve the tile design and interface
* Package the game as an executable JAR file

## License

This project was created for educational purposes.
