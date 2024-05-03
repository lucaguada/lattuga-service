package dev.vegetable.lattuga.net.http.server.endpoint;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpContext;
import dev.vegetable.lattuga.net.Http;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public record SimpleEndpoint(Http.Server server, HttpContext endpoint, Http.Exchange.Filter... filters) implements Http.Server.Endpoint {
  @Override
  public Http.Server.Endpoint header(String name, String... value) {
    return this;
  }

  @Override
  public Http.Server exchange(BiConsumer<Http.Request, Http.Response> handler) {
    Stream.of(filters)
        .map(filter -> )
    endpoint.getFilters().addAll(List.of(filters));
    endpoint.setHandler(exchange -> handler.accept(new SimpleRequest(exchange), new SimpleResponse(exchange)));
    return server;
  }

  @Override
  public Http.Server.Endpoint contentType(String type) {
    return this;
  }
}
