package dev.vegetable.lattuga.net.http.server.exchange.filter.chain;

import com.sun.net.httpserver.HttpExchange;
import dev.vegetable.lattuga.net.Http;

public record Stop(HttpExchange exchange) implements Http.Exchange.Filter.Chain {
  @Override
  public Http.Exchange.Filter.Chain follow() {
    exchange.close();
    return Done.UNIT;
  }
}
