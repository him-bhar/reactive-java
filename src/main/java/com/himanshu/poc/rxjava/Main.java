package com.himanshu.poc.rxjava;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
  private static Logger LOG = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {
    AtomicInteger subscriberThreadCtr = new AtomicInteger(0);
    AtomicInteger observerThreadCtr = new AtomicInteger(0);
    PersonSubscriptions subscriptions = new PersonSubscriptions();
    Observable<Person> personObservable = subscriptions.getPersons(10, false);
    ExecutorService executorServiceSubscribe = Executors.newFixedThreadPool(3, r -> new Thread(r, "Subscribe-Thread-" + subscriberThreadCtr.incrementAndGet()));
    ExecutorService executorServiceObserve = Executors.newFixedThreadPool(3, r -> new Thread(r, "Observe-Thread-" + observerThreadCtr.incrementAndGet()));
    personObservable
            .subscribeOn(Schedulers.from(executorServiceSubscribe))
            .observeOn(Schedulers.from(executorServiceObserve))
            .map(p -> {
              LOG.info(p.toString());
              return p;
            })
            .blockingSubscribe(new Observer<Person>() {
              @Override
              public void onSubscribe(Disposable disposable) {
                LOG.info("Subscription started");
              }

              @Override
              public void onNext(Person person) {
                LOG.info("Rvcd: " + person);
              }

              @Override
              public void onError(Throwable throwable) {
                LOG.info("Rcvd Error: " + throwable);
              }

              @Override
              public void onComplete() {
                LOG.info("Subscription Complete");
              }
            });
    executorServiceSubscribe.shutdownNow();
    executorServiceObserve.shutdownNow();
  }
}
