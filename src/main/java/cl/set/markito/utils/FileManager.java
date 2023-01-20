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

    public int downloadFile(String URL, String targetFilePathname) {
        Boolean found=false;
        printf(ANSI_YELLOW + "downloadFile from %s to %s", URL, targetFilePathname);

        try (BufferedInputStream in = new BufferedInputStream(new URL(URL).openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(targetFilePathname)) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
                if ( !found ) {
                    found = true;
                }
                printf(".");
            }
            printf("done.\n");
            if ( found ) {
                return 0;
            } else {
                return 1;
            }
        } catch (Exception e) {
            printf(ANSI_RED+"failed.\n");
            return -1;
        }
    }

    public String readFileToString(String filePathname) {
        printf(ANSI_YELLOW + "readFileToString %s...", filePathname);
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(filePathname), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (Exception e) {
            printf(ANSI_RED+"ERROR:%s", e.getMessage());
            return null;
        }
        printf("done.\n");
        return contentBuilder.toString();
    }

    public boolean checkIfFileIsDownloaded(String fileName) {
        printf(ANSI_YELLOW + "checkIfFileIsDownloaded %s..", fileName);

        String filePathname = userDownloadsFolder + fileName;
        File file = new File(filePathname);
        if (file.exists() && !file.isDirectory()) {
            printf(ANSI_YELLOW + "found.\n");
            return true;
        } else {
            printf(ANSI_YELLOW + "not found.\n");
            return false;
        }
    }

    public boolean waitForFileDownloaded(String fileName, long timeoutInSeconds) {
        int i = 0;
        String filePathname = FileSystems.getDefault().getPath(userDownloadsFolder, fileName).toString();

        printf(ANSI_YELLOW + "WaitForFileDownloaded %s..", filePathname);
        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (checkIfFileIsDownloaded(fileName)) {
                printf(ANSI_YELLOW + "found.\n");
                return true;
            }
            printf(ANSI_YELLOW + ".");
        } while (i++ < timeoutInSeconds);
        printf(ANSI_RED + "ERROR: File %s not found in %d\n", filePathname, timeoutInSeconds);
        return false;
    }

    public void deleteDownloadedFileIfExists(String fileName) {
        printf(ANSI_YELLOW + "deleteDownloadedFileIfExists %s..", fileName);

        if (checkIfFileIsDownloaded(fileName)) {
            String filePathname = System.getProperty("user.home") + "/Downloads/" + fileName;
            File file = new File(filePathname);
            file.delete();
            printf(ANSI_YELLOW + "done.\n");
        } else {
            printf(ANSI_YELLOW + "not found.\n");
        }
    }

    public File[] findFilesByNameRegex(String nameRegex, String folder) {
        File[] files;
        printf(ANSI_YELLOW + "findFilesByNameRegex regEx=%s in folder=%s..", nameRegex, folder);

        try {
            File f = new File(folder);

            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File f, String name) {
                    return name.matches(nameRegex);
                }
            };
            files = f.listFiles(filter);
            printf(ANSI_YELLOW + "done.\n");
            return files;
        } catch (Exception e) {
            printf(ANSI_RED + "ERROR:%s.\n", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public boolean deleteFile( String filename) { 
        printf(ANSI_YELLOW+"DeleteFile %s\n", filename);
        try {
            File myObj = new File(filename); 
            return myObj.delete();
        }
        catch (Exception e){
            printf(ANSI_RED+"failed!!! %s\n", e.getMessage());
            return false;
        }

    } 
}