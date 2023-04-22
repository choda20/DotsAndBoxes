package com.example.dotsandboxes.model.classes;

public class HumanPlayer extends Player{
    /**
     * empty constructor that resets the players score to 0.
     */
    public HumanPlayer() {
        this.score = 0;
    } // empty constructor

    /**
     * this function is an implementation of the abstract play() from player
     * class, however it is not used since a move is made through the UI and
     * this class has no access to it.
     * @return a throwaway move since it is not used
     */
    public ModelLine makeMove() {
        return null;} // quiet the compiler
}
