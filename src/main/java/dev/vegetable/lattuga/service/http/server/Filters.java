package dev.vegetable.lattuga.service.http.server;

import com.sun.net.httpserver.Filter;
import dev.vegetable.lattuga.service.Http;
import dev.vegetable.lattuga.service.http.server.filter.Factory;
import dev.vegetable.lattuga.service.http.server.filter.LogFilter;
import dev.vegetable.lattuga.service.http.server.filter.MethodFilter;
import dev.vegetable.lattuga.service.http.server.filter.PathFilter;

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
