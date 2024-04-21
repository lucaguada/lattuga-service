package dev.vegetable.lattuga.service.log;

import dev.vegetable.lattuga.service.Log;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;

import java.util.UUID;

@SuppressWarnings("preview")
public record RedisLog(Log log, RedisClient redis, StatefulRedisConnection<String, String> connection, RedisAsyncCommands<String, String> commands) implements Log {
  public RedisLog(Log log, RedisClient redis) {
    var connected = redis.connect();
    this(log, redis, connected, connected.async());
  }

  @Override
  public void close() {
    redis.close();
    connection.close();
  }

  @Override
  public String getName() {
    return log.getName();
  }

  @Override
  public void log(Level level, String format, Object... params) {
    commands.set(STR."log-\{UUID.randomUUID().toString().toLowerCase()}", String.format(TEMPLATE, level, log.getName(), format.formatted(params)));
    log.log(level, format, params);
  }
}
