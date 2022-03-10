package cycling;

import java.time.LocalDateTime;
import java.util.ArrayList;

//TODO fill in document
/**

*/
public class Stage {
    /*
    TODO Variables:
    stageID (int)
    stageName (str)
    stageDescription (str)
    stageLength (double)
    stageStartTime (time?)
    stageType (StageType)
    segments ([] or <>)
    */
    private static int noOfStages = 0;

    private int id;
    private String name;
    private String description;
    private double length;
    private StageType type;
    private ArrayList<Segment> segments = new ArrayList<Segment>();
    private LocalDateTime startTime;
    private boolean prepared = false;

    Stage(String name, String description, double length,
          LocalDateTime startTime, StageType type) {
        this.name = name;
        this.description = description;
        this.length = length;
        this.startTime = startTime;
        this.type = type;
        id = noOfStages++;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getLength() {
        return length;
    }

    public void addSegment(Segment segment) {
        segments.add(segment);
    }

    public void removeSegment(Segment segment) {
        segments.remove(segment);
    }

    public StageType getType() {
        return type;
    }

    public Segment[] getSegments() {
        Segment[] segmentArr = new Segment[segments.size()];
        segmentArr = segments.toArray(segmentArr);
        return segmentArr;
    }

    public void prepare() {
        prepared = true;
    }

    public boolean isPrepared() {
        return prepared;
    }
}
