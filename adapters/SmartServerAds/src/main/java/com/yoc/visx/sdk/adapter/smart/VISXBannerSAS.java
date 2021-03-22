package com.yoc.visx.sdk.adapter.smart;

import android.content.Context;

import com.smartadserver.android.library.model.SASAdElement;
import com.smartadserver.android.library.model.SASAdPlacement;
import com.smartadserver.android.library.ui.SASBannerView;
import com.smartadserver.android.library.util.SASConfiguration;
import com.yoc.visx.sdk.adapter.VisxMediationAdapter;
import com.yoc.visx.sdk.mediation.VISXMediationEventListener;
import com.yoc.visx.sdk.util.VISXLog;

import java.util.Map;

public class VISXBannerSAS implements VisxMediationAdapter {

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
            public void onBannerAdLoaded(SASBannerView sasBannerView, SASAdElement sasAdElement) {
                VISXLog.i("VISXBannerSAS SASBannerView onBannerAdLoaded");
                eventListener.onAdLoaded();
                eventListener.showAd(sasBannerView);
            }

            @Override
            public void onBannerAdFailedToLoad(SASBannerView sasBannerView, Exception e) {
                VISXLog.i("VISXBannerSAS SASBannerView onBannerAdFailedToLoad | Exception: " + e);
                eventListener.onAdLoadingFailed(e.getMessage());

                //TODO Need to check if exception is no_ad_fill, then call next mediation adapter
                eventListener.initNextMediationAdapter();
            }

            @Override
            public void onBannerAdClicked(SASBannerView sasBannerView) {
                VISXLog.i("VISXBannerSAS SASBannerView onBannerAdClicked");
            }

            @Override
            public void onBannerAdExpanded(SASBannerView sasBannerView) {
                VISXLog.i("VISXBannerSAS SASBannerView onBannerAdExpanded");
            }

            @Override
            public void onBannerAdCollapsed(SASBannerView sasBannerView) {
                VISXLog.i("VISXBannerSAS SASBannerView onBannerAdCollapsed");
            }

            @Override
            public void onBannerAdResized(SASBannerView sasBannerView) {
                VISXLog.i("VISXBannerSAS SASBannerView onBannerAdResized");
            }

            @Override
            public void onBannerAdClosed(SASBannerView sasBannerView) {
                VISXLog.i("VISXBannerSAS SASBannerView onBannerAdClosed");
            }

            @Override
            public void onBannerAdVideoEvent(SASBannerView sasBannerView, int i) {
                VISXLog.i("VISXBannerSAS SASBannerView onBannerAdVideoEvent");
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
