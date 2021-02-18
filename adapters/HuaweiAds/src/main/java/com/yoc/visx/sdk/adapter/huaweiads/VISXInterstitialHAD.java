package com.yoc.visx.sdk.adapter.huaweiads;

import android.content.Context;

import com.huawei.hms.ads.AdListener;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.InterstitialAd;
import com.yoc.visx.sdk.adapter.VisxMediationAdapter;
import com.yoc.visx.sdk.mediation.VISXMediationEventListener;

import java.util.List;

public class VISXInterstitialHAD implements VisxMediationAdapter {
    private InterstitialAd interstitialAd;

    public VISXInterstitialHAD() {
    }

    @Override
    public void loadAd(String adUnitID, Context context, VISXMediationEventListener eventListener, List<int[]> adSizes) {
        interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdId(adUnitID);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdFailed(int errorCode) {
                super.onAdFailed(errorCode);
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
                eventListener.onAdLeftApplication();
            }

            @Override
            public void onAdLoaded() {
                eventListener.onAdLoaded();
            }
        });
        interstitialAd.loadAd(new AdParam.Builder().build());
    }

    @Override
    public void show() {
        interstitialAd.show();
    }

    @Override
    public void destroy() {
        if (interstitialAd != null) {
            interstitialAd.setAdListener(null);
            interstitialAd = null;
        }
    }
}
