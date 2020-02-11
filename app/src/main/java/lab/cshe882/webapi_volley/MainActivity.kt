package lab.cshe882.webapi_volley

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    val url = "http://mdevws.csh.org.tw/WebAPI/HumanResource/HumanResource/CheckUserLoginInformation"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.text)

        send_request.setOnClickListener {


            val params = HashMap<String,String>()
            params["qrCodeStr"] = "CII6I0000AA4I4AE4C26G4400AEE004EGIGG26600AEE004EIGGC6GG"
            params["DeviceType"] = "1"
            params["uuid"] = "f69c2363-46f0-412c-8a2b-ec0ac456febc"
            params["token"] = "03937e6e2d94dd7feabb745b97f7283d31d0c7841214c52437bf4eb41c18c49b"
            val jsonObject = JSONObject(params as Map<String, String>)

            val request = JsonObjectRequest(Request.Method.POST, url, jsonObject,
                Response.Listener { response ->

                    var EmpResult = Gson().fromJson(response.toString(), EmployeeResult::class.java)
                    val result = EmpResult.resultCode

                    for (name in EmpResult.resultMsg.withIndex()) {
                        Log.e("ResultName:==>", name.value.EmpName)
                    }

                    var strResp = response.toString();
                    val jsonObj: JSONObject = JSONObject(strResp)

                    val jsonArray: JSONArray = jsonObj.getJSONArray("resultMsg")

                    val empJson = jsonArray.getJSONObject(0)
                    val jsonstr = empJson.toString()
                    val empNo = empJson.get("EmpNo").toString()

                    var jsonStr = Gson().fromJson(jsonstr, Employee::class.java)

                    textView.text = jsonStr.EmpName


                    //textView.text = "Response is: ${response}"
                    Log.e("Result:" ,"Response is: ${response}")

                },Response.ErrorListener { error ->
                    textView.text = error.toString()
                    Log.e("Error", error.toString())
                })

            request.retryPolicy = DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,0,1f
            )

            VolleySingleton.getInstance(this).addToRequestQueue(request)


//            //Use StringRequest
//            val que = Volley.newRequestQueue(this)
//            val stringRequest = StringRequest(Request.Method.POST, url,
//                Response.Listener<String> { response ->
//
//
//                    var strResp = response.toString();
//                    val jsonObj: JSONObject = JSONObject(strResp)
//                    val jsonArray: JSONArray = jsonObj.getJSONArray("resultMsg")
//
//                textView.text = "Response is: ${response}"
//                Log.e("Result:" ,"Response is: ${response}")
//            },
//                Response.ErrorListener {error ->
//                textView.text = error.toString()
//                    Log.e("Error", error.toString())
//            })
//            que.add(stringRequest)

//            fun getParams(): Map<String, String> {
//                val params: MutableMap<String, String> = hashMapOf()
//                params["qrCodeStr"] = "CII6I0000AA4I4AE4C26G4400AEE004EGIGG26600AEE004EIGGC6GG"
//                params["DeviceType"] = "1"
//                params["uuid"] = "f69c2363-46f0-412c-8a2b-ec0ac456febc"
//                params["token"] = "03937e6e2d94dd7feabb745b97f7283d31d0c7841214c52437bf4eb41c18c49b"
//                return params
//            }
        }

    }
}

class VolleySingleton constructor(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: VolleySingleton? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: VolleySingleton(context).also {
                    INSTANCE = it
                }
            }
    }
    private val requestQueue: RequestQueue by lazy {
        // applicationContext is key, it keeps you from leaking the
        // Activity or BroadcastReceiver if someone passes one in.
        Volley.newRequestQueue(context.applicationContext)
    }
    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }
}

data class EmployeeResult (
    val resultCode: Int,
    val resultMsg: Array<Employee>
)

data class Employee (
    val EmpNo: Int,
    val EmpCode: String,
    val EmpName: String,
    val EngName: String,
    val IdNo: String
)