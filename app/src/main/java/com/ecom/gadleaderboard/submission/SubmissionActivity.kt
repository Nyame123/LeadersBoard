package com.ecom.gadleaderboard.submission

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ecom.gadleaderboard.R
import com.ecom.gadleaderboard.Utils
import com.ecom.gadleaderboard.api_request.ApiClient
import com.ecom.gadleaderboard.api_request.ApiService
import com.ecom.gadleaderboard.models.LearningLeader
import kotlinx.android.synthetic.main.activity_submission.*
import kotlinx.android.synthetic.main.fragment_learning.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubmissionActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submission)
        apiService = ApiClient.googleFormsAdapter().create(ApiService::class.java)
        val inflater = LayoutInflater.from(this)

        back_img.setOnClickListener{
            finish()
        }

        submit.setOnClickListener { v->
            val call = apiService.postResults(email_address.text.toString(),first_name.text.toString(),last_name.text.toString(),github_link.text.toString())
            call.enqueue(object : Callback<Void> {
                val view = inflater.inflate(R.layout.successful_mark,null)
                val mark = view.findViewById<ImageView>(R.id.mark)
                val message = view.findViewById<TextView>(R.id.textual)
                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {
                    if (response.isSuccessful) {
                        mark.setImageResource(R.drawable.successful)
                        message.text = getString(R.string.successful_message)
                    }else{
                        mark.setImageResource(R.drawable.error)
                        message.text = getString(R.string.error_message)
                    }

                    Utils.createAlert(this@SubmissionActivity,view)
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    mark.setImageResource(R.drawable.error)
                    message.text = getString(R.string.error_message)
                    Utils.createAlert(this@SubmissionActivity,view)
                }

            })
        }


    }
}