package de.mnreinisch.pp.watcher.domain.exceptions;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TechnicalException extends Exception {

    public TechnicalException(String message, Throwable cause){
        super("An technical error occurred: " + message, cause);
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, "An technical error occurred: " + message, cause);
        cause.printStackTrace();
    }

    public TechnicalException(String message){
        super("An technical error occurred: " + message);
        Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).severe("An technical error occurred: " + message);
    }

}
