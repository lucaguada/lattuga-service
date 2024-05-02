package dev.vegetable.lattuga.service.http.server.filter;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public final class PathFilter extends Filter {
  private final String path;

  public PathFilter(String path) {this.path = path;}

  @Override
  public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
    if (exchange.getRequestURI().getPath().equals(path)) {
      chain.doFilter(exchange);
    } else {
      exchange.sendResponseHeaders(404, -1);
      exchange.close();
    }
  }

  @Override
  public String description() {
    return "%s %s".formatted(PathFilter.class.getSimpleName(), path);
  }
}
