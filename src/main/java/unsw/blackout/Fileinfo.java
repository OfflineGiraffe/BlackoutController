package unsw.blackout;

public class Fileinfo extends BlackoutController {
        private String fileName;
        private String content;
        private int fileSize;
        private String transferringContent;
        private boolean isComplete;
        private String sentFrom;
        private String sentTo;

        public Fileinfo(String fileName, String content, boolean isComplete) {
                this.fileName = fileName;
                this.content = content;
                this.fileSize = content.length();
                this.isComplete = isComplete;
                this.transferringContent = "";
                this.sentFrom = "";
                this.sentTo = "";
        }

        public int getfileSize() {
                return fileSize;
        }

        public String getTransferringContent() {
                return transferringContent;
        }

        public void setTransferringContent(Device s) {
                if (getTransferringSize() != this.fileSize && !isComplete) {
                        this.transferringContent = this.content.substring(0, getTransferringSize() + 1);
                }
        }

        public int getTransferringSize() {
                return transferringContent.length();
        }

        public void setTransferringSize(int transferringSize, Satellite s) {
                if (transferringSize > content.length()) {
                        transferringSize = content.length();
                }
                this.transferringContent = this.content.substring(0, transferringSize);
        }

        // task 1
        public void setTransferringContent(String content) {
                this.transferringContent = content;
        }

        public void resetTransferringContent() {
                this.transferringContent = "";
        }

        public String getSentFrom() {
                return sentFrom;
        }

        public void setSentFrom(String sentFrom) {
                this.sentFrom = sentFrom;
        }

        public String getSentTo() {
                return sentTo;
        }

        public void setSentTo(String sentTo) {
                this.sentTo = sentTo;
        }

        public int getFileSize() {
                return content.length();
        }

        public String getFileName() {
                return fileName;
        }

        public void setFileName(String fileName) {
                this.fileName = fileName;
        }

        public String getContent() {
                if (isComplete) {
                        return content;
                } else {
                        return transferringContent;
                }
        }

        public void setContent(String content) {
                this.content = content;
        }

        public boolean isCompleteFunction() {
                return content.equals(transferringContent);
        }

        public void setComplete(boolean isComplete) {
                this.isComplete = isComplete;
        }

        public boolean isComplete() {
                return isComplete;
        }
}
