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

import javax.naming.spi.DirStateFactory.Result;

/**
 * CyclingPortal class which implements CyclingPortalInterface. The
 * no-argument constructor of this class initialises the CyclingPortal
 * as an empty platform with no initial racing teams nor races within it.
 */
public class CyclingPortal implements CyclingPortalInterface {

	// pointsTable[type][rank]
	private final int[][] pointsTable = {
		{50, 30, 20, 18, 16, 14, 12, 10, 8, 7, 6, 5, 4, 3, 2},
		{30, 25, 22, 19, 17, 15, 13, 11, 9, 7, 6, 5, 4, 3, 2},
		{20, 17, 15, 13, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1}
	};

	// mountainPointsTable[rank][type]
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

    private ArrayList<Race> races = new ArrayList<Race>();

	private ArrayList<Team> teams = new ArrayList<Team>();

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
			throw new IDNotRecognisedException("No race with an ID of "+ Integer.toString(raceId) + " exists");
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
		if (stage.isPrepared()) {
			throw new InvalidStageStateException("Stage is already 'waiting " +
					"for results'");
		}
		if (location > stage.getLength() || location < 0) {
			throw new InvalidLocationException("Location out of bounds");
		}
		if (stage.getType() == StageType.TT) {
			throw new InvalidStageTypeException("Time-trial stages cannot " +
					"contain segments");
		}
		Segment segment = new Segment(location, type, averageGradient, length);

