package com.yoc.visx.sdk.mediation;

import android.app.Activity;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Keep;

import com.appnexus.opensdk.MediatedBannerAdView;
import com.appnexus.opensdk.MediatedBannerAdViewController;
import com.appnexus.opensdk.ResultCode;
import com.appnexus.opensdk.TargetingParameters;
import com.yoc.visx.sdk.VisxAdManager;
import com.yoc.visx.sdk.util.AdSize;
import com.yoc.visx.sdk.view.category.ActionTracker;

import static com.yoc.visx.sdk.mediation.MediationUtil.TEST_TAG;

/**
 * The custom adaptor for banners must implement our mobile SDK
 * interface `MediatedBannerAdView`. In this example, the class also
 * implements the `AdListener` interface from the mediated SDK,
 * in order to handle the events from the mediated SDK.
 */
@Keep
public class VISXCustomEventBannerXandr implements MediatedBannerAdView {

    private VisxAdManager visxAdManager;
    MediatedBannerAdViewController controller = null;

    @Override
    public View requestAd(MediatedBannerAdViewController mediatedBannerAdViewController
            , Activity activity
            , String parameter
            , String adId
            , int width
            , int height
            , TargetingParameters targetingParameters) {

        controller = mediatedBannerAdViewController;

        visxAdManager = new VisxAdManager.Builder()
                .visxAdUnitID(adId)
                .adSize(new AdSize(new Size(width, height)))
                .setMediation()
                .customTargetParams(MediationUtil.getCustomTargetingParamsXandr(targetingParameters))
                .context(activity)
                .callback(new ActionTracker() {
                    @Override
                    public void onAdRequestStarted(VisxAdManager visxAdManager) {
                        Log.i(TEST_TAG, "VISXCustomEventBannerXandr onAdRequestStarted()");
                    }

                    @Override
                    public void onAdResponseReceived(VisxAdManager visxAdManager, String message) {
                        Log.i(TEST_TAG, "VISXCustomEventBannerXandr onAdResponseReceived()");
                        controller.onAdLoaded();
                    }

                    @Override
                    public void onAdLoadingStarted(VisxAdManager visxAdManager) {
                        Log.i(TEST_TAG, "VISXCustomEventBannerXandr onAdLoadingStarted()");
                    }

                    @Override
                    public void onAdLoadingFinished(VisxAdManager visxAdManager, String message) {
                        Log.i(TEST_TAG, "VISXCustomEventBannerXandr onAdLoadingFinished()");

                    }

                    @Override
                    public void onAdLoadingFailed(VisxAdManager visxAdManager, String message, boolean isFinal) {
                        Log.i(TEST_TAG, "VISXCustomEventBannerXandr onAdLoadingFailed(): " + message);
                        controller.onAdFailed(ResultCode.getNewInstance(ResultCode.CUSTOM_ADAPTER_ERROR));
                    }

                    @Override
                    public void onAdClicked() {
                        Log.i(TEST_TAG, "VISXCustomEventBannerGMA onAdClicked()");
                    }

                    @Override
                    public void onAdLeftApplication() {
                        Log.i(TEST_TAG, "VISXCustomEventBannerGMA onAdLeftApplication()");
                    }
                })
                .build();
        return visxAdManager.getAdContainer();
    }

    @Override
    public void destroy() {
        // Called when the mediated SDK's view is no longer being shown.
        Log.i(TEST_TAG, "VISXCustomEventBannerXandr destroy()");
        if (visxAdManager != null) {
            visxAdManager.stop();
        }
    }

    @Override
    public void onPause() {
        Log.i(TEST_TAG, "VISXCustomEventBannerXandr onPause()");
        if (visxAdManager != null) {
            visxAdManager.pause();
        }
    }

    @Override
    public void onResume() {
        Log.i(TEST_TAG, "VISXCustomEventBannerXandr onResume()");
        if (visxAdManager != null) {
            visxAdManager.resume();
        }
    }

    @Override
    public void onDestroy() {
        Log.i(TEST_TAG, "VISXCustomEventBannerXandr onDestroy()");
        if (visxAdManager != null) {
            visxAdManager.stop();
        }
    }
}
