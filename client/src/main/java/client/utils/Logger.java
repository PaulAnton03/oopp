package client.utils;

public class Logger {

    public enum LogLevel {
        DEBUG, INFO, WARN, ERROR
    }

    public static void log(String msg) {
        log(msg, LogLevel.INFO);
    }

    public static void log(String msg, LogLevel level) {
        System.out.println("[" + level + "] " + msg);
    }
}
