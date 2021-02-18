package com.yoc.visx.sdk.adapter.huaweiads;

import android.content.Context;

import com.huawei.hms.ads.AdListener;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.BannerAdSize;
import com.huawei.hms.ads.banner.BannerView;
import com.yoc.visx.sdk.adapter.VisxMediationAdapter;
import com.yoc.visx.sdk.mediation.VISXMediationEventListener;

import java.util.List;

public class VISXBannerHAD implements VisxMediationAdapter {
    private static final int WIDTH_ARRAY_ELEMENT = 0;
    private static final int HEIGHT_ARRAY_ELEMENT = 1;

    private BannerView bannerView;


    public VISXBannerHAD() {
    }

    @Override
    public void loadAd(String adUnitID, Context context, VISXMediationEventListener eventListener, List<int[]> adSizes) {
        bannerView = new BannerView(context);
        bannerView.setAdId(adUnitID);
        bannerView.setBannerAdSize(getAdSizes(adSizes)[0]);
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

    private BannerAdSize[] getAdSizes(List<int[]> adSizes) {
        BannerAdSize[] adSizesList = new BannerAdSize[adSizes.size()];
        for (int i = 0; i < adSizes.size(); i++) {
            int width = adSizes.get(i)[WIDTH_ARRAY_ELEMENT];
            int height = adSizes.get(i)[HEIGHT_ARRAY_ELEMENT];
            adSizesList[i] = new BannerAdSize(width, height);
        }
        return adSizesList;
    }
}
