package dev.vegetable.lattuga;

import com.sun.net.httpserver.*;
import io.lettuce.core.RedisClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface Main {
  Log log = Log.forType(Main.class);

  static void main(String[] args) {
    try (final var log = Log.withRedis(Main.class, RedisClient.create("redis://eKsRFG2g8keAIel5eGH@hv-par6-007.clvrcld.net:12019"))) {
      final var httpServer = HttpServer.create();


      httpServer.bind(new InetSocketAddress(8080), 0);
      httpServer.createContext("/", HttpHandlers.of(200, "Hello, world!"));
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
      return "";
    }
  }
}
