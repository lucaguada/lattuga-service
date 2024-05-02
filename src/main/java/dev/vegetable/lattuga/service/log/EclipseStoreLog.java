package dev.vegetable.lattuga.service.log;

import dev.vegetable.lattuga.service.Log;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.StampedLock;

public record EclipseStoreLog(Log log, EmbeddedStorageManager storage, Journal journal) implements Log {
  private static final class Journal {
    private static final StampedLock stamped = new StampedLock();

    private final Map<UUID, String> logs = new HashMap<>();

    public void append(String log) {
      final var stamp = stamped.writeLock();
      logs.put(UUID.randomUUID(), log);
      stamped.unlockWrite(stamp);
    }
  }

  public EclipseStoreLog(Log log, EmbeddedStorageManager storage) {
    this(log, storage, (Journal) storage.setRoot(new Journal()));
  }

  @Override
  public void close() {
    storage.storeRoot();
    storage.shutdown();
  }

  @Override
  public String getName() {
    return log.getName();
  }

  @Override
  public void log(Level level, String format, Object... params) {
    journal.append(String.format(TEMPLATE, level, log.getName(), format.formatted(params)));
    log.log(level, format, params);
  }
}
