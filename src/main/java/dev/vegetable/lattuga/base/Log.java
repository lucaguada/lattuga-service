package dev.vegetable.lattuga.base;

import dev.vegetable.lattuga.base.log.EclipseStoreLog;
import dev.vegetable.lattuga.base.log.SystemLog;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageManager;

import java.util.ResourceBundle;

public sealed interface Log extends System.Logger, AutoCloseable permits SystemLog, EclipseStoreLog {
  String TEMPLATE = "[%s] %s: %s%n";

  static <ANY> Log of(Class<ANY> type) {
    return new SystemLog<>(type);
  }

  static <ANY> Log withEclipseStore(Class<ANY> type, EmbeddedStorageManager storage) {
    return new EclipseStoreLog(Log.of(type), storage);
  }

  default void info(String message, Object... params) {
    log(System.Logger.Level.INFO, message.formatted(params));
  }

  default void error(String message, Object... params) {
    log(System.Logger.Level.ERROR, message.formatted(params));
  }

  default void warning(String message, Object... params) {
    log(System.Logger.Level.WARNING, message.formatted(params));
  }

  default void debug(String message, Object... params) {
    log(System.Logger.Level.DEBUG, message.formatted(params));
  }

  default void trace(String message, Object... params) {
    log(System.Logger.Level.TRACE, message.formatted(params));
  }

  @Override
  default void log(Level level, ResourceBundle bundle, String message, Throwable thrown) {
    log(level, message, thrown.getMessage());
  }

  @Override
  default void log(Level level, ResourceBundle bundle, String message, Object... params) {
    log(level, message.formatted(params));
  }

  @Override
  default boolean isLoggable(Level level) {return true;}
}

