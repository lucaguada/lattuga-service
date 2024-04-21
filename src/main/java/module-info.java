module lattuga.service {
  requires jdk.httpserver;
  requires lettuce.core;

  exports dev.vegetable.lattuga;
  exports dev.vegetable.lattuga.service;
}
