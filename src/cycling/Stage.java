package cycling;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.io.Serializable;

class Stage implements Serializable {
    
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

    public static void resetNoOfStages() {
        noOfStages = 0;
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

    public StageType getType() {
        return type;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public boolean isPrepared() {
        return prepared;
    }

    public void addSegment(Segment segment) {
        segments.add(segment);
    }

    public void removeSegment(Segment segment) {
        segments.remove(segment);
    }

    public Segment[] getSegments() {
        Segment[] segmentArr = new Segment[segments.size()];
        segmentArr = segments.toArray(segmentArr);
        return segmentArr;
    }

    public void prepare() {
        prepared = true;
    }
}
