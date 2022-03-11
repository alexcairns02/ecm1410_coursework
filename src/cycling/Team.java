package cycling;

import java.util.ArrayList;

public class Team {
    private static int noOfTeams = 0;

    private int id;
    private String name;
    private String description;
    private ArrayList<Rider> riders = new ArrayList<>();

    Team(String name, String description) {
        this.name = name;
        this.description = description;
        id = noOfTeams++;
    }

    public static void resetNoOfTeams() {
        noOfTeams = 0;
    }

    public void addRider(Rider rider) {
        riders.add(rider);
    }

    public void removeRider(Rider rider) {
        riders.remove(rider);
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

    public Rider[] getRiders() {
        Rider[] riderArr = new Rider[riders.size()];
        riderArr = riders.toArray(riderArr);
        return riderArr;
    }
}
