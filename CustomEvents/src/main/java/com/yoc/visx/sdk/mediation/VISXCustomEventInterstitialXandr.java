package com.yoc.visx.sdk.mediation;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.Keep;

import com.appnexus.opensdk.MediatedInterstitialAdView;
import com.appnexus.opensdk.MediatedInterstitialAdViewController;
import com.appnexus.opensdk.ResultCode;
import com.appnexus.opensdk.TargetingParameters;
import com.yoc.visx.sdk.VisxAdManager;
import com.yoc.visx.sdk.adview.tracker.VisxCallbacks;
import com.yoc.visx.sdk.util.ad.AdSize;

/**
 * The custom adaptor for interstitials must implement Xandr mobile SDK
 * interface `MediatedInterstitialAdView`. In this example, the class also
 * implements the `AdListener` interface from the mediated SDK,
 * in order to handle the events from the mediated SDK.
 */
@Keep
public class VISXCustomEventInterstitialXandr implements MediatedInterstitialAdView {

    private static final String TAG = VISXCustomEventInterstitialXandr.class.getSimpleName();

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
                .callback(new VisxCallbacks() {
                    @Override
                    public void onAdRequestStarted(VisxAdManager visxAdManager) {
                        Log.i(TAG, "onAdRequestStarted()");
                    }

                    @Override
                    public void onAdResponseReceived(VisxAdManager visxAdManager, String message) {
                        Log.i(TAG, "onAdResponseReceived()");
                    }

                    @Override
                    public void onAdLoadingStarted(VisxAdManager visxAdManager) {
                        Log.i(TAG, "onAdLoadingStarted()");
                    }

                    @Override
                    public void onAdLoadingFinished(VisxAdManager visxAdManager, String message) {
                        Log.i(TAG, "onAdLoadingFinished()");
                        controller.onAdLoaded();
                    }

                    @Override
                    public void onAdLoadingFailed(String message, int errorCode, boolean isFinal) {
                        Log.i(TAG, "onAdLoadingFailed() ErrorCode: " + errorCode + "Message: " + message + " isFinal: " + isFinal);
                        controller.onAdFailed(ResultCode.getNewInstance(ResultCode.CUSTOM_ADAPTER_ERROR));
                    }

                    @Override
                    public void onAdSizeChanged(int width, int height) {
                        Log.i(TAG, "onAdSizeChanged()");
                    }

                    @Override
                    public void onAdClicked() {
                        Log.i(TAG, "onAdClicked()");
                    }

                    @Override
                    public void onAdLeftApplication() {
                        Log.i(TAG, "onAdLeftApplication()");
                    }

                    @Override
                    public void onVideoFinished() {
                        Log.i(TAG, "onAdLeftApplication()");
                    }

                    @Override
                    public void onEffectChange(String effect) {
                        Log.i(TAG, "onEffectChange() -> effect: " + effect);
                    }

                    @Override
                    public void onAdViewable() {
                        Log.i(TAG, "onAdViewable()");
                    }

                    @Override
                    public void onAdResumeApplication() {
                        Log.i(TAG, "onAdResumeApplication()");
                    }

                    @Override
                    public void onAdClosed() {
                        Log.i(TAG, "onAdClosed()");
                    }

                    @Override
                    public void onInterstitialClosed() {
                        Log.i(TAG, "onInterstitialClosed()");
                    }

                    @Override
                    public void onInterstitialWillBeClosed() {
                        Log.i(TAG, "onInterstitialWillBeClosed()");
                    }

                    @Override
                    public void onLandingPageClosed() {
                        Log.i(TAG, "onLandingPageClosed()");
                    }

                    @Override
                    public void onLandingPageOpened(boolean inExternalBrowser) {
                        Log.i(TAG, "onLandingPageOpened()");
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
        Log.i(TAG, "destroy()");
        if (visxAdManager != null) visxAdManager.stop();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause()");
        if (visxAdManager != null) visxAdManager.pause();
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume()");
        if (visxAdManager != null) visxAdManager.resume();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy()");
        if (visxAdManager != null) visxAdManager.stop();
    }
}

