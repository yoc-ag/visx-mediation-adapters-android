package com.yoc.visx.sdk.adapter.googleads;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerAdView;
import com.yoc.visx.sdk.mediation.VISXMediationEventListener;
import com.yoc.visx.sdk.mediation.adapter.VisxMediationAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class VISXBannerGAD implements VisxMediationAdapter {

    private static final String AD_UNIT = "adunit";
    private static final String AD_SIZES = "sizes";

    private AdManagerAdView adView;

    public VISXBannerGAD() {
    }

    @Override
    public void loadAd(final Map<String, String> parametersMap, final Context context,
                       final VISXMediationEventListener eventListener) {

        adView = new AdManagerAdView(context);
        adView.setAdUnitId(parametersMap.get(AD_UNIT));
        adView.setAdSizes(getSizes(parametersMap.get(AD_SIZES)));

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
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
                eventListener.onAdLoadingFailed(errorCodeMessage, "GAD Banner");
                destroy();
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

        // Request and load an Ad with custom parameters
        final AdManagerAdRequest.Builder adRequestBuilder = new AdManagerAdRequest.Builder();
        if (parametersMap != null) {
            parametersMap.remove(AD_SIZES);
            parametersMap.remove(AD_UNIT);
            for (Map.Entry<String, String> entry : parametersMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                adRequestBuilder.addCustomTargeting(key, value);
            }
        }
        adView.loadAd(adRequestBuilder.build());
    }

    @Override
    public void show() {
        // method used for interstitial
    }

    @Override
    public void destroy() {
        adView.setAdListener(null);
        adView.destroy();
    }

    private AdSize[] getSizes(String sizes) {
        String sizesTrimmed = sizes.replaceAll("[\\[ \\] ,]", " ");

        List<Integer> sizeList = new ArrayList<>();

        Scanner scanner = new Scanner(sizesTrimmed);
        while (scanner.hasNextInt()) {
            sizeList.add(scanner.nextInt());
        }

        AdSize[] adSizesList = new AdSize[sizeList.size() / 2];
        int position = 0;
        for (int i = 0; i < sizeList.size(); i += 2) {
            adSizesList[position] = new AdSize(sizeList.get(i), sizeList.get(i + 1));
            position++;
        }
        return adSizesList;
    }
}
