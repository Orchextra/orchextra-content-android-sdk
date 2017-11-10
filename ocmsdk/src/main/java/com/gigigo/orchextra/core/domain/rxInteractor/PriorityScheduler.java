package com.gigigo.orchextra.core.domain.rxInteractor;

import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.schedulers.ScheduledRunnable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * A class to be used with RxJava's {@link Scheduler} interface. Though this class is not a {@link
 * Scheduler} itself, calling {@link #priority(int)} will return one. E.x.: {@code
 * PriorityScheduler
 * scheduler = new PriorityScheduler(); Observable.just(1, 2, 3) .subscribeOn(scheduler.priority(10))
 * .subscribe(); }
 */
public class PriorityScheduler {

  private final PriorityBlockingQueue<ComparableRunnable> queue = new PriorityBlockingQueue<>();
  private final AtomicInteger workerCount = new AtomicInteger();
  private final int concurrency;
  private ExecutorService executorService;

  private PriorityScheduler(int concurrency) {
    this.executorService = Executors.newFixedThreadPool(concurrency);
    this.concurrency = concurrency;
  }

  /**
   * Creates a {@link PriorityScheduler} with as many threads as the machine's available
   * processors.
   * <p>
   * <b>Note:</b> this does not ensure that the priorities will be adheared to exactly, as the
   * JVM's threading policy might allow one thread to dequeue an action, then let a second thread
   * dequeue the next action, run it, dequeue another, run it, etc. before the first thread runs
   * its action. It does however ensure that at the dequeue step, the thread will receive the
   * highest priority action available.
   */
  public static PriorityScheduler create() {
    return new PriorityScheduler(Runtime.getRuntime().availableProcessors() - 1);
  }

  /**
   * Creates a {@link PriorityScheduler} using at most {@code concurrency} concurrent actions.
   * <p>
   * <b>Note:</b> this does not ensure that the priorities will be adheared to exactly, as the
   * JVM's threading policy might allow one thread to dequeue an action, then let a second thread
   * dequeue the next action, run it, dequeue another, run it, etc. before the first thread runs
   * its action. It does however ensure that at the dequeue step, the thread will receive the
   * highest priority action available.
   */
  public static PriorityScheduler withConcurrency(int concurrency) {
    return new PriorityScheduler(concurrency);
  }

  public static PriorityScheduler get() {
    return Holder.INSTANCE;
  }

  /**
   * Prioritize {@link io.reactivex.functions.Action  action}s with a numerical priority
   * value. The higher the priority, the sooner it will run.
   */
  public Scheduler priority(final int priority) {
    return new InnerPriorityScheduler(priority);
  }

  public void removeQueue() {
    if (queue.isEmpty()) {
      return;
    }

    //List<ComparableRunnable> comparableRunnableList = new ArrayList<>();
    //
    //Iterator<ComparableRunnable> it = queue.iterator();
    //
    //if (it.hasNext()) {
    //  ComparableRunnable runnable = it.next();
    //  if (!(runnable.priority == Priority.HIGH.getPriority())
    //      && !(runnable.priority == Priority.HIGHEST.getPriority())) {
    //
    //    comparableRunnableList.add(runnable);
    //  }
    //}

    queue.clear();
    //queue.addAll(comparableRunnableList);

    //System.out.println("Before: " + size + " After: " + queue.size());

    //try {
    //  List<Runnable> runnableList = executorService.shutdownNow();
    //  if (!executorService.awaitTermination(1000, TimeUnit.MICROSECONDS)) {
    //    this.executorService = Executors.newFixedThreadPool(concurrency);
    //
    //    Iterator<Runnable> it = runnableList.iterator();
    //    while (it.hasNext()) {
    //      //Runnable runnable = it.next();
    //      //if (runnable instanceof ComparableRunnable) {
    //      //if (((ComparableRunnable) runnable).priority == Priority.HIGHEST.priority) {
    //      it.remove();
    //      //}
    //      //}
    //    }
    //  }
    //} catch (InterruptedException e) {
    //  e.printStackTrace();
    //}
  }

