package picture.tool.utils;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class RxJavaFx {

  private RxJavaFx() {

  }

  public static <T> Observable<T> fromObservableValue(
    final ObservableValue<T> observableValue) {
    return Observable.create(emitter -> {
      final ChangeListener<T> listener = (ov, prev, current) -> {
        if (null != current) {
          emitter.onNext(current);
        }
      };
      observableValue.addListener(listener);
      emitter.setDisposable(new ObservableValueDispose<>(observableValue, listener));
    });
  }
}

class ObservableValueDispose<T> implements Disposable {

  private final ObservableValue<T> observableValue;

  private final ChangeListener<T> listener;

  private boolean disposed = false;

  public ObservableValueDispose(ObservableValue<T> observableValue, ChangeListener<T> listener) {
    this.observableValue = observableValue;
    this.listener = listener;
  }

  @Override
  public void dispose() {
    if (null != observableValue) {
      this.observableValue.removeListener(listener);
    }
    disposed = true;
  }

  @Override
  public boolean isDisposed() {
    return disposed;
  }
}