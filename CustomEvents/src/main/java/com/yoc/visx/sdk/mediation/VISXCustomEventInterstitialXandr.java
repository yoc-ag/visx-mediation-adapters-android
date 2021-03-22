package com.yoc.visx.sdk.mediation;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.Keep;

import com.appnexus.opensdk.MediatedInterstitialAdView;
import com.appnexus.opensdk.MediatedInterstitialAdViewController;
import com.appnexus.opensdk.ResultCode;
import com.appnexus.opensdk.TargetingParameters;
import com.yoc.visx.sdk.VisxAdManager;
import com.yoc.visx.sdk.util.AdSize;
import com.yoc.visx.sdk.view.category.ActionTracker;

import static com.yoc.visx.sdk.mediation.MediationUtil.TEST_TAG;

/**
 * The custom adaptor for interstitials must implement Xandr mobile SDK
 * interface `MediatedInterstitialAdView`. In this example, the class also
 * implements the `AdListener` interface from the mediated SDK,
 * in order to handle the events from the mediated SDK.
 */
@Keep
public class VISXCustomEventInterstitialXandr implements MediatedInterstitialAdView {

    private VisxAdManager visxAdManager;
    MediatedInterstitialAdViewController controller = null;

    @Override
    public void requestAd(MediatedInterstitialAdViewController mediatedInterstitialAdViewController,
                          Activity activity, String parameter,
                          String adId, TargetingParameters targetingParameters) {

        controller = mediatedInterstitialAdViewController;

        visxAdManager = new VisxAdManager.Builder()
                .visxAdUnitID(adId)
                .adSize(AdSize.INTERSTITIAL_320x480) //TODO Check for correct size
                .setMediation()
                .customTargetParams(MediationUtil.getCustomTargetingParamsXandr(targetingParameters))
                .context(activity)
                .callback(new ActionTracker() {
                    @Override
                    public void onAdRequestStarted(VisxAdManager visxAdManager) {
                        Log.i(TEST_TAG, "VISXCustomEventInterstitialXandr onAdRequestStarted()");
                    }

                    @Override
                    public void onAdResponseReceived(VisxAdManager visxAdManager, String message) {
                        Log.i(TEST_TAG, "VISXCustomEventInterstitialXandr onAdResponseReceived()");
                    }

                    @Override
                    public void onAdLoadingStarted(VisxAdManager visxAdManager) {
                        Log.i(TEST_TAG, "VISXCustomEventInterstitialXandr onAdLoadingStarted()");
                    }

                    @Override
                    public void onAdLoadingFinished(VisxAdManager visxAdManager, String message) {
                        Log.i(TEST_TAG, "VISXCustomEventInterstitialXandr onAdLoadingFinished()");
                        controller.onAdLoaded();
                    }

                    @Override
                    public void onAdLoadingFailed(VisxAdManager visxAdManager, String message, boolean isFinal) {
                        Log.i(TEST_TAG, "VISXCustomEventInterstitialXandr onAdLoadingFinished()");
                        controller.onAdFailed(ResultCode.getNewInstance(ResultCode.CUSTOM_ADAPTER_ERROR));
                    }

                    @Override
                    public void onAdClicked() {
                        Log.i(TEST_TAG, "VISXCustomEventInterstitialXandr onAdClicked()");
                    }

                    @Override
                    public void onAdLeftApplication() {
                        Log.i(TEST_TAG, "VISXCustomEventInterstitialXandr onAdLeftApplication()");
                    }
                })
                .build();
    }

    // Call when the mediated SDK should display the interstitial ad
    @Override
    public void show() {
        visxAdManager.showModalInterstitial();
    }

    // Returns whether or not the mediated SDK has an interstitial ad
    // fetched and ready to to be shown.
    @Override
    public boolean isReady() {
        //return visxAdManager.isMysteryAdReady();
        return true;
    }

    @Override
    public void destroy() {
        // Called when the mediated SDK's view is no longer being shown.
        Log.i(TEST_TAG, "VISXCustomEventInterstitialXandr destroy()");
        if (visxAdManager != null) {
            visxAdManager.stop();
        }
    }

    @Override
    public void onPause() {
        Log.i(TEST_TAG, "VISXCustomEventInterstitialXandr onPause()");
        if (visxAdManager != null) {
            visxAdManager.pause();
        }
    }

    @Override
    public void onResume() {
        Log.i(TEST_TAG, "VISXCustomEventInterstitialXandr onResume()");
        if (visxAdManager != null) {
            visxAdManager.resume();
        }
    }

    @Override
    public void onDestroy() {
        Log.i(TEST_TAG, "VISXCustomEventInterstitialXandr onDestroy()");
        if (visxAdManager != null) {
            visxAdManager.stop();
        }
    }
}

