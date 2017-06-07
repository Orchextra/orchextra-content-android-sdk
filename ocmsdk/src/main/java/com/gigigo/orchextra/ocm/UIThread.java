package com.gigigo.orchextra.ocm;

import com.gigigo.orchextra.core.domain.rxExecutor.PostExecutionThread;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import orchextra.javax.inject.Inject;
import orchextra.javax.inject.Singleton;

@Singleton
public class UIThread implements PostExecutionThread {

    @Inject
    public UIThread() {
    }

    @Override
    public Scheduler getScheduler() {
        return AndroidSchedulers.mainThread();
    }
}
