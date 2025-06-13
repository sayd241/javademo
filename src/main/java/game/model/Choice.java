package game.model;

/**
 * Represents the possible choices in the game.
 */
public enum Choice {
    ROCK("Rock", "/assets/rock.png"),
    PAPER("Paper", "/assets/paper.png"),
    SCISSORS("Scissors", "/assets/scissors.png");

    private final String displayName;
    private final String imagePath;

    Choice(String displayName, String imagePath) {
        this.displayName = displayName;
        this.imagePath = imagePath;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getImagePath() {
        return imagePath;
    }

    /**
     * Determines if this choice beats another choice.
     * 
     * @param other The choice to compare against
     * @return true if this choice beats the other choice
     */
    public boolean beats(Choice other) {
        return (this == ROCK && other == SCISSORS) ||
               (this == SCISSORS && other == PAPER) ||
               (this == PAPER && other == ROCK);
    }
}
