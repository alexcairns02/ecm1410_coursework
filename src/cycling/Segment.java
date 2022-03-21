package cycling;

import java.io.Serializable;

/**
 * Segment class. Represents a single segment in the cycling competition.
 *
 * @author Joey Griffiths & Alexander Cairns
 *
 */
class Segment implements Serializable {

    /**
     * The number of instances of Segment, automatically incremented when the
     * constructor is called. Used for allocation of IDs.
     */
    private static int noOfSegments = 0;

    /**
     * The ID of the segment.
     */
    private final int id;

    /**
     * The location (distance from the start of the stage) the segment is
     * found at.
     */
    private final double location;

    /**
     * The type that this segment is.<br>
     * Taken from the {@link SegmentType} enum.
     */
    private final SegmentType type;

    /**
     * The average gradient of the segment.
     */
    private double averageGradient;

    /**
     * The length of the segment, in km.
     */
    private double length;

    /**
     * Segment class constructor.<br>
     * Assigns a location, a type, an average gradient, a length, and an
     * automatic ID using the number of instances of Segment.
     *
     * @param location The location of the segment in the stage.
     * @param type The type that this segment is.
     * @param averageGradient The average gradient of the segment.
     * @param length The length of the segment.
     */
    Segment(double location, SegmentType type, double averageGradient, double length) {
        this.location = location;
        this.type = type;
        this.averageGradient = averageGradient;
        this.length = length;
        id = noOfSegments++;
    }

    /**
     * Segment class constructor.<br>
     * Assigns a location, a type, and an automatic ID using the number of
     * instances of Segment.
     *
     * @param location The location of the segment in the stage.
     * @param type The type that this segment is.
     */
    Segment(double location, SegmentType type) {
        this.location = location;
        this.type = type;
        id = noOfSegments++;
    }

    /**
     * Method to reset the static variable noOfSegments. Used to reset the
     * CyclingPortal so that IDs start from 0 again.
     */
    public static void resetNoOfSegments() {
        noOfSegments = 0;
    }

    /**
     * Method to get the ID of the segment.
     *
     * @return The ID of the segment.
     */
    public int getId() {
        return id;
    }

    /**
     * Method to get the location of the segment in the stage.
     *
     * @return The location of the segment.
     */
    public double getLocation() {
        return location;
    }

    /**
     * Method to get the type that the segment is.
     *
     * @return The type of the segment.
     */
    public SegmentType getType() {
        return type;
    }

    /**
     * Method to get the average gradient of the segment.
     *
     * @return The averageGradient of the stage.
     */
    public double getAverageGradient() {
        return averageGradient;
    }

    /**
     * Method to get the length of the segment.
     *
     * @return The length of the segment.
     */
    public double getLength() {
        return length;
    }
}
