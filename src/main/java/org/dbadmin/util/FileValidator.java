package org.dbadmin.util;

/**
 * Created by henrynguyen on 6/22/16.
 */
import org.dbadmin.model.FileBucket;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class FileValidator implements Validator {

    /**
     * Assign form into file bucket
     * @param clazz
     * @return
     */
    public boolean supports(Class<?> clazz) {
        return FileBucket.class.isAssignableFrom(clazz);
    }

    /**
     * Validate uploaded file.
     * @param obj an uploaded file.
     * @param errors an handler to set up error messages.
     */
    public void validate(Object obj, Errors errors) {
        FileBucket file = (FileBucket) obj;

        if(file.getFile()!=null){

        }
    }
}
