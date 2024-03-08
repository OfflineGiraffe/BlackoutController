package unsw.blackout;

import unsw.utils.Angle;

public class StandardSatellite extends Satellite {
        private double speed = 2500;
        private int maxRange = 150000;
        private int avaliableStorage = 80;
        private int uploadBandwidth = 1;
        private int downloadBandwidth = 1;
        private int downloadNumber;
        private int uploadNumber;

        public StandardSatellite(String id, String type, double height, Angle position) {
                super(id, type, height, position);
                this.avaliableStorage = 80;
        }

        public int getDownloadNumber() {
                return downloadNumber;
        }

        public void setDownloadNumber(int downloadNumber) {
                this.downloadNumber = downloadNumber;
        }

        public int getUploadNumber() {
                return uploadNumber;
        }

        public void setUploadNumber(int uploadNumber) {
                this.uploadNumber = uploadNumber;
        }

        public int getAvaliableStorage() {
                return avaliableStorage;
        }

        public void setAvaliableStorage(int avaliableStorage) {
                this.avaliableStorage = avaliableStorage;
        }

        public double getSpeed() {
                return speed;
        }

        public int getMaxRange() {
                return maxRange;
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

        public void setDownloadBandwidth(int downloadBandwidth) {
                this.downloadBandwidth = downloadBandwidth;
        }

        @Override
        public boolean storageFull() {
                if (super.getDownloadNumber() > 2) {
                        return true;
                }
                return false;
        }

}
