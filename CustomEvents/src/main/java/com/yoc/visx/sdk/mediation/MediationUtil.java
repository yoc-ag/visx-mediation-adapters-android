package com.yoc.visx.sdk.mediation;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Size;

import com.appnexus.opensdk.AdView;
import com.appnexus.opensdk.TargetingParameters;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.VersionInfo;
import com.google.android.gms.ads.mediation.customevent.CustomEventBannerListener;
import com.google.android.gms.ads.mediation.customevent.CustomEventInterstitialListener;
import com.yoc.visx.sdk.BuildConfig;
import com.yoc.visx.sdk.VisxAdManager;
import com.yoc.visx.sdk.util.AdSize;
import com.yoc.visx.sdk.util.PlacementType;
import com.yoc.visx.sdk.util.VISXLog;

import java.util.HashMap;
import java.util.Map;

final class MediationUtil {

    /**
     * Private constructor preventing creating instances of this class
     */
    private MediationUtil() {
    }

    private static Map<String, String> paramsMap = new HashMap<>();
    private static com.yoc.visx.sdk.util.AdSize visxAdSize;
    private static final String AUID_KEY = "auid";
    private static final String APP_DOMAIN_KEY = "app_domain";
    private static final String SIZE_KEY = "size";
    public static final String TEST_TAG = "--->";

    /**
     * Getting the SDK Version from
     *
     * @return VersionInfo
     * @see BuildConfig#VERSION_NAME
     * defined in SDK build.gradle and processing for populating
     * @see VersionInfo
     */
    static VersionInfo getVersionInfo(boolean isSDKVersion) {
        Log.i(TEST_TAG, "MediationUtil getSDKVersionInfo()");
        String versionString = BuildConfig.VERSION_NAME;
        String[] splits = versionString.split("\\.");

        if (splits.length >= 4) {
            int major = Integer.parseInt(splits[0]);
            int minor = Integer.parseInt(splits[1]);
            int micro = isSDKVersion ?
                    Integer.parseInt(splits[2]) :
                    Integer.parseInt(splits[2]) * 100 + Integer.parseInt(splits[3]);
            return new VersionInfo(major, minor, micro);
        }

        return new VersionInfo(0, 0, 0);
    }

    /**
     * Creating and setting parameter map from server response from Google Mediation Adapters
     *
     * @param parameters by splitting the response first by ";" for separating different key <> value pair value
     *                   and then by "=" for splitting key and value strings
     *                   (correct string parameters example: auid=910570;app_domain=yoc.com;size=300x250)
     * @see VISXCustomEventBannerGMA#requestBannerAd(Context, CustomEventBannerListener, String
     *, com.google.android.gms.ads.AdSize, MediationAdRequest, Bundle)
     * @see VISXCustomEventInterstitialGMA#requestInterstitialAd(Context, CustomEventInterstitialListener
     *, String, MediationAdRequest, Bundle)
     * <p>
     * Creating and populating parameter map with String key <> String value
     * from String
     */
    static void setParameterMap(String parameters) {
        if (!TextUtils.isEmpty(parameters)) {
            String[] keyValuePairs = parameters.split(";");
            for (String keyValuePair : keyValuePairs) {
                if (!TextUtils.isEmpty(keyValuePair)) {
                    String[] tokens = keyValuePair.split("=");
                    if (!TextUtils.isEmpty(tokens[0]) && !TextUtils.isEmpty(tokens[1])) {
                        paramsMap.put(tokens[0], tokens[1]);
                    }
                }
            }
        } else {
            Log.w(MediationUtil.class.getSimpleName(), "Mediation parameter response null or empty");
        }
    }

    /**
     * Method for getting the appDomain value from
     * serverParameters response that are stored inside paramsMap
     *
     * @return value of appDomain
     * @see MediationUtil#setParameterMap(String)
     */
    static String getAppDomain() {
        String appDomain = "";
        if (paramsMap != null && !TextUtils.isEmpty(paramsMap.get(APP_DOMAIN_KEY))) {
            appDomain = paramsMap.get(APP_DOMAIN_KEY);
        }
        return appDomain;
    }

    /**
     * Method for getting the auid value from
     * serverParameters response that are stored inside paramsMap
     *
     * @return value of auid
     * @see MediationUtil#setParameterMap(String)
     */
    static String getAuid() {
        String auid = "";
        if (paramsMap != null && !TextUtils.isEmpty(paramsMap.get(AUID_KEY))) {
            auid = paramsMap.get(AUID_KEY);
        }
        return auid;
    }

