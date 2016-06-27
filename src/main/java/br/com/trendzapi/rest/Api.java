package br.com.trendzapi.rest;

import android.app.Activity;
import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import br.com.trendzapi.ScreenActivity;
import br.com.trendzapi.cdp.CustomJsonObjectRequest;

public class Api {
    //private static final String prefixURL = "http://192.168.1.6/projetos/trendznew/restserver/";
    private static final String prefixURL = "http://www.trendz.digital/restserver/";

    public static void buscaAd(final Context context, String appKey, String imei) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String url = prefixURL + "apianuncio/anunciorandom?app_key=" + appKey + "&imei=" + imei;

        CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Integer code = Integer.parseInt((String) response.get("code"));

                            if (code == 200) {
                                JSONArray array = response.getJSONArray("status");
                                JSONObject c = array.getJSONObject(0);

                                String extension = c.getString("an_extensao");
                                String file = prefixURL + "public/upload/" + c.getString("an_arquivo");
                                String link = c.getString("an_link");

                                ScreenActivity.launch((Activity) context, extension, file, link);
                            }
                        } catch (JSONException e) {
                            //e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //error.printStackTrace();
                    }
                }
        );

        request.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

    public static void registraInfoAcesso(Context context, HashMap<String, String> params) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String url = prefixURL + "apiinfo/cadastrar";

        CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //error.printStackTrace();
                    }
                }
        );

        requestQueue.add(request);
    }
}