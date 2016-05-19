package br.com.trendzapi.application;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.util.HashMap;

import br.com.trendzapi.application.rest.Api;

public class TrendzAd {
    private Context mContext;
    private Api api;
    private String mAppKey, mOperator;

    public TrendzAd(Context context, String appKey) {
        mContext = context;
        mAppKey = appKey;
        mOperator = getOperatorName();

        registerAccess();
    }

    public void registerAccess() {
        api = new Api(mContext);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("app_id", mAppKey);
        params.put("operadora", mOperator);

        api.registraAcesso(params);
    }

    public void makeAd() {
        api = new Api(mContext);
        api.buscaAd(mAppKey);
    }

    private String getOperatorName() {
        String ret = "";

        try {
            TelephonyManager tManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            ret = tManager.getSimOperatorName();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }


    /* GET METADATA DECLARED IN MANIFEST */
    /*private String getAppKey() {
        String ret = "";

        try {
            ApplicationInfo ai = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            ret = bundle.getString("trendzapi.applicationKey");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return ret;
    }*/
}