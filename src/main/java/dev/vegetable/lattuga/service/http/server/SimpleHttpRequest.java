package dev.vegetable.lattuga.service.http.server;

import com.sun.net.httpserver.HttpExchange;
import dev.vegetable.lattuga.service.Http;

public record SimpleHttpRequest(HttpExchange exchange) implements Http.Request {
}
