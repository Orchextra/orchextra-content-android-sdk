package com.gigigo.orchextra.core.sdk.application;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import com.gigigo.orchextra.core.domain.rxInteractor.PriorityScheduler;
import java.util.Iterator;
import java.util.Stack;

public class OcmSdkLifecycle implements Application.ActivityLifecycleCallbacks {

  private final PriorityScheduler priorityScheduler;

  private Stack<ActivityLifecyleWrapper> activityStack = new Stack<>();

  public OcmSdkLifecycle(PriorityScheduler priorityScheduler) {
    this.priorityScheduler = priorityScheduler;
  }

  @Override public void onActivityCreated(Activity activity, Bundle bundle) {
  }

  @Override public void onActivityStarted(Activity activity) {
    this.activityStack.push(new ActivityLifecyleWrapper(activity, true, false));
  }

  @Override public void onActivityResumed(Activity activity) {
    if (!activityStack.isEmpty()) activityStack.peek().setIsPaused(false);
  }

  @Override public void onActivityPaused(Activity activity) {
    if (!activityStack.isEmpty()) activityStack.peek().setIsPaused(true);
  }

  @Override public void onActivityStopped(Activity activity) {
    removeActivityFromStack(activity);
  }

  @Override public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
  }

  @Override public void onActivityDestroyed(Activity activity) {
    priorityScheduler.removeQueue();
  }

  private void removeActivityFromStack(Activity activity) {
    Iterator<ActivityLifecyleWrapper> iter = activityStack.iterator();
    while (iter.hasNext()) {
      ActivityLifecyleWrapper activityLifecyleWrapper = iter.next();
      if (activityLifecyleWrapper.getActivity().equals(activity)) {
        iter.remove();
      }
    }
  }

  public Activity getCurrentActivity() {
    for (ActivityLifecyleWrapper activityLifecyleWrapper : activityStack) {
      if (!activityLifecyleWrapper.isPaused()) {
        return activityLifecyleWrapper.getActivity();
      }
    }

    for (ActivityLifecyleWrapper activityLifecyleWrapper : activityStack) {
      if (!activityLifecyleWrapper.isStopped()) {
        return activityLifecyleWrapper.getActivity();
      }
    }

    return null;
  }

  public boolean isActivityContextAvailable() {
    return (getCurrentActivity() != null);
  }
}