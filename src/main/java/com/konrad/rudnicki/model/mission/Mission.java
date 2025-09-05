package com.konrad.rudnicki.model.mission;

import com.konrad.rudnicki.model.rocket.Rocket;
import com.konrad.rudnicki.model.rocket.RocketStatus;

import java.util.ArrayList;
import java.util.List;

public class Mission {

    private final String name;
    private MissionStatus status;
    private final List<Rocket> rockets;

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
        updateStatus();
    }

    public void updateStatus() {
        if (rockets.isEmpty()) {
            this.status = MissionStatus.ENDED;
            return;
        }

        boolean anyRepair = rockets.stream()
                .anyMatch(r -> r.getStatus() == RocketStatus.IN_REPAIR);

        if (anyRepair) {
            this.status = MissionStatus.PENDING;
        } else {
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name)
            .append(" – ")
            .append(status)
            .append(" – Dragons: ")
            .append(rockets.size());

        if (!rockets.isEmpty()) {
            sb.append(System.lineSeparator());
            for (Rocket r : rockets) {
                sb.append("   o ")
                        .append(r.toString())
                        .append(System.lineSeparator());
            }
        }

        return sb.toString().trim();
    }
}
