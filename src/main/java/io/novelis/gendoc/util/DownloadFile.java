package io.novelis.gendoc.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;

public class DownloadFile {
    private final static Logger log = LoggerFactory.getLogger(io.novelis.gendoc.util.DownloadFile.class);

    public static void downloadFile(String fileDir, String fileName, HttpServletResponse response) throws FileNotFoundException {
        InputStream inputStream;

        File file = new File(fileDir + fileName);

        if (file.exists()) {

            String mimeType = URLConnection.guessContentTypeFromName(file.getName());
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }
            response.setContentType(mimeType);
            response.setHeader("Content-Disposition",
                "attachment; filename=\"" + file.getName() + "\"");
            response.setContentLength((int) file.length());
            inputStream = new BufferedInputStream(new FileInputStream(file));
            try {
                FileCopyUtils.copy(inputStream, response.getOutputStream());
            } catch (IOException e) {
                log.error(e.getMessage());
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        } else {
            throw new FileNotFoundException("Fichier introuvable.");
        }
    }
}
