package com.yoc.visx.sdk.mediation;

import static com.yoc.visx.sdk.mediation.MediationUtil.PARAMETER_KEY;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.mediation.Adapter;
import com.google.android.gms.ads.mediation.InitializationCompleteCallback;
import com.google.android.gms.ads.mediation.MediationAdLoadCallback;
import com.google.android.gms.ads.mediation.MediationConfiguration;
import com.google.android.gms.ads.mediation.MediationInterstitialAd;
import com.google.android.gms.ads.mediation.MediationInterstitialAdCallback;
import com.google.android.gms.ads.mediation.MediationInterstitialAdConfiguration;
import com.google.android.gms.ads.mediation.VersionInfo;
import com.yoc.visx.sdk.VisxAdManager;
import com.yoc.visx.sdk.adview.tracker.VisxCallbacks;

import java.util.List;

@Keep
public class VISXMediationAdapterInterstitialGAD extends Adapter {

    private static final String TAG = VISXMediationAdapterInterstitialGAD.class.getSimpleName();

    @Override
    public void initialize(@NonNull Context context,
                           @NonNull InitializationCompleteCallback initializationCompleteCallback,
                           @NonNull List<MediationConfiguration> list) {
        Log.i(TAG, "initialize()" +
                " context: " + context +
                " initializationCompleteCallback: " + initializationCompleteCallback +
                " List<MediationConfiguration>: " + list);
    }

    @NonNull
    @Override
    public VersionInfo getVersionInfo() {
        return MediationUtil.getVersionInfo(false);
    }

    @NonNull
    @Override
    public VersionInfo getSDKVersionInfo() {
        Log.i(TAG, "getSDKVersionInfo()");
        return MediationUtil.getVersionInfo(true);
    }

    @Override
    public void loadInterstitialAd(
            @NonNull MediationInterstitialAdConfiguration mediationInterstitialAdConfiguration,
            @NonNull MediationAdLoadCallback<MediationInterstitialAd, MediationInterstitialAdCallback> callback) {

        MediationUtil.setParameterMap(mediationInterstitialAdConfiguration.getServerParameters().get(PARAMETER_KEY).toString());
        new VisxAdManager.Builder()
                .visxAdUnitID(MediationUtil.getAuid())
                .adSize(MediationUtil.getInterstitialAdSize())
                .appDomain(MediationUtil.getAppDomain())
                .context(mediationInterstitialAdConfiguration.getContext())
                .setMediation()
                .callback(new VisxCallbacks() {
                    @Override
                    public void onAdRequestStarted(VisxAdManager visxAdManager) {
                        Log.i(TAG, "onAdRequestStarted()");
                    }

                    @Override
                    public void onAdResponseReceived(VisxAdManager visxAdManager, String message) {
                        Log.i(TAG, "onAdResponseReceived() Message: " + message);

                    }

                    @Override
                    public void onAdLoadingFinished(VisxAdManager visxAdManager, String message) {
                        Log.i(TAG, "onAdLoadingFinished() Message: " + message);
                        callback.onSuccess(context -> visxAdManager.showModalInterstitial());
                    }

                    @Override
                    public void onAdLoadingFailed(String message, int errorCode, boolean isFinal) {
                        Log.i(TAG, "onAdLoadingFailed() ErrorCode: " + errorCode + "Message: " + message + " isFinal: " + isFinal);
                        callback.onFailure(new AdError(errorCode, message, TAG));
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
                    public void onInterstitialWillBeClosed() {
                        Log.i(TAG, "onInterstitialWillBeClosed()");
                    }

                    @Override
                    public void onInterstitialClosed() {
                        Log.i(TAG, "onInterstitialClosed()");
                    }

                    @Override
                    public void onLandingPageOpened(boolean inExternalBrowser) {
                        Log.i(TAG, "onLandingPageOpened()");
                    }

                    @Override
                    public void onLandingPageClosed() {
                        Log.i(TAG, "onLandingPageClosed()");
                    }

                    @Override
                    public void onAdClosed() {
                        Log.i(TAG, "onAdClosed");
                    }
                })
                .build();
    }
}
