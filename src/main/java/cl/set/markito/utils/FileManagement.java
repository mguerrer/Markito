package cl.set.markito.utils;

import java.io.File;

public interface FileManagement {
    public int downloadFile(String URL, String targetFilePathname);
    public String readFileToString(String filePathname);
    public boolean checkIfFileIsDownloaded(String fileName);
    public boolean waitForFileDownloaded(String fileName, long timeoutInSeconds);
    public int deleteDownloadedFileIfExists(String fileName);
    public File[] findFilesByNameRegex(String nameRegex, String folder);
    public boolean deleteFile( String filename);
}
