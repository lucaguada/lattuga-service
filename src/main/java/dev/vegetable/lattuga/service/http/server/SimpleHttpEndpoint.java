package dev.vegetable.lattuga.service.http.server;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpContext;
import dev.vegetable.lattuga.service.Http;

import java.util.List;
import java.util.function.BiConsumer;

public record SimpleHttpEndpoint(Http.Server server, HttpContext endpoint, Filter... filters) implements Http.Server.Endpoint {
  @Override
  public Http.Server.Endpoint header(String name, String... value) {
    return this;
  }

  @Override
  public Http.Server exchange(BiConsumer<Http.Request, Http.Response> handler) {
    endpoint.getFilters().addAll(List.of(filters));
    endpoint.setHandler(exchange -> handler.accept(new SimpleHttpRequest(exchange), new SimpleHttpResponse(exchange)));
    return server;
  }

  @Override
  public Http.Server.Endpoint contentType(String type) {
    return this;
  }
}
