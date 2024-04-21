package dev.vegetable.lattuga.service.http.server;

import com.sun.net.httpserver.HttpContext;
import dev.vegetable.lattuga.service.Http;

import java.util.function.BiFunction;

public record SimpleHttpEndpoint(Http.Server server, HttpContext endpoint, MethodFilter path) implements Http.Server.Endpoint {
  @Override
  public Http.Server.Endpoint header(String name, String... value) {
    return this;
  }

  @Override
  public Http.Server.Endpoint contentType(String type) {
    return this;
  }

  @Override
  public Http.Server exchange(BiFunction<Http.Request, Http.Response, Http.Response> handler) {
    endpoint.setHandler(exchange -> {
      exchange.getRequestURI();
      var request = new SimpleHttpRequest(exchange);
      var response = new SimpleHttpResponse(exchange);
      handler.apply(request, response);
    });
    return server;
  }
}
