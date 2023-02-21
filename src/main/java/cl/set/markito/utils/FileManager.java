package cl.set.markito.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileManager extends DebugManager implements FileManagement {
    public String userHomeFolder = System.getProperty("user.home");
    public String userDownloadsFolder = userHomeFolder + "/Downloads/";

    /**
     * Downloads a file from URL to a local file with targetFilePathname.
     */
    public int downloadFile(String URL, String targetFilePathname) {
        Boolean found = false;
        print(ANSI_YELLOW + "downloadFile from " + URL + " to " + targetFilePathname);

        try (BufferedInputStream in = new BufferedInputStream(new URL(URL).openStream());
                FileOutputStream fileOutputStream = new FileOutputStream(targetFilePathname)) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
                if (!found) {
                    found = true;
                }
                print(".");
            }
            println("done.");
            if (found) {
                return 0;
            } else {
                return 1;
            }
        } catch (Exception e) {
            println(ANSI_RED + "failed.");
            return -1;
        }
    }

    /**
     * Reads the filePathname file's content and returns it in a String.
     */
    public String readFileToString(String filePathname) {
        printf(ANSI_YELLOW + "readFileToString %s...", filePathname);
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(filePathname), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (Exception e) {
            printf(ANSI_RED + "ERROR:%s\n", e.getMessage());
            return null;
        }
        printf("done.\n");
        return contentBuilder.toString();
    }

    /**
     * Check if fileName exists in ~/Downloads folder.
     */
    public boolean checkIfFileIsDownloaded(String fileName) {
        print(ANSI_YELLOW + "checkIfFileIsDownloaded " + fileName + "...");

        String filePathname = userDownloadsFolder + fileName;
        File file = new File(filePathname);
        if (file.exists() && !file.isDirectory()) {
            println(ANSI_YELLOW + "found.");
            return true;
        } else {
            println(ANSI_YELLOW + "not found.");
            return false;
        }
    }

    /**
     * Waits for fileName file to be present in folder ~/Downloads for
     * timeoutInSeconds seconds. After that period fails.
     */
    public boolean waitForFileDownloaded(String fileName, long timeoutInSeconds) {
        int i = 0;
        String filePathname = FileSystems.getDefault().getPath(userDownloadsFolder, fileName).toString();

        print(ANSI_YELLOW + "WaitForFileDownloaded" + filePathname + "...");
        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (checkIfFileIsDownloaded(fileName)) {
                println(ANSI_YELLOW + "found.");
                return true;
            }
            printf(ANSI_YELLOW + ".");
        } while (i++ < timeoutInSeconds);
        printf(ANSI_RED + "ERROR: File " + filePathname + " not found in " + timeoutInSeconds + "seconds.");
        return false;
    }

    /**
     * Deletes a fileName file if it exists in ~/Downloads folder.
     */
    public int deleteDownloadedFileIfExists(String fileName) {
        int retCode;
        print(ANSI_YELLOW + "deleteDownloadedFileIfExists..." + fileName);

        if (checkIfFileIsDownloaded(fileName)) {
            String filePathname = System.getProperty("user.home") + "/Downloads/" + fileName;
            File file = new File(filePathname);
            file.delete();
            println(ANSI_YELLOW + "done.");
            retCode = 0;
        } else {
            println(ANSI_YELLOW + "not found.");
            retCode = 1;
        }
        return retCode;
    }

    /**
     * Find a list of files with names matching nameRegex in folder.
     */
    public File[] findFilesByNameRegex(String nameRegex, String folder) {
        File[] files;
        print(ANSI_YELLOW + "findFilesByNameRegex regEx=" + nameRegex + " in folder=" + folder + "...");

        try {
            File f = new File(folder);

            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File f, String name) {
                    return name.matches(nameRegex);
                }
            };
            files = f.listFiles(filter);
            println(ANSI_YELLOW + "done.");
            return files;
        } catch (Exception e) {
            println(ANSI_RED + "ERROR:" + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes a fileName file.
     * @return 
     */
    public boolean deleteFile(String filename) {
        print(ANSI_YELLOW + "DeleteFile " + filename +"...");
        try {
            File myObj = new File(filename);
            println("done.");
            return myObj.delete();
        } catch (Exception e) {
            println(ANSI_RED + "failed!!! " + e.getMessage());
            return false;
        }
    }
}