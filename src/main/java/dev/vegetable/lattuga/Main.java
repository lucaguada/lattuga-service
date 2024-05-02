package dev.vegetable.lattuga;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import dev.vegetable.lattuga.service.Http;
import dev.vegetable.lattuga.service.Log;
import io.lettuce.core.RedisClient;
import org.eclipse.store.storage.embedded.types.EmbeddedStorage;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageManager;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface Main {
  Log log = Log.of(Main.class);

  static void main(String[] args) {
    try (final var log = Log.withEclipseStore(Main.class, EmbeddedStorage.start())) {
      Http.server()
        .bind(8080)
        .get("/")
        .exchange((_, response) ->
          response
            .header("Content-Type", "text/plain")
            .body("Hello, world!")
            .asText()
        )
        .start();
    } catch (InterruptedException e) {
      log.error("Interrupted: %s", e.getMessage());
    } catch (ExecutionException e) {
      log.error("Execution error: %s", e.getMessage());
    } catch (TimeoutException e) {
      log.error("Timeout: %s", e.getMessage());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
