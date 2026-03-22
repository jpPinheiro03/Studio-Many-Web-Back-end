package school.spetch.backend_Studio_many.exception;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({ "status", "error", "message", "errors" })
public class ErroResponse {
    private int status;
    private String httpError;
    private String message;
    private List<String> errors;

    public ErroResponse(int status, String httpError, String message) {
        this.status = status;
        this.httpError = httpError;
        this.message = message;
    }

    public ErroResponse(int status, String httpError, String message, List<String> errors) {
        this.status = status;
        this.httpError = httpError;
        this.message = message;
        this.errors = errors;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return httpError;
    }

    public void setError(String error) {
        this.httpError = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
