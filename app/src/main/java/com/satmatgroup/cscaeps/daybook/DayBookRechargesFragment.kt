package com.satmatgroup.cscaeps.daybook

import android.graphics.Color
import android.os.Bundle
import android.provider.AlarmClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.satmatgroup.cscaeps.R
import com.satmatgroup.cscaeps.model.UserDayBookModel
import com.satmatgroup.cscaeps.model.UserModel
import com.satmatgroup.cscaeps.network_calls.AppApiCalls
import com.satmatgroup.cscaeps.utils.AppCommonMethods
import com.satmatgroup.cscaeps.utils.AppConstants
import com.satmatgroup.cscaeps.utils.AppPrefs
import kotlinx.android.synthetic.main.fragment_pie_chart_recharges.*
import kotlinx.android.synthetic.main.fragment_pie_chart_recharges.view.*
import org.eazegraph.lib.models.PieModel
import org.json.JSONObject

class DayBookRechargesFragment : Fragment(), AppApiCalls.OnAPICallCompleteListener {

    lateinit var root: View
    private val DAYBOOK: String = "DAYBOOK"
    lateinit var userModel: UserModel
    var commssionSlabModelArrayList: ArrayList<UserDayBookModel>? = null

    lateinit var userDayBook: UserDayBookModel


    companion object {
        fun newInstance(message: String): DayBookRechargesFragment {

            val f = DayBookRechargesFragment()

            val bdl = Bundle(1)

            bdl.putString(AlarmClock.EXTRA_MESSAGE, message)

            f.setArguments(bdl)

            return f

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_pie_chart_recharges, container, false)

        val gson = Gson()
        val json = AppPrefs.getStringPref("userModel", requireContext())
        userModel = gson.fromJson(json, UserModel::class.java)

        userDayBook(
            userModel.cus_id, AppPrefs.getStringPref("deviceId", requireContext()).toString(),
            AppPrefs.getStringPref("deviceName", requireContext()).toString(),
            userModel.cus_pin,
            userModel.cus_pass,
            userModel.cus_mobile, userModel.cus_type
        )
        return root

    }

    private fun userDayBook(
        cus_id: String, deviceId: String, deviceName: String, pin: String,
        pass: String, cus_mobile: String, cus_type: String
    ) {
        root.progress_bar.visibility = View.VISIBLE

        if (AppCommonMethods(requireContext()).isNetworkAvailable) {
            val mAPIcall =
                AppApiCalls(requireContext(), DAYBOOK, this)
            mAPIcall.userDayBook(
                cus_id, deviceId, deviceName, pin,
                pass, cus_mobile, cus_type
            )
        } else {

            Toast.makeText(requireContext(), "Internet Error", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onAPICallCompleteListner(item: Any?, flag: String?, result: String) {
        if (flag.equals(
                DAYBOOK
            )
        ) {
            Log.e("DAYBOOK", result)
            val jsonObject = JSONObject(result)
            val status = jsonObject.getString(AppConstants.STATUS)
            Log.e(AppConstants.STATUS, status)
            //Log.e(AppConstants.MESSAGE_CODE, messageCode);
            if (status.contains("true")) {
                root.progress_bar.visibility = View.GONE

                val cast = jsonObject.getJSONArray("result")

                for (i in 0 until cast.length()) {
                    val notifyObjJson = cast.getJSONObject(i)
                    val type = notifyObjJson.getString("type")
                    Log.e("type", type)
                    userDayBook = Gson()
                        .fromJson<UserDayBookModel>(
                            notifyObjJson.toString(),
                            UserDayBookModel::class.java
                        )

                    if (type.equals("PENDING RECHARGES")) {
//                        tvPendingRecBalance.text = "Balance :\n"₹ " + userDayBook.bal
//                        tvPendingCount.text = "Count : " + userDayBook.cnt
                        root.tvPending.text = "₹ " + userDayBook.bal
                        root.tvCountPending.text = userDayBook.cnt


                    } else if (type.equals("SUCCESS RECHARGES")) {
//                        tvSuccessRecBalance.text = "Balance :\n₹ " + userDayBook.bal
//                        tvSuccessCount.text = "Count :" + userDayBook.cnt
                        root.tvSuccess.text = "₹ " + userDayBook.bal
                        root.tvCountSuccess.text = userDayBook.cnt


                    } else if (type.equals("FAILED RECHARGES")) {
//                        tvFailedRecBalance.text = "Balance :\n₹ " + userDayBook.bal
//                        tvFailedCount.text = "Count : " + userDayBook.cnt
                        root.tvFailed.text = "₹ " + userDayBook.bal
                        root.tvCountFailed.text = userDayBook.cnt
                    }
                }
                setData()
            }
        }
    }


    private fun setData() {
        
        // Set the data and color to the pie chart
        piechart.addPieSlice(
            PieModel(
                "Success", root.tvCountSuccess.text.toString().toFloat(),
                Color.parseColor("#66BB6A")
            )
        )
        piechart.addPieSlice(
            PieModel(
                "Pending", root.tvCountPending.text.toString().toFloat(),
                Color.parseColor("#FFA726")
            )
        )
        piechart.addPieSlice(
            PieModel(
                "Failed", root.tvCountFailed.text.toString().toFloat(),
                Color.parseColor("#EF5350")
            )
        )
        // To animate the pie chart
        piechart.startAnimation()
    }

}