package com.yoc.visx.sdk.adapter.googleads;

import android.content.Context;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.yoc.visx.sdk.mediation.VISXMediationEventListener;
import com.yoc.visx.sdk.adapter.VisxMediationAdapter;

import java.util.List;

public class VISXBannerGAD implements VisxMediationAdapter {

    private static final int WIDTH_ARRAY_ELEMENT = 0;
    private static final int HEIGHT_ARRAY_ELEMENT = 1;

    private PublisherAdView adView;

    public VISXBannerGAD() {
    }

    @Override
    public void loadAd(final String adUnitID, final Context context,
                       final VISXMediationEventListener eventListener, final List<int[]> adSizes) {

        adView = new PublisherAdView(context);
        adView.setAdUnitId(adUnitID);
        adView.setAdSizes(getAdSizes(adSizes));

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                if (errorCode == AdRequest.ERROR_CODE_NO_FILL) {
                    eventListener.initNextMediationAdapter();
                    return;
                }
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
                // Add the adView to the VisxAdViewContainer
                eventListener.showAd(adView);
            }

            @Override
            public void onAdOpened() {
            }
        });

        // Request and load an Ad
        final PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    @Override
    public void show() {
        // method used for interstital
    }

    @Override
    public void destroy() {
        adView.setAdListener(null);
        adView.destroy();
    }

    private AdSize[] getAdSizes(List<int[]> adSizes) {
        AdSize[] adSizesList = new AdSize[adSizes.size()];
        for (int i = 0; i < adSizes.size(); i++) {
            int width = adSizes.get(i)[WIDTH_ARRAY_ELEMENT];
            int height = adSizes.get(i)[HEIGHT_ARRAY_ELEMENT];
            adSizesList[i] = new AdSize(width, height);
        }
        return adSizesList;
    }
}
