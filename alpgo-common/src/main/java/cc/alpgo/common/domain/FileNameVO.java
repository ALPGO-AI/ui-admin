package cc.alpgo.common.domain;

public class FileNameVO {

    public FileNameVO(String fileName, String folderName) {
        this.fileName = fileName;
        this.folderName = folderName;
    }

    private String fileName;
    private String folderName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }
}
