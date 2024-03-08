package blackout;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import unsw.blackout.BlackoutController;
import unsw.blackout.FileTransferException;
import unsw.response.models.FileInfoResponse;
import unsw.response.models.EntityInfoResponse;
import unsw.utils.Angle;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

import java.util.Arrays;
import java.util.List;

import static blackout.TestHelpers.assertListAreEqualIgnoringOrder;

@TestInstance(value = Lifecycle.PER_CLASS)
public class Task2ExampleTests {
        @Test
        public void testEntitiesInRange() {
                // Task 2
                // Example from the specification
                BlackoutController controller = new BlackoutController();

                // Creates 1 satellite and 2 devices
                // Gets a device to send a file to a satellites and gets another device to
                // download it.
                // StandardSatellites are slow and transfer 1 byte per minute.
                controller.createSatellite("Satellite1", "StandardSatellite", 1000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(320));
                controller.createSatellite("Satellite2", "StandardSatellite", 1000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(315));
                controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
                controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));
                controller.createDevice("DeviceD", "HandheldDevice", Angle.fromDegrees(180));
                controller.createSatellite("Satellite3", "StandardSatellite", 2000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(175));

                assertListAreEqualIgnoringOrder(Arrays.asList("DeviceC", "Satellite2"),
                                controller.communicableEntitiesInRange("Satellite1"));
                assertListAreEqualIgnoringOrder(Arrays.asList("DeviceB", "DeviceC", "Satellite1"),
                                controller.communicableEntitiesInRange("Satellite2"));
                assertListAreEqualIgnoringOrder(Arrays.asList("Satellite2"),
                                controller.communicableEntitiesInRange("DeviceB"));

                assertListAreEqualIgnoringOrder(Arrays.asList("DeviceD"),
                                controller.communicableEntitiesInRange("Satellite3"));
        }

        @Test
        public void testSomeExceptionsForSend() {
                // just some of them... you'll have to test the rest
                BlackoutController controller = new BlackoutController();

                // Creates 1 satellite and 2 devices
                // Gets a device to send a file to a satellites and gets another device to
                // download it.
                // StandardSatellites are slow and transfer 1 byte per minute.
                controller.createSatellite("Satellite1", "StandardSatellite", 5000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(320));
                controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
                controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));

                String msg = "Hey";
                controller.addFileToDevice("DeviceC", "FileAlpha", msg);
                assertThrows(FileTransferException.VirtualFileNotFoundException.class,
                                () -> controller.sendFile("NonExistentFile", "DeviceC", "Satellite1"));

                assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));
                assertEquals(new FileInfoResponse("FileAlpha", "", msg.length(), false),
                                controller.getInfo("Satellite1").getFiles().get("FileAlpha"));
                controller.simulate(msg.length() * 2);
                assertThrows(FileTransferException.VirtualFileAlreadyExistsException.class,
                                () -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));
        }

        @Test
        public void testMovement() {
                // Task 2
                // Example from the specification
                BlackoutController controller = new BlackoutController();

                // Creates 1 satellite and 2 devices
                // Gets a device to send a file to a satellites and gets another device to
                // download it.
                // StandardSatellites are slow and transfer 1 byte per minute.
                controller.createSatellite("Satellite1", "StandardSatellite", 100 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(340));
                assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(340), 100 + RADIUS_OF_JUPITER,
                                "StandardSatellite"), controller.getInfo("Satellite1"));
                controller.simulate();
                assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(337.95), 100 + RADIUS_OF_JUPITER,
                                "StandardSatellite"), controller.getInfo("Satellite1"));
        }

        @Test
        public void testExample() {
                // Task 2
                // Example from the specification
                BlackoutController controller = new BlackoutController();

                // Creates 1 satellite and 2 devices
                // Gets a device to send a file to a satellites and gets another device to
                // download it.
                // StandardSatellites are slow and transfer 1 byte per minute.
                controller.createSatellite("Satellite1", "StandardSatellite", 10000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(320));
                controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
                controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));

                String msg = "Hey";
                controller.addFileToDevice("DeviceC", "FileAlpha", msg);
                assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));
                assertEquals(new FileInfoResponse("FileAlpha", "", msg.length(), false),
                                controller.getInfo("Satellite1").getFiles().get("FileAlpha"));

                controller.simulate(msg.length() * 2);
                assertEquals(new FileInfoResponse("FileAlpha", msg, msg.length(), true),
                                controller.getInfo("Satellite1").getFiles().get("FileAlpha"));

                assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "Satellite1", "DeviceB"));
                assertEquals(new FileInfoResponse("FileAlpha", "", msg.length(), false),
                                controller.getInfo("DeviceB").getFiles().get("FileAlpha"));

                controller.simulate(msg.length());
                assertEquals(new FileInfoResponse("FileAlpha", msg, msg.length(), true),
                                controller.getInfo("DeviceB").getFiles().get("FileAlpha"));

                // Hints for further testing:
                // - What about checking about the progress of the message half way through?
                // - Device/s get out of range of satellite
                // ... and so on.
        }

        @Test
        public void testRelayMovement() {
                // Task 2
                // Example from the specification
                BlackoutController controller = new BlackoutController();

                // Creates 1 satellite and 2 devices
                // Gets a device to send a file to a satellites and gets another device to
                // download it.
                // StandardSatellites are slow and transfer 1 byte per minute.
                controller.createSatellite("Satellite1", "RelaySatellite", 100 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(180));

                // moves in negative direction
                assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(180), 100 + RADIUS_OF_JUPITER,
                                "RelaySatellite"), controller.getInfo("Satellite1"));
                controller.simulate();
                assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(178.77), 100 + RADIUS_OF_JUPITER,
                                "RelaySatellite"), controller.getInfo("Satellite1"));
                controller.simulate();
                assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(177.54), 100 + RADIUS_OF_JUPITER,
                                "RelaySatellite"), controller.getInfo("Satellite1"));
                controller.simulate();
                assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(176.31), 100 + RADIUS_OF_JUPITER,
                                "RelaySatellite"), controller.getInfo("Satellite1"));

                controller.simulate(5);
                assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(170.18), 100 + RADIUS_OF_JUPITER,
                                "RelaySatellite"), controller.getInfo("Satellite1"));
                controller.simulate(24);
                assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(140.72), 100 + RADIUS_OF_JUPITER,
                                "RelaySatellite"), controller.getInfo("Satellite1"));
                // edge case
                controller.simulate();
                assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(139.49), 100 + RADIUS_OF_JUPITER,
                                "RelaySatellite"), controller.getInfo("Satellite1"));
                // coming back
                controller.simulate(1);
                assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(140.72), 100 + RADIUS_OF_JUPITER,
                                "RelaySatellite"), controller.getInfo("Satellite1"));
                controller.simulate(5);
                assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(146.85), 100 + RADIUS_OF_JUPITER,
                                "RelaySatellite"), controller.getInfo("Satellite1"));
        }

        @Test
        public void testTeleportingMovement() {
                // Test for expected teleportation movement behaviour
                BlackoutController controller = new BlackoutController();

                controller.createSatellite("Satellite1", "TeleportingSatellite", 10000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(0));

                controller.simulate();
                Angle clockwiseOnFirstMovement = controller.getInfo("Satellite1").getPosition();
                controller.simulate();
                Angle clockwiseOnSecondMovement = controller.getInfo("Satellite1").getPosition();
                assertTrue(clockwiseOnSecondMovement.compareTo(clockwiseOnFirstMovement) == 1);

                // It should take 250 simulations to reach theta = 180.
                // Simulate until Satellite1 reaches theta=180
                controller.simulate(250);

                // Verify that Satellite1 is now at theta=0
                assertTrue(controller.getInfo("Satellite1").getPosition().toDegrees() % 360 == 0);
        }

        @Test
        public void testRelaySatellite() {
                BlackoutController controller = new BlackoutController();
                controller.createSatellite("Sat1", "StandardSatellite", 5000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(200));
                controller.createDevice("DevA", "LaptopDevice", Angle.fromDegrees(150));
                assertListAreEqualIgnoringOrder(Arrays.asList(), controller.communicableEntitiesInRange("Sat1"));
                controller.createSatellite("Sat2", "RelaySatellite", 5000 + RADIUS_OF_JUPITER, Angle.fromDegrees(160));

                assertListAreEqualIgnoringOrder(Arrays.asList("Sat1", "DevA"),
                                controller.communicableEntitiesInRange("Sat2"));
                assertListAreEqualIgnoringOrder(Arrays.asList("DevA", "Sat2"),
                                controller.communicableEntitiesInRange("Sat1"));
        }

        @Test
        public void testTeleportingFile() {
                // Task 2
                // Example from the specification
                BlackoutController controller = new BlackoutController();

                // Creates 1 satellite and 2 devices
                // Gets a device to send a file to a satellites and gets another device to
                // download it.
                // StandardSatellites are slow and transfer 1 byte per minute.
                controller.createSatellite("Satellite1", "TeleportingSatellite", 10000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(320));
                controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
                controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));

                String msg = "HeyHowsItGoingThere";
                controller.addFileToDevice("DeviceC", "FileAlpha", msg);
                assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));
                assertEquals(new FileInfoResponse("FileAlpha", "", msg.length(), false),
                                controller.getInfo("Satellite1").getFiles().get("FileAlpha"));

                controller.simulate(2);
                assertEquals(new FileInfoResponse("FileAlpha", "HeyHowsItGoingThere", 19, true),
                                controller.getInfo("Satellite1").getFiles().get("FileAlpha"));
        }

        @Test
        public void testCommunicableTypes() {
                BlackoutController controller = new BlackoutController();

                // create all devices and satellites
                List<String> devices = Arrays.asList("HandheldDevice", "LaptopDevice", "DesktopDevice");
                for (String device : devices) {
                        controller.createDevice(device, device, Angle.fromDegrees(100));
                }
                List<String> satellites = Arrays.asList("StandardSatellite", "TeleportingSatellite", "RelaySatellite");
                controller.createDevice("AnotherDevice", "HandheldDevice", Angle.fromDegrees(150));
                controller.createSatellite("Another Satellite", "TeleportingSatellite", 10000 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(120));
                for (String satellite : satellites)
                        controller.createSatellite(satellite, satellite, RADIUS_OF_JUPITER + 100,
                                        Angle.fromDegrees(100));

                // check what each device is communicable with
                assertListAreEqualIgnoringOrder(
                                Arrays.asList("StandardSatellite", "TeleportingSatellite", "RelaySatellite",
                                                "Another Satellite"),
                                controller.communicableEntitiesInRange("LaptopDevice"));
                assertListAreEqualIgnoringOrder(
                                Arrays.asList("StandardSatellite", "TeleportingSatellite", "RelaySatellite",
                                                "Another Satellite"),
                                controller.communicableEntitiesInRange("HandheldDevice"));
                assertListAreEqualIgnoringOrder(
                                Arrays.asList("RelaySatellite", "TeleportingSatellite", "Another Satellite"),
                                controller.communicableEntitiesInRange("DesktopDevice"));
        }

}
