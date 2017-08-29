package com.himanshu.poc.rxjava;

import rx.Emitter;
import rx.Observable;
import rx.Scheduler;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import java.util.UUID;

public class RxJavaMultithreadedSample {
  private static PersonFactory pFactory = new PersonFactory();
  public static void main(String[] args) {
    Observable<Person> o = Observable.create(new Action1<Emitter<Person>>() {
      @Override
      public void call(Emitter<Person> personEmitter) {
        for (int i=0;i<50;i++) {
          personEmitter.onNext(pFactory.createPerson(UUID.randomUUID().toString()));
        }
        personEmitter.onCompleted();
      }
    }, Emitter.BackpressureMode.BUFFER);

    //o.subscribeOn(Schedulers.computation()).toBlocking().subscribe();
    o.flatMap(p -> Observable.just(p).subscribeOn(Schedulers.computation()).map(q -> {
        System.out.println("Processing "+q+" in thread. "+Thread.currentThread().getName());
        return q;}
      )
    ).toList().toBlocking().subscribe(val -> System.out.println(val+" in thread. "+Thread.currentThread().getName()));
  }
}
