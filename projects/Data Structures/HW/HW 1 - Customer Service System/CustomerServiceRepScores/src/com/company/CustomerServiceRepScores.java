package com.company;

import java.util.Arrays;

/**
 * This class simulates a customer service representative scoring system that will keep track of the scores received for each
 * representative given by clients. It will also notify the user when a representative's score has dipped bellow 2.5.
 *
 * Created by Judah Diament (methods and fields from slides) and Avraham (Avi) Amon (all new methods and fields) on 9/15/2016.
 */
public class CustomerServiceRepScores
{
    private int repQuantity;
    private int numberOfPossibleScores;
    private int numberOfRecentScores;
    private int[][] scores;
    private int[][] lastRecentScores;
    private double[] averages;

    /**
     * Constructs a CustomerServiceRepScores object that will allow for keeping track of scores received.
     * @param repQuantity The number of representatives we want to keep track of.
     * @param scoreQuantity The highest possible we want a user to receive. Lowest is 1.
     * @param numberOfRecentScores The number of recent scores we want to keep track of.
     */
    public CustomerServiceRepScores(int repQuantity, int scoreQuantity, int numberOfRecentScores)
    {
        this.repQuantity = repQuantity;
        this.numberOfPossibleScores = scoreQuantity;
        this.numberOfRecentScores = numberOfRecentScores;
        this.scores = new int[this.repQuantity][this.numberOfPossibleScores];
        this.lastRecentScores = new int[this.repQuantity][this.numberOfRecentScores];
        this.averages = new double[repQuantity];

        //initialize all score counts to zero
        for(int i = 0; i < this.scores.length; i++)
        {
            Arrays.fill(this.scores[i],0);
            Arrays.fill(this.lastRecentScores[i],0);
        }
    }

    //Test cases to ensure the program works.
    public static void main(String[] args) {
        CustomerServiceRepScores c = new CustomerServiceRepScores(3,5,3); //Testing with only 3 reps, scoring from 1 through 5, and 3 recent scores to keep track of instead of 20.

        // Testing for rep at index 2.
        c.addNewScore(2,3);
        c.addNewScore(2,3);
        c.addNewScore(2,1);
        c.addNewScore(2,3);
        c.addNewScore(2,3);
        c.addNewScore(2,3);
        c.addNewScore(2,3);
        c.addNewScore(2,2);
        c.addNewScore(2,1);
        System.out.println(Arrays.toString(c.getCumulativeScoreForRep(2)));
        System.out.println(c.getRepAverage(2));
        System.out.println(Arrays.toString(c.getRepAverageAndCumulativeScores(2)));

        //Testing for rep at index 0.
        c.addNewScore(0,5);
        c.addNewScore(0,2);
        c.addNewScore(0,3);
        c.addNewScore(0,1);
        c.addNewScore(0,3);
        c.addNewScore(0,3);
        c.addNewScore(0,3);
        c.addNewScore(0,2);
        c.addNewScore(0,2);
        c.addNewScore(0,1);
        System.out.println(Arrays.toString(c.getCumulativeScoreForRep(0)));
        System.out.println(c.getRepAverage(0));
        System.out.println(Arrays.toString(c.getRepAverageAndCumulativeScores(0)));

        //Testing for clearing a reps scores and average. Using rep at index 2 for testing.
        c.resetRepAverageAndCumulativeScores(2);

        //Testing for clearing scores and averages of all reps. Only rep at index 0 should have anything, but the method still works.
        c.resetAllRepAveragesAndCumulativeScores();

        System.out.println(Arrays.toString(c.getRepAverageAndCumulativeScores(2)));
        System.out.println(Arrays.toString(c.getRepAverageAndCumulativeScores(0)));
    }

    /**
     * Add a new score for the representative. Score will also be added to record of recent scores,
     * averages will automatically be calculated, and message will pop up if rep's score drops bellow
     * 2.5.
     * @param repID the representative who received this score.
     * @param score the score received
     */
    public void addNewScore(int repID, int score)
    {
        try{
            this.scores[repID][score-1] += 1;
            double previousAverage = getRepAverage(repID);
            changeLastRecentScores(this.lastRecentScores[repID], score);
            getNewLastRecentAverage(this.lastRecentScores[repID], repID);
            double newAverage = getRepAverage(repID);
            checkBadScore(previousAverage, newAverage, repID);
        }
        catch(IndexOutOfBoundsException e){
            System.out.println("Please enter a number between 1 and 5");
        }
        catch(Exception e){
            System.out.println("Please enter a number between 1 and 5");
        }
    }
    /**
     * Gets a copy of the current scores that a representative has received up to this point.
     * @param repID the id of the rep
     * @return an array of length this.numberOfPossibleScores with the current score totals for the rep
     */
    public int[] getCumulativeScoreForRep(int repID)
    {
        return Arrays.copyOf(this.scores[repID], this.scores[repID].length);

    }

