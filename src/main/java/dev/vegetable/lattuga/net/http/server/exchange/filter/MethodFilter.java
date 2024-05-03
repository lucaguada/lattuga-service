package dev.vegetable.lattuga.net.http.server.exchange.filter;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import dev.vegetable.lattuga.net.Http;

import java.io.IOException;

public final class MethodFilter extends Filter {
  private final Http.Method method;

  public MethodFilter(Http.Method method) {this.method = method;}

  @Override
  public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
    if (Http.Method.valueOf(exchange.getRequestMethod()) == method) {
      chain.doFilter(exchange);
    } else {
      exchange.sendResponseHeaders(405, -1);
      exchange.close();
    }
  }

  @Override
  public String description() {
    return "%s %s".formatted(MethodFilter.class.getSimpleName(), method.name());
  }
}
