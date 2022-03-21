package cycling;

import java.util.ArrayList;
import java.io.Serializable;

/**
 * Race class. Represents a single race in the cycling competition.
 * @author Joey Griffiths & Alexander Cairns
 */
class Race implements Serializable {
    
    /**
     * The number of instances of Race, automatically incremented when the
     * constructor is called. Used for allocation of IDs.
     */
    private static int numberOfRaces = 0;

    /**
     * The ID of the Race.
     */
    private final int id;

    /**
     * The name of the race.
     */
    private final String name;

    /**
     * The race description.
     */
    private String description;

    /**
     * The list of stages included in the race.
     */
    private ArrayList<Stage> stages = new ArrayList<>();

    /**
     * Race class constructor. Initialises a new race with a name and description,
     * and automatically assigns an ID using the number of instances of Race.
     * @param name The name of the race.
     * @param description The race description.
     * 
     */
    Race(String name, String description) {
        this.name = name;
        this.description = description;
        id = numberOfRaces++;
    }

    /**
     * Resets the static variable numberOfRaces. Used to reset the CyclingPortal
     * so that IDs start from 0 again.
     */
    public static void resetNoOfRaces() {
        numberOfRaces = 0;
    }

    /**
     * Adds a stage to the race.
     * @param stage A Stage object to add to the race's stages.
     */
    public void addStage(Stage stage) {
        stages.add(stage);
        assert (stages.size() > 0);
    }

    /**
     * Removes a stage from the race.
     * @param stage The Stage object to remove from the race's stages.
     */
    public void removeStage(Stage stage) {
        stages.remove(stage);
    }

    /**
     * Get the details of this particular Race.
     * @return A formatted string containing details about the race ID, name,
     *         description, number of stages, and total length.
     */
    public String getDetails() {
        double totalLength = getTotalLength();
        return "ID: "+id+" | Name: "+name+" | Description: "+description
                +" | No. of Stages: "+stages.size()
                +" | Total Length: "+totalLength;
    }

    /**
     * Method to return the ID of a race.
     * @return The ID of the race.
     */
    public int getId() {
        return id;
    }

    /**
     * Method to return the name of a race.
     * @return The name of the race.
     */
    public String getName() {
        return name;
    }

    /**
     * Method to return the description of a race.
     * @return The race description.
     */
    public String getDesc() {
        return description;
    }

    /**
     * Method to return the number of stages in a race.
     * @return The number of stages in the race.
     */
    public int getNoOfStages() {
        return stages.size();
    }

    /**
     * Method to return an array of Stage objects contained in the race.
     * @return An array of every Stage objects in the race.
     */
    public Stage[] getStages() {
        // Converts the stages ArrayList into an array
        Stage[] stageArr = new Stage[stages.size()];
        stageArr = stages.toArray(stageArr);
        return stageArr;
    }

    /**
     * Private method to compute the total length of the race, that is,
     * the sum of all the lengths of each stage in the race.
     * @return The total length of the race.
     */
    private double getTotalLength() {
        double totalLength = 0;
        double length;
        for (Stage stage : stages) {
            length = stage.getLength();
            totalLength += length;
        }
        return totalLength;
    }
}
