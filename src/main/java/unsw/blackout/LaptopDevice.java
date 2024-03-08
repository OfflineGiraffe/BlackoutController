package unsw.blackout;

import unsw.utils.Angle;

public class LaptopDevice extends Device {
        private int maxRange = 100000;

        public LaptopDevice(String id, String type, Angle position) {
                super(id, type, position);
        }

        public int getMaxRange() {
                return maxRange;
        }

}
