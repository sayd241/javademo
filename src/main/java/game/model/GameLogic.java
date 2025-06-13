package game.model;

import game.util.Constants;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.EnumMap;
import java.util.Map;

/**
 * Handles the core game logic and AI decision making.
 */
public class GameLogic {
    private final String gameMode;
    private final String difficulty;
    private final Random random;
    private final int[] scores;
    private final List<Choice> previousMoves;
    private final Map<Choice, Integer> moveFrequency;
    
    public GameLogic() {
        this(Constants.MODE_PVC, Constants.DIFFICULTY_MEDIUM);
    }
    
    public GameLogic(String gameMode, String difficulty) {
        this.gameMode = gameMode;
        this.difficulty = difficulty;
        this.random = new Random();
        this.scores = new int[2];
        this.previousMoves = new ArrayList<>();
        this.moveFrequency = new EnumMap<>(Choice.class);
        for (Choice choice : Choice.values()) {
            moveFrequency.put(choice, 0);
        }
    }
    
    public Choice getComputerChoice() {
        Choice[] choices = Choice.values();
        
        switch (difficulty.toUpperCase()) {
            case Constants.DIFFICULTY_EASY:
                return choices[random.nextInt(choices.length)];
                
            case Constants.DIFFICULTY_MEDIUM:
                // Try not to repeat previous moves
                if (!previousMoves.isEmpty()) {
                    Choice lastMove = previousMoves.get(previousMoves.size() - 1);
                    List<Choice> availableChoices = new ArrayList<>();
                    for (Choice choice : choices) {
                        if (choice != lastMove) {
                            availableChoices.add(choice);
                        }
                    }
                    return availableChoices.get(random.nextInt(availableChoices.size()));
                }
                return choices[random.nextInt(choices.length)];
                
            case Constants.DIFFICULTY_HARD:
                // Use move frequency to make smarter choices
                Choice mostFrequentChoice = getMostFrequentChoice();
                if (mostFrequentChoice != null) {
                    // Return the choice that beats the player's most frequent choice
                    return getWinningChoice(mostFrequentChoice);
                }
                return choices[random.nextInt(choices.length)];
                
            default:
                return choices[random.nextInt(choices.length)];
        }
    }
    
    public String determineWinner(Choice playerChoice, Choice computerChoice) {
        if (playerChoice == computerChoice) {
            return "Draw!";
        }
        
        boolean playerWins = (playerChoice == Choice.ROCK && computerChoice == Choice.SCISSORS) ||
                           (playerChoice == Choice.PAPER && computerChoice == Choice.ROCK) ||
                           (playerChoice == Choice.SCISSORS && computerChoice == Choice.PAPER);
        
        // Update move frequency for AI
        moveFrequency.put(playerChoice, moveFrequency.get(playerChoice) + 1);
        previousMoves.add(playerChoice);
        
        if (playerWins) {
            scores[0]++; // Player score
            return "You Win!";
        } else {
            scores[1]++; // Computer score
            return "You Lose!";
        }
    }
    
    private Choice getMostFrequentChoice() {
        if (moveFrequency.isEmpty()) return null;
        
        Choice mostFrequent = null;
        int maxFrequency = -1;
        
        for (Map.Entry<Choice, Integer> entry : moveFrequency.entrySet()) {
            if (entry.getValue() > maxFrequency) {
                maxFrequency = entry.getValue();
                mostFrequent = entry.getKey();
            }
        }
        
        return mostFrequent;
    }
    
    private Choice getWinningChoice(Choice choice) {
        return switch (choice) {
            case ROCK -> Choice.PAPER;
            case PAPER -> Choice.SCISSORS;
            case SCISSORS -> Choice.ROCK;
        };
    }
    
    public int[] getScores() {
        return scores;
    }
    
    public void resetScores() {
        scores[0] = 0;
        scores[1] = 0;
        previousMoves.clear();
    }
}
