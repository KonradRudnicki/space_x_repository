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


}
