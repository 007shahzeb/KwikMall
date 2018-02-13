package mall.kwik.kwikmall.utils;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by dharamveer on 24/1/18.
 */

public class RxBus {

    public RxBus() {
    }

    private PublishSubject<Object> bus = PublishSubject.create();

    public void send(Object o) {
        bus.onNext(o);

    }

    public Observable<Object> toObservable() {
        return bus;
    }

    public boolean hasObservable() {
        return bus.hasObservers();
    }

}
