package de.mnreinisch.pp.watcher.domain.exceptions;


import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomException extends Exception {
    public CustomException(String message, Throwable cause) {
        super(message, cause);
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.WARNING, message, cause);
    }

    public CustomException(String message) {
        super(message);

        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).warning(message);
    }
}
