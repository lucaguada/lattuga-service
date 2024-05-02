package dev.vegetable.lattuga.service.http.server;

import com.sun.net.httpserver.HttpServer;
import dev.vegetable.lattuga.service.Http;
import dev.vegetable.lattuga.service.http.server.filter.MethodFilter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public record SimpleHttpServer(HttpServer server, ExecutorService executor, Filters filters) implements Http.Server {
  public SimpleHttpServer(HttpServer server) {
    this(server, Executors.newVirtualThreadPerTaskExecutor(), Filters.factory());
  }
  public SimpleHttpServer {
    server.setExecutor(executor);
  }

  @Override
  public Endpoint endpoint(Method method, String path) {
    return new SimpleHttpEndpoint(this, server.createContext(path), filters.path(path), filters.method(method));
  }

  @Override
  public Server bind(int port) {
    try {
      server.bind(new InetSocketAddress(port), 0);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return this;
  }

  @Override
  public Server start() {
    server.start();
    return this;
  }

  @Override
  public Server stop() {
    server.stop(0);
    return this;
  }
}
