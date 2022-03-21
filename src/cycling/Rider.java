package cycling;

import java.util.ArrayList;
import java.io.Serializable;

/**
 * Rider class.<br>
 * Represents a single rider in the cycling competition.
 *
 * @author Joey Griffiths & Alexander Cairns
 *
 */
class Rider implements Serializable {

    /**
     * The number of instances of Rider, automatically incremented when the
     * constructor is called.<br>
     * Used for allocation of IDs.
     */
    private static int noOfRiders = 0;

    /**
     * The ID of the Rider.
     */
    private final int id;

    /**
     * The name of the rider.
     */
    private final String name;

    /**
     * The year of birth of the rider.
     */
    private final int yearOfBirth;

    /**
     * The ArrayList of StageResult objects that the rider contains.
     */
    private ArrayList<StageResult> results = new ArrayList<>();

    /**
     * Rider class constructor.<br>
     * Assigns a name, year of birth and automatically assigns an ID using
     * the number of instances of rider.
     *
     * @param name The name of the rider.
     * @param yearOfBirth The year of birth of the rider.
     */
    Rider(String name, int yearOfBirth) {
        this.name = name;
        this.yearOfBirth = yearOfBirth;
        id = noOfRiders++;
    }

    /**
     * Method to reset the static variable noOfRiders.<br>
     * Used to reset the CyclingPortal so that IDs start from 0 again.
     */
    public static void resetNoOfRiders() {
        noOfRiders = 0;
    }

    /**
     * Method to get the ID of the rider.
     *
     * @return The ID of the rider.
     */
    public int getId() {
        return id;
    }

    /**
     * Method to get the name of the rider.
     *
     * @return The name of the rider.
     */
    public String getName() {
        return name;
    }

    /**
     * Method to get the year of birth of the rider.
     *
     * @return The year of birth of the rider.
     */
    public int getYearOfBirth() {
        return yearOfBirth;
    }

    /**
     * Method to add a StageResult object to the 'results' ArrayList.
     *
     * @param result StageResult object representing the result that the
     *               rider achieved in a stage.
     */
    public void addResult(StageResult result) {
        results.add(result);
    }

    /**
     * Method to remove a StageResult object from the 'results' ArrayList.
     *
     * @param result StageResult object representing the result that the
     *               rider achieved in a stage.
     */
    public void removeResult(StageResult result) {
        results.remove(result);
    }

    /**
     * Method to get an array of all the StageResult objects stored in the
     * 'results' ArrayList.
     *
     * @return An array of StageResult objects stored in the 'results'
     * ArrayList.
     */
    public StageResult[] getResults() {
        StageResult[] resultArr = new StageResult[results.size()];
        resultArr = results.toArray(resultArr);
        return resultArr;
    }
}
