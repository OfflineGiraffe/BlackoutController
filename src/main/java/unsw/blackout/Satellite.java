package unsw.blackout;

import java.util.HashMap;
import java.util.Map;

import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;
import unsw.utils.MathsHelper;

public abstract class Satellite extends BlackoutController {
        private String satelliteId;
        private String type;
        private double height;
        private Angle position;
        private double speed;
        private Map<String, Fileinfo> files;
        private int maxRange;
        private int uploadBandwidth;
        private int downloadBandwidth;
        private int downloadNumber;
        private int uploadNumber;
        private int avaliableStorage;

        public Satellite(String satelliteId, String type, double height, Angle position) {
                this.satelliteId = satelliteId;
                this.type = type;
                this.height = height;
                this.position = position;
                this.files = new HashMap<>();

        }

        public int getDownloadBandwidth() {
                return downloadBandwidth;
        }

        public void setDownloadBandwidth(int downloadBandwidth) {
                this.downloadBandwidth = downloadBandwidth;
        }

        public int getAvaliableStorage() {
                return avaliableStorage;
        }

        public void setAvaliableStorage(int avaliableStorage) {
                this.avaliableStorage = avaliableStorage;
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

        public int getUploadBandwidth() {
                return uploadBandwidth;
        }

        public void setUploadBandwidth(int uploadBandwidth) {
                this.uploadBandwidth = uploadBandwidth;
        }

        public int getMaxRange() {
                return maxRange;
        }

        public double getSpeed() {
                return speed;
        }

        public String getSatelliteId() {
                return satelliteId;
        }

        public void setSatelliteId(String satelliteId) {
                this.satelliteId = satelliteId;
        }

        public String getType() {
                return type;
        }

        public void setType(String type) {
                this.type = type;
        }

        public double getHeight() {
                return height;
        }

        public void setHeight(double height) {
                this.height = height;
        }

        public Angle getPosition() {
                return position;
        }

        public void setPosition(Angle position) {
                this.position = position;
        }

        public Map<String, Fileinfo> getFiles() {
                return files;
        }

        public void addFile(String filename, Fileinfo fileInformation) {
                files.put(filename, fileInformation);
        }

        public void moveSatellite() {
                // = speed/height * 180/pi * simulation minutes
                Angle changeInAngle = Angle.fromRadians((getSpeed()) / (getHeight()));

                // position is less than 0
                if (position.subtract(changeInAngle).compareTo(Angle.fromRadians(0)) < 1) {
                        this.setPosition(this.getPosition().add(Angle.fromRadians(2 * Math.PI)));
                }

                this.setPosition(this.getPosition().subtract(changeInAngle));
        }

        public EntityInfoResponse satelliteInfo() {

                Map<String, FileInfoResponse> objectMap = new HashMap<>();

                for (Map.Entry<String, Fileinfo> entry : this.getFiles().entrySet()) {
                        Fileinfo fileinfo = entry.getValue();
                        FileInfoResponse individualFiles = newFileInfoResponse(fileinfo);

                        objectMap.put(fileinfo.getFileName(), individualFiles);
                }

                return new EntityInfoResponse(this.satelliteId, this.position, height, this.type, objectMap);

        }

        public FileInfoResponse newFileInfoResponse(Fileinfo file) {
                return new FileInfoResponse(file.getFileName(), file.getContent(), file.getFileSize(),
                                file.isCompleteFunction());
        }

        // checks if a device is visible and in range
        public boolean isVisibleAndInRange(Device d) {

                if (d.getType().equals("DesktopDevice") && getType().equals("StandardSatellite")) {
                        return false;
                }

                if (MathsHelper.isVisible(this.height, this.position, d.getPosition())) {
                        if (MathsHelper.getDistance(this.height, this.position, d.getPosition()) <= getMaxRange()) {
                                return true;
                        }
                }
                return false;
        }

        public boolean isVisibleAndInRange(Satellite s) {
                if (MathsHelper.isVisible(getHeight(), getPosition(), s.getHeight(), s.getPosition())) {
                        if (MathsHelper.getDistance(getHeight(), getPosition(), s.getHeight(),
                                        s.getPosition()) <= getMaxRange()) {
                                return true;
                        }
                }
                return false;
        }

        public boolean storageFull() {
                return false;
        }

        public Fileinfo findFileInformation(String givenFileName) {
                for (Map.Entry<String, Fileinfo> entry : getFiles().entrySet()) {
                        if (entry.getKey().equals(givenFileName)) {
                                return entry.getValue();
                        }
                }

                return null;
        }

        public boolean canDownload() {
                if (getDownloadNumber() >= getDownloadBandwidth()) {
                        return false;
                }
                return true;
        }

}
