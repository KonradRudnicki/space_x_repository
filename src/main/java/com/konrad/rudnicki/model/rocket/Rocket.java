package com.konrad.rudnicki.model.rocket;

import com.konrad.rudnicki.model.mission.Mission;

public class Rocket {

    private final String name;
    private RocketStatus status;
    private Mission mission;

    public Rocket(String name) {
        this.name = name;
        this.status = RocketStatus.ON_GROUND;
    }

    public boolean hasMission() {
        return mission != null;
    }

    public boolean isInRepair() {
        return status == RocketStatus.IN_REPAIR;
    }

    public String getName() {
        return name;
    }

    public RocketStatus getStatus() {
        return status;
    }

    public void setStatus(RocketStatus status) {
        this.status = status;
    }

    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
        this.status = RocketStatus.IN_SPACE;
    }

    @Override
    public String toString() {
        return name + " – " + status;
    }

}
