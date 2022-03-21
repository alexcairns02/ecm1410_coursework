package cycling;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.io.Serializable;

/**
 * Stage class.<br>
 * Represents a single stage in the cycling competition.
 *
 * @author Joey Griffiths & Alexander Cairns
 *
 */
class Stage implements Serializable {

    /**
     * The number of instances of Stage, automatically incremented when the
     * constructor is called.<br>
     * Used for allocation of IDs.
     */
    private static int noOfStages = 0;

    /**
     * The ID of the stage.
     */
    private final int id;

    /**
     * The name of the stage.
     */
    private final String name;

    /**
     * The description of the stage.
     */
    private final String description;

    /**
     * The length of the stage.
     */
    private final double length;

    /**
     * The stage's type.<br>
     * Taken from the {@link StageType} enum.
     */
    private final StageType type;

    /**
     * An ArrayList of Segment objects contained in the stage.
     */
    private ArrayList<Segment> segments = new ArrayList<>();

    /**
     * The start time of the race.
     */
    private final LocalDateTime startTime;

    /**
     * Whether the stage is prepared (has results associated with it) or not.
     */
    private boolean prepared = false;

    /**
     * Stage class constructor.<br>
     * Assigns a name, a description, a length, a start time, a type and an
     * automatic ID using the number of instances of stage.
     *
     * @param name The name of the stage.
     * @param description The description of the stage.
     * @param length The length of the stage.
     * @param startTime The start time of the stage.
     * @param type The stage's type.
     */
    Stage(String name, String description, double length,
          LocalDateTime startTime, StageType type) {
        this.name = name;
        this.description = description;
        this.length = length;
        this.startTime = startTime;
        this.type = type;
        id = noOfStages++;
    }

    /**
     * Resets the static variable noOfStages.<br>
     * Used to reset the CyclingPortal so that IDs start from 0 again.
     */
    public static void resetNoOfStages() {
        noOfStages = 0;
    }

    /**
     * Method to get the ID of the stage.
     *
     * @return The ID of the stage.
     */
    public int getId() {
        return id;
    }

    /**
     * Method to get the name of the stage.
     *
     * @return The name of the stage.
     */
    public String getName() {
        return name;
    }

    /**
     * Method to get the description of the stage.
     *
     * @return The description of the stage.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Method to get the length of the stage.
     *
     * @return The length of the stage.
     */
    public double getLength() {
        return length;
    }

    /**
     * Method to get the stage's type.
     *
     * @return The stage's type.
     */
    public StageType getType() {
        return type;
    }

    /**
     * Method to get the start time of the stage.
     *
     * @return The start time of the stage.
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Method to get whether the stage is prepared or not.
     *
     * @return true / false (prepared / not prepared)
     */
    public boolean isPrepared() {
        return prepared;
    }

    /**
     * Method to add a Segment object to the 'segments' ArrayList.
     *
     * @param segment Segment object to be added to the stage.
     */
    public void addSegment(Segment segment) {
        segments.add(segment);
    }

    /**
     * Method to remove a Segment object from the 'segments' ArrayList.
     *
     * @param segment Segment object to be removed from the stage.
     */
    public void removeSegment(Segment segment) {
        segments.remove(segment);
    }

    /**
     * Method to get an array of all the segments in the race.
     *
     * @return An array of Segment objects stored in the stage.
     */
    public Segment[] getSegments() {
        Segment[] segmentArr = new Segment[segments.size()];
        segmentArr = segments.toArray(segmentArr);
        return segmentArr;
    }

    /**
     * Method to set the status of the stage to prepared.
     */
    public void prepare() {
        prepared = true;
    }
}
