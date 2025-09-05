package com.konrad.rudnicki.repository;

import com.konrad.rudnicki.model.mission.Mission;
import com.konrad.rudnicki.model.mission.MissionStatus;
import com.konrad.rudnicki.model.rocket.Rocket;
import com.konrad.rudnicki.model.rocket.RocketStatus;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class DragonRocketRepositoryInMemory implements DragonRocketRepository {

    private final HashMap<String, Mission> missions;
    private final HashMap<String, Rocket> rockets;

    public DragonRocketRepositoryInMemory() {
        this.missions = new HashMap<>();
        this.rockets = new HashMap<>();
    }

    @Override
    public Rocket addNewRocket(String rocketName) {
        if(rockets.containsKey(rocketName))
            throw new IllegalArgumentException("Rockets with name " + rocketName + " already exists");

        Rocket rocket = new Rocket(rocketName);
        this.rockets.put(rocketName, rocket);

        return rocket;
    }

    @Override
    public Mission addNewMission(String missionName) {
        if(missions.containsKey(missionName))
            throw new IllegalArgumentException("Mission with name " + missionName + " already exists");

        Mission mission = new Mission(missionName);
        this.missions.put(missionName, mission);

        return mission;
    }

    @Override
    public Rocket getRocket(String name) {
        return rockets.get(name);
    }

    @Override
    public Mission getMission(String name) {
        return missions.get(name);
    }

    private Rocket getRocketOrThrow(String name) {
        Rocket r = rockets.get(name);
        if (r == null) throw new IllegalArgumentException("Rocket not found: " + name);
        return r;
    }

    private List<Rocket> getRocketsOrThrow(List<String> names) {
        if (names == null || names.isEmpty()) {
            throw new IllegalArgumentException("Rocket names list must not be null or empty");
        }

        List<Rocket> result = new ArrayList<>();
        for (String name : names) {
            Rocket r = getRocketOrThrow(name);
            result.add(r);
        }

        return result;
    }

    private Mission getMissionOrThrow(String name) {
        Mission m = missions.get(name);
        if (m == null) throw new IllegalArgumentException("Mission not found: " + name);
        return m;
    }

    @Override
    public void updateRocketStatus(String rocketName, RocketStatus status) {
        Rocket rocket = getRocketOrThrow(rocketName);
        rocket.setStatus(status);

        if (rocket.hasMission()) {
            rocket.getMission().updateStatus();
        }
    }

    @Override
    public void updateMissionStatus(String missionName, MissionStatus status) {
        Mission mission = getMissionOrThrow(missionName);
        mission.setStatus(status);
    }

    @Override
    public void assignRocket(String rocketName, String missionName) {
        Rocket rocket = getRocketOrThrow(rocketName);
        Mission mission = getMissionOrThrow(missionName);

        assignRocket(rocket, mission);
    }

    @Override
    public void assignRocketsToMission(List<String> rocketsToAssign, String missionName) {
        Mission mission = getMissionOrThrow(missionName);
        List<Rocket> rockets = getRocketsOrThrow(rocketsToAssign);

        for (Rocket rocket : rockets) {
            assignRocket(rocket, mission);
        }
    }

    private void assignRocket(Rocket rocket, Mission mission) {
        if (mission.isEnded()) {
            throw new IllegalStateException("Mission with name " + mission.getName() + " has already ended");
        }

        if (rocket.hasMission()) {
            throw new IllegalStateException("Rocket with name " + rocket.getName() +
                    " is already assigned to mission " + rocket.getMission().getName());
        }

        mission.addRocket(rocket);
        rocket.setMission(mission);
    }

    @Override
    public List<Mission> getMissionSummary() {
        return missions.values().stream()
                .sorted(
                        Comparator
                                .comparingInt((Mission m) -> m.getRockets() == null ? 0 : m.getRockets().size())
                                .reversed()
                                .thenComparing(Mission::getName, Comparator.reverseOrder())
                ).collect(Collectors.toList());
    }
}
