package com.konrad.rudnicki.repository;

import com.konrad.rudnicki.model.mission.Mission;
import com.konrad.rudnicki.model.mission.MissionStatus;
import com.konrad.rudnicki.model.rocket.Rocket;
import com.konrad.rudnicki.model.rocket.RocketStatus;

import java.util.List;

public interface DragonRocketRepository {

    Rocket addNewRocket(String rocketName);
    Mission addNewMission(String missionName);

    Rocket getRocket(String name);
    Mission getMission(String name);

    void updateRocketStatus(String rocketName, RocketStatus status);
    void updateMissionStatus(String missionName, MissionStatus status);

    void assignRocket(String rocketName, String missionName);
    void assignRocketsToMission(List<String> rocketsToAssign, String missionName);

    List<Mission> getMissionSummary();
}
