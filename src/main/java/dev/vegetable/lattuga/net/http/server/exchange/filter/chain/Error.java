package dev.vegetable.lattuga.net.http.server.exchange.filter.chain;

import com.sun.net.httpserver.HttpExchange;
import dev.vegetable.lattuga.net.Http;

import java.io.IOException;

import static dev.vegetable.lattuga.net.Http.Code.ServerError.INTERNAL_SERVER_ERROR;

public record Error(HttpExchange exchange, Http.Code code, Throwable throwable) implements Http.Exchange.Filter.Chain {
  @Override
  public Http.Exchange.Filter.Chain follow() {
    try {
      exchange.sendResponseHeaders(code.status(), -1);
    } catch (IOException throwable) {
      return new Error(exchange, INTERNAL_SERVER_ERROR, throwable);
    }
    return new Stop(exchange);
  }
}
