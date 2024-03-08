package unsw.blackout;

import unsw.utils.Angle;

public class HandheldDevice extends Device {
        private int maxRange = 50000;

        public HandheldDevice(String id, String type, Angle position) {
                super(id, type, position);
        }

        @Override
        public int getMaxRange() {
                return maxRange;
        }

}
