package unsw.blackout;

import java.util.ArrayList;
import java.util.List;

public class Helper extends BlackoutController {
        public static ArrayList<String> relayEntitiesInRange(Satellite relayS, ArrayList<String> oldEntitiesList,
                        ArrayList<Satellite> satellitesArray, ArrayList<Device> devicesArray) {
                ArrayList<String> newEntitiesFound = new ArrayList<String>();
                newEntitiesFound.add(relayS.getSatelliteId());

                for (Device deviceIterator : devicesArray) {
                        if (relayS.isVisibleAndInRange(deviceIterator)) {
                                if (!oldEntitiesList.contains(deviceIterator.getDeviceId())) {
                                        newEntitiesFound.add(deviceIterator.getDeviceId());
                                }
                        }
                }

                for (Satellite satelliteIterator : satellitesArray) {
                        if (relayS.isVisibleAndInRange(satelliteIterator) && relayS != satelliteIterator) {
                                if (!oldEntitiesList.contains(satelliteIterator.getSatelliteId())) {
                                        newEntitiesFound.add(satelliteIterator.getSatelliteId());
                                }
                        }
                }

                for (Satellite satelliteIterator : satellitesArray) {
                        if (satelliteIterator.getType().equals("RelaySatellite")) {
                                if (newEntitiesFound.contains(satelliteIterator.getSatelliteId())) {
                                        if (!oldEntitiesList.contains(satelliteIterator.getSatelliteId())) {
                                                newEntitiesFound.addAll(relayEntitiesInRange(satelliteIterator,
                                                                newEntitiesFound, satellitesArray, devicesArray));
                                        }
                                }
                        }
                }
                return newEntitiesFound;
        }

        public static List<String> returnCorrectEntities(List<String> entitiesInRange, Device d, Satellite s,
                        ArrayList<Device> devicesArray, ArrayList<Satellite> satelliteArray) {

                ArrayList<Satellite> newSateliteArray = new ArrayList<>();
                ArrayList<Device> newDeviceArrayList = new ArrayList<>();
                ArrayList<String> newEntitityList = new ArrayList<>();

                for (String iterator : entitiesInRange) {
                        Device dev = BlackoutController.idToDevice(iterator, devicesArray);
                        Satellite sat = idToSatellite(iterator, satelliteArray);

                        if (dev != null) {
                                newDeviceArrayList.add(idToDevice(iterator, devicesArray));
                        } else if (sat != null) {
                                newSateliteArray.add(idToSatellite(iterator, satelliteArray));
                        }

                }

                if (d != null) {
                        for (Satellite satelliteIterator : newSateliteArray) {
                                if (d.getType().equals("DesktopDevice")) {
                                        if (satelliteIterator.getType().equals("RelaySatellite")
                                                        || satelliteIterator.getType().equals("TeleportingSatellite")) {
                                                newEntitityList.add(satelliteIterator.getSatelliteId());

                                        }
                                } else {
                                        newEntitityList.add(satelliteIterator.getSatelliteId());
                                }
                        }
                } else if (s != null) {
                        for (Satellite satelliteIterator : newSateliteArray) {
                                newEntitityList.add(satelliteIterator.getSatelliteId());
                        }

                        for (Device deviceIterator : newDeviceArrayList) {
                                if (s.getType().equals("StandardSatellite")) {
                                        if (deviceIterator.getType().equals("LaptopDevice")
                                                        || deviceIterator.getType().equals("HandheldDevice")) {
                                                newEntitityList.add(deviceIterator.getDeviceId());
                                        }
                                } else {
                                        newEntitityList.add(deviceIterator.getDeviceId());
                                }
                        }
                }

                return newEntitityList;
        }

}
