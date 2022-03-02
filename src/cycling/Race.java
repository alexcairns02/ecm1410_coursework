package cycling;

import java.util.ArrayList;

public class Race {
    /*TODO Variables:
    generalClassificationTimes (dictionary thing?)*/

    //Keeps track of no. of instances of Race so that IDs can be automatically generated
    private static int numberOfRaces = 0;

    private int id;
    private String name;
    private String description;
    private int noOfStages = 0;
    private ArrayList<Stage> stages = new ArrayList<Stage>();
    private ArrayList<Team> teams = new ArrayList<Team>();

    public Race(String name, String description) {
        this.name = name;
        this.description = description;
        id = numberOfRaces++;
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
        String details = "ID: "+id+" | Name: "+name+" | Description: "+description+" | No. of Stages: "+noOfStages+" | Total Length: "+totalLength;
        return details;
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
        for (Stage stage : stages) {
            totalLength += stage.getLength();
        }
        return totalLength;
    }
}
