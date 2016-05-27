package br.com.trendzapi.application;

import android.content.Context;
import android.telephony.TelephonyManager;

import br.com.trendzapi.application.rest.Api;

public class TrendzAd {
    private Context mContext;
    private String mAppKey, mOperator, mPhoneNumber;

    public TrendzAd(Context context, String appKey) {
        mContext = context;
        mAppKey = appKey;
        mOperator = getOperatorName();
        mPhoneNumber = getPhoneNumber();
    }

    public void makeAd() {
        Api api = new Api(mContext);
        api.buscaAd(mAppKey, mOperator, mPhoneNumber);
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

    private String getPhoneNumber() {
        String ret = "";

        try {
            TelephonyManager tMgr = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            ret = tMgr.getLine1Number();
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