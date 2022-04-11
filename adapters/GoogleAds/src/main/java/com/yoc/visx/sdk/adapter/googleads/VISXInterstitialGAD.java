package com.yoc.visx.sdk.adapter.googleads;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback;
import com.yoc.visx.sdk.logger.VISXLog;
import com.yoc.visx.sdk.mediation.VISXMediationEventListener;
import com.yoc.visx.sdk.mediation.adapter.VisxMediationAdapter;

import java.util.Map;

public class VISXInterstitialGAD implements VisxMediationAdapter {

    private static final String AD_UNIT = "adunit";
    private AdManagerInterstitialAd publisherInterstitialAd;
    private Context context;

    public VISXInterstitialGAD() {
    }

    @Override
    public void loadAd(final Map<String, String> parametersMap, final Context context,
                       final VISXMediationEventListener eventListener) {

        this.context = context;
        String adUnit = "";
        if (parametersMap.get(AD_UNIT) != null) {
            adUnit = parametersMap.remove(AD_UNIT);
        }

        final AdManagerAdRequest.Builder adRequestBuilder = new AdManagerAdRequest.Builder();
        if (parametersMap != null) {
            for (Map.Entry<String, String> entry : parametersMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                adRequestBuilder.addCustomTargeting(key, value);
            }
        }

        AdManagerInterstitialAd.load(context,
                adUnit,
                adRequestBuilder.build(),
                new AdManagerInterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull AdManagerInterstitialAd adManagerInterstitialAd) {
                        super.onAdLoaded(adManagerInterstitialAd);
                        eventListener.onAdLoaded();
                        publisherInterstitialAd = adManagerInterstitialAd;
                        interstitialCallbackInit(publisherInterstitialAd);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);

                        if (loadAdError.getCode() == AdRequest.ERROR_CODE_NO_FILL) {
                            eventListener.mediationFailWithNoAd();
                            return;
                        }

                        String errorCodeMessage = "";
                        switch (loadAdError.getCode()) {
                            case AdRequest.ERROR_CODE_INTERNAL_ERROR:
                                errorCodeMessage = "ERROR_CODE_INTERNAL_ERROR";
                                break;
                            case AdRequest.ERROR_CODE_INVALID_REQUEST:
                                errorCodeMessage = "ERROR_CODE_INVALID_REQUEST";
                                break;
                            case AdRequest.ERROR_CODE_NETWORK_ERROR:
                                errorCodeMessage = "ERROR_CODE_NETWORK_ERROR";
                                break;
                            default:
                                errorCodeMessage = "AD FAILED TO LOAD - UNKNOWN ERROR";
                        }
                        eventListener.onAdLoadingFailed(errorCodeMessage, "GAD Interstitial");
                        destroy();
                    }
                });
    }

    private void interstitialCallbackInit(AdManagerInterstitialAd interstitialAd) {
        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when fullscreen content is dismissed.
                VISXLog.w("The ad was dismissed.");
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                // Called when fullscreen content failed to show.
                VISXLog.w("The ad failed to show.");
            }

            @Override
            public void onAdShowedFullScreenContent() {
                // Called when fullscreen content is shown.
                // Make sure to set your reference to null so you don't
                // show it a second time.
                publisherInterstitialAd = null;
                VISXLog.i("TAG", "The ad was shown.");
            }
        });
    }

    @Override
    public void show() {
        if (publisherInterstitialAd != null && context instanceof Activity) {
            publisherInterstitialAd.show((Activity) context);
        } else {
            VISXLog.w("Google interstitial Ad, in Google as Primary SDK in Mediation, wasn't ready yet.");
        }
    }

    @Override
    public void destroy() {
        if (publisherInterstitialAd != null) {
            publisherInterstitialAd = null;
        }
    }
}
