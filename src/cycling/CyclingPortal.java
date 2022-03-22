package cycling;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * CyclingPortal class which implements CyclingPortalInterface.
 * <p>
 *     The no-argument constructor of this class initialises the CyclingPortal
 *     as an empty platform with no initial racing teams nor races within it.
 * </p>
 *
 *
 * @author Joey Griffiths and Alexander Cairns
 *
 */
public class CyclingPortal implements CyclingPortalInterface {

	/**
	 * A private, final, 2D array of integers, used to represent the points
	 * earned for each rank in a stage, for different types of stages.<br>
 	 * To use: pointsTable[type][rank]
	 */
	private final int[][] pointsTable = {
		{50, 30, 20, 18, 16, 14, 12, 10, 8, 7, 6, 5, 4, 3, 2},
		{30, 25, 22, 19, 17, 15, 13, 11, 9, 7, 6, 5, 4, 3, 2},
		{20, 17, 15, 13, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1}
	};

	/**
	 * A private, final, 2D array of integers, used to represent the points
	 * earned for each rank in a mountain segment, for different types of
	 * mountain segments.<br>
	 * To use: mountainPointsTable[rank][type]
	 */
	private final int[][] mountainPointsTable = {
		{1, 2, 5, 10, 20},
		{0, 1, 3, 8, 15},
		{0, 0, 2, 6, 12},
		{0, 0, 1, 4, 10},
		{0, 0, 0, 2, 8},
		{0, 0, 0, 1, 6},
		{0, 0, 0, 0, 4},
		{0, 0, 0, 0, 2}
	};

	/**
	 * An ArrayList of Race objects.<br>
	 * Stores all active races in the system.
	 */
    private ArrayList<Race> races = new ArrayList<>();

	/**
	 * An ArrayList of Team objects.<br>
	 * Used to store all active teams in the system.
	 */
	private ArrayList<Team> teams = new ArrayList<>();

	@Override
	public int[] getRaceIds() {
		// Initialise int[] of the same length as races ArrayList
        int[] raceIds = new int[races.size()];
        for (int i=0; i<races.size(); i++) {
			// For each item in races, add this race's ID to raceIds
            raceIds[i] = races.get(i).getId();
        }
		return raceIds;
	}

	@Override
	public int createRace(String name, String description) throws IllegalNameException, InvalidNameException {
		// Race name input validation
        if (name == null) { throw new InvalidNameException("Race name cannot be null"); }
		if (name.isEmpty()) { throw new InvalidNameException("Race name cannot be an empty string"); }
		if (name.length() > 30) { throw new InvalidNameException("Race name cannot be greater than 30 characters"); }
        if (name.contains(" ")) { throw new InvalidNameException("Race name cannot contain white space"); }

        for (Race race : races) {
			// Searches through races to find one which has this same name
            if (race.getName().equals(name)) {
				// If the name already exists, exception thrown
                throw new IllegalNameException("Race name " + name + " already exists");
            }

		}

		// Checks passed, race in instantiated and added to the list of races
        Race race = new Race(name, description);
        races.add(race);
		assert (races.size() > 0);
		return race.getId();
	}

	@Override
	public String viewRaceDetails(int raceId) throws IDNotRecognisedException {
		// Races have a method to return their details
        return getRaceById(raceId).getDetails();
	}

	@Override
	public void removeRaceById(int raceId) throws IDNotRecognisedException {
		// Finds Race object and removes it from list of races
		races.remove(getRaceById(raceId));
	}

	@Override
	public int getNumberOfStages(int raceId) throws IDNotRecognisedException {
		// Finds Race object and uses its built in getNoOfStages() method
		return getRaceById(raceId).getNoOfStages();
	}

	@Override
	public int addStageToRace(int raceId, String stageName, String description, double length, LocalDateTime startTime, StageType type)
			throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException {
		// Stage name and length validation
        if (stageName == null) { throw new InvalidNameException("Stage name cannot be null"); }
		if (stageName.isEmpty()) { throw new InvalidNameException("Stage name cannot be an empty string"); }
		if (stageName.length() > 30) { throw new InvalidNameException("Stage name cannot be greater than 30 characters"); }
        if (length<5) { throw new InvalidLengthException("Stage length cannot be less than 5(km)"); }
        if (stageName.contains(" ")) { throw new InvalidNameException("Stage name cannot contain white space"); }

        Race raceToAddTo = null;
        for (Race race : races) {
            for (Stage stage : race.getStages()) {
				// Searches through stages to find one which has this same name
                if (stage.getName().equals(stageName)) {
					// If name already exists, exception thrown
                    throw new IllegalNameException("Stage name " + stageName + " already exists");
                }
            }
			// Using this loop to find the race with this ID instead of getRaceById() saves computation
            if (race.getId() == raceId) { raceToAddTo = race; }
        }
        if (raceToAddTo == null) {
			throw new IDNotRecognisedException("No race with an ID of "+ raceId + " exists");
		}
        
		// Checks passed, instantiates the stage and adds it to list of stages in race
		Stage stage = new Stage(stageName, description, length, startTime,
				type);
        raceToAddTo.addStage(stage);
		assert (raceToAddTo.getNoOfStages() > 0);
        return stage.getId();
	}

	@Override
	public int[] getRaceStages(int raceId) throws IDNotRecognisedException {
		// Finds the correct Race object and retrieves its stages
        Stage[] stages = getRaceById(raceId).getStages();

		// Converts array of Stage objects into array of corresponding IDs
        int[] stageIds = new int[stages.length];
        for (int i=0; i<stages.length; i++) {
            stageIds[i] = stages[i].getId();
        }
		return stageIds;
	}

	@Override
	public double getStageLength(int stageId) throws IDNotRecognisedException {
		// Finds Stage object and returns its length
		return getStageById(stageId).getLength();
	}

	@Override
	public void removeStageById(int stageId) throws IDNotRecognisedException {
		// Finds Race object and Stage object and uses the race's removeStage() method
		getRaceByStageId(stageId).removeStage(getStageById(stageId));
	}

