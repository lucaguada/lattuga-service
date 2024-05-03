package dev.vegetable.lattuga.net.http.server.exchange.filter.chain;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import dev.vegetable.lattuga.net.Http;

import java.io.IOException;

public record Next(HttpExchange exchange, Filter.Chain chain) implements Http.Exchange.Filter.Chain {
  @Override
  public Http.Exchange.Filter.Chain follow() {
    try {
      chain.doFilter(exchange);
    } catch (IOException throwable) {
      return new Error(exchange, Http.Code.ServerError.INTERNAL_SERVER_ERROR, throwable);
    }
    return Done.UNIT;
  }
}
