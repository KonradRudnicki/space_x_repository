package com.konrad.rudnicki.repository;


import org.junit.jupiter.api.BeforeEach;

class DragonRocketRepositoryTest {

    @BeforeEach
    void setUp() {
        DragonRocketRepository dragonRocketRepository = new DragonRocketRepositoryInMemory();
    }
}