  public enum Priority {
    LOWEST(0), LOW(1), MEDIUM(2), HIGH(3), HIGHEST(4);

    private final int priority;

    Priority(int priority) {
      this.priority = priority;
    }

    public int getPriority() {
      return priority;
    }
  }

  private static class Holder {
    static PriorityScheduler INSTANCE = create();
  }

  private static final class PriorityWorker extends Scheduler.Worker {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final PriorityBlockingQueue<ComparableRunnable> queue;
    private final int priority;

    private PriorityWorker(PriorityBlockingQueue<ComparableRunnable> queue, int priority) {
      this.queue = queue;
      this.priority = priority;
    }

    @Override public Disposable schedule(Runnable action) {
      return schedule(action, 0, MILLISECONDS);
    }

    @Override
    public Disposable schedule(@NonNull Runnable run, long delayTime, @NonNull TimeUnit unit) {
      final ComparableRunnable comparableRunnable = new ComparableRunnable(run, priority);

      final ScheduledRunnable scheduledRunnable =
          new ScheduledRunnable(comparableRunnable, compositeDisposable);

      scheduledRunnable.setFuture(new Future<Object>() {
        @Override public boolean cancel(boolean b) {
          return queue.remove(comparableRunnable);
        }

        @Override public boolean isCancelled() {
          return false;
        }

        @Override public boolean isDone() {
          return false;
        }

        @Override public Object get() throws InterruptedException, ExecutionException {
          return null;
        }

        @Override public Object get(long l, @NonNull TimeUnit timeUnit)
            throws InterruptedException, ExecutionException, TimeoutException {
          return null;
        }
      });
      compositeDisposable.add(scheduledRunnable);

      queue.offer(comparableRunnable, delayTime, unit);
      return scheduledRunnable;
    }

    @Override public void dispose() {
      compositeDisposable.dispose();
    }

    @Override public boolean isDisposed() {
      return compositeDisposable.isDisposed();
    }
  }

  private static final class ComparableRunnable
      implements Runnable, Comparable<ComparableRunnable> {

    private final Runnable runnable;
    private final int priority;

    private ComparableRunnable(Runnable runnable, int priority) {
      this.runnable = runnable;
      this.priority = priority;
    }

    @Override public void run() {
      runnable.run();
    }

    @Override public int compareTo(ComparableRunnable o) {
      if (o.priority == Priority.HIGHEST.getPriority() && o.priority == priority) {
        return 1;
      }
      return o.priority - priority;
    }
  }

  private final class InnerPriorityScheduler extends Scheduler {

    private final int priority;

    private InnerPriorityScheduler(int priority) {
      this.priority = priority;
    }

    @Override public Worker createWorker() {
      synchronized (workerCount) {
        final int[] numThread = { 0 };

        if (workerCount.get() < concurrency) {
          workerCount.incrementAndGet();
          executorService.submit(new Runnable() {
            @Override public void run() {
              while (true) {
                if (numThread[0] < 1) {
                  try {
                    ComparableRunnable runnable = queue.take();
                    runnable.run();
                    numThread[0]++;

                    //System.out.println("Thread: "
                    //    + Thread.currentThread()
                    //    + " numThreads: "
                    //    + numThread[0]
                    //    + " Priority:"
                    //    + runnable.priority);
                  } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                  } finally {
                    numThread[0]--;
                  }
                }

                //try {
                //  ComparableRunnable runnable = queue.take();
                //  runnable.run();
                //} catch (InterruptedException e) {
                //  Thread.currentThread().interrupt();
                //  break;
                //} finally {
                //  Object[] objects = queue.toArray();
                //}
              }
            }
          });
        }
      }
      return new PriorityWorker(queue, priority);
    }
  }
}
