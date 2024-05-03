package dev.vegetable.lattuga.net.http.server.exchange.filter;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import dev.vegetable.lattuga.base.Log;

import java.io.IOException;

public final class LogFilter extends Filter {
  private final Log log;

  public LogFilter(Log log) {this.log = log;}

  @Override
  public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
    log.info("Request: %s", exchange.getRequestURI());
    chain.doFilter(exchange);
  }

  @Override
  public String description() {
    return LogFilter.class.getSimpleName();
  }
}
