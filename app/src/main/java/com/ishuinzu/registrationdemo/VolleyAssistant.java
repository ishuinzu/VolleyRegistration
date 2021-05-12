package com.ishuinzu.registrationdemo;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyAssistant {
    private static VolleyAssistant volleyAssistant;
    private RequestQueue requestQueue;
    private final Context context;

    public VolleyAssistant(Context context) {
        this.context = context;
        this.requestQueue = getRequestQueue();
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }

    public static synchronized VolleyAssistant getInstance(Context context) {
        if (volleyAssistant == null) {
            volleyAssistant = new VolleyAssistant(context);
        }
        return volleyAssistant;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }
}