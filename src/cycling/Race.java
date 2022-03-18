package cycling;

import java.util.ArrayList;
import java.io.Serializable;

class Race implements Serializable {
    
    private static int numberOfRaces = 0;

    private int id;
    private String name;
    private String description;
    private int noOfStages = 0;
    private ArrayList<Stage> stages = new ArrayList<Stage>();
    private ArrayList<Team> teams = new ArrayList<Team>();

    Race(String name, String description) {
        this.name = name;
        this.description = description;
        id = numberOfRaces++;
    }

    public static void resetNoOfRaces() {
        numberOfRaces = 0;
    }

    public void addStage(Stage stage) {
        stages.add(stage);
        noOfStages++;
    }

    public void removeStage(Stage stage) {
        stages.remove(stage);
        noOfStages--;
    }

    public void addTeam(Team team) {
        teams.add(team);
    }

    public String getDetails() {
        /*Formatted string containing the race ID, name, description, the
	    number of stages, and the total length (i.e., the sum of all stages'
	    length).*/
        double totalLength = getTotalLength();
        return "ID: "+id+" | Name: "+name+" | Description: "+description
                +" | No. of Stages: "+noOfStages+" | Total Length: "+totalLength;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return description;
    }

    public int getNoOfStages() {
        return noOfStages;
    }

    public Stage[] getStages() {
        Stage[] stageArr = new Stage[stages.size()];
        stageArr = stages.toArray(stageArr);
        return stageArr;
    }

    private double getTotalLength() {
        double totalLength = 0;
        double length;
        for (Stage stage : stages) {
            length = stage.getLength();
            assert (length >= 0);
            totalLength += length;
        }
        return totalLength;
    }
}
