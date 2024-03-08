package unsw.blackout;

import java.util.HashMap;
import java.util.Map;

import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;
import unsw.utils.MathsHelper;

public abstract class Device extends BlackoutController {
        private String deviceId;
        private String type;
        private Angle position;
        private Map<String, Fileinfo> files;
        private double height = MathsHelper.RADIUS_OF_JUPITER;
        private int maxRange;

        public Device(String deviceId, String type, Angle position) {
                this.deviceId = deviceId;
                this.type = type;
                this.position = position;
                this.files = new HashMap<>();
        }

        public double getHeight() {
                return height;
        }

        public int getMaxRange() {
                return maxRange;
        }

        public String getDeviceId() {
                return deviceId;
        }

        public String getType() {
                return type;
        }

        public Angle getPosition() {
                return position;
        }

        public void setPosition(Angle position) {
                this.position = position;
        }

        public void addFile(String filename, Fileinfo fileInformation) {
                files.put(filename, fileInformation);
        }

        public Map<String, Fileinfo> getFiles() {
                return files;
        }

        public EntityInfoResponse deviceInfo() {
                Map<String, FileInfoResponse> objectMap = new HashMap<>();

                for (Map.Entry<String, Fileinfo> entry : this.files.entrySet()) {
                        Fileinfo fileinfo = entry.getValue();
                        FileInfoResponse individualFiles = new FileInfoResponse(fileinfo.getFileName(),
                                        fileinfo.getContent(), fileinfo.getFileSize(), fileinfo.isCompleteFunction());

                        objectMap.put(fileinfo.getFileName(), individualFiles);

                }

                EntityInfoResponse object = new EntityInfoResponse(this.deviceId, this.position, this.height, this.type,
                                objectMap);
                return object;

        }

        public boolean isVisibleAndInRange(Satellite s) {
                if (MathsHelper.isVisible(s.getHeight(), s.getPosition(), this.position)) {
                        if (MathsHelper.getDistance(s.getHeight(), s.getPosition(), this.position) <= getMaxRange()) {
                                return true;
                        }
                }
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
}
