package com.himanshu.poc.rxjava;

import java.util.concurrent.atomic.AtomicInteger;

public class PersonFactory {
  private AtomicInteger counter = new AtomicInteger(0);

  public Person createPerson(String name) {
    int ctr = counter.incrementAndGet();
    System.out.println("Creating new person with id: "+ ctr +". In thread: " + Thread.currentThread().getName());
    return new Person(ctr, name);
  }
}
