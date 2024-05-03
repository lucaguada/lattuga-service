package dev.vegetable.lattuga.net.http.server.exchange;

import com.sun.net.httpserver.Filter;
import dev.vegetable.lattuga.net.Http;
import dev.vegetable.lattuga.net.http.server.exchange.filter.Factory;
import dev.vegetable.lattuga.net.http.server.exchange.filter.LogFilter;
import dev.vegetable.lattuga.net.http.server.exchange.filter.MethodFilter;
import dev.vegetable.lattuga.net.http.server.exchange.filter.PathFilter;

public sealed interface Filters permits Factory {
  static Filters factory() {
    return Factory.Default;
  }

  default Filter method(Http.Method method) {
    return new MethodFilter(method);
  }

  default Filter log() {
    return new LogFilter();
  }

  default Filter path(String path) {
    return new PathFilter(path);
  }
}
