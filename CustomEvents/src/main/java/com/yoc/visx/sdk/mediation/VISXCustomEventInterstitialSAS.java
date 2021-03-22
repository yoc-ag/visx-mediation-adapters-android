package com.yoc.visx.sdk.mediation;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.smartadserver.android.library.mediation.SASMediationInterstitialAdapter;
import com.smartadserver.android.library.mediation.SASMediationInterstitialAdapterListener;
import com.yoc.visx.sdk.VisxAdManager;
import com.yoc.visx.sdk.view.category.ActionTracker;

import java.util.Map;

import static com.yoc.visx.sdk.mediation.MediationUtil.TEST_TAG;

@Keep
public class VISXCustomEventInterstitialSAS implements SASMediationInterstitialAdapter {

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
            , @NonNull Map<String, String> clientParameters
            , @NonNull final SASMediationInterstitialAdapterListener interstitialAdapterListener) {

        MediationUtil.setParameterMap(serverParametersString);

        visxAdManager = new VisxAdManager.Builder().context(context)
                .visxAdUnitID(MediationUtil.getAuid())
                .adSize(MediationUtil.getInterstitialAdSize())
                .appDomain(MediationUtil.getAppDomain())
                .setMediation()
                .customTargetParams(MediationUtil.getTargetingParamsFromSmartAdServerMap(clientParameters))
                .context(context)
                .callback(new ActionTracker() {
                    @Override
                    public void onAdRequestStarted(VisxAdManager visxAdManager) {
                        Log.i(TEST_TAG, "VISXCustomEventInterstitialSAS onAdRequestStarted()");
                    }

                    @Override
                    public void onAdResponseReceived(VisxAdManager visxAdManager, String message) {
                        Log.i(TEST_TAG, "VISXCustomEventInterstitialSAS onAdResponseReceived() Message: " + message);

                    }

                    @Override
                    public void onAdLoadingStarted(VisxAdManager visxAdManager) {
                        Log.i(TEST_TAG, "VISXCustomEventInterstitialSAS onAdLoadingStarted()");
                    }

                    @Override
                    public void onAdLoadingFinished(VisxAdManager visxAdManager, String message) {
                        Log.i(TEST_TAG, "VISXCustomEventInterstitialSAS onAdLoadingFinished()");
                        interstitialAdapterListener.onInterstitialLoaded();
                    }

                    @Override
                    public void onAdLoadingFailed(VisxAdManager visxAdManager, String message, boolean isFinal) {
                        Log.i(TEST_TAG, "VISXCustomEventInterstitialSAS onAdLoadingFailed()");
                        interstitialAdapterListener.adRequestFailed(message, isFinal);

                    }

                    @Override
                    public void onAdClicked() {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i(TEST_TAG, "VISXCustomEventInterstitialSAS onAdClicked()");
                                interstitialAdapterListener.onAdClicked();
                            }
                        });
                    }

                    @Override
                    public void onAdLeftApplication() {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i(TEST_TAG, "VISXCustomEventInterstitialSAS onAdLeftApplication()");
                                interstitialAdapterListener.onAdClosed();
                            }
                        });
                    }
                })
                .build();
    }

    @Override
    public void showInterstitial() throws Exception {
        if (visxAdManager != null) {
            visxAdManager.showModalInterstitial();
        }
    }

    @Override
    public void onDestroy() {
        if (visxAdManager != null) {
            visxAdManager.stop();
        }
    }
}
