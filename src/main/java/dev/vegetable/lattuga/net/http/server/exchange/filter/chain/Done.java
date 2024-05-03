package dev.vegetable.lattuga.net.http.server.exchange.filter.chain;

import dev.vegetable.lattuga.net.Http;

public enum Done implements Http.Exchange.Filter.Chain {
  UNIT;

  @Override
  public Http.Exchange.Filter.Chain follow() {
    return Done.UNIT;
  }
}
