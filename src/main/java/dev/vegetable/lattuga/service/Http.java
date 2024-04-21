package dev.vegetable.lattuga.service;

import com.sun.net.httpserver.HttpServer;
import dev.vegetable.lattuga.service.http.server.SimpleHttpServer;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.function.BiConsumer;

import static dev.vegetable.lattuga.service.Http.Code.Success.OK;
import static dev.vegetable.lattuga.service.Http.Response.Content.Type.JSON;

public interface Http<HTTP extends Http<HTTP, ENDPOINT>, ENDPOINT extends Http.Endpoint<HTTP, ENDPOINT>> {
  static Http.Server server() {
    try {
      return new SimpleHttpServer(HttpServer.create());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  ENDPOINT endpoint(Http.Method method, String path);

  default ENDPOINT get(String path) {return endpoint(Method.GET, path);}

  default ENDPOINT post(String path) {return endpoint(Method.POST, path);}

  interface Server extends Http<Http.Server, Http.Server.Endpoint> {
    interface Endpoint extends Http.Endpoint<Http.Server, Http.Server.Endpoint> {
      Endpoint contentType(String type);
    }

    Server bind(int port);

    Server start();

    Server stop();
  }

  interface Endpoint<HTTP extends Http<HTTP, ENDPOINT>, ENDPOINT extends Endpoint<HTTP, ENDPOINT>> {
    ENDPOINT header(String name, String... value);

    HTTP exchange(BiConsumer<Request, Response> handler);
  }

  interface Request {
    URI uri();
  }

  interface Code {
    static Code of(int code) {
      return switch (code) {
        case 200 -> OK;
        case 201 -> Success.CREATED;
        case 202 -> Success.ACCEPTED;
        case 204 -> Success.NO_CONTENT;
        default -> throw new IllegalArgumentException("Invalid code: %d".formatted(code));
      };
    }

    int status();

    enum Success implements Code {
      OK(200), CREATED(201), ACCEPTED(202), NO_CONTENT(204);

      private final int code;

      Success(int code) {this.code = code;}

      @Override
      public int status() {return code;}
    }
  }

  interface Response {
    Response header(String name, String... value);
    default Response contentType(Content type) {return header("Content-Type", type.toString());}

    default <CODE extends Code> Response status(CODE code) {return status(code, null);}

    <CODE extends Code> Response status(CODE code, String line);

    default Response status(int code) {return status(Code.of(code));}

    default Response status(int code, String line) {return status(Code.of(code), line);}

    <RECORD extends Record & Serializable> Response body(RECORD record);

    Response body(String text);

    Response body(byte[] bytes);

    void asJson();

    void asText();

    void asHtml();

    void asXml();

    void asBytes();

    interface Content {
      enum Type implements Content {
        JSON("application/json"), TEXT("plain/text"), HTML("plain/html"), XML("application/xml"), BYTES("application/octet-stream");

        private final String value;
        Type(String type) {this.value = type;}

        @Override
        public String toString() { return value;}
      }
    }
  }

  enum Method {
    GET, POST, PUT, PATCH, HEAD, OPTIONS, DELETE
  }

  public static void main(String[] args) {
    Http.server()
      .post("/api/v1/vegetable")
      .exchange((_, response) ->
        response
          .contentType(JSON)
          .status(OK)
          .body("Hello, world!")
          .asJson()
      )
      .bind(8080)
      .start()
      .stop();
  }
}
