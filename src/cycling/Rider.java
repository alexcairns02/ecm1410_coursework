package cycling;

public class Rider {
    /*TODO Variables:
    riderID (int)
        ???????????????????????????????????
    name (String)
    yearOfBirth (int)
    stageResults (dictionary thing?)
            ?adjustedElapsedTime (dictionary thing?)
    ranks (dictionary thing?)
            ?adjustedElapsedTime (dictionary thing?)
    points (dictionary thing?)
    mountainPoints (dictionary thing?)
    raceResults (dictionary thing?)
    points (dictionary thing?)
    mountainPoints(dictionary thing?)
    generalClassificationRank (dictionary thing?)
    pointsClassificationRank (dictionary thing?)
    mountainPointsClassificationRank (dictionary thing?)
        ???????????????????????????????????
    */

    private static int noOfRiders = 0;

    private int id;
    private String name;
    private int yearOfBirth;

    Rider(String name, int yearOfBirth) {
        this.name = name;
        this.yearOfBirth = yearOfBirth;
        id = noOfRiders++;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }
}
