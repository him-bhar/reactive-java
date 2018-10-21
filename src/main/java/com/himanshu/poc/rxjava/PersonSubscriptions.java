package com.himanshu.poc.rxjava;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersonSubscriptions {
  private static final Logger LOG = LoggerFactory.getLogger(PersonSubscriptions.class);
  private PersonSupplier personSupplier = new PersonSupplier();

  public Observable<Person> getPersons(int counter, boolean throwError) {
    return Observable.create(new ObservableOnSubscribe<Person>() {
      @Override
      public void subscribe(ObservableEmitter<Person> observableEmitter) throws Exception {
        for (int i=0;i<counter;i++) {
          try {
            if (counter/2 == i && throwError) {
              throw new IllegalArgumentException("Throwing an exception in observable");
            }
            LOG.info("Generating object");
            observableEmitter.onNext(personSupplier.get());
            Thread.currentThread().sleep(2000l);
          } catch (Exception e) {
            //throw new RuntimeException(e); //Terminates subscription
            observableEmitter.onError(e);

          }
        }
        observableEmitter.onComplete();
      }
    });
  }
}
