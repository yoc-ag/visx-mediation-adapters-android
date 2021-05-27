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
import com.yoc.visx.sdk.adapter.VisxMediationAdapter;
import com.yoc.visx.sdk.mediation.VISXMediationEventListener;
import com.yoc.visx.sdk.util.VISXLog;

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

        AdManagerInterstitialAd.load(context,
                parametersMap.get(AD_UNIT),
                new AdManagerAdRequest.Builder().build(),
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
                    case AdRequest.ERROR_CODE_NO_FILL:
                        errorCodeMessage = "ERROR_CODE_NO_FILL";
                        eventListener.initNextMediationAdapter();
                        break;
                    default:
                        errorCodeMessage = "AD FAILED TO LOAD - UNKNOWN ERROR";
                }
                eventListener.onAdLoadingFailed(errorCodeMessage);
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
