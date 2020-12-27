package de.mnreinisch.pp.watcher.domain.exceptions;


import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomException extends Exception {
    private static final String baseMessage = "An error occurred:\n";
    public CustomException(String message, Throwable cause, boolean useBaseMessage) {
        super((useBaseMessage ? baseMessage : "") + message, cause);
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.WARNING, createMessage(message,useBaseMessage), cause);
    }

    public CustomException(String message, boolean useBaseMessage) {
        super((useBaseMessage ? baseMessage : "") + message);

        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).warning(createMessage(message,useBaseMessage));
    }

    private String createMessage(String message, boolean useBaseMessage){
        return (useBaseMessage ? baseMessage : "") + message;
    }
}
