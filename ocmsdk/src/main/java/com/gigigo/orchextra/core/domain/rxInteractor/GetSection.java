package com.gigigo.orchextra.core.domain.rxInteractor;

import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;
import com.gigigo.orchextra.core.domain.rxExecutor.PostExecutionThread;
import com.gigigo.orchextra.core.domain.rxRepository.OcmRepository;
import io.reactivex.Observable;
import orchextra.javax.inject.Inject;
import timber.log.Timber;

/**
 * This class is an implementation of {@link UseCase} that represents a use case for
 * retrieving a collection of all {@link ContentData}.
 */
public class GetSection extends UseCase<ContentData, GetSection.Params> {

  private final OcmRepository ocmRepository;

  @Inject GetSection(OcmRepository ocmRepository, PriorityScheduler threadExecutor,
      PostExecutionThread postExecutionThread) {
    super(threadExecutor, postExecutionThread);
    this.ocmRepository = ocmRepository;
  }

  @Override Observable<ContentData> buildUseCaseObservable(Params params) {
    Timber.d("Force reload: %s", params.forceReload);
    return this.ocmRepository.getSectionElements(params.forceReload, params.contentUrl,
        params.imagesToDownload);
  }

  public static final class Params {

    private final boolean forceReload;
    private final String contentUrl;
    private final int imagesToDownload;

    private Params(boolean forceReload, String contentUrl, int imagesToDownload) {
      this.forceReload = forceReload;
      this.contentUrl = contentUrl;
      this.imagesToDownload = imagesToDownload;
    }

    public static Params forSection(boolean forceReload, String contentUrl, int imagestodownload) {
      return new Params(forceReload, contentUrl, imagestodownload);
    }
  }
}
