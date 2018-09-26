package org.dbadmin.util;

/**
 * Created by henrynguyen on 6/22/16.
 */
import org.dbadmin.model.FileBucket;
import org.dbadmin.model.MultiFileBucket;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class MultiFileValidator implements Validator {

    /**
     * assign form into the file bucket
     * @param clazz
     * @return
     */
    public boolean supports(Class<?> clazz) {
        return MultiFileBucket.class.isAssignableFrom(clazz);
    }

    /**
     * valicate multiples files
     * @param obj is a list of files
     * @param errors
     */
    public void validate(Object obj, Errors errors) {
        MultiFileBucket multiBucket = (MultiFileBucket) obj;

        int index=0;

        for(FileBucket file : multiBucket.getFiles()){
            if(file.getFile()!=null){
                if (file.getFile().getSize() == 0) {
                    errors.rejectValue("files["+index+"].file", "missing.file");
                }
            }
            index++;
        }

    }
}
