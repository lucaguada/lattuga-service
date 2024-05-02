package dev.vegetable.lattuga.service.http.server.filter;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import dev.vegetable.lattuga.Main;

import java.io.IOException;

public final class LogFilter extends Filter {
  @Override
  public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
    Main.log.info("Request: %s", exchange.getRequestURI());
    chain.doFilter(exchange);
  }

  @Override
  public String description() {
    return LogFilter.class.getSimpleName();
  }
}
