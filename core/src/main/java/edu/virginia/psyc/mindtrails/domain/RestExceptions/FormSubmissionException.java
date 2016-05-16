package edu.virginia.psyc.mindtrails.domain.RestExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by dan on 10/26/15.
 */
@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason = "There was a problem saving the form.  Please check the logs.")
public class FormSubmissionException extends RuntimeException {

    public FormSubmissionException() {
        super();
    }

    public FormSubmissionException(String description) {
        super(description);
    }

    public FormSubmissionException(Exception e) {
        super(e);
    }
}
