package com.yoc.visx.sdk.adapter.googleads;

import android.content.Context;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.yoc.visx.sdk.mediation.VISXMediationEventListener;
import com.yoc.visx.sdk.adapter.VisxMediationAdapter;

import java.util.List;

public class VISXInterstitialGAD implements VisxMediationAdapter {

    private PublisherInterstitialAd publisherInterstitialAd;

    public VISXInterstitialGAD() {
    }

    @Override
    public void loadAd(final String adUnitID, final Context context,
                       final VISXMediationEventListener eventListener, final List<int[]> adSizes) {

        publisherInterstitialAd = new PublisherInterstitialAd(context);
        publisherInterstitialAd.setAdUnitId(adUnitID);
        publisherInterstitialAd.loadAd(new PublisherAdRequest.Builder().build());

        publisherInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                String errorCodeMessage = "";
                switch (errorCode) {
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

            @Override
            public void onAdLeftApplication() {
                eventListener.onAdLeftApplication();
            }

            @Override
            public void onAdLoaded() {
                eventListener.onAdLoaded();
            }

            @Override
            public void onAdOpened() {
            }
        });
    }

    @Override
    public void show() {
        publisherInterstitialAd.show();
    }

    @Override
    public void destroy() {
        if (publisherInterstitialAd != null) {
            publisherInterstitialAd.setAdListener(null);
            publisherInterstitialAd = null;
        }
    }
}
