package com.gigigo.orchextra.core.domain.rxInteractor;

import android.support.v4.util.Preconditions;
import com.gigigo.orchextra.core.domain.entities.version.VersionData;
import com.gigigo.orchextra.core.domain.rxExecutor.PostExecutionThread;
import com.gigigo.orchextra.core.domain.rxRepository.OcmRepository;
import io.reactivex.Observable;
import orchextra.javax.inject.Inject;

/**
 * Created by alex on 17/11/2017.
 */

public class GetVersion extends UseCase<VersionData, GetVersion.Params>{

  private final OcmRepository ocmRepository;

  @Inject GetVersion(OcmRepository ocmRepository, PriorityScheduler threadExecutor,
      PostExecutionThread postExecutionThread) {
    super(threadExecutor, postExecutionThread);
    this.ocmRepository = ocmRepository;
  }

  @Override Observable<VersionData> buildUseCaseObservable(GetVersion.Params params) {
    return this.ocmRepository.getVersion();
  }

  public static final class Params {

    private Params() {}

    public static GetVersion.Params forVersion() {
      return new GetVersion.Params();
    }
  }
}
