package model;

// The Game Class represents the game itself. It provides behaviours of the game including winning and losing.

public class Game {
    public boolean hasWon;
    public boolean alphaCrash;
    public boolean movesCrash;
    public boolean interfereCrash;

    // EFFECTS: create a game and set the game status to neither winning nor losing.
    public Game() {
        hasWon = false;
        alphaCrash = false;
        movesCrash = false;
        interfereCrash = false;
    }

    // MODIFIES: this
    // EFFECTS: change the field hasWon to true
    public void win() {
        this.hasWon = true;
    }

    // MODIFIES: this
    // EFFECTS: change the field alphaCrash to true
    public void crashBecauseAlpha() {
        this.alphaCrash = true;
    }

    // MODIFIES: this
    // EFFECTS: change the field movesCrash to true
    public void crashBecauseRunOutOfMoves() {
        this.movesCrash = true;
    }

    // MODIFIES: this
    // EFFECTS: change the field interfereCrash to true
    public void crashBecauseInterference() {
        this.interfereCrash = true;
    }

    public boolean getHasWon() {
        return this.hasWon;
    }

    public boolean getAlphaCrash() {
        return this.alphaCrash;
    }

    public boolean getMovesCrash() {
        return this.movesCrash;
    }

    public boolean getInterfereCrash() {
        return this.interfereCrash;
    }
}