		stage.addSegment(segment);
		return segment.getId();
	}

	@Override
	public int addIntermediateSprintToStage(int stageId, double location) throws IDNotRecognisedException,
			InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {
		Stage stage = getStageById(stageId);
		if (stage.isPrepared()) {
			throw new InvalidStageStateException("Stage is already 'waiting " +
					"for results'");
		}
		if (location > stage.getLength() || location < 0) {
			throw new InvalidLocationException("Location out of bounds");
		}
		if (stage.getType() == StageType.TT) {
			throw new InvalidStageTypeException("Time-trial stages cannot " +
					"contain segments");
		}
		Segment segment = new Segment(location, SegmentType.SPRINT);
		stage.addSegment(segment);
		return segment.getId();
	}

	@Override
	public void removeSegment(int segmentId) throws IDNotRecognisedException, InvalidStageStateException {
		Stage stage = getStageBySegmentId(segmentId);
		if (stage.isPrepared()) {
			throw new InvalidStageStateException("Stage is already 'waiting " +
					"for results'");
		}
		stage.removeSegment(getSegmentById(segmentId));
	}

	@Override
	public void concludeStagePreparation(int stageId) throws IDNotRecognisedException, InvalidStageStateException {
		Stage stage = getStageById(stageId);
		if (stage.isPrepared()) {
			throw new InvalidStageStateException("Stage is already 'waiting " +
					"for results'");
		}
		stage.prepare();
	}

	@Override
	public int[] getStageSegments(int stageId) throws IDNotRecognisedException {
		Stage stage = getStageById(stageId);
		Segment[] segments = stage.getSegments();
		int[] segmentIds = new int[segments.length];
		for (int i=0; i<segments.length; i++) {
			segmentIds[i] = segments[i].getId();
		}
		return segmentIds;
	}

	@Override
	public int createTeam(String name, String description) throws IllegalNameException, InvalidNameException {
		if (name == null) { throw new IllegalNameException("Team name cannot be null"); }
		if (name.isEmpty()) { throw new IllegalNameException("Team name cannot be an empty string"); }
		if (name.length() > 30) { throw new IllegalNameException("Team name cannot be greater than 30 characters"); }
        if (name.contains(" ")) { throw new InvalidNameException("Team name cannot contain white space"); }


		for (Team team : teams) {
			if (team.getName() == name) {
				throw new IllegalNameException("Team with name \"" + name + "\" already exists");
			}
		}
		Team team = new Team(name, description);
		teams.add(team);
		return team.getId();
	}

	@Override
	public void removeTeam(int teamId) throws IDNotRecognisedException {
		teams.remove(getTeamById(teamId));
	}

	@Override
	public int[] getTeams() {
		int[] teamIds = new int[teams.size()];
		for (int i=0; i<teams.size(); i++) {
			teamIds[i] = teams.get(i).getId();
		}
		return teamIds;
	}

	@Override
	public int[] getTeamRiders(int teamId) throws IDNotRecognisedException {
		Rider[] riders = getTeamById(teamId).getRiders();
		int[] riderIds = new int[riders.length];
		for (int i=0; i<riders.length; i++) {
			riderIds[i] = riders[i].getId();
		}
		return riderIds;
	}

	@Override
	public int createRider(int teamID, String name, int yearOfBirth)
			throws IDNotRecognisedException, IllegalArgumentException {
		if (name == null) { throw new IllegalArgumentException("Rider name cannot be null"); }
		if (yearOfBirth < 1900) { throw new IllegalArgumentException("Rider yearOfBirth cannot be less than 1900"); }
		Rider rider = new Rider(name, yearOfBirth);
		getTeamById(teamID).addRider(rider);
		return rider.getId();
	}

	@Override
	public void removeRider(int riderId) throws IDNotRecognisedException {
        getTeamByRiderId(riderId).removeRider(getRiderById(riderId));

	}

	@Override
	public void registerRiderResultsInStage(int stageId, int riderId, LocalTime... checkpoints)
			throws IDNotRecognisedException, DuplicatedResultException, InvalidCheckpointsException,
			InvalidStageStateException {
		Stage stage = getStageById(stageId);
		if (stage.isPrepared()) {
			throw new InvalidStageStateException("Stage is already 'waiting " +
					"for results'");
		}
		if (checkpoints.length != stage.getSegments().length+2) {
			throw new InvalidCheckpointsException("Number of checkpoints " +
					"must be number of segments + 2");
		}
		StageResult stageResult = new StageResult(stage,
				checkpoints);
		Rider rider = getRiderById(riderId);
		if (getResultInStage(rider, stage) != null) {
			throw new DuplicatedResultException("A result for this stage " +
					"already exists");
		}
		rider.addResult(stageResult);
	}

	@Override
	public LocalTime[] getRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		StageResult result = getResultInStage(getRiderById(riderId), getStageById(stageId));
		if (result == null) {
			throw new IDNotRecognisedException("Rider " + riderId + "does not have any results in stage " + stageId);
		} else {
			return result.getCheckpoints();
		}
	}

	@Override
	public LocalTime getRiderAdjustedElapsedTimeInStage(int stageId, int riderId) throws IDNotRecognisedException {
		Rider riderToFind = getRiderById(riderId);
		Stage stage = getStageById(stageId);
		ArrayList<Rider> ridersInStage = getRidersInStage(stage);
		HashMap<Rider, LocalTime> ridersAndTimes = new HashMap<>();
		for (Rider rider : ridersInStage) {
			ridersAndTimes.put(rider, getElapsedTime(stage, rider));
		}
		HashMap<Rider, LocalTime> sortedMap = sortRidersByTimes(ridersAndTimes);
		Rider[] sortedRidersInStage = sortedMap.keySet().toArray(new Rider[ridersInStage.size()]);
		LocalTime[] sortedTimes = sortedMap.values().toArray(new LocalTime[ridersInStage.size()]);
		int streak = 0;
		for (int i=0; i<sortedRidersInStage.length; i++) {
			if (i > 0) {
				LocalTime elapsedTime = sortedTimes[i];
				LocalTime prevElapsedTime = sortedTimes[i-1];
				long timeDifference = prevElapsedTime.until(elapsedTime, ChronoUnit.MILLIS);
				assert (timeDifference >= 0);
				if (timeDifference < 1000) {
					streak++;
				} else {
					streak = 0;
				}
			}
			if (sortedRidersInStage[i].equals(riderToFind)) {
				if (streak == 0) {
					return sortedTimes[i];
				}
				else {
					return getElapsedTime(stage, sortedRidersInStage[i-streak]);
				}
			}
		}
		// Return null if given rider does not exist in this stage
		return null;
	}

	@Override
	public void deleteRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		Rider rider = getRiderById(riderId);
		StageResult result = getResultInStage(rider, getStageById(stageId));
		if (result == null) {
			throw new IDNotRecognisedException("Rider " + riderId + "does not have any results in stage " + stageId);
		} else {
			rider.removeResult(result);
		}
	}

	@Override
	public int[] getRidersRankInStage(int stageId) throws IDNotRecognisedException {
		HashMap<Rider, LocalTime> riderToResultMap = new HashMap<Rider, LocalTime>();
		Stage stage = getStageById(stageId);
		for (Team team : teams) {
			for (Rider rider : team.getRiders()) {
				StageResult result = getResultInStage(rider, stage);
				if (result != null) {
					LocalTime elapsedTime = getRiderAdjustedElapsedTimeInStage(stageId, rider.getId());
					riderToResultMap.put(rider, elapsedTime);
				}
			}
		}
		HashMap<Rider, LocalTime> sortedMap = sortRidersByTimes(riderToResultMap);
		int[] rankedRiders = new int[sortedMap.size()];
		int i = 0;
		for (Rider rider : sortedMap.keySet()) {
			rankedRiders[i] = rider.getId();
			i++;
		}
		return rankedRiders;
	}

	@Override
	public LocalTime[] getRankedAdjustedElapsedTimesInStage(int stageId) throws IDNotRecognisedException {
		int[] rankedRiderIds = getRidersRankInStage(stageId);
		LocalTime[] rankedTimes = new LocalTime[rankedRiderIds.length];
		for (int i=0;i<rankedRiderIds.length;i++) {
			rankedTimes[i] = getRiderAdjustedElapsedTimeInStage(stageId, rankedRiderIds[i]);
		}
		return rankedTimes;
	}

	@Override
	public int[] getRidersPointsInStage(int stageId) throws IDNotRecognisedException {
		// Retrieves a list of rider IDs in order of their rank
		int[] rankedRiders = getRidersRankInStage(stageId);
		StageType type = getStageById(stageId).getType();
		// Initialises a new array to contain points for each rider
		int[] points = new int[rankedRiders.length];
		for (int i=0;i<points.length;i++) {
			if (i > 14) {
				// If the rider ranked 15th or more, they get no points
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
		}
		return points;
	}

	@Override
	public int[] getRidersMountainPointsInStage(int stageId) throws IDNotRecognisedException {
		Stage stage = getStageById(stageId);
		Segment[] segments = stage.getSegments();
		int[] ridersRanks = getRidersRankInStage(stageId);
		Rider[] ridersInStage = new Rider[ridersRanks.length];
		for (int i=0;i<ridersInStage.length;i++) {
			ridersInStage[i] = getRiderById(ridersRanks[i]);
		}
		int[] mountainPoints = new int[ridersInStage.length];
		Arrays.fill(mountainPoints, 0);
		for (int i=0;i<segments.length;i++) {
			SegmentType type = segments[i].getType();
			if (type == SegmentType.SPRINT) {
				continue;
			}
			HashMap<Rider, LocalTime> resultToTimeMap = new HashMap<Rider, LocalTime>();
			for (Rider rider : ridersInStage) {
				StageResult result = getResultInStage(rider, stage);
				if (result != null) {
					LocalTime[] checkpoints = result.getCheckpoints();
					LocalTime segmentTime = timeDifference(checkpoints[i], checkpoints[i+1]);
					resultToTimeMap.put(rider, segmentTime);
				}
			}
			HashMap<Rider, LocalTime> sortedMap = sortRidersByTimes(resultToTimeMap);
			int a = 0;
			for (Rider rider : sortedMap.keySet()) {
				for (int b=0;b<8;b++) {
					if (ridersInStage[b].equals(rider)) {
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
		ArrayList<Rider> riders = getRidersInRace(race);
		HashMap<Rider, LocalTime> riderTimes = new HashMap<Rider, LocalTime>();
		for (Rider rider : riders) {
			riderTimes.put(rider, LocalTime.of(0, 0, 0));
			for (Stage stage : stages) {
				LocalTime t = getRiderAdjustedElapsedTimeInStage(stage.getId(),
						rider.getId());
				if (t != null) {
					riderTimes.replace(rider,
						riderTimes.get(rider).plusHours(t.getHour())
								.plusMinutes(t.getMinute())
								.plusSeconds(t.getSecond())
								.plusNanos(t.getNano()));
				}

				}
		}
		HashMap<Rider, LocalTime> sortedMap = sortRidersByTimes(riderTimes);
		LocalTime[] times = new LocalTime[riders.size()];
		sortedMap.values().toArray(times);
		return times;
	}

	@Override
	public int[] getRidersPointsInRace(int raceId) throws IDNotRecognisedException {
		Race race = getRaceById(raceId);
		Stage[] stages = race.getStages();
		ArrayList<Rider> riders = getRidersInRace(race);
		Map<Rider, Integer> riderPoints = new HashMap<Rider, Integer>();
		for (Rider rider : riders) {
			riderPoints.put(rider, 0);
			for (Stage stage : stages) {
				int[] ranks = getRidersRankInStage(stage.getId());
				int index = 0;
				for (int i=0; i<ranks.length; i++) {
					if (ranks[i] == rider.getId()) {
						index = i;
					}
				}
				int points = getRidersPointsInStage(stage.getId())[index];
				riderPoints.replace(rider, riderPoints.get(rider) + points);
			}
		}
		int[] sortedPoints = new int[riders.size()];
		int[] riderRanks = getRidersGeneralClassificationRank(raceId);
		for ( int i=0; i<riderRanks.length; i++ ) {
			sortedPoints[i] = riderPoints.get(getRiderById(riderRanks[i]));
		}
		return sortedPoints;
	}

	@Override
	public int[] getRidersMountainPointsInRace(int raceId) throws IDNotRecognisedException {
		Race race = getRaceById(raceId);
		Stage[] stages = race.getStages();
		ArrayList<Rider> riders = getRidersInRace(race);
		Map<Rider, Integer> riderPoints = new HashMap<Rider, Integer>();
		for (Rider rider : riders) {
			riderPoints.put(rider, 0);
			for (Stage stage : stages) {
				int[] ranks = getRidersRankInStage(stage.getId());
				int index = 0;
				for (int i=0; i<ranks.length; i++) {
					if (ranks[i] == rider.getId()) {
						index = i;
					}
				}
				int points =
						getRidersMountainPointsInStage(stage.getId())[index];
				riderPoints.replace(rider, riderPoints.get(rider) + points);
			}
		}
		int[] sortedPoints = new int[riders.size()];
		int[] riderRanks = getRidersGeneralClassificationRank(raceId);
		for ( int i=0; i<riderRanks.length; i++ ) {
			sortedPoints[i] = riderPoints.get(getRiderById(riderRanks[i]));
		}
		return sortedPoints;
	}

	@Override
	public int[] getRidersGeneralClassificationRank(int raceId) throws IDNotRecognisedException {
		Race race = getRaceById(raceId);
		Stage[] stages = race.getStages();
		ArrayList<Rider> riders = getRidersInRace(race);
		HashMap<Rider, LocalTime> riderTimes = new HashMap<Rider, LocalTime>();
		for (Rider rider : riders) {
			riderTimes.put(rider, LocalTime.of(0, 0, 0));
			for (Stage stage : stages) {
				LocalTime t = getRiderAdjustedElapsedTimeInStage(stage.getId(),
						rider.getId());
				riderTimes.replace(rider,
						riderTimes.get(rider).plusHours(t.getHour())
								.plusMinutes(t.getMinute())
								.plusSeconds(t.getSecond()) );
			}
		}
		HashMap<Rider, LocalTime> sortedMap = sortRidersByTimes(riderTimes);
		int[] riderIds = new int[riders.size()];
		Set<Rider> keys = sortedMap.keySet();
		int i = 0;
		for ( Rider key : keys ) {
			riderIds[i] = key.getId();
			i++;
		}
		return riderIds;
	}

	@Override
	public int[] getRidersPointClassificationRank(int raceId) throws IDNotRecognisedException {
		int[] riderRanks = getRidersGeneralClassificationRank(raceId);
		int[] riderPointsSortedByTime = getRidersPointsInRace(raceId);
		HashMap<Rider, Integer> riderPoints = new HashMap<>();
		for (int i=0; i<riderRanks.length;i++) {
			riderPoints.put(getRiderById(riderRanks[i]),
					riderPointsSortedByTime[i]);
		}
		HashMap<Rider, Integer> sortedMap = sortRidersByPoints(riderPoints);
		int[] riderIds = new int[riderRanks.length];
		Set<Rider> keys = sortedMap.keySet();
		int i = 0;
		for ( Rider key : keys ) {
			riderIds[i] = key.getId();
			i++;
		}
		for (int j = 0; j < riderIds.length / 2; j++) {
			int temp = riderIds[j];
			riderIds[j] = riderIds[riderIds.length - 1 - j];
			riderIds[riderIds.length - 1 - j] = temp;
		}
		return riderIds;
	}

	@Override
	public int[] getRidersMountainPointClassificationRank(int raceId) throws IDNotRecognisedException {
		int[] riderRanks = getRidersGeneralClassificationRank(raceId);
		int[] riderPointsSortedByTime = getRidersMountainPointsInRace(raceId);
		HashMap<Rider, Integer> riderPoints = new HashMap<>();
		for (int i=0; i<riderRanks.length;i++) {
			riderPoints.put(getRiderById(riderRanks[i]),
					riderPointsSortedByTime[i]);
		}
		HashMap<Rider, Integer> sortedMap = sortRidersByPoints(riderPoints);
		int[] riderIds = new int[riderRanks.length];
		Set<Rider> keys = sortedMap.keySet();
		int i = 0;
		for ( Rider key : keys ) {
			riderIds[i] = key.getId();
			i++;
		}
		return riderIds;
	}

	/**
	 * Public getter method to return a list of all teams stored in the
	 * 'teams' ArrayList.
	 * @return An ArrayList of team objects.
	 */
	public ArrayList<Team> getTeamsList() {
		return teams;
	}

	/**
	 * Public getter method to return a list of all races stored in the
	 * 'races' ArrayList.
	 * @return An ArrayList of race objects.
	 */
	public ArrayList<Race> getRacesList() {
		return races;
	}

	/**
	 * Private method to find the time difference between two local times,
	 * outputting the result as a LocalTime object.
	 * @param time1 LocalTime object.
	 * @param time2 LocalTime object.
	 * @return LocalTime object, in format HH:MM:SS:mm, representing
	 * difference between time1 and time2.
	 */
	private LocalTime timeDifference(LocalTime time1, LocalTime time2) {
		long elapsedTimeInMilliSecs = time1.until(time2, ChronoUnit.MILLIS);
		int hours = (int) elapsedTimeInMilliSecs / 3600000;
		int mins = (int) (elapsedTimeInMilliSecs % 3600000) / 60000;
		int secs = (int) ((elapsedTimeInMilliSecs % 3600000) % 60000) / 1000;
		int millis = (int) ((elapsedTimeInMilliSecs % 3600000) % 60000) % 1000;
		return LocalTime.of(hours, mins, secs, millis);
	}

	/**
	 * Private method to sort a HashMap of Rider : LocalTime by their
	 * adjusted elapsed times (LocalTime).
	 * @param initalMap The HashMap to be sorted.
	 * @return A HashMap object sorted by the LocalTime value (descending).
	 */
	private HashMap<Rider, LocalTime> sortRidersByTimes(HashMap<Rider, LocalTime> initalMap) {
		return initalMap.entrySet().stream()
				.sorted(Entry.comparingByValue())
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue,
						(e1, e2) -> e1, LinkedHashMap::new));
	}

	/**
	 * Private method to sort a HashMap of Rider : Integer by the rider's
	 * points (integer).
	 * @param initalMap The HashMap to be sorted.
	 * @return A HashMap object sorted by the integer value (descending).
	 */
	private HashMap<Rider, Integer> sortRidersByPoints(HashMap<Rider, Integer> initalMap) {
		return initalMap.entrySet().stream()
				.sorted(Entry.comparingByValue())
				.collect(Collectors.toMap(Entry::getKey, Entry::getValue,
						(e1, e2) -> e1, LinkedHashMap::new));
	}

	private StageResult getResultInStage(Rider rider, Stage stage) {
		for (StageResult result : rider.getResults()) {
			if (result.getStage().equals(stage)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Private method to find a Race object based on its unique ID.
	 * <p>
	 *     Iterates through the 'races' ArrayList, checking each race's ID
	 *     through the 'getId()' getter method, until the race matching the ID
	 *     is found.
	 * </p>
	 * @param id The ID of the race to be found.
	 * @return A Race object, corresponding to the unique ID provided.
	 * @throws IDNotRecognisedException If the ID does not match any Race in
	 * the system.
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
	 * @param id The ID of the stage to be found.
	 * @return A Stage object, corresponding to the unique ID provided.
	 * @throws IDNotRecognisedException If the ID does not match any Stage in
	 * the system.
	 */
    private Stage getStageById(int id) throws IDNotRecognisedException {
        for (Race race : races) {
            for (Stage stage : race.getStages()) {
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
	 * @param id The ID of the segment to be found.
	 * @return A Segment object, corresponding to the unique ID provided.
	 * @throws IDNotRecognisedException If the ID does not match any Segment
	 * in the system.
	 */
	private Segment getSegmentById(int id) throws IDNotRecognisedException {
		for (Race race : races) {
			for (Stage stage : race.getStages()) {
				for (Segment segment : stage.getSegments()) {
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
	 * @param id The ID of the team to be found.
	 * @return A Team object, corresponding to the unique ID provided.
	 * @throws IDNotRecognisedException If the ID does not match any Team in
	 * the system.
	 */
	private Team getTeamById(int id) throws IDNotRecognisedException {
		for (Team team : teams) {
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
	 * @param id The ID of the rider to be found.
	 * @return A Rider object, corresponding to the unique ID provided.
	 * @throws IDNotRecognisedException If the ID does not match any Rider in
	 * the system.
	 */
	private Rider getRiderById(int id) throws IDNotRecognisedException {
		for (Team team : teams) {
			for (Rider rider : team.getRiders()) {
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
	 * @param id The ID of the stage contained in the race to be found.
	 * @return A Race object, corresponding to the Stage ID provided.
	 * @throws IDNotRecognisedException If the ID does not match any Stages
	 * in the system.
	 */
    private Race getRaceByStageId(int id) throws IDNotRecognisedException {
        for (Race race : races) {
            for (Stage stage : race.getStages()) {
    			if (stage.getId() == id) {
    				return race;
    			}
    		}
        }
        throw new IDNotRecognisedException("No stage with an ID of " + id + " exists");
    }

	private Stage getStageBySegmentId(int id) throws IDNotRecognisedException {
		for (Race race : races) {
			for (Stage stage : race.getStages()) {
				for (Segment segment : stage.getSegments()) {
					if (segment.getId() == id) {
						return stage;
					}
				}
			}
		}
		throw new IDNotRecognisedException("No segment with an ID of " + id + " exists");
	}

	private Team getTeamByRiderId(int id) throws IDNotRecognisedException {
		for (Team team : teams) {
			for (Rider rider : team.getRiders()) {
				if (rider.getId() == id) {
					return team;
				}
			}
		}
		throw new IDNotRecognisedException("No rider with an ID of " + id + " exists");
	}

	private LocalTime getElapsedTime(Stage stage, Rider rider) {
		StageResult result = getResultInStage(rider, stage);
		if (result != null) {
			LocalTime[] checkpoints = result.getCheckpoints();
			return timeDifference(checkpoints[0], checkpoints[checkpoints.length-1]);
		} else {
			return null;
		}
	}

	private ArrayList<Rider> getRidersInStage(Stage stage) {
		ArrayList<Rider> riders = new ArrayList<>();
		for (Team team : teams) {
			for (Rider rider : team.getRiders()) {
				if (getResultInStage(rider, stage) != null) {
					riders.add(rider);
				}
			}
		}
		return riders;
	}

	private ArrayList<Rider> getRidersInRace(Race race) {
		ArrayList<Rider> riders = new ArrayList<Rider>();
		for (Stage stage : race.getStages()) {
			ArrayList<Rider> ridersInStage = getRidersInStage(stage);
			for (Rider rider : ridersInStage) {
				if (!riders.contains(rider)) {
					riders.add(rider);
				}
			}
		}
		return riders;
	}
}
