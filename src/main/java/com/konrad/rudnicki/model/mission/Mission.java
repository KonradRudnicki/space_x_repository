package com.konrad.rudnicki.model.mission;

import com.konrad.rudnicki.model.rocket.Rocket;
import com.konrad.rudnicki.model.rocket.RocketStatus;

import java.util.ArrayList;
import java.util.List;

public class Mission {

    private final String name;
    private MissionStatus status;
    private List<Rocket> rockets;

    public Mission(String name) {
        this.name = name;
        this.status = MissionStatus.SCHEDULED;
        this.rockets = new ArrayList<Rocket>();
    }

    public boolean isEnded() {
        return status == MissionStatus.ENDED;
    }

    public void addRocket(Rocket rocket) {
        this.rockets.add(rocket);

        if (rocket.isInRepair())
            this.status = MissionStatus.PENDING;
        else {
            this.status = MissionStatus.IN_PROGRESS;
        }
    }

    public void updateStatus() {
        if (rockets.isEmpty()) {
            status = MissionStatus.ENDED;
            return;
        }

        long rocketsInRepairCount = this.rockets.stream().filter(Rocket::isInRepair).count();
        if (rocketsInRepairCount >= 1)
            this.status = MissionStatus.PENDING;
        else {
            this.status = MissionStatus.IN_PROGRESS;
        }
    }

    public String getName() {
        return name;
    }

    public MissionStatus getStatus() {
        return status;
    }

    public void setStatus(MissionStatus status) {
        this.status = status;
    }

    public List<Rocket> getRockets() {
        return rockets;
    }

    public void setRockets(List<Rocket> rockets) {
        this.rockets = rockets;
    }
}
