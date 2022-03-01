package cycling;

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

    Stage(String name, String description, double length, StageType type) {
        this.name = name;
        this.description = description;
        this.length = length;
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
}
