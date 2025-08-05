package com.wilderBackend.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Generic Response object for all controller.
 */
@Value
@Builder
@AllArgsConstructor
public class Response implements Serializable {

    @Serial
    private static final long serialVersionUID = 6090126975288080876L;

    private Integer status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "IST")
    private Date date = new Date();

    private Object data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> errors;


    public Response() {
        this.status = HttpStatus.OK.value();
        this.data = null;
        this.errors = null;
    }
}