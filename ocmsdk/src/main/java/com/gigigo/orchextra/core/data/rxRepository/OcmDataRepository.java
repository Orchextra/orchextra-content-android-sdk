package com.gigigo.orchextra.core.data.rxRepository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import com.gigigo.orchextra.core.AppExecutors;
import com.gigigo.orchextra.core.data.ElementComparator;
import com.gigigo.orchextra.core.data.ElementFilter;
import com.gigigo.orchextra.core.data.OcmDbDataSource;
import com.gigigo.orchextra.core.data.OcmNetworkDataSource;
import com.gigigo.orchextra.core.data.rxRepository.rxDatasource.OcmDataStore;
import com.gigigo.orchextra.core.data.rxRepository.rxDatasource.OcmDataStoreFactory;
import com.gigigo.orchextra.core.data.rxRepository.rxDatasource.OcmDiskDataStore;
import com.gigigo.orchextra.core.domain.entities.contentdata.ContentData;
import com.gigigo.orchextra.core.domain.entities.elements.Element;
import com.gigigo.orchextra.core.domain.entities.elements.ElementData;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContent;
import com.gigigo.orchextra.core.domain.entities.menus.MenuContentData;
import com.gigigo.orchextra.core.domain.rxRepository.OcmRepository;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import gigigo.com.vimeolibs.VimeoInfo;
import io.reactivex.Observable;
import timber.log.Timber;

@Singleton
public class OcmDataRepository implements OcmRepository {
    private final OcmDataStoreFactory ocmDataStoreFactory;
    private final OcmDbDataSource ocmDbDataSource;
    private final OcmNetworkDataSource ocmNetworkDataSource;
    private final AppExecutors appExecutors;

    @Inject
    public OcmDataRepository(OcmDataStoreFactory ocmDataStoreFactory, OcmDbDataSource ocmDbDataSource,
                             OcmNetworkDataSource ocmNetworkDataSource, AppExecutors appExecutors) {
        this.ocmDataStoreFactory = ocmDataStoreFactory;
        this.ocmDbDataSource = ocmDbDataSource;
        this.ocmNetworkDataSource = ocmNetworkDataSource;
        this.appExecutors = appExecutors;
    }

    @Override
    public Observable<MenuContentData> getMenus() {

        return Observable.create(emitter -> {
            appExecutors.diskIO().execute(() -> {

                MenuContentData cacheMenuContentData;
                MenuContentData networkMenuContentData;

                try {
                    cacheMenuContentData = ocmDbDataSource.getMenus();
                } catch (Exception e) {
                    Timber.e("cache getMenus()");
                    cacheMenuContentData = null;
                }

                try {
                    networkMenuContentData = ocmNetworkDataSource.getMenus();
                } catch (Exception e) {
                    Timber.e("network getMenus()");
                    networkMenuContentData = null;
                }

                MenuContentData menuContentData =
                        getUpdatedMenuContentData(cacheMenuContentData, networkMenuContentData);

                if (menuContentData == null) {
                    Timber.e("menuContentData is null check menu request");
                    return;
                }

                emitter.onNext(menuContentData);
                emitter.onComplete();
            });
        });
    }

    @Override
    public Observable<ContentData> getSectionElements(boolean forceReload, String contentUrl,
                                                      int numberOfElementsToDownload) {

        Timber.d("getSectionElements(forceReload: %s)", forceReload);

        return Observable.create(emitter -> {
            ContentData contentData;
            if (forceReload) {
                ocmDbDataSource.deleteElementCache();
                ocmNetworkDataSource.getMenus();
                contentData = ocmNetworkDataSource.getSectionElements(contentUrl);
            } else {
                try {
                    contentData = ocmDbDataSource.getSectionElements(contentUrl);
                } catch (Exception e) {
                    Timber.i(e, "getSectionElements() EMPTY");
                    contentData = ocmNetworkDataSource.getSectionElements(contentUrl);
                }
            }

            emitter.onNext(sortContentData(contentData));
            emitter.onComplete();
        });
    }

    private ContentData sortContentData(final ContentData contentData) {
        List<Element> elements = contentData.getContent().getElements();

        ElementFilter elementFilter = new ElementFilter();
        elements = elementFilter.removeFinishedElements(elements);

        Collections.sort(elements, new ElementComparator());
        contentData.getContent().setElements(elements);
        return contentData;
    }

    @Override
    public Observable<ContentData> doSearch(String textToSearch) {
        OcmDataStore ocmDataStore = ocmDataStoreFactory.getCloudDataStore();
        return ocmDataStore.searchByText(textToSearch);
    }

    @Override
    public Observable<ElementData> getDetail(boolean forceReload, String elementUrl) {
        OcmDataStore ocmDataStore = ocmDataStoreFactory.getDataStoreForDetail(forceReload, elementUrl);
        return ocmDataStore.getElementById(elementUrl);
    }

    @Override
    public Observable<VimeoInfo> getVideo(Context context, boolean forceReload, String videoId,
                                          boolean isWifiConnection, boolean isFastConnection) {
        OcmDataStore ocmDataStore = ocmDataStoreFactory.getDataStoreForVideo(forceReload, videoId);
        return ocmDataStore.getVideoById(context, videoId, isWifiConnection, isFastConnection);
    }

    @WorkerThread
    @Override
    public Observable<Void> clear(boolean images, boolean data) {
        OcmDiskDataStore ocmDataStore = ocmDataStoreFactory.getDiskDataStore();
        appExecutors.diskIO().execute(() -> {
            try {
                ocmDataStore.getOcmCache().evictAll(images, data);
            } catch (Exception e) {
                Timber.e("evictAll()");
            }
        });
        return Observable.empty();
    }

    private MenuContentData getUpdatedMenuContentData(@NonNull MenuContentData cacheMenuContentData,
                                                      @Nullable MenuContentData networkMenuContentData) {

        if (cacheMenuContentData.getMenuContentList().isEmpty()) {
            Timber.i("Data from cloud");
            return networkMenuContentData;
        }

        MenuContentData updatedMenuContentData = new MenuContentData();

        if (networkMenuContentData != null) {
            updatedMenuContentData.setElementsCache(networkMenuContentData.getElementsCache());
            updatedMenuContentData.setMenuContentList(networkMenuContentData.getMenuContentList());

            for (MenuContent menuContent : networkMenuContentData.getMenuContentList()) {
                for (Element element : menuContent.getElements()) {

                    Boolean updated = checkContentVersion(element, cacheMenuContentData);
                    Timber.d("Element %s; Updated %s", element.getSlug(), updated);
                    element.setHasNewVersion(updated);
                    Timber.d("Element %s; Updated %s", element.getSlug(), updated);
                }
            }

            return updatedMenuContentData;
        } else {

            Timber.w("Data only from cache");
            return cacheMenuContentData;
        }
    }

    private Boolean checkContentVersion(@NonNull Element element,
                                        @NonNull MenuContentData cacheMenuContentData) {
        for (MenuContent menuContent : cacheMenuContentData.getMenuContentList()) {
            for (Element cacheElement : menuContent.getElements()) {
                if (cacheElement.getSlug().equals(element.getSlug())) {
                    return cacheElement.getContentVersion() == null || !cacheElement.getContentVersion()
                            .equals(element.getContentVersion());
                }
            }
        }
        return false;
    }
}
