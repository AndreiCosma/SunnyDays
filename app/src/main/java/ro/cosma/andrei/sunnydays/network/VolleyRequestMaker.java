package ro.cosma.andrei.sunnydays.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/*Created by Cosma Andrei
* 9/17/2017*/
public class VolleyRequestMaker {
    private static VolleyRequestMaker instance;

    private RequestQueue requestQueue;

    private static Context mContext;


    private VolleyRequestMaker(Context context) {
        VolleyRequestMaker.mContext = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized VolleyRequestMaker getInstance(Context context) {
        if (null == instance) {
            instance = new VolleyRequestMaker(context);
        }
        return instance;
    }


    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
