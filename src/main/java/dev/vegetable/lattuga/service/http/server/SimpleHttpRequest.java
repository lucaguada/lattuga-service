package dev.vegetable.lattuga.service.http.server;

import com.sun.net.httpserver.HttpExchange;
import dev.vegetable.lattuga.service.Http;

import java.net.URI;

public record SimpleHttpRequest(HttpExchange exchange) implements Http.Request {
  @Override
  public URI uri() {
    return exchange.getRequestURI();
  }

  public String body() {
    return exchange.getRequestBody().toString();
  }
}
