package com.yoc.visx.sdk.mediation;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.smartadserver.android.library.mediation.SASMediationInterstitialAdapter;
import com.smartadserver.android.library.mediation.SASMediationInterstitialAdapterListener;
import com.yoc.visx.sdk.VisxAdManager;
import com.yoc.visx.sdk.adview.tracker.VisxCallbacks;

import java.util.Map;

@Keep
public class VISXCustomEventInterstitialSAS implements SASMediationInterstitialAdapter {

    private static final String TAG = VISXCustomEventInterstitialSAS.class.getSimpleName();

    private VisxAdManager visxAdManager;

    /**
     * @param context                     the {@link android.content.Context} needed by the mediation SDK to make the ad request
     * @param serverParametersString      a String containing all needed parameters (as returned by Smart ad delivery)
     *                                    to make the mediation ad call
     * @param clientParameters            additional client-side parameters (user specific, like location)
     * @param interstitialAdapterListener the {@link SASMediationInterstitialAdapterListener} provided to
     *                                    this {@link com.smartadserver.android.library.mediation.SASMediationAdapter} to notify Smart SDK of events occurring
     */
    @Override
    public void requestInterstitialAd(@NonNull final Context context
            , @NonNull String serverParametersString
            , @NonNull Map<String, Object> clientParameters
            , @NonNull final SASMediationInterstitialAdapterListener interstitialAdapterListener) {

        MediationUtil.setParameterMap(serverParametersString);

        visxAdManager = new VisxAdManager.Builder().context(context)
                .visxAdUnitID(MediationUtil.getAuid())
                .adSize(MediationUtil.getInterstitialAdSize())
                .appDomain(MediationUtil.getAppDomain())
                .setMediation()
                .customTargetParams(MediationUtil.getTargetingParamsFromSmartAdServerMap(clientParameters))
                .context(context)
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
                    public void onAdLoadingStarted(VisxAdManager visxAdManager) {
                        Log.i(TAG, "onAdLoadingStarted()");
                    }

                    @Override
                    public void onAdLoadingFinished(VisxAdManager visxAdManager, String message) {
                        Log.i(TAG, "onAdLoadingFinished()");
                        interstitialAdapterListener.onInterstitialLoaded();
                    }

                    @Override
                    public void onAdLoadingFailed(String message, int errorCode, boolean isFinal) {
                        Log.i(TAG, "onAdLoadingFailed() ErrorCode: " + errorCode + "Message: " + message + " isFinal: " + isFinal);
                        interstitialAdapterListener.adRequestFailed(message, isFinal);
                    }

                    @Override
                    public void onAdSizeChanged(int width, int height) {
                        Log.i(TAG, "onAdSizeChanged()");
                    }

                    @Override
                    public void onAdClicked() {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i(TAG, "onAdClicked()");
                                interstitialAdapterListener.onAdClicked();
                            }
                        });
                    }

                    @Override
                    public void onAdLeftApplication() {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i(TAG, "onAdLeftApplication()");
                                interstitialAdapterListener.onAdClosed();
                            }
                        });
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
                        interstitialAdapterListener.onAdClosed();
                        Log.i(TAG, "onInterstitialClosed()");
                    }

                    @Override
                    public void onLandingPageOpened(boolean inExternalBrowser) {
                        Log.i(TAG, "onLandingPageOpened()");
                        interstitialAdapterListener.onAdClosed();
                    }

                    @Override
                    public void onLandingPageClosed() {
                        Log.i(TAG, "onLandingPageClosed()");
                        interstitialAdapterListener.onAdClosed();
                    }

                    @Override
                    public void onAdClosed() {
                        Log.i(TAG, "onAdClosed");
                    }
                })
                .build();
    }

    @Override
    public void showInterstitial() throws Exception {
        if (visxAdManager != null) visxAdManager.showModalInterstitial();
    }

    @Override
    public void onDestroy() {
        if (visxAdManager != null) visxAdManager.stop();
    }
}
