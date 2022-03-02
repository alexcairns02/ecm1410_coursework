package cycling;

import java.io.IOException;
import java.net.IDN;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

/*
	https://vle.exeter.ac.uk/pluginfile.php/2463307/mod_label/intro/coursework_v2.pdf

    TODO Implement functionality for each method
 */

public class CyclingPortal implements CyclingPortalInterface {

    private ArrayList<Race> races = new ArrayList<Race>();

	private ArrayList<Team> teams = new ArrayList<Team>();

	@Override
	public int[] getRaceIds() {
        int[] raceIds = new int[races.size()];
        for (int i=0; i<races.size(); i++) {
            raceIds[i] = races.get(i).getId();
        }
		return raceIds;
	}

	@Override
	public int createRace(String name, String description) throws IllegalNameException, InvalidNameException {
        if (name == null) { throw new InvalidNameException("Race name cannot be null"); }
		if (name.isEmpty()) { throw new InvalidNameException("Race name cannot be an empty string"); }
		if (name.length() > 30) { throw new InvalidNameException("Race name cannot be greater than 30 characters"); }
        if (name.contains(" ")) { throw new InvalidNameException("Race name cannot contain white space"); }
        for (Race race : races) {
            if (race.getName().equals(name)) {
                throw new IllegalNameException("Race name " + name + " already exists");
            }
        }
        Race race = new Race(name, description);
        races.add(race);
		return race.getId();
	}

	@Override
	public String viewRaceDetails(int raceId) throws IDNotRecognisedException {
        return getRaceById(raceId).getDetails();
	}

	@Override
	public void removeRaceById(int raceId) throws IDNotRecognisedException {
		races.remove(getRaceById(raceId));
	}

	@Override
	public int getNumberOfStages(int raceId) throws IDNotRecognisedException {
		return getRaceById(raceId).getNoOfStages();
	}

	@Override
	public int addStageToRace(int raceId, String stageName, String description, double length, LocalDateTime startTime, StageType type)
			throws IDNotRecognisedException, IllegalNameException, InvalidNameException, InvalidLengthException {
        if (stageName == null) { throw new InvalidNameException("Stage name cannot be null"); }
		if (stageName.isEmpty()) { throw new InvalidNameException("Stage name cannot be an empty string"); }
		if (stageName.length() > 30) { throw new InvalidNameException("Stage name cannot be greater than 30 characters"); }
        if (length<5) { throw new InvalidLengthException("Stage length cannot be less than 5(km)"); }
        Race raceToAddTo = null;
        for (Race race : races) {
            for (Stage stage : race.getStages()) {
                if (stage.getName().equals(stageName)) {
                    throw new IllegalNameException("Stage name " + stageName + " already exists");
                }
            }
            if (race.getId() == raceId) { raceToAddTo = race; }
        }
        if (raceToAddTo == null) { throw new IDNotRecognisedException("No race with an ID of " + Integer.toString(raceId) + " exists"); }
        Stage stage = new Stage(stageName, description, length, startTime, type);
        raceToAddTo.addStage(stage);
        return stage.getId();
	}

	@Override
	public int[] getRaceStages(int raceId) throws IDNotRecognisedException {
        Race race = getRaceById(raceId);
        Stage[] stages = race.getStages();
        int[] stageIds = new int[stages.length];
        for (int i=0; i<stages.length; i++) {
            stageIds[i] = stages[i].getId();
        }
		return stageIds;
	}

	@Override
	public double getStageLength(int stageId) throws IDNotRecognisedException {
		return getStageById(stageId).getLength();
	}

	@Override
	public void removeStageById(int stageId) throws IDNotRecognisedException {
		getRaceByStageId(stageId).removeStage(getStageById(stageId));
	}

	@Override
	public int addCategorizedClimbToStage(int stageId, Double location, SegmentType type, Double averageGradient,
			Double length) throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException,
			InvalidStageTypeException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int addIntermediateSprintToStage(int stageId, double location) throws IDNotRecognisedException,
			InvalidLocationException, InvalidStageStateException, InvalidStageTypeException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void removeSegment(int segmentId) throws IDNotRecognisedException, InvalidStageStateException {
		// TODO Auto-generated method stub

	}

	@Override
	public void concludeStagePreparation(int stageId) throws IDNotRecognisedException, InvalidStageStateException {
		// TODO Auto-generated method stub

	}

	@Override
	public int[] getStageSegments(int stageId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int createTeam(String name, String description) throws IllegalNameException, InvalidNameException {
		if (name == null) { throw new IllegalNameException("Team name cannot be null"); }
		if (name.isEmpty()) { throw new IllegalNameException("Team name cannot be an empty string"); }
		if (name.length() > 30) { throw new IllegalNameException("Team name cannot be greater than 30 characters"); }
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getTeamRiders(int teamId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub

	}

	@Override
	public LocalTime[] getRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub

		return null;
	}

	@Override
	public LocalTime getRiderAdjustedElapsedTimeInStage(int stageId, int riderId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub

	}

	@Override
	public int[] getRidersRankInStage(int stageId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LocalTime[] getRankedAdjustedElapsedTimesInStage(int stageId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersPointsInStage(int stageId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersMountainPointsInStage(int stageId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void eraseCyclingPortal() {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveCyclingPortal(String filename) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadCyclingPortal(String filename) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeRaceByName(String name) throws NameNotRecognisedException {
		// TODO Auto-generated method stub

	}

	@Override
	public LocalTime[] getGeneralClassificationTimesInRace(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersPointsInRace(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersMountainPointsInRace(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersGeneralClassificationRank(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersPointClassificationRank(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getRidersMountainPointClassificationRank(int raceId) throws IDNotRecognisedException {
		// TODO Auto-generated method stub
		return null;
	}

    private Race getRaceById(int id) throws IDNotRecognisedException {
        for (Race race : races) {
			if (race.getId() == id) {
				return race;
			}
		}
        throw new IDNotRecognisedException("No race with an ID of " + Integer.toString(id) + " exists");
    }

    private Stage getStageById(int id) throws IDNotRecognisedException {
        for (Race race : races) {
            for (Stage stage : race.getStages()) {
    			if (stage.getId() == id) {
    				return stage;
    			}
    		}
        }
        throw new IDNotRecognisedException("No stage with an ID of " + Integer.toString(id) + " exists");
    }

	private Team getTeamById(int id) throws IDNotRecognisedException {
		for (Team team : teams) {
			if (team.getId() == id) {
				return team;
			}
		}
		throw new IDNotRecognisedException("No team with an ID of " + Integer.toString(id) + " exists");
	}

	private Rider getRiderById(int id) throws IDNotRecognisedException {
		for (Team team : teams) {
			for (Rider rider : team.getRiders()) {
				if (rider.getId() == id) {
					return rider;
				}
			}
		}
		throw new IDNotRecognisedException("No rider with an ID of " + Integer.toString(id) + " exists");
	}

    private Race getRaceByStageId(int id) throws IDNotRecognisedException {
        for (Race race : races) {
            for (Stage stage : race.getStages()) {
    			if (stage.getId() == id) {
    				return race;
    			}
    		}
        }
        throw new IDNotRecognisedException("No stage with an ID of " + Integer.toString(id) + " exists");
    }

	private Team getTeamByRiderId(int id) throws IDNotRecognisedException {
		for (Team team : teams) {
			for (Rider rider : team.getRiders()) {
				if (rider.getId() == id) {
					return team;
				}
			}
		}
		throw new IDNotRecognisedException("No rider with an ID of " + Integer.toString(id) + " exists");
	}
}
