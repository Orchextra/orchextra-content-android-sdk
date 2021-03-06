package com.gigigo.orchextra.core.domain.rxInteractor;

import com.gigigo.orchextra.core.domain.rxExecutor.PostExecutionThread;
import com.gigigo.orchextra.core.domain.rxRepository.OcmRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * This class is an implementation of {@link UseCase} that represents a use case for
 * deleting stored data}.
 */
public class ClearCache extends UseCase<Void, ClearCache.Params> {

    private final OcmRepository ocmRepository;

    @Inject
    ClearCache(OcmRepository ocmRepository, PriorityScheduler threadExecutor,
               PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.ocmRepository = ocmRepository;
    }

    @Override
    Observable<Void> buildUseCaseObservable(Params params) {
        return this.ocmRepository.clear(params.images, params.data);
    }

    public static final class Params {

        private final boolean images;
        private final boolean data;

        private Params(boolean images, boolean data) {
            this.images = images;
            this.data = data;
        }

        public static Params create(boolean images, boolean data) {
            return new Params(images, data);
        }
    }
}
