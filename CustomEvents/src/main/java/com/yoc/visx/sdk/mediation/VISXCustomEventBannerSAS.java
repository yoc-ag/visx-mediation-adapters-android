package com.yoc.visx.sdk.mediation;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import com.smartadserver.android.library.mediation.SASMediationBannerAdapter;
import com.smartadserver.android.library.mediation.SASMediationBannerAdapterListener;
import com.yoc.visx.sdk.VisxAdManager;
import com.yoc.visx.sdk.view.category.ActionTracker;

import java.util.Map;

import static com.yoc.visx.sdk.mediation.MediationUtil.TEST_TAG;

@Keep
public class VISXCustomEventBannerSAS implements SASMediationBannerAdapter {

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
                                @NonNull Map<String, String> clientParameters,
                                @NonNull final SASMediationBannerAdapterListener bannerAdapterListener) {

        MediationUtil.setParameterMap(serverParametersString);

        visxAdManager = new VisxAdManager.Builder()
                .visxAdUnitID(MediationUtil.getAuid())
                .adSize(MediationUtil.getBannerAdSize())
                .appDomain(MediationUtil.getAppDomain())
                .setMediation()
                .customTargetParams(MediationUtil.getTargetingParamsFromSmartAdServerMap(clientParameters))
                .context(context)
                .callback(new ActionTracker() {
                    @Override
                    public void onAdRequestStarted(VisxAdManager visxAdManager) {
                        Log.i(TEST_TAG, "VISXCustomEventBannerSAS onAdRequestStarted()");
                    }

                    @Override
                    public void onAdResponseReceived(VisxAdManager visxAdManager, String message) {
                        Log.i(TEST_TAG, "VISXCustomEventBannerSAS onAdResponseReceived(): " + message);
                        if (visxAdManager.getVisxAdViewContainer().getParent() != null) {
                            ((ViewGroup) (visxAdManager.getVisxAdViewContainer().getParent())).removeView(visxAdManager.getVisxAdViewContainer());
                        }
                        bannerAdapterListener.onBannerLoaded(visxAdManager.getVisxAdViewContainer());
                    }

                    @Override
                    public void onAdLoadingStarted(VisxAdManager visxAdManager) {
                        Log.i(TEST_TAG, "VISXCustomEventBannerSAS onAdLoadingStarted()");
                    }

                    @Override
                    public void onAdLoadingFinished(VisxAdManager visxAdManager, String message) {
                        Log.i(TEST_TAG, "VISXCustomEventBannerSAS onAdLoadingFinished()");
                    }

                    @Override
                    public void onAdLoadingFailed(VisxAdManager visxAdManager, String message, boolean isFinal) {
                        Log.i(TEST_TAG, "VISXCustomEventBannerSAS onAdLoadingFinished() - message: " + message + " is final: " + isFinal);
                        bannerAdapterListener.adRequestFailed(message, isFinal);
                    }

                    @Override
                    public void onAdClicked() {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i(TEST_TAG, "VISXCustomEventBannerSAS onAdClicked()");
                                bannerAdapterListener.onAdClicked();
                            }
                        });
                    }

                    @Override
                    public void onAdLeftApplication() {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i(TEST_TAG, "VISXCustomEventBannerSAS onAdLeftApplication()");
                                bannerAdapterListener.onAdLeftApplication();
                            }
                        });
                    }
                })
                .build();
    }

    @Override
    public void onDestroy() {
        if (visxAdManager != null) {
            visxAdManager.stop();
        }
    }
}
