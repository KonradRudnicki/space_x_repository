package com.konrad.rudnicki.repository;


import com.konrad.rudnicki.model.mission.Mission;
import com.konrad.rudnicki.model.mission.MissionStatus;
import com.konrad.rudnicki.model.rocket.Rocket;
import com.konrad.rudnicki.model.rocket.RocketStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DragonRocketRepositoryTest {

    private DragonRocketRepository repo;

    @BeforeEach
    void setUp() {
        repo = new DragonRocketRepositoryInMemory();
    }

    @Test
    @DisplayName("Adding a new rocket sets status ON_GROUND and is retrievable by name")
    void addNewRocket_defaultsToOnGround_andRetrievable() {
        Rocket r = repo.addNewRocket("Dragon 1");
        assertNotNull(r);
        assertEquals("Dragon 1", r.getName());
        assertEquals(RocketStatus.ON_GROUND, r.getStatus());

        Rocket byName = repo.getRocket("Dragon 1");
        assertSame(r, byName, "getRocket should return the same instance");
    }

    @Test
    @DisplayName("Adding a new mission sets status SCHEDULED and is retrievable by name")
    void addNewMission_defaultsToScheduled_andRetrievable() {
        Mission m = repo.addNewMission("Luna1");
        assertNotNull(m);
        assertEquals("Luna1", m.getName());
        assertEquals(MissionStatus.SCHEDULED, m.getStatus());

        Mission byName = repo.getMission("Luna1");
        assertSame(m, byName, "getMission should return the same instance");
    }

    @Test
    @DisplayName("Assigning a rocket to a mission sets rocket=IN_SPACE and mission=IN_PROGRESS")
    void assignRocket_setsStatuses() {
        Rocket r = repo.addNewRocket("Dragon 2");
        Mission m = repo.addNewMission("Transit");

        repo.assignRocket("Dragon 2", "Transit");

        assertEquals(RocketStatus.IN_SPACE, repo.getRocket("Dragon 2").getStatus());
        Mission after = repo.getMission("Transit");
        assertEquals(1, after.getRockets().size());
        assertTrue(after.getRockets().contains(r));
        assertEquals(MissionStatus.IN_PROGRESS, after.getStatus(), "At least one rocket assigned and none in repair -> IN_PROGRESS");
    }

    @Test
    @DisplayName("A rocket can be assigned to only one mission")
    void assignRocket_onlyOneMission_allowed() {
        repo.addNewRocket("Dragon X");
        repo.addNewMission("Mars");
        repo.addNewMission("Venus");

        repo.assignRocket("Dragon X", "Mars");
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> repo.assignRocket("Dragon X", "Venus"));
        assertTrue(ex.getMessage() == null || ex.getMessage().toLowerCase().contains("already assigned"));
    }

    @Test
    @DisplayName("Cannot assign rockets to an ENDED mission")
    void cannotAssignToEndedMission() {
        repo.addNewRocket("Dragon XL");
        Mission m = repo.addNewMission("Double Landing");
        repo.updateMissionStatus("Double Landing", MissionStatus.ENDED);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> repo.assignRocket("Dragon XL", "Double Landing"));
        assertTrue(ex.getMessage() == null || ex.getMessage().toLowerCase().contains("already ended"));
    }

    @Test
    @DisplayName("Setting a rocket IN_REPAIR makes its mission PENDING")
    void inRepair_makesMissionPending() {
        repo.addNewRocket("RepairMe");
        repo.addNewMission("Luna2");
        repo.assignRocket("RepairMe", "Luna2");

        repo.updateRocketStatus("RepairMe", RocketStatus.IN_REPAIR);

        assertEquals(RocketStatus.IN_REPAIR, repo.getRocket("RepairMe").getStatus());
        assertEquals(MissionStatus.PENDING, repo.getMission("Luna2").getStatus());
    }

    @Test
    @DisplayName("When no assigned rockets are IN_REPAIR, mission with >=1 rocket is IN_PROGRESS")
    void missionLeavesPendingWhenNoRepairs() {
        repo.addNewRocket("R1");
        repo.addNewRocket("R2");
        repo.addNewMission("Transit-2");

        repo.assignRocket("R1", "Transit-2");
        repo.assignRocket("R2", "Transit-2");

        // put one in repair -> pending
        repo.updateRocketStatus("R1", RocketStatus.IN_REPAIR);
        assertEquals(MissionStatus.PENDING, repo.getMission("Transit-2").getStatus());

        // back to in space -> no repairs -> in progress
        repo.updateRocketStatus("R1", RocketStatus.IN_SPACE);
        assertEquals(MissionStatus.IN_PROGRESS, repo.getMission("Transit-2").getStatus());
    }

    @Test
    @DisplayName("Bulk assignment adds rockets and updates mission status accordingly")
    void bulkAssign_updatesStatus() {
        Rocket a = repo.addNewRocket("A");
        Rocket b = repo.addNewRocket("B");
        Mission m = repo.addNewMission("Bulk");

        repo.assignRocketsToMission(List.of(a.getName(), b.getName()), m.getName());

        Mission after = repo.getMission("Bulk");
        assertEquals(2, after.getRockets().size());
        assertEquals(MissionStatus.IN_PROGRESS, after.getStatus());
        assertEquals(RocketStatus.IN_SPACE, repo.getRocket("A").getStatus());
        assertEquals(RocketStatus.IN_SPACE, repo.getRocket("B").getStatus());
    }

    @Test
    @DisplayName("Mission summary sorts by rocket count desc, then name desc for ties")
    void summary_sorting() {
        // create missions
        Mission transit = repo.addNewMission("Transit");
        Mission luna1 = repo.addNewMission("Luna1");
        Mission vertical = repo.addNewMission("Vertical Landing");
        Mission mars = repo.addNewMission("Mars");
        Mission luna2 = repo.addNewMission("Luna2");
        Mission doubleLanding = repo.addNewMission("Double Landing");

        // rockets
        Rocket red = repo.addNewRocket("Red Dragon");
        Rocket xl = repo.addNewRocket("Dragon XL");
        Rocket heavy = repo.addNewRocket("Falcon Heavy");
        Rocket d1 = repo.addNewRocket("Dragon 1");
        Rocket d2 = repo.addNewRocket("Dragon 2");

        // Assign per example
        repo.assignRocket("Dragon 1", "Luna1");
        repo.assignRocket("Dragon 2", "Luna1");

        repo.assignRocket("Red Dragon", "Transit");
        repo.assignRocket("Dragon XL", "Transit");
        repo.assignRocket("Falcon Heavy", "Transit");

        // mark some missions ended to ensure mixed statuses present
        repo.updateMissionStatus("Vertical Landing", MissionStatus.ENDED);
        repo.updateMissionStatus("Double Landing", MissionStatus.ENDED);
        repo.updateMissionStatus("Luna1", MissionStatus.PENDING);

        List<Mission> summary = repo.getMissionSummary();
        for (Mission mission : summary) {
            System.out.println(mission);
        }

        // Expected order by counts: Transit(3), Luna1(2), then the four with 0 rockets:
        // names: Vertical Landing, Mars, Luna2, Double Landing (descending alphabetical)
        List<String> expected = Arrays.asList(
                "Transit",
                "Luna1",
                "Vertical Landing",
                "Mars",
                "Luna2",
                "Double Landing"
        );

        List<String> actual = summary.stream().map(Mission::getName).toList();
        assertEquals(expected, actual, "Summary order must match spec (count desc, then name desc).");
    }

    @Nested
    @DisplayName("Error handling")
    class Errors {

        @Test
        @DisplayName("Assign/update on unknown entities should throw IllegalArgumentException")
        void opsOnUnknowns_throw() {
            repo.addNewMission("OnlyMission");
            assertThrows(IllegalArgumentException.class, () -> repo.assignRocket("NoRocket", "OnlyMission"));
            repo.addNewRocket("OnlyRocket");
            assertThrows(IllegalArgumentException.class, () -> repo.assignRocket("OnlyRocket", "NoMission"));
            assertThrows(IllegalArgumentException.class, () -> repo.updateRocketStatus("NoRocket", RocketStatus.ON_GROUND));
            assertThrows(IllegalArgumentException.class, () -> repo.updateMissionStatus("NoMission", MissionStatus.SCHEDULED));
        }
    }
}