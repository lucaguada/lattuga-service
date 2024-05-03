package dev.vegetable.lattuga.net.http.server.exchange;

import com.sun.net.httpserver.HttpExchange;
import dev.vegetable.lattuga.net.Http;

import java.io.InputStream;
import java.net.URI;
import java.util.Optional;
import java.util.function.Function;

public record SimpleExchange(HttpExchange exchange) implements Http.Exchange {
  @Override
  public URI uri() {
    return exchange.getRequestURI();
  }

  @Override
  public Optional<Http.Method> method() {
    return Http.Method.from(exchange.getRequestMethod());
  }

  @Override
  public <BODY> Optional<BODY> body(Function<? super InputStream, ? extends Optional<BODY>> codec) {
    return codec.apply(exchange.getRequestBody());
  }
}
