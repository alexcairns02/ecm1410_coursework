package cycling;

public class Segment {
    /*TODO Create Segment class:
    Variables:
    segmentID (int)
    segmentLocation (double)
    segmentType (SegmentType)
    averageGradient (double)
    segmentLength (double)*/

    private static int noOfSegments = 0;

    private int id;
    private double location;
    private SegmentType type;
    private double averageGradient;
    private double length;

    Segment(double location, SegmentType type, double averageGradient, double length) {
        this.location = location;
        this.type = type;
        this.averageGradient = averageGradient;
        this.length = length;
        id = noOfSegments++;
    }

    Segment(double location, SegmentType type) {
        this.location = location;
        this.type = type;
        id = noOfSegments++;
    }

    public int getId() {
        return id;
    }

    public double getLocation() {
        return location;
    }

    public SegmentType getType() {
        return type;
    }

    public double getAverageGradient() {
        return averageGradient;
    }

    public double getLength() {
        return length;
    }
}
