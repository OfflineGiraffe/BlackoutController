package unsw.blackout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import unsw.blackout.FileTransferException.VirtualFileAlreadyExistsException;
import unsw.blackout.FileTransferException.VirtualFileNoBandwidthException;
import unsw.blackout.FileTransferException.VirtualFileNoStorageSpaceException;
import unsw.blackout.FileTransferException.VirtualFileNotFoundException;
import unsw.response.models.EntityInfoResponse;
import unsw.utils.Angle;

public class BlackoutController {
        private ArrayList<Device> devicesArray = new ArrayList<Device>();
        private ArrayList<Satellite> satellitesArray = new ArrayList<Satellite>();

        public static Device idToDevice(String deviceId, ArrayList<Device> devicesArray) {
                for (Device deviceIterator : devicesArray) {
                        if (deviceIterator.getDeviceId().equals(deviceId)) {
                                return deviceIterator;
                        }
                }

                return null;
        }

        public static Satellite idToSatellite(String satelliteId, ArrayList<Satellite> satellitesArray) {
                for (Satellite satelliteIterator : satellitesArray) {
                        if (satelliteIterator.getSatelliteId().equals(satelliteId)) {
                                return satelliteIterator;
                        }
                }

                return null;
        }

        public void createDevice(String deviceId, String type, Angle position) {
                // TODO: Task 1a)

                if (type.equals("HandheldDevice")) {
                        HandheldDevice device = new HandheldDevice(deviceId, type, position);
                        devicesArray.add(device);

                } else if (type.equals("DesktopDevice")) {
                        DesktopDevice device = new DesktopDevice(deviceId, type, position);
                        devicesArray.add(device);

                } else if (type.equals("LaptopDevice")) {
                        LaptopDevice device = new LaptopDevice(deviceId, type, position);
                        devicesArray.add(device);

                }

        }

        public void removeDevice(String deviceId) {
                // TODO: Task 1b)

                devicesArray.remove(idToDevice(deviceId, devicesArray));

        }

        public void createSatellite(String satelliteId, String type, double height, Angle position) {
                // TODO: Task 1c)

                if (type.equals("RelaySatellite")) {
                        RelaySatellite satellite = new RelaySatellite(satelliteId, type, height, position);
                        satellitesArray.add(satellite);
                } else if (type.equals("StandardSatellite")) {
                        StandardSatellite satellite = new StandardSatellite(satelliteId, type, height, position);
                        satellitesArray.add(satellite);
                } else if (type.equals("TeleportingSatellite")) {
                        TeleportingSatellite satellite = new TeleportingSatellite(satelliteId, type, height, position);
                        satellitesArray.add(satellite);
                }

        }

        public void removeSatellite(String satelliteId) {
                // TODO: Task 1d)

                satellitesArray.remove(idToSatellite(satelliteId, satellitesArray));

        }

        public List<String> listDeviceIds() {
                // TODO: Task 1e)
                List<String> arrayDeviceId = new ArrayList<String>();

                for (Device deviceIterator : devicesArray) {
                        arrayDeviceId.add(deviceIterator.getDeviceId());
                }

                return arrayDeviceId;
        }

        public List<String> listSatelliteIds() {
                // TODO: Task 1f)

                List<String> arraySatelliteId = new ArrayList<String>();

                for (Satellite iterator : satellitesArray) {
                        arraySatelliteId.add(iterator.getSatelliteId());
                }

                return arraySatelliteId;
        }

        public void addFileToDevice(String deviceId, String filename, String content) {
                // TODO: Task 1g)

                Device returnedDevice = idToDevice(deviceId, devicesArray);

                Fileinfo fileInfo = new Fileinfo(filename, content, true);
                fileInfo.setTransferringContent(content);

                if (returnedDevice != null) {
                        returnedDevice.addFile(filename, fileInfo);
                }

        }

        public EntityInfoResponse getInfo(String id) {
                // TODO: Task 1h)

                Device d = idToDevice(id, devicesArray);
                Satellite s = idToSatellite(id, satellitesArray);

                // is a device
                if (d != null) {
                        return d.deviceInfo();
                } else {
                        return s.satelliteInfo();
                }

        }

