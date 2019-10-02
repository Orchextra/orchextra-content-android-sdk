package com.gigigo.orchextra.core.domain.rxInteractor;

import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;
import com.gigigo.orchextra.core.domain.rxExecutor.PostExecutionThread;
import com.gigigo.orchextra.core.domain.rxRepository.OcmRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * This class is an implementation of {@link UseCase} that represents a use case for
 * retrieving a collection of all {@link MenuContentData}.
 */
public class GetMenus extends UseCase<MenuContentData, GetMenus.Params> {

    private final OcmRepository ocmRepository;

    @Inject
    GetMenus(OcmRepository ocmRepository, PriorityScheduler threadExecutor,
             PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.ocmRepository = ocmRepository;
    }

    @Override
    Observable<MenuContentData> buildUseCaseObservable(Params params) {
        return this.ocmRepository.getMenus();
    }

    public static final class Params {
        public Params() {
        }
    }
}
