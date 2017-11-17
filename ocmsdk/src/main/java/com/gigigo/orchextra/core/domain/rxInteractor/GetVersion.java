package com.gigigo.orchextra.core.domain.rxInteractor;

import com.gigigo.orchextra.core.domain.entities.version.VersionData;
import com.gigigo.orchextra.core.domain.rxExecutor.PostExecutionThread;
import com.gigigo.orchextra.core.domain.rxRepository.OcmRepository;
import io.reactivex.Observable;
import orchextra.javax.inject.Inject;

/**
 * Created by alex on 17/11/2017.
 */

public class GetVersion extends UseCase<VersionData, GetSection.Params>{

  private final OcmRepository ocmRepository;

  @Inject GetVersion(OcmRepository ocmRepository, PriorityScheduler threadExecutor,
      PostExecutionThread postExecutionThread) {
    super(threadExecutor, postExecutionThread);
    this.ocmRepository = ocmRepository;
  }

  @Override Observable<VersionData> buildUseCaseObservable(GetSection.Params params) {
    return this.ocmRepository.getVersion();
  }
}
