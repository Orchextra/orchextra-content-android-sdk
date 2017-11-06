package com.gigigo.orchextra.core.domain.rxInteractor;

import com.gigigo.orchextra.core.domain.rxExecutor.PostExecutionThread;
import com.gigigo.orchextra.core.domain.rxExecutor.ThreadExecutor;
import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.TestScheduler;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class) public class UseCaseTest {

  private UseCaseTestClass useCase;

  private TestDisposableObserver<Object> testObserver;

  @Mock private PriorityScheduler mockThreadExecutor;
  @Mock private PostExecutionThread mockPostExecutionThread;

  @Rule public ExpectedException expectedException = ExpectedException.none();

  @Before public void setUp() {
    this.useCase = new UseCaseTestClass(mockThreadExecutor, mockPostExecutionThread);
    this.testObserver = new TestDisposableObserver<>();
    when(mockThreadExecutor.priority(PriorityScheduler.Priority.LOW.getPriority())).thenReturn(new TestScheduler());
    when(mockPostExecutionThread.getScheduler()).thenReturn(new TestScheduler());
  }

  @Test public void testBuildUseCaseObservableReturnCorrectResult() {
    useCase.execute(testObserver, Params.EMPTY, PriorityScheduler.Priority.LOW);

    assertThat(testObserver.valuesCount).isZero();
  }

  @Test public void testSubscriptionWhenExecutingUseCase() {
    useCase.execute(testObserver, Params.EMPTY, PriorityScheduler.Priority.LOW);
    useCase.dispose();

    assertThat(testObserver.isDisposed()).isTrue();
  }

  @Test public void testShouldFailWhenExecuteWithNullObserver() {
    expectedException.expect(NullPointerException.class);
    useCase.execute(null, Params.EMPTY, PriorityScheduler.Priority.LOW);
  }

  private static class UseCaseTestClass extends UseCase<Object, Params> {

    UseCaseTestClass(PriorityScheduler threadExecutor, PostExecutionThread postExecutionThread) {
      super(threadExecutor, postExecutionThread);
    }

    @Override Observable<Object> buildUseCaseObservable(Params params) {
      return Observable.empty();
    }

    @Override public void execute(DisposableObserver<Object> observer, Params params, PriorityScheduler.Priority priority) {
      super.execute(observer, params, priority);
    }
  }

  private static class TestDisposableObserver<T> extends DisposableObserver<T> {
    private int valuesCount = 0;

    @Override public void onNext(T value) {
      valuesCount++;
    }

    @Override public void onError(Throwable e) {
      // no-op by default.
    }

    @Override public void onComplete() {
      // no-op by default.
    }
  }

  private static class Params {
    private static Params EMPTY = new Params();

    private Params() {
    }
  }
}
