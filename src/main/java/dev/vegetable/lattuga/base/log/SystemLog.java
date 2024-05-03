package dev.vegetable.lattuga.base.log;

import dev.vegetable.lattuga.base.Log;

import java.util.ResourceBundle;

public record SystemLog<ANY>(String getName) implements Log {
  public SystemLog(Class<ANY> type) {
    this(type.getSimpleName());
  }

  @Override
  public void log(Level level, String msg) {
    System.out.printf(TEMPLATE, level, getName, msg);
  }

  @Override
  public void log(Level level, String message, Throwable thrown) {
    System.out.printf(TEMPLATE, level, getName, message);
    thrown.printStackTrace(System.out);
  }

  @Override
  public void log(Level level, ResourceBundle bundle, String message, Throwable thrown) {
    log(level, message, thrown);
  }

  @Override
  public void log(Level level, ResourceBundle bundle, String message, Object... params) {
    log(level, message.formatted(params));
  }

  @Override
  public void close() {}
}
