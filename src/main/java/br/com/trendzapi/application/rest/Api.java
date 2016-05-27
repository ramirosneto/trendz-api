package br.com.trendzapi.application.rest;

import android.content.Context;
import android.content.Intent;

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

import br.com.trendzapi.application.ScreenActivity;
import br.com.trendzapi.application.cdp.CustomJsonObjectRequest;

public class Api {
    private Context mContext;
    //private static final String prefixURL = "http://192.168.1.3/projetos/trendzapi/restserver/";
    private static final String prefixURL = "http://www.payatphone.com/api/restserver/";

    public Api(Context context) {
        this.mContext = context;
    }

    public void buscaAd(String appKey, String operator, String number) {
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        String url = prefixURL + "apianuncio/anunciorandom?app_key=" + appKey + "&operator=" + operator + "&number=" + number;

        CustomJsonObjectRequest request = new CustomJsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Integer code = Integer.parseInt((String) response.get("code"));

                            if (code == 200) {
                                JSONArray array = response.getJSONArray("status");
                                JSONObject c = array.getJSONObject(0);

                                String extensao = c.getString("an_extensao");
                                String arquivo = prefixURL + "public/upload/" + c.getString("an_arquivo");
                                String link = c.getString("an_link");

                                Intent intent = new Intent(mContext, ScreenActivity.class);
                                intent.putExtra("extensao", extensao);
                                intent.putExtra("arquivo", arquivo);
                                intent.putExtra("link", link);
                                mContext.startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        // aumentar timeout
        request.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

    public void registraAcesso(HashMap<String, String> params) {
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        String url = prefixURL + "apiacesso/cadastrar";

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
                        error.printStackTrace();
                    }
                }
        );

        // aumentar timeout
        request.setRetryPolicy(new DefaultRetryPolicy(
                40000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }
}