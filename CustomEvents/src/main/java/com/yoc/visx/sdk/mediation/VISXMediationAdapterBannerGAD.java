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
import com.google.android.gms.ads.mediation.MediationBannerAd;
import com.google.android.gms.ads.mediation.MediationBannerAdCallback;
import com.google.android.gms.ads.mediation.MediationBannerAdConfiguration;
import com.google.android.gms.ads.mediation.MediationConfiguration;
import com.google.android.gms.ads.mediation.VersionInfo;
import com.yoc.visx.sdk.VisxAdManager;
import com.yoc.visx.sdk.adview.tracker.VisxCallbacks;

import java.util.List;

@Keep
public class VISXMediationAdapterBannerGAD extends Adapter {

    private static final String TAG = VISXMediationAdapterBannerGAD.class.getSimpleName();

    @Override
    public void initialize(@NonNull Context context, @NonNull InitializationCompleteCallback initializationCompleteCallback, @NonNull List<MediationConfiguration> list) {
        Log.i(TAG, "initialize()" +
                " context: " + context +
                " initializationCompleteCallback: " + initializationCompleteCallback +
                " List<MediationConfiguration>: " + list);
    }

    @NonNull
    @Override
    public VersionInfo getSDKVersionInfo() {
        return MediationUtil.getVersionInfo(true);
    }

    @NonNull
    @Override
    public VersionInfo getVersionInfo() {
        return MediationUtil.getVersionInfo(false);
    }

    @Override
    public void loadBannerAd(
            @NonNull MediationBannerAdConfiguration mediationBannerAdConfiguration,
            @NonNull MediationAdLoadCallback<MediationBannerAd, MediationBannerAdCallback> callback) {

        MediationUtil.setParameterMap(mediationBannerAdConfiguration.getServerParameters().get(PARAMETER_KEY).toString());
        new VisxAdManager.Builder()
                .visxAdUnitID(MediationUtil.getAuid())
                .adSize(MediationUtil.getBannerAdSize())
                .appDomain(MediationUtil.getAppDomain())
                .setMediation()
                .context(mediationBannerAdConfiguration.getContext())
                .callback(new VisxCallbacks() {
                    @Override
                    public void onAdRequestStarted(VisxAdManager visxAdManager) {
                        Log.i(TAG, "onAdRequestStarted()");
                    }

                    @Override
                    public void onAdResponseReceived(VisxAdManager visxAdManager, String message) {
                        Log.i(TAG, "onAdResponseReceived() Message: " + message);
                        callback.onSuccess(visxAdManager::getAdContainer);
                    }

                    @Override
                    public void onAdLoadingFinished(VisxAdManager visxAdManager, String message) {
                        Log.i(TAG, "onAdLoadingFinished() Message: " + message);

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
}
