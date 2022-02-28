package cycling;

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
    private int id;
    private String name;
    private String description;
    private double length;
    private StageType type;

    Stage() {
        return;
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
}
