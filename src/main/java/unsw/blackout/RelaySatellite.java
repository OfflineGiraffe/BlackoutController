package unsw.blackout;

import unsw.utils.Angle;

public class RelaySatellite extends Satellite {
        private double speed = 1500;
        private int maxRange = 300000;

        public RelaySatellite(String id, String type, double height, Angle position) {
                super(id, type, height, position);
        }

        public double getSpeed() {
                return speed;
        }

        public int getMaxRange() {
                return maxRange;
        }

        public void moveSatellite() {

                double changeInDirection = -1;
                Angle change = Angle.fromRadians((this.getSpeed()) / (this.getHeight()));

                if (speed < 0) {
                        // check if it rotates below 360
                        if (getPosition().subtract(change).compareTo(Angle.fromRadians(0)) < 1) {
                                setPosition(getPosition().add(Angle.fromRadians(2 * Math.PI)));
                                // check if it has reached 190 and in the correct side/position
                        } else if (getPosition().subtract(change).compareTo(Angle.fromDegrees(190)) >= 1
                                        && getPosition().compareTo(Angle.fromDegrees(190)) < 1) {
                                speed *= changeInDirection;
                                // check if it has went above 360
                        } else if (getPosition().subtract(change).compareTo(Angle.fromRadians(2 * Math.PI)) >= 1) {
                                setPosition(getPosition().subtract(Angle.fromRadians(2 * Math.PI)));
                        }

                        setPosition(getPosition().subtract(change));

                } else {
                        // check if it went below 0
                        if (getPosition().subtract(change).compareTo(Angle.fromRadians(0)) < 1) {
                                setPosition(getPosition().add(Angle.fromRadians(2 * Math.PI)));
                                // check if it is below 140 and its position
                        } else if (getPosition().subtract(change).compareTo(Angle.fromDegrees(140)) < 1
                                        && getPosition().compareTo(Angle.fromDegrees(140)) >= 1) {
                                speed *= changeInDirection;
                                // check if it is on the threshhold angle and which side its in
                        } else if (getPosition().compareTo(Angle.fromDegrees(345)) >= 1
                                        && getPosition().compareTo(Angle.fromDegrees(360)) < 1) {
                                speed *= changeInDirection;
                        }

                        setPosition(getPosition().subtract(change));

                }
        }

}
