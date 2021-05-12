package test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import java.time.LocalTime;

import test.test_helpers.DummyConnection;
import test.test_helpers.ResponseHelper;
import test.test_helpers.TestHelper;

@TestInstance(value = Lifecycle.PER_CLASS)
public class Task2ExampleTests {
    @Test
    public void testExample() {
        // Task 2
        // Example from the specification
        // Creates 1 satellite and 3 devices
        // Activates 2 of the devices and then schedules connections

        TestHelper plan = new TestHelper().createDevice("HandheldDevice", "DeviceA", 30)
                .createDevice("LaptopDevice", "DeviceB", 180).createDevice("DesktopDevice", "DeviceC", 330)
                .createSatellite("NasaSatellite", "Satellite1", 10000, 340)
                .scheduleDeviceActivation("DeviceA", LocalTime.of(0, 0), 400)
                .scheduleDeviceActivation("DeviceC", LocalTime.of(7, 0), 300)
                .showWorldState(new ResponseHelper(LocalTime.of(0, 0))
                        .expectSatellite("NasaSatellite", "Satellite1", 10000, 340, 85,
                                new String[] { "DeviceA", "DeviceC" })
                        .expectDevice("HandheldDevice", "DeviceA", 30, false,
                                new LocalTime[][] { { LocalTime.of(0, 0), LocalTime.of(6, 40) } })
                        .expectDevice("LaptopDevice", "DeviceB", 180)
                        .expectDevice("DesktopDevice", "DeviceC", 330, false,
                                new LocalTime[][] { { LocalTime.of(7, 0), LocalTime.of(12, 0) } })
                        .toString())
                .simulate(500).showWorldState(
                        new ResponseHelper(LocalTime.of(8, 20)).expectSatellite("NasaSatellite", "Satellite1", 10000,
                                344.25, 85, new String[] { "DeviceA", "DeviceC" },
                                new DummyConnection[] {
                                        new DummyConnection("DeviceA", LocalTime.of(0, 0), LocalTime.of(6, 41)),
                                        new DummyConnection("DeviceC", LocalTime.of(7, 0)), //
                                })
                                .expectDevice("HandheldDevice", "DeviceA", 30, false,
                                        new LocalTime[][] { { LocalTime.of(0, 0), LocalTime.of(6, 40) } })
                                .expectDevice("LaptopDevice", "DeviceB", 180).expectDevice("DesktopDevice", "DeviceC",
                                        330, true, new LocalTime[][] { { LocalTime.of(7, 0), LocalTime.of(12, 0) } })
                                .toString());
        plan.executeTestPlan();
    }

    @Test
    public void testMovementOfSatellites() {
        // Task 2
        // This test is here to help you make sure your movement of satellite code is
        // correct

        TestHelper plan = new TestHelper().createSatellite("BlueOriginSatellite", "Wandavision", 6000, 0)
                .createSatellite("SovietSatellite", "FalconAndTheWinterSoldier", 6000, 90)
                .createSatellite("SpaceXSatellite", "Loki", 6000, 180)
                .createSatellite("NasaSatellite", "BadBatch", 6000, 270).simulate(1440)
                .showWorldState(new ResponseHelper(LocalTime.of(0, 0))
                        .expectSatellite("NasaSatellite", "BadBatch", 6000, 290.4, 85, new String[] {})
                        .expectSatellite("SovietSatellite", "FalconAndTheWinterSoldier", 6000, 310, -1000,
                                new String[] {})
                        .expectSatellite("SpaceXSatellite", "Loki", 6000, 193.2, 55, new String[] {})
                        .expectSatellite("BlueOriginSatellite", "Wandavision", 6000, 33.84, 141, new String[] {})
                        .toString());
        plan.executeTestPlan();
    }

}