	@Override
	public int addCategorizedClimbToStage(int stageId, Double location, SegmentType type, Double averageGradient,
			Double length) throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException,
			InvalidStageTypeException {
		Stage stage = getStageById(stageId);
		// Input validation
		if (stage.isPrepared()) {
			throw new InvalidStageStateException("Stage is already 'waiting for results'");
		}
		if (location > stage.getLength() || location < 0) {
			throw new InvalidLocationException("Location out of bounds");
		}
		if (stage.getType() == StageType.TT) {
			throw new InvalidStageTypeException("Time-trial stages cannot contain segments");
		}
		// If arguments are valid, new Segment is instantiated and added to stage's list of segments
		Segment segment = new Segment(location, type, averageGradient, length);
		stage.addSegment(segment);
		return segment.getId();
	}

	@Override
	public int addIntermediateSprintToStage(int stageId, double location) throws IDNotRecognisedException,
			InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {
		Stage stage = getStageById(stageId);
		// Input validation
		if (stage.isPrepared()) {
			throw new InvalidStageStateException("Stage is already 'waiting for results'");
		}
		if (location > stage.getLength() || location < 0) {
			throw new InvalidLocationException("Location out of bounds");
		}
		if (stage.getType() == StageType.TT) {
			throw new InvalidStageTypeException("Time-trial stages cannot contain segments");
		}
		// If arguments are valid, new Segment is instantiated and added to stage's list of segments
		Segment segment = new Segment(location, SegmentType.SPRINT);
		stage.addSegment(segment);
		return segment.getId();
	}

	@Override
	public void removeSegment(int segmentId) throws IDNotRecognisedException, InvalidStageStateException {
		// Finds the stage the segment is located in
		Stage stage = getStageBySegmentId(segmentId);
		if (stage.isPrepared()) {
			throw new InvalidStageStateException("Stage is already 'waiting for results'");
		}
		// Removes segment from stage
		stage.removeSegment(getSegmentById(segmentId));
	}

	@Override
	public void concludeStagePreparation(int stageId) throws IDNotRecognisedException, InvalidStageStateException {
		// Finds the correct stage from the ID
		Stage stage = getStageById(stageId);
		if (stage.isPrepared()) {
			throw new InvalidStageStateException("Stage is already 'waiting for results'");
		}
		// Prepares the stage
		stage.prepare();
	}

	@Override
	public int[] getStageSegments(int stageId) throws IDNotRecognisedException {
		Stage stage = getStageById(stageId);
		// Retrieves an array of Segment objects
		Segment[] segments = stage.getSegments();
		// Method must return an int[] of IDs
		int[] segmentIds = new int[segments.length];
		for (int i=0; i<segments.length; i++) {
			// Adds the ID of each segment to the new array
			segmentIds[i] = segments[i].getId();
		}
		return segmentIds;
	}

	@Override
	public int createTeam(String name, String description) throws IllegalNameException, InvalidNameException {
		// Name validation checks
		if (name == null) { throw new IllegalNameException("Team name cannot be null"); }
		if (name.isEmpty()) { throw new IllegalNameException("Team name cannot be an empty string"); }
		if (name.length() > 30) { throw new IllegalNameException("Team name cannot be greater than 30 characters"); }
        if (name.contains(" ")) { throw new InvalidNameException("Team name cannot contain white space"); }

		for (Team team : teams) {
			if (team.getName() == name) {
				// Loops through each team to check the name is not already present
				throw new IllegalNameException("Team with name \"" + name + "\" already exists");
			}
		}

		// Instantiates new Team and adds it to the list of teams
		Team team = new Team(name, description);
		teams.add(team);
		return team.getId();
	}

	@Override
	public void removeTeam(int teamId) throws IDNotRecognisedException {
		// Finds the Team with this ID and removes it from the list of teams
		teams.remove(getTeamById(teamId));
	}

	@Override
	public int[] getTeams() {
		// Initialises a new int[] to store team IDs
		int[] teamIds = new int[teams.size()];
		for (int i=0; i<teams.size(); i++) {
			// Adds the ID of each team in the teams list to teamIds
			teamIds[i] = teams.get(i).getId();
		}
		return teamIds;
	}

	@Override
	public int[] getTeamRiders(int teamId) throws IDNotRecognisedException {
		// Retrieves a Rider[] of all riders in the team
		Rider[] riders = getTeamById(teamId).getRiders();
		// Method needs to return an int[] of IDs
		int[] riderIds = new int[riders.length];
		for (int i=0; i<riders.length; i++) {
			// Adds the ID of each rider to riderIds
			riderIds[i] = riders[i].getId();
		}
		return riderIds;
	}

	@Override
	public int createRider(int teamID, String name, int yearOfBirth)
			throws IDNotRecognisedException, IllegalArgumentException {
		// Input validation checks
		if (name == null) { throw new IllegalArgumentException("Rider name cannot be null"); }
		if (yearOfBirth < 1900) {
			throw new IllegalArgumentException("Rider yearOfBirth cannot be less than 1900");
		}

		// If arguments are valid, new Rider is instantiated and added to the team specified
		Rider rider = new Rider(name, yearOfBirth);
		getTeamById(teamID).addRider(rider);
		return rider.getId();
	}

	@Override
	public void removeRider(int riderId) throws IDNotRecognisedException {
		// Finds the correct team and removes this rider from it
        getTeamByRiderId(riderId).removeRider(getRiderById(riderId));

	}

	@Override
	public void registerRiderResultsInStage(int stageId, int riderId, LocalTime... checkpoints)
			throws IDNotRecognisedException, DuplicatedResultException, InvalidCheckpointsException,
			InvalidStageStateException {
		Stage stage = getStageById(stageId);
		Rider rider = getRiderById(riderId);
		// Makes sure stage has finished preparation before results are registered
		if (!stage.isPrepared()) {
			throw new InvalidStageStateException("Stage is not 'waiting for results'");
		}
		// Checkpoints input validation
		if (checkpoints.length != stage.getSegments().length+2) {
			throw new InvalidCheckpointsException("Number of checkpoints must be number of segments + 2");
		}
		// Rider can only have one StageResult per stage
		if (getResultInStage(rider, stage) != null) {
			throw new DuplicatedResultException("A result for this stage already exists");
		}
		// If arguments are valid, new StageResult is instantiated storing these checkpoints
		// and is added to rider's results
		StageResult stageResult = new StageResult(stage, checkpoints);
		rider.addResult(stageResult);
	}

