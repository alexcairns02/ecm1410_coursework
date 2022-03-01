package cycling;

import java.util.ArrayList;

public class Team {
    private static int noOfTeams = 0;

    private int id;
    private String name;
    private String description;
    private ArrayList<Rider> riders = new ArrayList<Rider>();

    Team(String name, String description) {
        this.name = name;
        this.description = description;
        id = noOfTeams++;
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
}
