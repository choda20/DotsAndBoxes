package com.example.dotsandboxes.model.classes;

public abstract class Player {
    protected String name; // the player's name
    protected int score; // the player's score

    /**
     * empty constructor that resets the players score to 0.
     */
    public Player() {
        score = 0;
    } // constructor

    // general getters
    public String getName() {return name;}
    public int getScore() {return score;}

    // general setters
    public void setName(String name) {this.name = name;}
    public void setScore(int score) {this.score = score;}


    /**
     * abstract function that tells the player to pick a move to perform and
     * returns it
     * @return Pair<Point,LineType>, a Point that holds the x(Row),y(Column)
     * of the line that was picked as a move, and a LineType that signifies
     * which array the line is
     * in(LineType.Horizontal = horizontalLines and so on) stored in a Pair.
     */
    public abstract ModelLine makeMove();
    // abstract function that configures how the player makes a move
}
