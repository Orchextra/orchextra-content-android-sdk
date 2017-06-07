package com.gigigo.orchextra.core.domain.rxInteractor;

import com.gigigo.orchextra.core.domain.entities.elementcache.ElementCache;
import com.gigigo.orchextra.core.domain.entities.elements.ElementData;
import com.gigigo.orchextra.core.domain.rxExecutor.PostExecutionThread;
import com.gigigo.orchextra.core.domain.rxExecutor.ThreadExecutor;
import com.gigigo.orchextra.core.domain.rxRepository.OcmRepository;
import io.reactivex.Observable;
import orchextra.javax.inject.Inject;

/**
 * This class is an implementation of {@link UseCase} that represents a use case for
 * retrieving a collection of all {@link ElementData}.
 */
public class GetDetail extends UseCase<ElementData, GetDetail.Params> {

  private final OcmRepository ocmRepository;

  @Inject GetDetail(OcmRepository ocmRepository, ThreadExecutor threadExecutor,
      PostExecutionThread postExecutionThread) {
    super(threadExecutor, postExecutionThread);
    this.ocmRepository = ocmRepository;
  }

  @Override Observable<ElementData> buildUseCaseObservable(Params params) {
    return this.ocmRepository.getDetail(params.forceReload, params.content);
  }

  public static final class Params {

    private final boolean forceReload;
    private final String content;

    private Params(boolean forceReload, String content) {
      this.forceReload = forceReload;
      this.content = content;
    }

    public static Params forDetail(boolean forceReload, String content) {
      return new Params(forceReload, content);
    }
  }
}
