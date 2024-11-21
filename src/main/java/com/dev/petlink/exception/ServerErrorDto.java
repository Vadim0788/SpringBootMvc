package com.dev.petlink.exception;

import java.time.LocalDateTime;

public record ServerErrorDto (
        String message,
        String detailedMessage,
        LocalDateTime dateTime
) {

}
