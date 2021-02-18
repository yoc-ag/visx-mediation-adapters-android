package com.yoc.visx.sdk.mediation;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Keep;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.mediation.Adapter;
import com.google.android.gms.ads.mediation.InitializationCompleteCallback;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.MediationConfiguration;
import com.google.android.gms.ads.mediation.VersionInfo;
import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitial;
import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitialListener;
import com.yoc.visx.sdk.VisxAdManager;
import com.yoc.visx.sdk.view.category.ActionTracker;

import java.util.List;

import static com.yoc.visx.sdk.mediation.MediationUtil.TEST_TAG;

@Keep
public class VISXCustomEventInterstitialGMA extends Adapter implements CustomEventInterstitial {

    private VisxAdManager visxAdManager;
    private CustomEventInterstitialListener customEventListener;

    public VISXCustomEventInterstitialGMA() {
        Log.i(TEST_TAG, "VISXCustomEventInterstitialGMA CONSTRUCTOR");
    }

    @Override
    public void initialize(Context context, InitializationCompleteCallback initializationCompleteCallback, List<MediationConfiguration> list) {
        Log.i(TEST_TAG, "VISXCustomEventInterstitialGMA initialize()" +
                " context: " + context +
                " initializationCompleteCallback: " + initializationCompleteCallback +
                " List<MediationConfiguration>: " + list);
    }

    @Override
    public VersionInfo getVersionInfo() {
        return MediationUtil.getVersionInfo(false);
    }

    @Override
    public VersionInfo getSDKVersionInfo() {
        Log.i(TEST_TAG, "VISXCustomEventInterstitialGMA getSDKVersionInfo()");
        return MediationUtil.getVersionInfo(true);
    }

    @Override
    public void requestInterstitialAd(final Context context, CustomEventInterstitialListener customEventInterstitialListener, String serverParameter, MediationAdRequest mediationAdRequest, Bundle bundle) {
        MediationUtil.setParameterMap(serverParameter);
        this.customEventListener = customEventInterstitialListener;
        visxAdManager = new VisxAdManager.Builder().context(context)
                .visxAdUnitID(MediationUtil.getAuid())
                .adSize(MediationUtil.getInterstitialAdSize())
                .appDomain(MediationUtil.getAppDomain())
                .context(context)
                .callback(new ActionTracker() {
                    @Override
                    public void onAdRequestStarted(VisxAdManager visxAdManager) {

                    }

                    @Override
                    public void onAdResponseReceived(VisxAdManager visxAdManager, String message) {
                        Log.i(TEST_TAG, "VISXCustomEventInterstitialGMA onAdResponseReceived() Message: " + message);
                        customEventListener.onAdLoaded();
                    }

                    @Override
                    public void onAdLoadingStarted(VisxAdManager visxAdManager) {
                        Log.i(TEST_TAG, "VISXCustomEventInterstitialGMA onAdLoadingStarted()");
                    }

                    @Override
                    public void onAdLoadingFinished(VisxAdManager visxAdManager, String message) {
                        Log.i(TEST_TAG, "VISXCustomEventInterstitialGMA onAdLoadingFinished()");
                    }

                    @Override
                    public void onAdLoadingFailed(VisxAdManager visxAdManager, String message, boolean isFinal) {
                        Log.i(TEST_TAG, "VISXCustomEventInterstitialGMA onAdLoadingFailed()");
                        customEventListener.onAdFailedToLoad(AdRequest.ERROR_CODE_INTERNAL_ERROR);
                    }

                    @Override
                    public void onAdClicked() {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                customEventListener.onAdClicked();
                            }
                        });
                    }

                    @Override
                    public void onAdLeftApplication() {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                customEventListener.onAdLeftApplication();
                            }
                        });
                    }
                })
                .build();
    }

    @Override
    public void showInterstitial() {
        Log.i(TEST_TAG, "VISXCustomEventInterstitialGMA showInterstitial()");
        visxAdManager.showModalInterstitial();
        customEventListener.onAdOpened();
    }

    @Override
    public void onDestroy() {
        Log.i(TEST_TAG, "VISXCustomEventInterstitialGMA onDestroy()");
        if (visxAdManager != null) {
            visxAdManager.stop();
        }
    }

    @Override
    public void onPause() {
        Log.i(TEST_TAG, "VISXCustomEventInterstitialGMA onPause()");
        if (visxAdManager != null) {
            visxAdManager.pause();
        }
    }

    @Override
    public void onResume() {
        Log.i(TEST_TAG, "VISXCustomEventInterstitialGMA onResume()");
        if (visxAdManager != null) {
            visxAdManager.resume();
        }
    }

}
