package cycling;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Team class.<br>
 * Represents a team in the cycling competition.
 * 
 * @author Joey Griffiths & Alexander Cairns
 */
class Team implements Serializable {

    /**
     * The number of instances of Team, automatically incremented when
     * the constructor is called.<br>
     * Used for allocation of IDs.
     */
    private static int noOfTeams = 0;

    /**
     * The ID of the team.
     */
    private final int id;

    /**
     * The name of the team.
     */
    private final String name;

    /**
     * The team's description.
     */
    private final String description;

    /**
     * An ArrayList of Rider objects contained in the team.
     */
    private ArrayList<Rider> riders = new ArrayList<>();

    /**
     * Team class constructor.<br>
     * Assigns a name, a description and an automatic ID using the number of
     * isntances of team.
     *
     * @param name The team's name.
     * @param description The team's description.
     */
    Team(String name, String description) {
        this.name = name;
        this.description = description;
        id = noOfTeams++;
    }

    /**
     * Resets the static variable noOfTeams.<br>
     * Used to reset the CyclingPortal so that IDs start from 0 again.
     */
    public static void resetNoOfTeams() {
        noOfTeams = 0;
    }

    /**
     * Method to add a rider to the team.
     *
     * @param rider The Rider object to be added to the 'riders' ArrayList.
     */
    public void addRider(Rider rider) {
        riders.add(rider);
    }

    /**
     * Method to remove a rider from the team.
     *
     * @param rider The Rider object to be removed from the 'riders' ArrayList.
     */
    public void removeRider(Rider rider) {
        riders.remove(rider);
    }

    /**
     * Method to get the ID of the team.
     *
      * @return The team's ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Method to get the name of the team.
     *
     * @return The team's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Method to get the description of the team.
     *
     * @return The team's description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Method to get a list of all riders in the team.
     *
     * @return An array of Rider objects contained in the team.
     */
    public Rider[] getRiders() {
        Rider[] riderArr = new Rider[riders.size()];
        riderArr = riders.toArray(riderArr);
        return riderArr;
    }
}
