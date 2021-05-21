package com.yoc.visx.sdk.mediation;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.mediation.Adapter;
import com.google.android.gms.ads.mediation.InitializationCompleteCallback;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.MediationConfiguration;
import com.google.android.gms.ads.mediation.VersionInfo;
import com.google.android.gms.ads.mediation.customevent.CustomEventBanner;
import com.google.android.gms.ads.mediation.customevent.CustomEventBannerListener;
import com.yoc.visx.sdk.VisxAdManager;
import com.yoc.visx.sdk.view.category.ActionTracker;

import java.util.List;

import static com.yoc.visx.sdk.mediation.MediationUtil.TEST_TAG;

@Keep
public class VISXCustomEventBannerGMA extends Adapter implements CustomEventBanner {

    private VisxAdManager visxAdManager;
    private CustomEventBannerListener customEventListener;

    public VISXCustomEventBannerGMA() {
        Log.i(TEST_TAG, "VISXCustomEventBannerGMA CONSTRUCTOR");
    }

    @Override
    public void initialize(@NonNull Context context,
                           @NonNull InitializationCompleteCallback initializationCompleteCallback,
                           @NonNull List<MediationConfiguration> list) {
        Log.i(TEST_TAG, "VISXCustomEventBannerGMA initialize()" +
                " context: " + context +
                " initializationCompleteCallback: " + initializationCompleteCallback +
                " List<MediationConfiguration>: " + list);
    }

    @NonNull
    @Override
    public VersionInfo getVersionInfo() {
        Log.i(TEST_TAG, "VISXCustomEventBannerGMA getVersionInfo()");
        return MediationUtil.getVersionInfo(false);
    }

    @NonNull
    @Override
    public VersionInfo getSDKVersionInfo() {
        return MediationUtil.getVersionInfo(true);
    }

    @Override
    public void requestBannerAd(@NonNull final Context context,
                                @NonNull final CustomEventBannerListener customEventBannerListener,
                                String serverParameter,
                                @NonNull AdSize adSize,
                                @NonNull MediationAdRequest mediationAdRequest,
                                Bundle bundle) {
        MediationUtil.setParameterMap(serverParameter);
        this.customEventListener = customEventBannerListener;
        visxAdManager = new VisxAdManager.Builder()
                .visxAdUnitID(MediationUtil.getAuid())
                .adSize(MediationUtil.getBannerAdSize())
                .appDomain(MediationUtil.getAppDomain())
                .setMediation()
                .context(context)
                .callback(new ActionTracker() {
                    @Override
                    public void onAdRequestStarted(VisxAdManager visxAdManager) {
                        Log.i(TEST_TAG, "VISXCustomEventBannerGMA onAdRequestStarted()");
                    }

                    @Override
                    public void onAdResponseReceived(VisxAdManager visxAdManager, String message) {
                        Log.i(TEST_TAG, "VISXCustomEventBannerGMA onAdResponseReceived() Message: " + message);
                        customEventListener.onAdLoaded(visxAdManager.getAdContainer());
                    }

                    @Override
                    public void onAdLoadingStarted(VisxAdManager visxAdManager) {
                        Log.i(TEST_TAG, "VISXCustomEventBannerGMA onAdLoadingStarted()");
                    }

                    @Override
                    public void onAdLoadingFinished(VisxAdManager visxAdManager, String message) {
                        Log.i(TEST_TAG, "VISXCustomEventBannerGMA onAdLoadingFinished() Message: " + message);
                    }

                    @Override
                    public void onAdLoadingFailed(VisxAdManager visxAdManager, String message, boolean isFinal) {
                        Log.i(TEST_TAG, "VISXCustomEventBannerGMA onAdLoadingFailed() Message: " + message + " isFinal: " + isFinal);
                    }

                    @Override
                    public void onAdClicked() {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i(TEST_TAG, "VISXCustomEventBannerGMA onAdClicked()");
                                customEventListener.onAdClicked();
                            }
                        });
                    }

                    @Override
                    public void onAdLeftApplication() {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i(TEST_TAG, "VISXCustomEventBannerGMA onAdLeftApplication()");
                                customEventListener.onAdLeftApplication();
                            }
                        });
                    }
                })
                .build();
    }

    @Override
    public void onDestroy() {
        Log.i(TEST_TAG, "VISXCustomEventBannerGMA onDestroy()");
        if (visxAdManager != null) {
            visxAdManager.stop();
        }
    }

    @Override
    public void onPause() {
        Log.i(TEST_TAG, "VISXCustomEventBannerGMA onPause()");
        if (visxAdManager != null) {
            visxAdManager.pause();
        }
    }

    @Override
    public void onResume() {
        Log.i(TEST_TAG, "VISXCustomEventBannerGMA onResume()");
        if (visxAdManager != null) {
            visxAdManager.resume();
        }
    }
}
