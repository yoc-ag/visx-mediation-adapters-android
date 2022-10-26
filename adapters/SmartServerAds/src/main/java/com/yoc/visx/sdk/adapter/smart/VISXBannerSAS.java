package com.yoc.visx.sdk.adapter.smart;

import android.content.Context;

import androidx.annotation.NonNull;

import com.smartadserver.android.library.model.SASAdElement;
import com.smartadserver.android.library.model.SASAdPlacement;
import com.smartadserver.android.library.ui.SASBannerView;
import com.smartadserver.android.library.util.SASConfiguration;
import com.yoc.visx.sdk.logger.VISXLog;
import com.yoc.visx.sdk.mediation.VISXMediationEventListener;
import com.yoc.visx.sdk.mediation.adapter.VisxMediationAdapter;

import java.util.Map;

public class VISXBannerSAS implements VisxMediationAdapter {

    private static final String TAG = "VISXBannerSAS SASBannerView ";
    private static final String PAGE_NAME = "pid";
    private static final String SITE_ID = "sid";
    private static final String FORMAT_ID = "fmtid";
    private static final String TARGET = "tgt";
    private static final String SUPPLY_CHAIN = "supplyChainObjectString";
    private SASBannerView sasBannerView;

    public VISXBannerSAS() {
    }

    @Override
    public void loadAd(Map<String, String> parametersMap, Context context, VISXMediationEventListener eventListener) {
        SASConfiguration.getSharedInstance().configure(context, Integer.parseInt(parametersMap.get(SITE_ID)));

        sasBannerView = new SASBannerView(context);
        SASAdPlacement sasAdPlacement = new SASAdPlacement(
                Integer.parseInt(parametersMap.get(SITE_ID)),
                parametersMap.get(PAGE_NAME),
                Integer.parseInt(parametersMap.get(FORMAT_ID)),
                parametersMap.get(TARGET),
                parametersMap.get(SUPPLY_CHAIN));
        sasBannerView.setBannerListener(new SASBannerView.BannerListener() {
            @Override
            public void onBannerAdLoaded(@NonNull SASBannerView sasBannerView, @NonNull SASAdElement sasAdElement) {
                VISXLog.i(TAG + "onBannerAdLoaded");
                eventListener.onAdLoaded();
                eventListener.showAd(sasBannerView);
            }

            @Override
            public void onBannerAdFailedToLoad(@NonNull SASBannerView sasBannerView, @NonNull Exception e) {
                VISXLog.i(TAG + "onBannerAdFailedToLoad | Exception: " + e);
                eventListener.onAdLoadingFailed(e.getMessage(), "SAS Banner");

                //TODO Need to check if exception is no_ad_fill, then call next mediation adapter
                eventListener.mediationFailWithNoAd();
            }

            @Override
            public void onBannerAdClicked(@NonNull SASBannerView sasBannerView) {
                VISXLog.i(TAG + "onBannerAdClicked");
            }

            @Override
            public void onBannerAdExpanded(@NonNull SASBannerView sasBannerView) {
                VISXLog.i(TAG + "onBannerAdExpanded");
            }

            @Override
            public void onBannerAdCollapsed(@NonNull SASBannerView sasBannerView) {
                VISXLog.i(TAG + "onBannerAdCollapsed");
            }

            @Override
            public void onBannerAdResized(@NonNull SASBannerView sasBannerView) {
                VISXLog.i(TAG + "onBannerAdResized");
            }

            @Override
            public void onBannerAdClosed(@NonNull SASBannerView sasBannerView) {
                VISXLog.i(TAG + "onBannerAdClosed");
            }

            @Override
            public void onBannerAdVideoEvent(@NonNull SASBannerView sasBannerView, int i) {
                VISXLog.i(TAG + "onBannerAdVideoEvent");
            }
        });
        sasBannerView.loadAd(sasAdPlacement);
    }

    @Override
    public void show() {
        // method used for interstitial
    }

    @Override
    public void destroy() {
        if (sasBannerView != null) {
            sasBannerView.setBannerListener(null);
            sasBannerView = null;
        }
    }
}