    /**
     * Gets the averages of a specific rep.
     * @param repID The representative who's average we want.
     * @return The average of the representative.
     */
    public double getRepAverage(int repID){
        return this.averages[repID];
    }

    /**
     * Recalculates the representative's average based on rep's newly entered score.
     * @param lastRecentRep The inner array of the lastRecentScores array for a specific representative.
     * @param repID The representative who's average we want to change.
     */
    private void getNewLastRecentAverage(int[] lastRecentRep, int repID) {
        double sum = 0.0;
        int numberOfNonZeros = 0; // Keeps track of the number of non-zero scores. If we reach
        // a score that is zero, we know we have reached the last score.
        for (int score : lastRecentRep) {
            if (score == 0) {
                break; // We only want to get an average if number of scores entered == the numberOfRecentScores field.
            }
            sum += score;
            numberOfNonZeros++;
        }
        if (numberOfNonZeros != lastRecentRep.length) {
            this.averages[repID] = 0; // No average as 20 scores have yet to be entered.
        } else {
            this.averages[repID] = sum / numberOfNonZeros; // Gives us the average if number of scores entered == the numberOfRecentScores field.
        }
    }

    /**
     * Adds the newly added score to the lastRecentScores array by pushing off the oldest value and adding the newest value
     * to the head of the array.
     * @param lastRecentRep The inner array of the lastRecentScores array for a specific representative.
     * @param score The score the representative received.
     */
    private void changeLastRecentScores(int[] lastRecentRep, int score) {
        int lastRecentLength = lastRecentRep.length;
        for(int i = lastRecentLength - 1; i > 0; i--) {
            lastRecentRep[i] = lastRecentRep[i - 1];
        }
        lastRecentRep[0] = score;
    }

    /**
     * Checks the previous and new averages after a score was added to print a warning if the representative's score has
     * fallen below 2.5. Message will only be printed once until user's average goes above and then again below 2.5.
     * @param previousAverage The representative's previous average.
     * @param newAverage The representative's new average.
     * @param repID The representative who's averages we want to compare.
     */
    private void checkBadScore(double previousAverage, double newAverage, int repID){
        if((newAverage < 2.5 && previousAverage >= 2.5) || (newAverage < 2.5 && previousAverage == 0) && newAverage != 0){
            System.out.println("Rep " + repID  + "’s running average has dropped to " + newAverage + ".");
        }
    }

    /**
     * Get's both the representative's current cumulative scores, as well as their current average.
     * Data will be returned together in one array where the last index is the average, and all of the previous ones
     * correspond to the current cumulative scores of the rep.
     * @param repID The representative who's average and cumulative scores we want.
     * @return An array containing the cumulative scores followed by the average in the last index.
     */
    public double[] getRepAverageAndCumulativeScores(int repID){
        double[] cumulativeAndAverage = new double[numberOfPossibleScores + 1]; // First 5 store the cumulative scores, and the last index stores the average.
        for(int i = 0; i < scores[repID].length; i++){
            cumulativeAndAverage[i] = scores[repID][i];
        }
        cumulativeAndAverage[numberOfPossibleScores] = getRepAverage(repID);
        return cumulativeAndAverage;
    }

    /**
     * Resets the representative's current cumulative scores and average.
     * @param repID The representative who's data we want to reset.
     */
    public void resetRepAverageAndCumulativeScores(int repID){
        Arrays.fill(this.scores[repID], 0);
        this.averages[repID] = 0;
    }

    /**
     * Resets all representatives' current cumulative scores and averages.
     */
    public void resetAllRepAveragesAndCumulativeScores(){
        for(int i = 0; i < this.repQuantity; i++) {
            Arrays.fill(this.scores[i], 0);
            this.averages[i] = 0;
        }
    }
}