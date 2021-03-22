package com.yoc.visx.sdk.adapter.smart;

import android.content.Context;

import com.smartadserver.android.library.model.SASAdElement;
import com.smartadserver.android.library.model.SASAdPlacement;
import com.smartadserver.android.library.ui.SASInterstitialManager;
import com.smartadserver.android.library.util.SASConfiguration;
import com.yoc.visx.sdk.adapter.VisxMediationAdapter;
import com.yoc.visx.sdk.mediation.VISXMediationEventListener;
import com.yoc.visx.sdk.util.VISXLog;

import java.util.Map;

public class VISXInterstitialSAS implements VisxMediationAdapter {

    private static final String PAGE_NAME = "pid";
    private static final String SITE_ID = "sid";
    private static final String FORMAT_ID = "fmtid";
    private static final String TARGET = "tgt";
    private static final String SUPPLY_CHAIN = "supplyChainObjectString";
    private SASInterstitialManager sasInterstitialManager;

    public VISXInterstitialSAS() {
    }

    @Override
    public void loadAd(Map<String, String> parametersMap, Context context, VISXMediationEventListener eventListener) {
        SASConfiguration.getSharedInstance().configure(context, Integer.parseInt(parametersMap.get(SITE_ID)));

        SASAdPlacement sasAdPlacement = new SASAdPlacement(
                Integer.parseInt(parametersMap.get(SITE_ID)),
                parametersMap.get(PAGE_NAME),
                Integer.parseInt(parametersMap.get(FORMAT_ID)),
                parametersMap.get(TARGET),
                parametersMap.get(SUPPLY_CHAIN));
        sasInterstitialManager = new SASInterstitialManager(context, sasAdPlacement);

        sasInterstitialManager.setInterstitialListener(new SASInterstitialManager.InterstitialListener() {
            @Override
            public void onInterstitialAdLoaded(SASInterstitialManager sasInterstitialManager, SASAdElement sasAdElement) {
                VISXLog.i("VISXInterstitialSAS SASInterstitialManager onInterstitialAdLoaded");
                eventListener.onAdLoaded();
            }

            @Override
            public void onInterstitialAdFailedToLoad(SASInterstitialManager sasInterstitialManager, Exception e) {
                VISXLog.i("VISXInterstitialSAS SASInterstitialManager onInterstitialAdFailedToLoad | Exception: " + e);
                eventListener.onAdLoadingFailed(e.getMessage());

                //TODO Need to check if exception is no_ad_fill, then call next mediation adapter
                eventListener.initNextMediationAdapter();
            }

            @Override
            public void onInterstitialAdShown(SASInterstitialManager sasInterstitialManager) {
                VISXLog.i("VISXInterstitialSAS SASInterstitialManager onInterstitialAdShown");
            }

            @Override
            public void onInterstitialAdFailedToShow(SASInterstitialManager sasInterstitialManager, Exception e) {
                VISXLog.i("VISXInterstitialSAS SASInterstitialManager onInterstitialAdFailedToShow | Exception: " + e);
                eventListener.onAdLoadingFailed(e.getMessage());
            }

            @Override
            public void onInterstitialAdClicked(SASInterstitialManager sasInterstitialManager) {
                VISXLog.i("VISXInterstitialSAS SASInterstitialManager onInterstitialAdClicked");
            }

            @Override
            public void onInterstitialAdDismissed(SASInterstitialManager sasInterstitialManager) {
                VISXLog.i("VISXInterstitialSAS SASInterstitialManager onInterstitialAdDismissed");
            }

            @Override
            public void onInterstitialAdVideoEvent(SASInterstitialManager sasInterstitialManager, int i) {
                VISXLog.i("VISXInterstitialSAS SASInterstitialManager onInterstitialAdVideoEvent | Video Event: " + i);
            }
        });
        sasInterstitialManager.loadAd();

    }

    @Override
    public void show() {
        if (sasInterstitialManager.isShowable()) {
            sasInterstitialManager.show();
        }
    }

    @Override
    public void destroy() {
        if (sasInterstitialManager != null) {
            sasInterstitialManager.setInterstitialListener(null);
            sasInterstitialManager = null;
        }
    }
}
