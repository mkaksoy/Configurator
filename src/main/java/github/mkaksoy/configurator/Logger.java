package github.mkaksoy.configurator;

import java.util.logging.Level;

class Logger {
    private final java.util.logging.Logger logger;

    protected Logger(String name) {
        this.logger = java.util.logging.Logger.getLogger(name);
    }

    public void log(LogType type, String message) {
        message = type.name() + ": " + message;

        switch (type) {
            case ERROR -> logger.log(Level.SEVERE, message);
            case WARNING -> logger.log(Level.WARNING, message);
            case INFO -> logger.log(Level.INFO, message);
        }
    }
}