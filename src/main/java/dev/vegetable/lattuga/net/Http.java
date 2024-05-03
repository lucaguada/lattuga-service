package dev.vegetable.lattuga.net;

import com.sun.net.httpserver.HttpServer;
import dev.vegetable.lattuga.net.http.server.SimpleServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static dev.vegetable.lattuga.net.Http.Code.Success.OK;
import static dev.vegetable.lattuga.net.Http.Response.Content.Type.JSON;

public interface Http<HTTP extends Http<HTTP, ENDPOINT>, ENDPOINT extends Http.Endpoint<HTTP, ENDPOINT>> {
  static Http.Server server() {
    try {
      return new SimpleServer(HttpServer.create());
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

  interface Exchange {
    URI uri();

    Optional<Method> method();

    <BODY> Optional<BODY> body(Function<? super InputStream, ? extends Optional<BODY>> codec);

    interface Filter {
      Chain chain(Exchange exchange);

      interface Chain {
        Chain follow();
      }
    }
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

    enum ClientError implements Code {
      BAD_REQUEST(400), UNAUTHORIZED(401), FORBIDDEN(403), NOT_FOUND(404), METHOD_NOT_ALLOWED(405);

      private final int code;

      ClientError(int code) {this.code = code;}

      @Override
      public int status() {return code;}
    }

    enum ServerError implements Code {
      INTERNAL_SERVER_ERROR(500), NOT_IMPLEMENTED(501), BAD_GATEWAY(502), SERVICE_UNAVAILABLE(503), GATEWAY_TIMEOUT(504);

      private final int code;

      ServerError(int code) {this.code = code;}

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
        JSON("application/json"), TEXT("plain/text"), HTML("plain/html"), XML("application/xml"), STREAM("application/octet-stream");

        private final String value;
        Type(String type) {this.value = type;}

        @Override
        public String toString() { return value;}
      }
    }
  }

  enum Method {
    GET, POST, PUT, PATCH, HEAD, OPTIONS, DELETE;

    private static final Collection<Method> methods = List.of(values());

    public static Optional<Method> from(String name) {
      return methods.stream()
        .filter(method -> method.name().equalsIgnoreCase(name))
        .findFirst();
    }
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
