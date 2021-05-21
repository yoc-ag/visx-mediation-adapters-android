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
                        bannerAdapterListener.onBannerLoaded(visxAdManager.getAdContainer());
                    }

                    @Override
                    public void onAdLoadingStarted(VisxAdManager visxAdManager) {
                        Log.i(TEST_TAG, "VISXCustomEventBannerSAS onAdLoadingStarted()");
                    }

                    @Override
                    public void onAdLoadingFinished(final VisxAdManager visxAdManager, String message) {
                        updateAdContainerSize(visxAdManager);
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