        public void simulate() {
                // TODO: Task 2a)

                for (Satellite satellite : satellitesArray) {
                        satellite.moveSatellite();
                }

                for (Satellite satelliteIterator : satellitesArray) {
                        for (Map.Entry<String, Fileinfo> entry : satelliteIterator.getFiles().entrySet()) {
                                Fileinfo fileinfo = entry.getValue();
                                if (!fileinfo.isCompleteFunction()) {
                                        fileinfo.setTransferringSize(
                                                        fileinfo.getTransferringSize()
                                                                        + satelliteIterator.getDownloadBandwidth(),
                                                        satelliteIterator);
                                        if (fileinfo.isCompleteFunction()) {
                                                satelliteIterator.setDownloadNumber(
                                                                satelliteIterator.getDownloadNumber() - 1);
                                        }
                                } else {
                                        fileinfo.setComplete(fileinfo.isCompleteFunction());
                                }
                        }
                }

                for (Device deviceIterator : devicesArray) {
                        for (Map.Entry<String, Fileinfo> entry : deviceIterator.getFiles().entrySet()) {
                                Fileinfo fileinfo = entry.getValue();
                                if (!fileinfo.isCompleteFunction()) {
                                        fileinfo.setTransferringContent(deviceIterator);
                                } else {
                                        fileinfo.setComplete(fileinfo.isCompleteFunction());
                                }
                        }
                }

        }

        /**
         * Simulate for the specified number of minutes. You shouldn't need to modify
         * this function.
         */
        public void simulate(int numberOfMinutes) {
                for (int i = 0; i < numberOfMinutes; i++) {
                        simulate();
                }
        }

        public List<String> communicableEntitiesInRange(String id) {
                // TODO: Task 2 b)

                ArrayList<String> entitiesInRange = new ArrayList<>();

                Device d = idToDevice(id, devicesArray);
                Satellite s = idToSatellite(id, satellitesArray);
                // is a device. wanna check satellitesArray
                if (d != null) {
                        for (Satellite satelliteIterator : satellitesArray) {
                                if (d.isVisibleAndInRange(satelliteIterator)) {
                                        entitiesInRange.add(satelliteIterator.getSatelliteId());
                                        if (satelliteIterator.getType().equals("RelaySatellite")) {
                                                entitiesInRange.addAll(Helper.relayEntitiesInRange(satelliteIterator,
                                                                entitiesInRange, satellitesArray, devicesArray));
                                        }
                                }
                        }
                } else if (s != null) {
                        for (Device deviceIterator : devicesArray) {
                                if (s.isVisibleAndInRange(deviceIterator)) {
                                        entitiesInRange.add(deviceIterator.getDeviceId());
                                }
                        }

                        for (Satellite satelliteIterator : satellitesArray) {
                                if (s.isVisibleAndInRange(satelliteIterator) && satelliteIterator != s) {
                                        entitiesInRange.add(satelliteIterator.getSatelliteId());
                                        if (satelliteIterator.getType().equals("RelaySatellite")) {
                                                entitiesInRange.addAll(Helper.relayEntitiesInRange(satelliteIterator,
                                                                entitiesInRange, satellitesArray, devicesArray));
                                        }
                                }
                        }
                }

                List<String> newEntitityList = Helper.returnCorrectEntities(entitiesInRange, d, s, devicesArray,
                                satellitesArray);
                HashSet<String> nonRepeat = new HashSet<String>(newEntitityList);
                ArrayList<String> result = new ArrayList<String>(nonRepeat);

                result.remove(id);
                return result;
        }

