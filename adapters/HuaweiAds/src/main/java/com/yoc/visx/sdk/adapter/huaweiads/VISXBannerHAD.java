package com.yoc.visx.sdk.adapter.huaweiads;

import android.content.Context;

import com.huawei.hms.ads.AdListener;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.BannerAdSize;
import com.huawei.hms.ads.banner.BannerView;
import com.yoc.visx.sdk.adapter.VisxMediationAdapter;
import com.yoc.visx.sdk.mediation.VISXMediationEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class VISXBannerHAD implements VisxMediationAdapter {

    private static final String AD_UNIT = "adunit";
    private static final String AD_SIZES = "sizes";

    private BannerView bannerView;


    public VISXBannerHAD() {
    }

    @Override
    public void loadAd(Map<String, String> parametersMap, Context context, VISXMediationEventListener eventListener) {
        bannerView = new BannerView(context);
        bannerView.setAdId(parametersMap.get(AD_UNIT));
        bannerView.setBannerAdSize(getSizes(parametersMap.get(AD_SIZES))[0]);
        bannerView.setAdListener(new AdListener() {
            @Override
            public void onAdFailed(int errorCode) {
                if (errorCode == AdParam.ErrorCode.NO_AD) {
                    eventListener.initNextMediationAdapter();
                    return;
                }
                String errorMessage;
                switch (errorCode) {
                    case AdParam.ErrorCode.INNER:
                        errorMessage = "ERROR_CODE_INTERNAL_ERROR";
                        break;
                    case AdParam.ErrorCode.INVALID_REQUEST:
                        errorMessage = "ERROR_CODE_INVALID_REQUEST";
                        break;
                    case AdParam.ErrorCode.NETWORK_ERROR:
                        errorMessage = "ERROR_CODE_NETWORK_ERROR";
                        break;
                    case AdParam.ErrorCode.BANNER_AD_EXPIRE:
                        errorMessage = "ERROR_CODE_BANNER_AD_EXPIRED";
                        break;
                    case AdParam.ErrorCode.AD_LOADING:
                        errorMessage = "ERROR_CODE_BANNER_AD_LOADING";
                        break;
                    case AdParam.ErrorCode.BANNER_AD_CANCEL:
                        errorMessage = "ERROR_CODE_BANNER_AD_TASK_REMOVED";
                        break;
                    case AdParam.ErrorCode.LOW_API:
                        errorMessage = "ERROR_CODE_API_VERSION_NOT_SUPPORTED";
                        break;
                    default:
                        errorMessage = "AD FAILED TO LOAD - UNKNOWN ERROR";
                }
                eventListener.onAdLoadingFailed(errorMessage);
                destroy();
            }

            @Override
            public void onAdLeave() {
                super.onAdLeave();
                eventListener.onAdLeftApplication();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                eventListener.onAdLoaded();
                eventListener.showAd(bannerView);
            }
        });
        bannerView.loadAd(new AdParam.Builder().build());
    }

    @Override
    public void show() {
        //method used for interstitial
    }

    @Override
    public void destroy() {
        if (bannerView != null) {
            bannerView.setAdListener(null);
            bannerView.destroy();
        }
    }

    private BannerAdSize[] getSizes(String sizes) {
        String sizesTrimmed = sizes.replaceAll("[\\[ \\] ,]", " ");

        List<Integer> sizeList = new ArrayList<>();

        Scanner scanner = new Scanner(sizesTrimmed);
        while (scanner.hasNextInt()) {
            sizeList.add(scanner.nextInt());
        }

        BannerAdSize[] adSizesList = new BannerAdSize[sizeList.size() / 2];
        int position = 0;
        for (int i = 0; i < sizeList.size(); i += 2) {
            adSizesList[position] = new BannerAdSize(sizeList.get(i), sizeList.get(i + 1));
            position++;
        }
        return adSizesList;
    }
}