	@Override
	public LocalTime[] getRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		// Fetches the StageResult that corresponds to this rider and stage
		StageResult result = getResultInStage(getRiderById(riderId), getStageById(stageId));
		if (result == null) {
			throw new IDNotRecognisedException("Rider "+riderId +" does not have any results in stage "+stageId);
		} else {
			// Returns the array of checkpoints for the result
			return result.getCheckpoints();
		}
	}

	@Override
	public LocalTime getRiderAdjustedElapsedTimeInStage(int stageId, int riderId) throws IDNotRecognisedException {
		Rider riderToFind = getRiderById(riderId);
		Stage stage = getStageById(stageId);
		// Retrieves a list of all riders in the stage
		ArrayList<Rider> ridersInStage = getRidersInStage(stage);

		// HashMap to associate riders with elapsed times
		HashMap<Rider, LocalTime> ridersAndTimes = new HashMap<>();
		for (Rider rider : ridersInStage) {
			// Adds each rider in the stage and their elapsed time in the stage to the HashMap
			ridersAndTimes.put(rider, getElapsedTime(stage, rider));
		}

		// HashMap is sorted by values (elapsed times)
		HashMap<Rider, LocalTime> sortedMap = sortRidersByTimes(ridersAndTimes);

		// The HashMap is split into two arrays of riders and times
		Rider[] sortedRidersInStage = sortedMap.keySet().toArray(new Rider[ridersInStage.size()]);
		LocalTime[] sortedTimes = sortedMap.values().toArray(new LocalTime[ridersInStage.size()]);

		// streak represents the number of riders in a row who finished the stage with
		// less than 1 second between them
		int streak = 0;
		for (int i=0; i<sortedRidersInStage.length; i++) {
			// First rider is skipped as it has no previous rider
			if (i > 0) {
				LocalTime elapsedTime = sortedTimes[i];
				LocalTime prevElapsedTime = sortedTimes[i-1];
				assert (elapsedTime.equals(sortedMap.get(sortedRidersInStage[i])));

				// Calculates the time difference between two adjacent elapsed times
				long timeDifference = prevElapsedTime.until(elapsedTime, ChronoUnit.MILLIS);
				assert (timeDifference >= 0);

				if (timeDifference < 1000) {
					// If the time difference is less than 1 second (1000ms), streak is incremented
					streak++;
				} else {
					// Otherwise, the streak ends
					streak = 0;
				}
			}

			if (sortedRidersInStage[i].equals(riderToFind)) {
				// When the rider we are looking for is found in the loop
				if (streak == 0) {
					// If there is no streak (previous rider was more than 1 second apart),
					// the rider's adjusted elapsed time is simply the rider's elapsed time
					return sortedTimes[i];
				}
				else {
					// If there is a streak, the elapsed time of the rider who began the streak
					// is returned
					return getElapsedTime(stage, sortedRidersInStage[i-streak]);
				}
			}
		}
		// Return null if given rider does not exist in this stage
		return null;
	}

	@Override
	public void deleteRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		// Retrieves the Rider object and the StageResult that corresponds to it and this stage
		Rider rider = getRiderById(riderId);
		StageResult result = getResultInStage(rider, getStageById(stageId));
		if (result == null) {
			throw new IDNotRecognisedException("Rider "+riderId+" does not have any results in stage "+stageId);
		} else {
			// Removes the result if it exists in the stage
			rider.removeResult(result);
		}
	}

	@Override
	public int[] getRidersRankInStage(int stageId) throws IDNotRecognisedException {
		// HashMap to associate riders with their adjusted elapsed times is initialised
		HashMap<Rider, LocalTime> riderToResultMap = new HashMap<Rider, LocalTime>();
		Stage stage = getStageById(stageId);

		for (Rider rider : getRidersInStage(stage)) {
			// Finds the StageResult for each rider in this stage
			StageResult result = getResultInStage(rider, stage);
			if (result != null) {
				// If the rider is in this stage, their adjusted elapsed time is retrieved
				// And associated with them in the HashMap
				LocalTime elapsedTime = getRiderAdjustedElapsedTimeInStage(stageId, rider.getId());
				riderToResultMap.put(rider, elapsedTime);
			}
		}


		// The hashmap is sorted so that riders will be order of their ranking
		HashMap<Rider, LocalTime> sortedMap = sortRidersByTimes(riderToResultMap);

		// The method needs to return an int[] of IDs, so one is initialised
		int[] rankedRiders = new int[sortedMap.size()];
		int i = 0;
		for (Rider rider : sortedMap.keySet()) {
			// Each rider's ID from the sorted HashMap is added to the int[] in order
			rankedRiders[i] = rider.getId();
			i++;
		}
		return rankedRiders;
	}

	@Override
	public LocalTime[] getRankedAdjustedElapsedTimesInStage(int stageId) throws IDNotRecognisedException {
		// Retrieves an int[] of ranked rider IDs
		int[] rankedRiderIds = getRidersRankInStage(stageId);
		LocalTime[] rankedTimes = new LocalTime[rankedRiderIds.length];
		for (int i=0;i<rankedRiderIds.length;i++) {
			// Retrieves the adjusted elapsed time for each rider in the stage and adds it
			// to the ranked array of times
			rankedTimes[i] = getRiderAdjustedElapsedTimeInStage(stageId, rankedRiderIds[i]);
		}
		return rankedTimes;
	}

	@Override
	public int[] getRidersPointsInStage(int stageId) throws IDNotRecognisedException {
		Stage stage = getStageById(stageId);
		// Retrieves a list of rider IDs in order of their rank
		int[] rankedRiders = getRidersRankInStage(stageId);
		StageType type = stage.getType();
		// Initialises a new array to contain points for each rider
		int[] points = new int[rankedRiders.length];
		for (int i=0;i<points.length;i++) {
			// i represents the current rider in the loop's ranking
			if (i > 14) {
				// If the rider ranked 16th or more, they get no points
				points[i] = 0;
			} else {
				// Looks up points table attribute to assign points
				switch (type) {
					case FLAT:
						points[i] = pointsTable[0][i];
						break;
					case MEDIUM_MOUNTAIN:
						points[i] = pointsTable[1][i];
						break;
					default: // HIGH_MOUNTAIN or TT
						points[i] = pointsTable[2][i];
						break;
				}
			}

			// Calculates and adds the points aquired from immediate sprints in the stage
			points[i] += getImmediateSprintPoints(getRiderById(rankedRiders[i]), stage);
		}
		return points;
	}

	@Override
	public int[] getRidersMountainPointsInStage(int stageId) throws IDNotRecognisedException {
		Stage stage = getStageById(stageId);
		Segment[] segments = stage.getSegments();
		// Ranked list of rider IDs in the stage
		int[] ridersRanks = getRidersRankInStage(stageId);
		// Ranked list of Rider objects in the stage
		Rider[] ridersInStage = new Rider[ridersRanks.length];
		for (int i=0;i<ridersInStage.length;i++) {
			// Adds the Rider object for each ID to the array
			ridersInStage[i] = getRiderById(ridersRanks[i]);
		}

		// Initalises an array of points for each rider in the stage, starting with 0 for all
		int[] mountainPoints = new int[ridersInStage.length];
		Arrays.fill(mountainPoints, 0);

		for (int i=0;i<segments.length;i++) {
			SegmentType type = segments[i].getType();
			if (type == SegmentType.SPRINT) {
				// Sprint segments are ignored when calculating points
				continue;
			}

			// HashMap to associate riders with their segment time for this segment
			HashMap<Rider, LocalTime> resultToTimeMap = new HashMap<Rider, LocalTime>();

			for (Rider rider : ridersInStage) {
				StageResult result = getResultInStage(rider, stage);
				if (result != null) {
					// Segment time is the time a rider reaches the segment
					LocalTime[] checkpoints = result.getCheckpoints();
					assert (checkpoints.length == segments.length + 2);
					LocalTime segmentTime = timeDifference(checkpoints[0], checkpoints[i+1]);
					resultToTimeMap.put(rider, segmentTime);
				}
			}

			// Sorts this segments HashMap based on values (segment times)
			HashMap<Rider, LocalTime> sortedMap = sortRidersByTimes(resultToTimeMap);

			// The rank of the current rider in this segment for each rider
			int a = 0;
			for (Rider rider : sortedMap.keySet()) {
				// Only checks the first 8 riders in the segment, as the others will
				// recieve no points for this segment
				if (a > 7) { break; }

				for (int b=0;b<ridersInStage.length;b++) {
					// b represents the position of the rider in the final points array
					if (ridersInStage[b].equals(rider)) {
						// Where the rider in the segment matches up with the rider in the stage,
						// the rider's total points is incremented by the points for this segment
						// which is looked up in the points table, based on rank and segment type
						switch (type) {
							case C4:
								mountainPoints[b] += mountainPointsTable[a][0];
								break;
							case C3:
								mountainPoints[b] += mountainPointsTable[a][1];
								break;
							case C2:
								mountainPoints[b] += mountainPointsTable[a][2];
								break;
							case C1:
								mountainPoints[b] += mountainPointsTable[a][3];
								break;
							case HC:
								mountainPoints[b] += mountainPointsTable[a][4];
								break;
							default:
								assert (false);
						}
						break;
					}
				}
				a++;
			}
		}
		return mountainPoints;
	}

	@Override
	public void eraseCyclingPortal() {
		// Resets all static counter attributes so that IDs start from 0 again
		Team.resetNoOfTeams();
		Rider.resetNoOfRiders();
		Race.resetNoOfRaces();
		Stage.resetNoOfStages();
		Segment.resetNoOfSegments();
		StageResult.resetTotalResults();
		// Clears list of teams and races in CyclingPortal
		teams.clear();
		races.clear();
	}

	@Override
	public void saveCyclingPortal(String filename) throws IOException {
		// ObjectOutputStream can serialise an object and write it to a file
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
		try {
			// Serialises this CyclingPortal object
			oos.writeObject(this);
		} finally {
			// ObjectOutputStream must close regardless of if write is successful
			oos.close();
		}
	}

	@Override
	public void loadCyclingPortal(String filename) throws IOException, ClassNotFoundException {
		// ObjectInputStream can read a serialised file and deserialise the object
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
		try {
			// Reads the serialised object and deserialises it
			Object obj = ois.readObject();
			CyclingPortal cyclingPortal;
			if (obj instanceof CyclingPortal) {
				cyclingPortal = (CyclingPortal)obj;
				// Replaces this object attributes with those of loaded CyclingPortal
				teams = cyclingPortal.getTeamsList();
				races = cyclingPortal.getRacesList();
			}
		} finally {
			// ObjectInputStream must close regardless of if read is successful
			ois.close();
		}
	}

	@Override
	public void removeRaceByName(String name) throws NameNotRecognisedException {
		for (Race race : races) {
			// Searches through each race until a matching name is found
			if (race.getName().equals(name)) {
				// Removes this race from the list
				races.remove(race);
				// Exits the method so that the for loop does not continue
				return;
			}
		}
		throw new NameNotRecognisedException("No race exists with name " + name);
	}

	@Override
	public LocalTime[] getGeneralClassificationTimesInRace(int raceId) throws IDNotRecognisedException {
		Race race = getRaceById(raceId);
		Stage[] stages = race.getStages();
		// Retrieves all riders participating in this race
		ArrayList<Rider> riders = getRidersInRace(race);
		// Initialises a HashMap to associate riders with their elapsed times
		HashMap<Rider, LocalTime> riderTimes = new HashMap<Rider, LocalTime>();
		for (Rider rider : riders) {
			// Every participating rider is added to the HashMap
			riderTimes.put(rider, LocalTime.of(0, 0, 0));

			for (Stage stage : stages) {
				// The rider's elapsed time for each stage is found
				LocalTime t = getRiderAdjustedElapsedTimeInStage(stage.getId(),	rider.getId());
				// If this result exists (rider has finished the stage), HashMap
				// value for the rider is incremented by the elapsed time for this stage
				if (t != null) {
					riderTimes.replace(rider, riderTimes.get(rider).plusHours(t.getHour())
														.plusMinutes(t.getMinute())
														.plusSeconds(t.getSecond())
														.plusNanos(t.getNano()));
				}
			}
		}

		// The HashMap is then sorted by values (total elapsed times)
		HashMap<Rider, LocalTime> sortedMap = sortRidersByTimes(riderTimes);

		// The method needs to return a LocalTime[], so one is initialised
		LocalTime[] times = new LocalTime[riders.size()];
		// Sorted classification times are added to this array
		sortedMap.values().toArray(times);
		return times;
	}

	@Override
	public int[] getRidersPointsInRace(int raceId) throws IDNotRecognisedException {
		Race race = getRaceById(raceId);
		Stage[] stages = race.getStages();
		// Retrieves all riders participating in this race
		ArrayList<Rider> riders = getRidersInRace(race);
		// Initialises a HashMap to associate riders with their points
		Map<Rider, Integer> riderPoints = new HashMap<Rider, Integer>();

		for (Rider rider : riders) {
			// Every participating rider is added to the HashMap
			riderPoints.put(rider, 0);
			for (Stage stage : stages) {
				// Loops through every stage in the race and retrieves an array of
				// ranked rider IDs
				int[] ranks = getRidersRankInStage(stage.getId());

				// Finds the index of the current rider in this array
				int indexOfRider = -1;
				for (int i=0; i<ranks.length; i++) {
					if (ranks[i] == rider.getId()) {
						indexOfRider = i;
					}
				}

				if (indexOfRider != -1) {
					// Retrieves an array of ranked rider's points in the stage
					// Note: this is sorted by time and so items in this array and the
					//       ranks array will match up with each other
					int[] pointsArr = getRidersPointsInStage(stage.getId());
					int points = pointsArr[indexOfRider];

					// Increments the rider's total points by the points in this stage
					riderPoints.replace(rider, riderPoints.get(rider) + points);
				}
			}
		}

		// The method needs to return an int[] sorted by elapsed time, so one is initialised
		int[] sortedPoints = new int[riders.size()];
		// Retrieves array of rider IDs sorted by elapsed time
		int[] riderRanks = getRidersGeneralClassificationRank(raceId);
		for ( int i=0; i<riderRanks.length; i++ ) {
			// Finds the rider for every ID in the array of ranks and adds its corresponding
			// number of points in the HashMap to the array of sorted points
			sortedPoints[i] = riderPoints.get(getRiderById(riderRanks[i]));
		}
		return sortedPoints;
	}

	@Override
	public int[] getRidersMountainPointsInRace(int raceId) throws IDNotRecognisedException {
		Race race = getRaceById(raceId);
		Stage[] stages = race.getStages();
		ArrayList<Rider> riders = getRidersInRace(race);
		// HashMap to associatie riders in the race and their total points in the race
		HashMap<Rider, Integer> riderPoints = new HashMap<Rider, Integer>();

		for (Rider rider : riders) {
			// Adds each rider and an inital value of 0 points to the HashMap
			riderPoints.put(rider, 0);
			for (Stage stage : stages) {
				if (getResultInStage(rider, stage) != null) {
					// For each stage in the race, retrieves the riders ranks if they are in the stage
					// (riders in a race should have results in all stages in the race)
					int[] ranks = getRidersRankInStage(stage.getId());

					// The index of the current rider in the array of ranks for the stage
					int indexOfRider = -1;
					for (int i=0; i<ranks.length; i++) {
						if (ranks[i] == rider.getId()) {
							// When the ID in ranks that matches with this current rider is found,
							// the index is assigned and the loop is broken out of
							indexOfRider = i;
							break;
						}
					}

					if (indexOfRider != -1) {
						// Finds all the riders' mountain points in this stage
						int[] pointsArr = getRidersMountainPointsInStage(stage.getId());
						int points = 0;
						if (pointsArr.length > 0) {
							// The current rider's points in this stage are found
							points = pointsArr[indexOfRider];
						}
						// Rider's total points for the race is incremented by their points in this stage
						riderPoints.replace(rider, riderPoints.get(rider) + points);
					}
				}
			}
		}

		// Method needs to return an int[] of points, so one is initialised
		int[] sortedPoints = new int[riders.size()];
		// Points need to be sorted by total elapsed times, so these are retrieved
		int[] riderRanks = getRidersGeneralClassificationRank(raceId);
		for ( int i=0; i<riderRanks.length; i++ ) {
			// Adds the points of each rider to the points array,
			// ordered by general classification rank
			sortedPoints[i] = riderPoints.get(getRiderById(riderRanks[i]));
		}
		return sortedPoints;
	}

	@Override
	public int[] getRidersGeneralClassificationRank(int raceId) throws IDNotRecognisedException {
		Race race = getRaceById(raceId);
		Stage[] stages = race.getStages();
		ArrayList<Rider> riders = getRidersInRace(race);
		// HashMap to associate riders with their total adjusted elapsed times
		HashMap<Rider, LocalTime> riderTimes = new HashMap<Rider, LocalTime>();
		
		for (Rider rider : riders) {
			// Adds each rider in the race to the HashMap, initialising their time as 0:0:0
			riderTimes.put(rider, LocalTime.of(0, 0, 0));
			for (Stage stage : stages) {
				// Calculates the adjusted elapsed time for each stage for this rider
				LocalTime t = getRiderAdjustedElapsedTimeInStage(stage.getId(), rider.getId());
				if (t != null) {
					// If the rider has a result in this stage, their total time is incremented
					// by the adjusted elapsed time in this stage
					riderTimes.replace(rider, riderTimes.get(rider).plusHours(t.getHour())
														.plusMinutes(t.getMinute())
														.plusSeconds(t.getSecond()));
				}
			}
		}

		// The HashMap is sorted by values (total elapsed times)
		HashMap<Rider, LocalTime> sortedMap = sortRidersByTimes(riderTimes);

		// The method needs to return an int[] of IDs, so one is initialised
		int[] riderIds = new int[riders.size()];
		int i = 0;
		for (Rider key : sortedMap.keySet()) {
			// The ID of each rider in the sorted HashMap is added to the int[] in order
			riderIds[i] = key.getId();
			i++;
		}
		return riderIds;
	}

	@Override
	public int[] getRidersPointClassificationRank(int raceId) throws IDNotRecognisedException {
		// Retrieves race's ranking and points sorted by time
		// This is so that the array of times aligns with the array of rider IDs
		// Therefore, they can be paired up into a HashMap
		int[] riderRanks = getRidersGeneralClassificationRank(raceId);
		int[] riderPointsSortedByTime = getRidersPointsInRace(raceId);
		assert (riderRanks.length == riderPointsSortedByTime.length);
		HashMap<Rider, Integer> riderPoints = new HashMap<>();

		for (int i=0; i<riderRanks.length;i++) {
			// Each rider and their corresponding points are put into the HashMap
			riderPoints.put(getRiderById(riderRanks[i]),
					riderPointsSortedByTime[i]);
		}

		// The HashMap is then sorted by number of points (sorted by value)
		HashMap<Rider, Integer> sortedMap = sortRidersByPoints(riderPoints);
		assert (sortedMap.size() == riderPoints.size());

		// The method needs to return an int[], so one is initialised
		int[] riderIds = new int[riderRanks.length];
		int i = 0;
		for ( Rider key : sortedMap.keySet() ) {
			// Finds each key in the sorted HashMap (the rider), and adds its ID to the int[]
			riderIds[i] = key.getId();
			i++;
		}

		// The array needs to be in descending order, so it is reversed
		reverseArray(riderIds);
		return riderIds;
	}

	@Override
	public int[] getRidersMountainPointClassificationRank(int raceId) throws IDNotRecognisedException {
		// These two arrays associate rider IDs with their mountain points in the race
		int[] riderRanks = getRidersGeneralClassificationRank(raceId);
		int[] riderPointsSortedByTime = getRidersMountainPointsInRace(raceId);
		assert (riderRanks.length == riderPointsSortedByTime.length);

		// HashMap to associate riders with their total mountain points in the race
		HashMap<Rider, Integer> riderPoints = new HashMap<>();

		for (int i=0; i<riderRanks.length;i++) {
			// Adds each rider in the race and their associated mountain points to the HashMap
			riderPoints.put(getRiderById(riderRanks[i]), riderPointsSortedByTime[i]);
		}

		// The HashMap is sorted by values (total mountain points)
		HashMap<Rider, Integer> sortedMap = sortRidersByPoints(riderPoints);

		// The method needs to return an int[] of rider IDs, so one is initialised
		int[] riderIds = new int[riderRanks.length];
		int i = 0;
		for ( Rider key : sortedMap.keySet() ) {
			// Finds each key in the sorted HashMap (the rider), and adds its ID to the int[]
			riderIds[i] = key.getId();
			i++;
		}
		
		// The array needs to be in descending order, so it is reversed
		reverseArray(riderIds);
		return riderIds;
	}

	/**
	 * Public getter method to return a list of all teams stored in the
	 * 'teams' ArrayList.
	 *
	 * @return An ArrayList of team objects.
	 *
	 */
	public ArrayList<Team> getTeamsList() {
		return teams;
	}

	/**
	 * Public getter method to return a list of all races stored in the
	 * 'races' ArrayList.
	 *
	 * @return An ArrayList of race objects.
	 *
	 */
	public ArrayList<Race> getRacesList() {
		return races;
	}

	/**
	 * Private method to find the time difference between two local times,
	 * outputting the result as a LocalTime object.
	 *
	 * @param time1 LocalTime object.
	 * @param time2 LocalTime object.
	 * @return LocalTime object, in format HH:MM:SS:NN, representing
	 * difference between time1 and time2.
	 *
	 */
	private LocalTime timeDifference(LocalTime time1, LocalTime time2) {
		// Works out time difference in milliseconds
		long elapsedTimeInMilliSecs = time1.until(time2, ChronoUnit.MILLIS);

		// Conversion of ms into hours, mins, secs, nanos
		int hours = (int) elapsedTimeInMilliSecs / 3600000;
		int mins = (int) (elapsedTimeInMilliSecs % 3600000) / 60000;
		int secs = (int) ((elapsedTimeInMilliSecs % 3600000) % 60000) / 1000;
		int nanos = (int) (((elapsedTimeInMilliSecs % 3600000) % 60000) % 1000) * 1000000;

		// Returns a LocalTime object
		return LocalTime.of(hours, mins, secs, nanos);
	}

	/**
	 * Private method to sort a HashMap of Rider : LocalTime by their
	 * adjusted elapsed times (LocalTime).
	 *
	 * @param initialMap The HashMap to be sorted.
	 * @return A HashMap object sorted by the LocalTime value (descending).
	 *
	 */
	private HashMap<Rider, LocalTime> sortRidersByTimes(HashMap<Rider, LocalTime> initialMap) {
		return initialMap.entrySet().stream()
				.sorted(Entry.comparingByValue())
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue,
						(e1, e2) -> e1, LinkedHashMap::new));
	}

	/**
	 * Private method to sort a HashMap of Rider : Integer by the rider's
	 * points (integer).
	 *
	 * @param initialMap The HashMap to be sorted.
	 * @return A HashMap object sorted by the integer value (descending).
	 *
	 */
	private HashMap<Rider, Integer> sortRidersByPoints(HashMap<Rider, Integer> initialMap) {
		return initialMap.entrySet().stream()
				.sorted(Entry.comparingByValue())
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue,
						(e1, e2) -> e1, LinkedHashMap::new));
	}

	/**
	 * Private method to reverse an int[], used to sort rider IDs by descending order.
	 * 
	 * @param array The array to reverse.
	 */
	private void reverseArray(int[] array) {
		for (int i = 0; i < array.length / 2; i++) {
			// Swaps first and last elements in the array and gradually moves inwards
			// swapping elements until meeting at the middle
			int temp = array[i];
			array[i] = array[array.length - 1 - i];
			array[array.length - 1 - i] = temp;
		}}

	/**
	 * Private method to find the StageResult object for a particular rider and stage.
	 *
	 * @param rider The rider to look for the result in.
	 * @param stage The stage to look up the result for.
	 * @return A StageResult object representing the rider's result in the stage, or
	 * 		   null if the rider does not have a result in the stage.
	 * 
	 */
	private StageResult getResultInStage(Rider rider, Stage stage) {
		for (StageResult result : rider.getResults()) {
			if (result.getStage().equals(stage)) {
				// A StageResult with the correct Stage was found, returns the StageResult
				return result;
			}
		}
		// A StageResult with the correct Stage was not found, returns null
		return null;
	}

	/**
	 * Private method to calculate the points a rider obtained from
	 * immediate sprints in a stage.
	 * 
	 * @param rider The rider in question.
	 * @param stage The stage in question.
	 * @return An int representing the number of points obtained in the stage.
	 */
	private int getImmediateSprintPoints(Rider riderToFind, Stage stage) {
		int totalPoints = 0;
		Segment[] segments = stage.getSegments();
		ArrayList<Rider> ridersInStage = getRidersInStage(stage);

		for (int i=0; i<segments.length; i++) {
			if (!segments[i].getType().equals(SegmentType.SPRINT)) {
				// Ignores segments that aren't immediate sprints
				continue;
			}

			// HashMap to associate riders with their times for this segment
			HashMap<Rider, LocalTime> riderTimesAtSegment = new HashMap<>();

			for (Rider rider : ridersInStage) {
				StageResult result = getResultInStage(rider, stage);
				if (result != null) {
					// Gets the rider's checkpoints in this stage
					LocalTime[] checkpoints = result.getCheckpoints();
					assert (checkpoints.length == segments.length + 2);

					// Calculates time this segment was reached and associates it with the rider in the HashMap
					LocalTime timeAtSegment = timeDifference(checkpoints[0], checkpoints[i+1]);
					riderTimesAtSegment.put(rider, timeAtSegment);
				}
			}

			// HashMap is sorted by values (segment time)
			HashMap<Rider, LocalTime> sortedMap = sortRidersByTimes(riderTimesAtSegment);

			// List to contain all riders in the stage, sorted by time they reached this segment
			ArrayList<Rider> riderRankingsAtSegment = new ArrayList<Rider>();
			for (Rider key : sortedMap.keySet()) {
				// Adds each rider in the HashMap to the list in order
				riderRankingsAtSegment.add(key);
			}

			// The ranking of this rider is the index they have in the ranked list
			int riderRanking = riderRankingsAtSegment.indexOf(riderToFind);

			if (riderRanking < 15) {
				// Looks up table to find points to add for the rider
				// Adds no points if the rider reaches the segment 16th or more
				totalPoints += pointsTable[2][riderRanking];
			}

		}
		return totalPoints;
	}

	/**
	 * Private method to find a Race object based on its unique ID.
	 * <p>
	 *     Iterates through the 'races' ArrayList, checking each race's ID
	 *     through the 'getId()' getter method, until the race matching the ID
	 *     is found.
	 * </p>
	 *
	 * @param id The ID of the race to be found.
	 * @return A Race object, corresponding to the unique ID provided.
	 * @throws IDNotRecognisedException If the ID does not match any Race in
	 * the system.
	 * 
	 */
    private Race getRaceById(int id) throws IDNotRecognisedException {
        for (Race race : races) {
			if (race.getId() == id) {
				return race;
			}
		}
        throw new IDNotRecognisedException("No race with an ID of " + id + " exists");
    }

	/**
	 * Private method to find a Stage object based on its unique ID.
	 * <p>
	 *     Iterates through the 'races' ArrayList, and again through the
	 *     race's Stages (through the 'getStages()' getter method), until the
	 *     Stage matching the ID is found.
	 * </p>
	 *
	 * @param id The ID of the stage to be found.
	 * @return A Stage object, corresponding to the unique ID provided.
	 * @throws IDNotRecognisedException If the ID does not match any Stage in
	 * the system.
	 * 
	 */
    private Stage getStageById(int id) throws IDNotRecognisedException {
        for (Race race : races) {
            for (Stage stage : race.getStages()) {
				// Loops through every Stage in the CyclingPortal
				// If an ID match is found, returns this Stage
    			if (stage.getId() == id) {
    				return stage;
    			}
    		}
        }
        throw new IDNotRecognisedException("No stage with an ID of " + id + " exists");
    }

	/**
	 * Private method to find a Segment object based on its unique ID.
	 * <p>
	 *     Iterates through the 'races' ArrayList, and again through the
	 *     race's Stages (through the 'getStages()' getter method), and again
	 *     through the stage's Segments (through the 'getSegments()' getter
	 *     method), until the Segment matching the ID is found.
	 * </p>
	 *
	 * @param id The ID of the segment to be found.
	 * @return A Segment object, corresponding to the unique ID provided.
	 * @throws IDNotRecognisedException If the ID does not match any Segment
	 * in the system.
	 * 
	 */
	private Segment getSegmentById(int id) throws IDNotRecognisedException {
		for (Race race : races) {
			for (Stage stage : race.getStages()) {
				for (Segment segment : stage.getSegments()) {
					// Loops through every Segment in the CyclingPortal
					// If an ID match is found, returns this Segment
					if (segment.getId() == id) {
						return segment;
					}
				}
			}
		}
		throw new IDNotRecognisedException("No segment with an ID of " + id + " exists");
	}

	/**
	 * Private method to find a Team object based on its unique ID.
	 * <p>
	 *     Iterates through the 'teams' ArrayList, until the Team matching
	 *     the ID is found.
	 * </p>
	 *
	 * @param id The ID of the team to be found.
	 * @return A Team object, corresponding to the unique ID provided.
	 * @throws IDNotRecognisedException If the ID does not match any Team in
	 * the system.
	 * 
	 */
	private Team getTeamById(int id) throws IDNotRecognisedException {
		for (Team team : teams) {
			// Loops through every Team in the CyclingPortal
			// If an ID match is found, returns this Team
			if (team.getId() == id) {
				return team;
			}
		}
		throw new IDNotRecognisedException("No team with an ID of " + id + " exists");
	}

	/**
	 * Private method to find a Rider object based on its unique ID.
	 * <p>
	 *     Iterates through the 'teams' ArrayList, and again through the
	 *     team's riders (through the 'getRiders()' getter method), until the
	 *     Rider matching the ID is found.
	 * </p>
	 *
	 * @param id The ID of the rider to be found.
	 * @return A Rider object, corresponding to the unique ID provided.
	 * @throws IDNotRecognisedException If the ID does not match any Rider in
	 * the system.
	 * 
	 */
	private Rider getRiderById(int id) throws IDNotRecognisedException {
		for (Team team : teams) {
			for (Rider rider : team.getRiders()) {
				// Loops through every Rider in the CyclingPortal
				// If an ID match is found, returns this Rider
				if (rider.getId() == id) {
					return rider;
				}
			}
		}
		throw new IDNotRecognisedException("No rider with an ID of " + id + " exists");
	}

	/**
	 * Private method to find a Race object based on the ID of a Stage object
	 * inside the race.
	 * <p>
	 *     Iterates through the 'races' ArrayList, and again through the
	 *     race's stages (through the 'getStages()' getter method), until a
	 *     Stage matching the ID is found.
	 * </p>
	 *
	 * @param id The ID of the stage contained in the race to be found.
	 * @return A Race object, corresponding to the Stage ID provided.
	 * @throws IDNotRecognisedException If the ID does not match any Stages
	 * in the system.
	 * 
	 */
    private Race getRaceByStageId(int id) throws IDNotRecognisedException {
        for (Race race : races) {
            for (Stage stage : race.getStages()) {
				// Loops through every Stage in the CyclingPortal
				// If an ID match is found, returns the Race this Stage is in
    			if (stage.getId() == id) {
    				return race;
    			}
    		}
        }
        throw new IDNotRecognisedException("No stage with an ID of " + id + " exists");
    }

	/**
	 * Private method to find a Stage object based on the ID of a Segment object
	 * inside the Stage.
	 * <p>
	 *     Iterates through the 'races' ArrayList, and again through the
	 *     race's stages (through the 'getStages()' getter method), and again
	 *     through the stage's segments (through the 'getSegments()' getter
	 *     method) until a
	 *     Segment matching the ID is found.
	 * </p>
	 *
	 * @param id The ID of the segment contained in the stage to be found.
	 * @return A Stage object, corresponding to the Segment ID provided.
	 * @throws IDNotRecognisedException If the ID does not match any Segments
	 * in the system.
	 *
	 */
	private Stage getStageBySegmentId(int id) throws IDNotRecognisedException {
		for (Race race : races) {
			for (Stage stage : race.getStages()) {
				for (Segment segment : stage.getSegments()) {
					// Loops through every Segment in the CyclingPortal
					// If an ID match is found, returns the Stage this Segment is in
					if (segment.getId() == id) {
						return stage;
					}
				}
			}
		}
		throw new IDNotRecognisedException("No segment with an ID of " + id + " exists");
	}

	/**
	 * Private method to find a Team object based on the ID of a Rider object
	 * inside the Team.
	 * <p>
	 *     Iterates through the 'teams' ArrayList, and again through the
	 *     team's riders (through the 'getRiders()' getter method), until a
	 *     Rider matching the ID is found.
	 * </p>
	 *
	 * @param id The ID of the rider contained in the team to be found.
	 * @return A Team object, corresponding to the Rider ID provided.
	 * @throws IDNotRecognisedException If the ID does not match any Riders
	 * in the system.
	 *
	 */
	private Team getTeamByRiderId(int id) throws IDNotRecognisedException {
		for (Team team : teams) {
			for (Rider rider : team.getRiders()) {
				// Loops through every Rider in the CyclingPortal
				// If an ID match is found, returns the Team this Rider is in
				if (rider.getId() == id) {
					return team;
				}
			}
		}
		throw new IDNotRecognisedException("No rider with an ID of " + id + " exists");
	}

	/**
	 * Private method to calculate the elapsed time a rider took in a stage.
	 *
	 * @param stage The stage in question.
	 * @param rider The particular rider who's elapsed time we want to find.
	 * @return A LocalTime object in the form HH:MM:SS:nn which represents
	 * the elapsed time the rider took to complete the stage.
	 *
	 */
	private LocalTime getElapsedTime(Stage stage, Rider rider) {
		// Finds the appropriate StageResult object
		StageResult result = getResultInStage(rider, stage);
		// If this StageResult exists
		if (result != null) {
			LocalTime[] checkpoints = result.getCheckpoints();
			// Returns the difference in time between the first and last checkpoint
			// i.e. between the start and finish of the stage
			return timeDifference(checkpoints[0], checkpoints[checkpoints.length-1]);
		} else {
			return null;
		}
	}

	/**
	 * Private method to return an ArrayList of riders in a particular stage.
	 *
	 * @param stage The stage in question.
	 * @return An ArrayList of Rider objects, all of which have results for
	 * the stage in question.
	 *
	 */
	private ArrayList<Rider> getRidersInStage(Stage stage) {
		// Initalises a list to hold the riders in the stage
		ArrayList<Rider> riders = new ArrayList<>();
		for (Team team : teams) {
			for (Rider rider : team.getRiders()) {
				// Loops through every Rider in the CyclingPortal
				// If the Rider has a StageResult for this Stage, it is added to the list
				if (getResultInStage(rider, stage) != null) {
					riders.add(rider);
				}
			}
		}
		return riders;
	}

	/**
	 * Private method to return an ArrayList of riders in a particular race.
	 *
	 * @param race The race in question.
	 * @return An ArrayList of Rider objects, all of which have results for
	 * the race in question.
	 *
	 */
	private ArrayList<Rider> getRidersInRace(Race race) {
		// Initalises a list to hold the riders in the race
		ArrayList<Rider> ridersInRace = new ArrayList<Rider>();
		for (Stage stage : race.getStages()) {
			// Forms a list of riders in each stage
			ArrayList<Rider> ridersInStage = getRidersInStage(stage);
			for (Rider rider : ridersInStage) {
				// Adds any new rider found into the final list
				if (!ridersInRace.contains(rider)) {
					ridersInRace.add(rider);
				}
			}
		}
		return ridersInRace;
	}
}