        public void sendFile(String fileName, String fromId, String toId) throws FileTransferException {
                // TODO: Task 2 c)

                Device sendingFromDevice = idToDevice(fromId, devicesArray);
                Device receivingDevice = idToDevice(toId, devicesArray);
                Satellite sendingFromSatellite = idToSatellite(fromId, satellitesArray);
                Satellite receivingToSatellite = idToSatellite(toId, satellitesArray);

                if (sendingFromDevice != null) {
                        Fileinfo newFilePrevious = sendingFromDevice.findFileInformation(fileName);

                        if (sendingFromDevice.getFiles().get(fileName) == null
                                        || !sendingFromDevice.getFiles().get(fileName).isCompleteFunction()) {
                                throw new VirtualFileNotFoundException(fileName);
                        }

                        Fileinfo newFile = new Fileinfo(fileName, newFilePrevious.getContent(), false);

                        if (receivingToSatellite.storageFull()) {
                                throw new VirtualFileNoStorageSpaceException("Too much storage");
                        }

                        if (receivingToSatellite.getAvaliableStorage() - newFile.getfileSize() < 0) {
                                throw new VirtualFileNoStorageSpaceException("used all the bytes");
                        }

                        // If the file already exist on the destination satellite
                        if (receivingToSatellite.findFileInformation(fileName) != null) {
                                throw new VirtualFileAlreadyExistsException(fileName);
                        }

                        // is a relay satellite
                        if (receivingToSatellite.getType().equals("RelaySatellite")
                                        || !receivingToSatellite.canDownload()) {
                                throw new VirtualFileNoBandwidthException(receivingToSatellite.getType());
                        }

                        newFile.setComplete(newFile.isCompleteFunction());
                        newFile.setSentFrom(fromId);
                        newFile.setSentTo(toId);
                        receivingToSatellite.addFile(fileName, newFile);
                        receivingToSatellite.setAvaliableStorage(
                                        receivingToSatellite.getAvaliableStorage() - newFile.getfileSize());
                        receivingToSatellite.setDownloadNumber(receivingToSatellite.getDownloadNumber() + 1);

                } else if (sendingFromSatellite != null) {

                        if (sendingFromSatellite.getFiles().get(fileName) == null) {
                                throw new VirtualFileNotFoundException(fileName);
                        }

                        Fileinfo newFilePrevious = sendingFromSatellite.findFileInformation(fileName);
                        if (receivingToSatellite != null) {
                                Fileinfo newFile = new Fileinfo(fileName, newFilePrevious.getContent(), false);
                                // If the file already exist on the destination satellite
                                if (receivingToSatellite.findFileInformation(fileName) != null) {
                                        throw new VirtualFileAlreadyExistsException(fileName);
                                }

                                // is a relay satellite
                                if (receivingToSatellite.getType().equals("RelaySatellite")) {
                                        throw new VirtualFileNoBandwidthException(receivingToSatellite.getType());
                                }

                                if (receivingToSatellite.storageFull()) {
                                        throw new VirtualFileNoStorageSpaceException("Too much storage");
                                }

                                if (receivingToSatellite.getAvaliableStorage() - newFile.getfileSize() < 0) {
                                        throw new VirtualFileNoStorageSpaceException("used all the bytes");
                                }

                                newFile.resetTransferringContent();
                                newFile.setComplete(newFile.isCompleteFunction());
                                newFile.setSentFrom(fromId);
                                newFile.setSentTo(toId);
                                receivingToSatellite.addFile(fileName, newFile);
                                receivingToSatellite.setAvaliableStorage(
                                                receivingToSatellite.getAvaliableStorage() - newFile.getfileSize());
                                receivingToSatellite.setDownloadNumber(receivingToSatellite.getDownloadNumber() + 1);
                        } else {
                                // If the file already exist on the destination satellite
                                if (receivingDevice.findFileInformation(fileName) != null) {
                                        throw new VirtualFileAlreadyExistsException(fileName);
                                }

                                Fileinfo newFile = new Fileinfo(fileName, newFilePrevious.getContent(), false);
                                newFile.resetTransferringContent();
                                newFile.setComplete(newFile.isCompleteFunction());
                                newFile.setSentFrom(fromId);
                                newFile.setSentTo(toId);
                                receivingDevice.addFile(fileName, newFile);

                        }

                }

        }

        public void createDevice(String deviceId, String type, Angle position, boolean isMoving) {
                createDevice(deviceId, type, position);
                // TODO: Task 3
        }

        public void createSlope(int startAngle, int endAngle, int gradient) {
                // TODO: Task 3
                // If you are not completing Task 3 you can leave this method blank :)
        }
}
