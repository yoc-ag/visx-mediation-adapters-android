package com.yoc.visx.sdk.adapter.smart;

import android.content.Context;

import androidx.annotation.NonNull;

import com.smartadserver.android.library.model.SASAdElement;
import com.smartadserver.android.library.model.SASAdPlacement;
import com.smartadserver.android.library.ui.SASInterstitialManager;
import com.smartadserver.android.library.util.SASConfiguration;
import com.yoc.visx.sdk.logger.VISXLog;
import com.yoc.visx.sdk.mediation.VISXMediationEventListener;
import com.yoc.visx.sdk.mediation.adapter.VisxMediationAdapter;

import java.util.Map;
import java.util.Objects;

public class VISXInterstitialSAS implements VisxMediationAdapter {

    private static final String TAG = "VISXInterstitialSAS SASInterstitialManager ";
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
        SASConfiguration.getSharedInstance().configure(context, Integer.parseInt(Objects.requireNonNull(parametersMap.get(SITE_ID))));

        SASAdPlacement sasAdPlacement = new SASAdPlacement(
                Integer.parseInt(parametersMap.get(SITE_ID)),
                parametersMap.get(PAGE_NAME),
                Integer.parseInt(parametersMap.get(FORMAT_ID)),
                parametersMap.get(TARGET),
                parametersMap.get(SUPPLY_CHAIN));
        sasInterstitialManager = new SASInterstitialManager(context, sasAdPlacement);

        sasInterstitialManager.setInterstitialListener(new SASInterstitialManager.InterstitialListener() {
            @Override
            public void onInterstitialAdLoaded(@NonNull SASInterstitialManager sasInterstitialManager, @NonNull SASAdElement sasAdElement) {
                VISXLog.i(TAG + "onInterstitialAdLoaded");
                eventListener.onAdLoaded();
            }

            @Override
            public void onInterstitialAdFailedToLoad(@NonNull SASInterstitialManager sasInterstitialManager, @NonNull Exception e) {
                VISXLog.i(TAG + "onInterstitialAdFailedToLoad | Exception: " + e);
                eventListener.onAdLoadingFailed(e.getMessage(), "SAS Interstitial");

                //TODO Need to check if exception is no_ad_fill, then call next mediation adapter
                eventListener.mediationFailWithNoAd();
            }

            @Override
            public void onInterstitialAdShown(@NonNull SASInterstitialManager sasInterstitialManager) {
                VISXLog.i(TAG + "onInterstitialAdShown");
            }

            @Override
            public void onInterstitialAdFailedToShow(@NonNull SASInterstitialManager sasInterstitialManager, @NonNull Exception e) {
                VISXLog.i(TAG + "onInterstitialAdFailedToShow | Exception: " + e);
                eventListener.onAdLoadingFailed(e.getMessage(), "SAS Interstitial");
            }

            @Override
            public void onInterstitialAdClicked(@NonNull SASInterstitialManager sasInterstitialManager) {
                VISXLog.i(TAG + "onInterstitialAdClicked");
            }

            @Override
            public void onInterstitialAdDismissed(@NonNull SASInterstitialManager sasInterstitialManager) {
                VISXLog.i(TAG + "onInterstitialAdDismissed");
            }

            @Override
            public void onInterstitialAdVideoEvent(@NonNull SASInterstitialManager sasInterstitialManager, int i) {
                VISXLog.i(TAG + "onInterstitialAdVideoEvent | Video Event: " + i);
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
