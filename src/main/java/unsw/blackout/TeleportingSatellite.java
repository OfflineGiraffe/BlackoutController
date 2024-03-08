package unsw.blackout;

import unsw.utils.Angle;

public class TeleportingSatellite extends Satellite {
        private double speed = -1000;
        private int maxRange = 200000;
        private int avaliableStorage = 200;
        private int uploadBandwidth = 10;
        private int downloadBandwidth = 15;

        public TeleportingSatellite(String id, String type, double height, Angle position) {
                super(id, type, height, position);
        }

        public double getSpeed() {
                return speed;
        }

        public int getMaxRange() {
                return maxRange;
        }

        public int getAvaliableStorage() {
                return avaliableStorage;
        }

        public void setAvaliableStorage(int avaliableStorage) {
                this.avaliableStorage = avaliableStorage;
        }

        public int getUploadBandwidth() {
                return uploadBandwidth;
        }

        public void setUploadBandwidth(int uploadBandwidth) {
                this.uploadBandwidth = uploadBandwidth;
        }

        public int getDownloadBandwidth() {
                return downloadBandwidth;
        }

        public void setDownloadBandwitth(int downloadBandwitth) {
                this.downloadBandwidth = downloadBandwitth;
        }

        @Override
        public void moveSatellite() {

                double changeInDirection = -1;
                Angle change = Angle.fromRadians((getSpeed()) / (getHeight()));
                Angle zeroAngle = Angle.fromRadians(0);

                // if negative speed (opposite dirceiton)
                if (speed < 0) {
                        // check if is above 360
                        if (getPosition().subtract(change).compareTo(Angle.fromRadians(2 * Math.PI)) >= 1) {
                                setPosition(getPosition().subtract(Angle.fromRadians(2 * Math.PI)));
                                setPosition(getPosition().subtract(change));
                                // check if it is above 180 and in the right side/position
                        } else if (getPosition().subtract(change).compareTo(Angle.fromRadians(Math.PI)) >= 1
                                        && getPosition().compareTo(Angle.fromRadians(Math.PI)) < 1) {

                                setPosition(Angle.fromRadians(2 * Math.PI));
                                speed *= changeInDirection;
                        } else {
                                setPosition(getPosition().subtract(change));
                        }

                } else {
                        // check if it is below 0
                        if (getPosition().subtract(change).compareTo(zeroAngle) < 1) {
                                setPosition(getPosition().add(Angle.fromRadians(2 * Math.PI)));
                                // check if is below 180 and ifi ts in the right position
                        } else if (getPosition().subtract(change).compareTo(Angle.fromRadians(Math.PI)) < 1
                                        && getPosition().compareTo(Angle.fromRadians(Math.PI)) >= 1) {

                                setPosition(zeroAngle);
                                speed *= changeInDirection;
                        }

                        setPosition(getPosition().subtract(change));

                }

        }

}
