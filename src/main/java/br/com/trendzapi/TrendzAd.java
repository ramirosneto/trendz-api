package br.com.trendzapi;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.location.Address;
import android.os.AsyncTask;
import android.support.multidex.MultiDex;
import android.telephony.TelephonyManager;

import java.net.URL;
import java.util.HashMap;
import java.util.List;

import br.com.trendzapi.location.GPSTracker;
import br.com.trendzapi.permission.Permissions;
import br.com.trendzapi.rest.Api;

public class TrendzAd {
    private Context mContext;
    private String mAppKey, mImei;

    public TrendzAd(Context context, String appKey) {
        mContext = context;
        mAppKey = appKey;
        MultiDex.install(mContext);

        boolean permissionReadPhoneState = Permissions.checkReadPhoneStatePermission(mContext);
        if (permissionReadPhoneState)
            mImei = getImeiDeviceId();

        new getDataTask().execute();
    }

    public void makeAd() {
        Api.buscaAd(mContext, mAppKey, mImei);
    }

    private String getImeiDeviceId() {
        String ret = "";

        try {
            TelephonyManager tMgr = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            ret = tMgr.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    private String getOperatorName() {
        String ret = "";

        try {
            TelephonyManager tMgr = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            ret = tMgr.getSimOperatorName();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    private class getDataTask extends AsyncTask<URL, Integer, Void> {
        @Override
        protected Void doInBackground(URL... arg) {
            HashMap<String, String> params = new HashMap<>();
            params.put("imei", mImei);
            params.put("operadora", getOperatorName());

            boolean permissionAccounts = Permissions.checkGetAccountsPermission(mContext);
            if (permissionAccounts) {
                Account[] accounts = AccountManager.get(mContext).getAccounts();
                for (Account account : accounts) {
                    if (account.type.equals("com.google"))
                        params.put("email_google", account.name);
                    if (account.type.equals("com.facebook.auth.login"))
                        params.put("email_facebook", account.name);
                }
            }

            boolean permissionFineLocation = Permissions.checkFineLocationPermission(mContext);
            boolean permissionCoarseLocation = Permissions.checkCoarseLocationPermission(mContext);
            if (permissionFineLocation && permissionCoarseLocation) {
                GPSTracker gpsTracker = new GPSTracker(mContext);
                List<Address> addresses = gpsTracker.getLocation();
                if(addresses != null) {
                    params.put("address", addresses.get(0).getAddressLine(0));
                    params.put("city", addresses.get(0).getLocality());
                    params.put("state", addresses.get(0).getAdminArea());
                    params.put("zip", addresses.get(0).getPostalCode());
                    params.put("country", addresses.get(0).getCountryName());
                }
            }

            Api.registraInfoAcesso(mContext, params);

            return null;
        }
    }

    /**************** ------ ********************/
    public void getInfoFacebook() {
        /*FacebookSdk.sdkInitialize(this);
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String id = object.getString("id");
                            String name = object.getString("name");
                            String email = object.getString("email");
                            String birthday = object.getString("birthday");
                            String gender = object.getString("gender");
                            String profileImg = response.getJSONObject().getJSONObject("picture").getJSONObject("data").getString("url");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday,picture.type(large)");
        request.setParameters(parameters);
        request.executeAsync();*/
    }
}