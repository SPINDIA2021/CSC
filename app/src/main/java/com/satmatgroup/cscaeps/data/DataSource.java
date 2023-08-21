package com.satmatgroup.cscaeps.data;




import com.satmatgroup.cscaeps.network.NetworkCall;
import com.satmatgroup.cscaeps.network.ServiceCallBack;

import okhttp3.RequestBody;
import retrofit2.http.Part;


public interface DataSource {

    void getCategory(ServiceCallBack myAppointmentPresenter, NetworkCall networkCall);

    void saveRetry(String txnid, ServiceCallBack myAppointmentPresenter, NetworkCall networkCall);

}

