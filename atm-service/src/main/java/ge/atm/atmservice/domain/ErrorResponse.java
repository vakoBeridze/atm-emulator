package ge.atm.atmservice.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(
        description = "Error response"
)
public class ErrorResponse {
    @ApiModelProperty(
            value = "HTTP status of the response",
            required = true,
            example = "404"
    )
    private int status;
    @ApiModelProperty(
            value = "Error message",
            required = true,
            example = "Item not found"
    )
    private String message;

    public ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public ErrorResponse() {
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
