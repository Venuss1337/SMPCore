package com.venuss.smpcore.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogUtil {

    private static Logger logger;
    private static boolean debugMode;

    public LogUtil(Logger logger) {
        logger = logger;
        debugMode = false;
    }

    public void setDebug(boolean debugMode) {
        debugMode = debugMode;
    }

    public static void warning(String message) {
        logger.log(Level.WARNING, message);
    }

    public static void info(String message) {
        logger.log(Level.INFO, message);
    }

    public static void severe(String message) {
        logger.log(Level.SEVERE, message);
    }

    public static void debug(String message) {
        if (!debugMode) return;
        logger.log(Level.INFO, message);
    }
}
