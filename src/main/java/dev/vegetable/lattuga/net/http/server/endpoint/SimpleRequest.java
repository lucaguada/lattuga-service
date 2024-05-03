package dev.vegetable.lattuga.net.http.server.endpoint;

import com.sun.net.httpserver.HttpExchange;
import dev.vegetable.lattuga.net.Http;

import java.net.URI;

public record SimpleRequest(HttpExchange exchange) implements Http.Request {
  @Override
  public URI uri() {
    return exchange.getRequestURI();
  }

  public String body() {
    return exchange.getRequestBody().toString();
  }
}
