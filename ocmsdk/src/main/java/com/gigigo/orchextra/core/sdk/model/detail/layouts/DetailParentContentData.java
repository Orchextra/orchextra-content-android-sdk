package com.gigigo.orchextra.core.sdk.model.detail.layouts;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.gigigo.orchextra.core.controller.views.UiBaseContentData;
import com.gigigo.orchextra.core.domain.entities.elementcache.FederatedAuthorization;
import com.gigigo.orchextra.core.sdk.actions.ActionHandler;
import com.gigigo.orchextra.core.sdk.di.injector.Injector;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.BrowserContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.CustomTabsContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.DeepLinkContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.ScanContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.VuforiaContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.vimeo.VimeoContentData;
import com.gigigo.orchextra.core.sdk.model.detail.viewtypes.youtube.YoutubeContentData;
import com.gigigo.orchextra.core.sdk.ui.views.toolbars.DetailToolbarView;
import com.gigigo.orchextra.ocm.OCManager;
import com.gigigo.orchextra.ocm.OcmEvent;
import com.gigigo.orchextra.ocm.callbacks.OnFinishViewListener;
import com.gigigo.orchextra.ocmsdk.R;

import javax.inject.Inject;

public abstract class DetailParentContentData extends UiBaseContentData {

    protected OnFinishViewListener onFinishListener;
    protected OnShareListener onShareListener;
    protected DetailToolbarView detailToolbarView;
    @Inject
    ActionHandler actionHandler;
    View mView;
    private String nameArticle;
    private boolean changeToolbarIconToClose = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getDetailLayout(), container, false);

        initDi();
        initDetailViews(mView);
        customizeToolbar();

        return mView;
    }

    private void initDi() {
        Injector injector = OCManager.getInjector();
        if (injector != null) {
            injector.injectDetailContentData(this);
        }
    }

    private void initDetailViews(View view) {
        detailToolbarView = (DetailToolbarView) view.findViewById(R.id.detailToolbarView);

        initViews(view);
    }

    private void customizeToolbar() {
        if (detailToolbarView == null) {
            return;
        }

        if (!TextUtils.isEmpty(nameArticle)) {
            detailToolbarView.setToolbarTitle(nameArticle);
        }

        if (changeToolbarIconToClose) {
            detailToolbarView.setToolbarIcon(R.drawable.ox_close);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setOnClickListenerButtons();
    }

    protected void setOnClickListenerButtons() {
        detailToolbarView.setOnClickBackButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeView();
            }
        });

        detailToolbarView.setShareButtonVisible(onShareListener != null);

        if (onShareListener != null) {
            detailToolbarView.setOnClickShareButtonListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onShareListener.onShare();
                }
            });
        }
    }

    public void setOnFinishListener(OnFinishViewListener onFinishListener) {
        this.onFinishListener = onFinishListener;
    }

    protected boolean checkIfOxActionAndExecute(UiBaseContentData uiBaseContentData) {
        Class<? extends UiBaseContentData> detailContentDataClass = uiBaseContentData.getClass();
        if (detailContentDataClass.equals(VuforiaContentData.class)) {
            launchOxVuforia();
            OCManager.notifyEvent(OcmEvent.OPEN_IR);
            return true;
        } else if (detailContentDataClass.equals(ScanContentData.class)) {
            lauchOxScan();
            OCManager.notifyEvent(OcmEvent.OPEN_BARCODE);
            return true;
        } else if (detailContentDataClass.equals(BrowserContentData.class)) {
            launchExternalBrowser(((BrowserContentData) uiBaseContentData).getUrl(),
                    ((BrowserContentData) uiBaseContentData).getFederatedAuthorization());
            OCManager.notifyEvent(OcmEvent.VISIT_URL);
            return true;
        } else if (detailContentDataClass.equals(YoutubeContentData.class)) {
            launchYoutubePlayer(((YoutubeContentData) uiBaseContentData).getVideoId());
            OCManager.notifyEvent(OcmEvent.PLAY_YOUTUBE);
            return true;
        } else if (detailContentDataClass.equals(VimeoContentData.class)) {
            launchVimeoPlayer(((VimeoContentData) uiBaseContentData).getVideoId());
            OCManager.notifyEvent(OcmEvent.PLAY_VIMEO);
            return true;
        } else if (detailContentDataClass.equals(DeepLinkContentData.class)) {
            processDeepLink(((DeepLinkContentData) uiBaseContentData).getUri());
            OCManager.notifyEvent(OcmEvent.VISIT_URL);
            return true;
        } else if (detailContentDataClass.equals(CustomTabsContentData.class)) {
            launchCustomTabs(((CustomTabsContentData) uiBaseContentData).getUrl(),
                    ((CustomTabsContentData) uiBaseContentData).getFederatedAuthorization());
            OCManager.notifyEvent(OcmEvent.VISIT_URL);
            return true;
        }
        return false;
    }

    private void launchCustomTabs(String url, FederatedAuthorization federatedAuthorization) {
        actionHandler.launchCustomTabs(url, federatedAuthorization);
    }

    private void processDeepLink(String uri) {
        actionHandler.processDeepLink(uri);
    }

    private void launchYoutubePlayer(String videoId) {
        actionHandler.launchYoutubePlayer(videoId);
    }

    private void launchVimeoPlayer(String videoId) {
        actionHandler.launchVimeoPlayer(videoId);
    }

    private void launchExternalBrowser(String url, FederatedAuthorization fedexA) {
        actionHandler.launchExternalBrowser(url, fedexA);
    }

    private void lauchOxScan() {
        actionHandler.lauchOxScan();
    }

    private void launchOxVuforia() {
        actionHandler.launchOxVuforia();
    }

    public void setOnShareListener(OnShareListener onShareListener) {
        this.onShareListener = onShareListener;
    }

    protected abstract void initViews(View view);

    protected abstract int getDetailLayout();

    protected abstract void closeView();

    public void setArticleName(String nameArticle) {
        this.nameArticle = nameArticle;
    }

    protected void changeIconToolbar() {
        this.changeToolbarIconToClose = true;
    }

    @Override
    public void onDestroy() {

        System.out.println(
                "----onDestroy------------------------------------------artivcle coordinator content data");
        if (mView != null) unbindDrawables(mView);

        ((ViewGroup) mView).removeAllViews();
        System.gc();

        Glide.get(this.getContext()).clearMemory();

        onFinishListener = null;
        onShareListener = null;
        detailToolbarView = null;
        Glide.get(this.getContext()).clearMemory();

        if (getHost() != null) {
            super.onDestroy();
        }
    }

    private void unbindDrawables(View view) {
        System.gc();
        Runtime.getRuntime().gc();
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

    public interface OnShareListener {
        void onShare();
    }
}
