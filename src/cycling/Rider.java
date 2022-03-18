package cycling;

import java.util.ArrayList;
import java.io.Serializable;

class Rider implements Serializable {

    private static int noOfRiders = 0;

    private final int id;
    private final String name;
    private final int yearOfBirth;
    private ArrayList<StageResult> results = new ArrayList<StageResult>();

    Rider(String name, int yearOfBirth) {
        this.name = name;
        this.yearOfBirth = yearOfBirth;
        id = noOfRiders++;
    }

    public static void resetNoOfRiders() {
        noOfRiders = 0;
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

    public void addResult(StageResult result) {
        results.add(result);
    }

    public void removeResult(StageResult result) {
        results.remove(result);
    }

    public StageResult[] getResults() {
        StageResult[] resultArr = new StageResult[results.size()];
        resultArr = results.toArray(resultArr);
        return resultArr;
    }
}
