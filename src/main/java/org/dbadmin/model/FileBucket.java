package org.dbadmin.model;

/**
 * Created by henrynguyen on 6/22/16.
 */
import org.springframework.web.multipart.MultipartFile;

public class FileBucket {

    MultipartFile file;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
