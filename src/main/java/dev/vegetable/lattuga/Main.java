package dev.vegetable.lattuga;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import dev.vegetable.lattuga.service.Http;
import dev.vegetable.lattuga.service.Log;
import io.lettuce.core.RedisClient;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface Main {
  Log log = Log.of(Main.class);

  static void main(String[] args) {
    try (final var log = Log.withRedis(Main.class, RedisClient.create("redis://eKsRFG2g8keAIel5eGH@hv-par6-007.clvrcld.net:12019"))) {
      Http.server()
        .bind(8080)
        .get("/")
        .exchange((_, response) ->
          response
            .header("Content-Type", "text/plain")
            .body("Hello, world!")
            .asText())
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

  final class LogFilter extends Filter {
    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
      log.info("Request: %s", exchange.getRequestURI());
      chain.doFilter(exchange);
    }

    @Override
    public String description() {
      return LogFilter.class.getSimpleName();
    }
  }
}
