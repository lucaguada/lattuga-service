package dev.vegetable.lattuga;

import dev.vegetable.lattuga.base.Log;
import dev.vegetable.lattuga.net.Http;
import org.eclipse.store.storage.embedded.types.EmbeddedStorage;

import static dev.vegetable.lattuga.net.Http.Response.Content.Type.TEXT;

@SuppressWarnings("resource")
public enum Service {
  Lattuga(Log.withEclipseStore(Service.class, EmbeddedStorage.start()), Http.server().bind(8080));

  private final Log log;
  private final Http.Server server;

  Service(Log log, Http.Server server) {
    this.log = log;
    this.server = server;
  }

  public Service start() {
    server
      .get("/").exchange((_, response) ->
        response
          .contentType(TEXT)
          .body("Hello, world!")
          .asText()
      )
      .post("/stop").exchange((_, response) -> {
        response
          .contentType(TEXT)
          .body("Stopping server...")
          .asText();
        server.stop();
      })
      .start();

    return this;
  }

  static void main(String[] args) {
    Lattuga.start();
  }
}
