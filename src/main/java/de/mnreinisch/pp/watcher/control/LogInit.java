package de.mnreinisch.pp.watcher.control;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogInit {
    private static String globalLogFolder = ".\\logs\\global\\";

    private static String globalLogFile = ".\\logs\\global\\log_" + LocalDate.now().format(DateTimeFormatter.ISO_DATE) + ".log";


    private static File devMode = new File(".\\config\\devMode");


    public static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static void initializeLogger() throws IOException {
        init(LOGGER, globalLogFolder, globalLogFile, false);
    }

    private static void init(Logger logger, String logFolder, String logFile, boolean ignoreDevMode) throws IOException {
        logger.setLevel(Level.ALL);
        File logFolderAsFile = new File(logFolder);

        if(!logFolderAsFile.exists()){
            logFolderAsFile.mkdir();
        }

        if(ignoreDevMode || !devMode.exists()){
            FileHandler fileHandler = new FileHandler(logFile, true);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);

            logger.addHandler(fileHandler);
        }
    }

    private LogInit(){}
}
