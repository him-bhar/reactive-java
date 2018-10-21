package com.himanshu.poc.rxjava;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class PersonSupplier implements Supplier<Person> {

  private AtomicInteger counter = new AtomicInteger(0);

  @Override
  public Person get() {
    return new Person(counter.incrementAndGet(), UUID.randomUUID().toString());
  }
}
