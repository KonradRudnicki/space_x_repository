package com.konrad.rudnicki.model.mission;

import com.konrad.rudnicki.model.rocket.Rocket;
import com.konrad.rudnicki.model.rocket.RocketStatus;

import java.util.List;

public class Mission {

    private final String name;
    private Mission status;
    private List<Rocket> rockets;

    public Mission(String name) {
        this.name = name;
    }
}
