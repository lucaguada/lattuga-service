package dev.vegetable.lattuga.service.http.server;

import com.sun.net.httpserver.HttpExchange;
import dev.vegetable.lattuga.service.Http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

import static dev.vegetable.lattuga.service.Http.Code.Success.OK;

public record SimpleHttpResponse(HttpExchange exchange) implements Http.Response {

  @Override
  public Http.Response header(String name, String... value) {
    exchange.getResponseHeaders().put(name, List.of(value));
    return this;
  }

  @Override
  public <CODE extends Http.Code> Http.Response status(CODE code, String line) {
    try {
      switch (code) {
        case Http.Code status when status == OK && exchange.getAttribute("submit_content") instanceof byte[] content:
          exchange.sendResponseHeaders(code.status(), content.length);
          break;
        case Http.Code status when status == OK && exchange.getAttribute("submit_content") instanceof String content:
          exchange.sendResponseHeaders(code.status(), content.getBytes().length);
          break;
        default:
          exchange.sendResponseHeaders(code.status(), -1);
          break;
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return this;
  }

  @Override
  public <RECORD extends Record & Serializable> Http.Response body(RECORD record) {
    exchange.setAttribute("submit_content", record);
    return this;
  }

  @Override
  public Http.Response body(String text) {
    exchange.setAttribute("submit_content", text);
    return this;
  }

  @Override
  public Http.Response body(byte[] bytes) {
    exchange.setAttribute("submit_content", bytes);
    return this;
  }

  @Override
  public void asJson() {

  }

  @Override
  public void asText() {
    try {
      exchange.getResponseBody().write(
        exchange
          .getAttribute("submit_content")
          .toString()
          .getBytes()
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    } finally {
      exchange.close();
    }
  }

  @Override
  public void asHtml() {

  }

  @Override
  public void asXml() {

  }

  @Override
  public void asBytes() {
    try {
      switch (exchange.getAttribute("submit_content")) {
        case byte[] content -> exchange.getResponseBody().write(content);
        case String content -> exchange.getResponseBody().write(content.getBytes());
        case Serializable serializable when serializable instanceof Record content -> {
          try (
            final var bytes = new ByteArrayOutputStream();
            final var object = new ObjectOutputStream(bytes);
          ) {
            object.writeObject(content);
            exchange.getResponseBody().write(bytes.toByteArray());
          }
        }
        default -> throw new IllegalStateException("Unexpected value: %s".formatted(exchange.getAttribute("submit_content")));
      }
      exchange.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
