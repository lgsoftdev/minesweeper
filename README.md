## Project: Minesweeper

A simplified version of the Minesweeper game.

### Features

- The game can randomly generate 10 mines in a 10x10 grid. Grid is represented in a 2D array.
- The user can enter coordinates (eg. 0,2) of a square tile (cell) to check a location for a mine.
- Grid is redrawn after every guess revealing the number of mines surrounding the tile.
- If the user selects a mine, the game responds "BOOM!" and the game is lost.
- If every non-mine tile has been revealed, the game is won.
- Grid is drawn to the console after every user command.

### Bonus

- User can configure number of mines and grid size via command line.
- Discovering an empty square will reveal all empty square tiles around it, and cascade into other nearby empty squares.
