package com.konrad.rudnicki.repository;

import com.konrad.rudnicki.model.mission.Mission;
import com.konrad.rudnicki.model.mission.MissionStatus;
import com.konrad.rudnicki.model.rocket.Rocket;
import com.konrad.rudnicki.model.rocket.RocketStatus;

import java.util.HashMap;
import java.util.List;

public class DragonRocketRepositoryInMemory implements DragonRocketRepository {

    private final HashMap<String, Mission> missions;
    private final HashMap<String, Rocket> rockets;

    public DragonRocketRepositoryInMemory() {
        this.missions = new HashMap<>();
        this.rockets = new HashMap<>();
    }

    @Override
    public Rocket addNewRocket(String rocketName) {
        return null;
    }

    @Override
    public Mission addNewMission(String missionName) {
        return null;
    }

    @Override
    public Rocket getRocket(String name) {
        return null;
    }

    @Override
    public Mission getMission(String name) {
        return null;
    }

    @Override
    public void updateRocketStatus(String rocketName, RocketStatus status) {

    }

    @Override
    public void updateMissionStatus(String missionName, MissionStatus status) {

    }

    @Override
    public void assignRocket(String rocketName, String missionName) {

    }

    @Override
    public void assignRocketsToMission(List<Rocket> rockets, Mission mission) {

    }

    @Override
    public List<Mission> getMissionSummary() {
        return List.of();
    }
}
