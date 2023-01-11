package ru.practicum.ewmcore.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorDto {
    private List<String> errors;
    private String message;
    private String reason;
    private HttpStatus status;
    private String timestamp;
}
