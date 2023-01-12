package co.zip.candidate.userapi.dto.error;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ErrorDto {
    String message;
    String details;
}