    /**
     * Method for getting the size value from
     * serverParameters response that are stored inside paramsMap and store in
     *
     * @return AdSize
     * @see AdSize object, ready for setting as adSize inside the
     * @see VisxAdManager.Builder#adSize(AdSize)
     * <p>
     * First thing is splitting the 'size' value from the paramsMap by "x", then initiate a new AdSize, with given Size and INLINE PlacementType
     * If SIZE_KEY is null, we will fallback to a 320x50 banner
     * @see MediationUtil#setParameterMap(String)
     */
    static AdSize getBannerAdSize() {
        if (paramsMap != null && !TextUtils.isEmpty(paramsMap.get(SIZE_KEY))) {
            String[] sizeList = paramsMap.get(SIZE_KEY).split("x");
            if (sizeList[0] != null && sizeList[1] != null) {
                visxAdSize = new AdSize(new Size(Integer.parseInt(sizeList[0]), Integer.parseInt(sizeList[1])), PlacementType.INLINE);
            } else {
                visxAdSize = AdSize.SMARTPHONE_320x50;
            }
        } else {
            visxAdSize = AdSize.SMARTPHONE_320x50;
        }
        return visxAdSize;
    }

    /**
     * Method for getting the size value from
     * serverParameters response that are stored inside paramsMap and store in
     *
     * @return AdSize
     * @see AdSize object, ready for setting as adSize inside the
     * @see VisxAdManager.Builder#adSize(AdSize)
     * <p>
     * First thing is splitting the 'size' value from the paramsMap by "x", then initiate a new AdSize, with given Size and INTERSTITIAL PlacementType
     * If SIZE_KEY is null, we will fallback to a 320x480 interstitial
     * @see MediationUtil#setParameterMap(String)
     */
    static AdSize getInterstitialAdSize() {
        if (paramsMap != null && !TextUtils.isEmpty(paramsMap.get(SIZE_KEY))) {
            String[] sizeList = paramsMap.get(SIZE_KEY).split("x");
            if (sizeList[0] != null && sizeList[1] != null) {
                visxAdSize = new AdSize(new Size(Integer.parseInt(sizeList[0]), Integer.parseInt(sizeList[1])), PlacementType.INTERSTITIAL);
            } else {
                visxAdSize = AdSize.INTERSTITIAL_320x480;
            }
        } else {
            visxAdSize = AdSize.INTERSTITIAL_320x480;
        }
        return visxAdSize;
    }

    /**
     * XANDR Mapping TargetingParameters to our CustomTargetingParameters HashMap
     *
     * @param targetingParameters - values from Xandr
     * @return customTargetingParams
     * @see VisxAdManager.Builder#customTargetParams(HashMap)
     */
    static HashMap<String, String> getCustomTargetingParamsXandr(TargetingParameters targetingParameters) {
        final HashMap<String, String> customTargetingParams = new HashMap<>();

        if (targetingParameters != null) {

            if (targetingParameters.getCustomKeywords() != null && !targetingParameters.getCustomKeywords().isEmpty()) {
                for (int i = 0; i < targetingParameters.getCustomKeywords().size(); i++) {
                    String key = targetingParameters.getCustomKeywords().get(i).first;
                    String value = targetingParameters.getCustomKeywords().get(i).second;
                    customTargetingParams.put(key, value);
                }
            } else {
                VISXLog.w("Mediation from Xandr - TargetingParams is NULL or EMPTY");
            }

            if (!TextUtils.isEmpty(targetingParameters.getAge())) {
                customTargetingParams.put("age", targetingParameters.getAge());
            } else {
                VISXLog.w("Mediation from Xandr - Age is NULL or EMPTY");
            }

            if (targetingParameters.getGender() != null) {
                if (targetingParameters.getGender() == AdView.GENDER.MALE) {
                    customTargetingParams.put("gender", "male");
                } else if (targetingParameters.getGender() == AdView.GENDER.FEMALE) {
                    customTargetingParams.put("gender", "female");
                } else if ((targetingParameters.getGender() == AdView.GENDER.UNKNOWN)) {
                    customTargetingParams.put("gender", "unknown");
                }
            } else {
                VISXLog.w("Mediation from Xandr - Gender is NULL or EMPTY");
            }


            if (!TextUtils.isEmpty(targetingParameters.getExternalUid())) {
                customTargetingParams.put("externalUid", targetingParameters.getExternalUid());
            } else {
                VISXLog.w("Mediation from Xandr - ExternalUid is NULL or EMPTY");
            }

            if (targetingParameters.getLocation() != null) {
                customTargetingParams.put("lat", String.valueOf(targetingParameters.getLocation().getLatitude()));
                customTargetingParams.put("lon", String.valueOf(targetingParameters.getLocation().getLongitude()));
            } else {
                VISXLog.w("Mediation from Xandr - Location is NULL");
            }
        } else {
            VISXLog.w("TargetingParameters from Xandr is NULL");
        }

        return customTargetingParams;
    }

    /**
     * Smart Ad Server Mapping TargetingParameters Map to our CustomTargetingParameters HashMap
     *
     * @param clientParameters - Map with String key and String value
     * @return - HashMap resembling SDK type for customTargetingParameters
     */
    static HashMap<String, String> getTargetingParamsFromSmartAdServerMap(Map<String, String> clientParameters) {
        HashMap<String, String> customTargetParam = new HashMap<>();
        if (clientParameters instanceof HashMap<?, ?>) {
            customTargetParam = (HashMap<String, String>) clientParameters;
        }
        return customTargetParam;
    }
}

