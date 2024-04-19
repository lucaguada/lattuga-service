package dev.vegetable.lattuga.log;

import dev.vegetable.lattuga.Log;

import java.util.ResourceBundle;

public record NamedLog<ANY>(String getName) implements Log {
  public NamedLog(Class<ANY> type) {
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
