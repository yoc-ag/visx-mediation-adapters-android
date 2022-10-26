package com.yoc.visx.sdk.mediation;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.smartadserver.android.library.mediation.SASMediationBannerAdapter;
import com.smartadserver.android.library.mediation.SASMediationBannerAdapterListener;
import com.yoc.visx.sdk.VisxAdManager;
import com.yoc.visx.sdk.adview.tracker.VisxCallbacks;

import java.util.Map;

@Keep
public class VISXCustomEventBannerSAS implements SASMediationBannerAdapter {

    private static final String TAG = VISXCustomEventBannerSAS.class.getSimpleName();

    private VisxAdManager visxAdManager;

    /**
     * @param context                the {@link android.content.Context} needed by the mediation SDK to make the ad request
     * @param serverParametersString a String containing all needed parameters (as returned by Smart ad delivery)
     *                               to make the mediation ad call
     * @param clientParameters       additional client-side parameters (user specific, like location)
     * @param bannerAdapterListener  the {@link SASMediationBannerAdapterListener} provided to
     *                               this {@link com.smartadserver.android.library.mediation.SASMediationAdapter} to notify Smart SDK of events occurring
     */
    @Override
    public void requestBannerAd(@NonNull final Context context,
                                @NonNull String serverParametersString,
                                @NonNull Map<String, Object> clientParameters,
                                @NonNull final SASMediationBannerAdapterListener bannerAdapterListener) {

        MediationUtil.setParameterMap(serverParametersString);

        visxAdManager = new VisxAdManager.Builder()
                .visxAdUnitID(MediationUtil.getAuid())
                .adSize(MediationUtil.getBannerAdSize())
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
                        Log.i(TAG, "onAdResponseReceived(): " + message);
                        bannerAdapterListener.onBannerLoaded(visxAdManager.getAdContainer());
                    }

                    @Override
                    public void onAdLoadingStarted(VisxAdManager visxAdManager) {
                        Log.i(TAG, "onAdLoadingStarted()");
                    }

                    @Override
                    public void onAdLoadingFinished(final VisxAdManager visxAdManager, String message) {
                        updateAdContainerSize(visxAdManager);
                        Log.i(TAG, "onAdLoadingFinished()");
                    }

                    @Override
                    public void onAdLoadingFailed(String message, int errorCode, boolean isFinal) {
                        Log.i(TAG, "onAdLoadingFailed() ErrorCode: " + errorCode + "Message: " + message + " isFinal: " + isFinal);
                        bannerAdapterListener.adRequestFailed(message, isFinal);
                    }

                    @Override
                    public void onAdSizeChanged(int width, int height) {
                        Log.i(TAG, "onAdSizeChanged()");
                    }

                    @Override
                    public void onAdClicked() {
                        ((Activity) context).runOnUiThread(() -> {
                            Log.i(TAG, "onAdClicked()");
                            bannerAdapterListener.onAdClicked();
                        });
                    }

                    @Override
                    public void onAdLeftApplication() {
                        ((Activity) context).runOnUiThread(() -> {
                            Log.i(TAG, "onAdLeftApplication()");
                            bannerAdapterListener.onAdLeftApplication();
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

    private void updateAdContainerSize(VisxAdManager visxAdManager) {
        if (visxAdManager != null &&
                visxAdManager.getAdContainer() != null && // VisxAdContainer
                visxAdManager.getAdContainer().getParent() != null && // RelativeLayout in SmartAd view hierarchy
                visxAdManager.getAdContainer().getParent().getParent() != null && // SASBannerView
                visxAdManager.getAdContainer().getParent().getParent().getParent() != null // Container for holding the SmartAd with fixed sizes
        ) {
            View smartAdContainer = (View) visxAdManager.getAdContainer().getParent().getParent().getParent();
            ViewGroup.LayoutParams layoutParams = smartAdContainer.getLayoutParams();
            if (layoutParams != null) {
                layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                smartAdContainer.setLayoutParams(layoutParams);
            }
        }
    }


    @Override
    public void onDestroy() {
        if (visxAdManager != null) {
            visxAdManager.stop();
        }
    }
}
