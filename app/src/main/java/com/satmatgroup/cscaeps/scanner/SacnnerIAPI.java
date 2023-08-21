package com.satmatgroup.cscaeps.scanner;

import com.satmatgroup.cscaeps.network.BaseResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface SacnnerIAPI {

    String BASE_URL="https://aeps.mewarpe.com/";


    @Multipart
    @POST("paytransfer_api.php")
    Call<ScannerResponse> saveScan(@Part("vpa") RequestBody vpa,
                                   @Part("name") RequestBody name,
                                   @Part("amount") RequestBody amount,
                                   @Part("mon_no") RequestBody mon_no);


    @Multipart
    @POST("paytransfer_api.php")
    Call<ScannerFalseResponse> saveFalseScan(@Part("vpa") RequestBody vpa,
                                             @Part("name") RequestBody name,
                                             @Part("amount") RequestBody amount,
                                             @Part("mon_no") RequestBody mon_no);
}
