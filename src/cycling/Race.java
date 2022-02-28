package cycling;

import java.util.ArrayList;

public class Race {
    /*TODO Variables:
    raceID (int)
    raceName (str)
    raceDescription (str)
    noOfStages (int)
    stages ([] or <>)
    teams ([] or <>)
    generalClassificationTimes (dictionary thing?)
    Constructors:
    Race(str name, str description):
    Auto-gen raceID*/

    private int id;
    private String name;
    private String description;
    private int noOfStages;
    private ArrayList<Stage> stages = new ArrayList<Stage>();

    Race(String name, String description, int noOfStages) {
        this.name = name;
        this.description = description;
        this.noOfStages = noOfStages;
    }

    public void addStage(Stage stage) {

    }
}
