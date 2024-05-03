package dev.vegetable.lattuga.net.http.server.exchange.filter;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import dev.vegetable.lattuga.net.Http;
import dev.vegetable.lattuga.net.http.server.exchange.SimpleExchange;
import dev.vegetable.lattuga.net.http.server.exchange.filter.chain.Next;

import java.io.IOException;

public final class SimpleFilter extends Filter {
  private final Http.Exchange.Filter filter;

  public SimpleFilter(Http.Exchange.Filter filter) {this.filter = filter;}

  @Override
  public void doFilter(HttpExchange exchange, Chain chain) {
    switch (filter.chain(new SimpleExchange(chain))) {
      case Error error -> exchange.sendResponseHeaders(error.code().status(), -1);
    }
    filter
      .chain(new SimpleExchange(exchange))
      .follow();
  }

  @Override
  public String description() {
    return "";
  }
}
