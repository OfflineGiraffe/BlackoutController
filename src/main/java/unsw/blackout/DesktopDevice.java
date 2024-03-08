package unsw.blackout;

import unsw.utils.Angle;

public class DesktopDevice extends Device {
        private int maxRange = 200000;

        public DesktopDevice(String id, String type, Angle position) {
                super(id, type, position);
        }

        @Override
        public int getMaxRange() {
                return maxRange;
        }
}
