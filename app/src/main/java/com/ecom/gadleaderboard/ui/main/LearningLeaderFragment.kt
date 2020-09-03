package com.ecom.gadleaderboard.ui.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.ecom.gadleaderboard.R
import com.ecom.gadleaderboard.api_request.ApiClient
import com.ecom.gadleaderboard.api_request.ApiService
import com.ecom.gadleaderboard.custom_views.CustomRecyclerView
import com.ecom.gadleaderboard.models.LearningLeader
import kotlinx.android.synthetic.main.fragment_learning.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * A placeholder fragment containing a simple view.
 */
class LearningLeaderFragment : Fragment(), CustomRecyclerView.BindViewsListener {

    private lateinit var pageViewModel: PageViewModel
    private lateinit var apiService: ApiService
    private lateinit var leaners: ArrayList<LearningLeader>
    private var mHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_learning, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiService = ApiClient.liveAdapter().create(ApiService::class.java)
        leaners = arrayListOf()
        recyclerview.setBindViewsListener(this)
        progressBar.visibility = View.VISIBLE
        val call = apiService.fetchLearningLeaders()
        call.enqueue(object : Callback<List<LearningLeader>> {

            override fun onResponse(
                call: Call<List<LearningLeader>>,
                response: Response<List<LearningLeader>>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        leaners.addAll(response.body()!!)

                        progressBar.visibility = View.GONE
                        if (leaners.isNotEmpty()) {
                            recyclerview.addModels(leaners)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<LearningLeader>>, t: Throwable) {
                progressBar.visibility = View.GONE
            }

        })


    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): LearningLeaderFragment {
            return LearningLeaderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    override fun bindViews(view: View?, objects: MutableList<*>?, position: Int) = Thread {
        mHandler.post {
            try {

                val learner = leaners.get(position)
                var imageView = view!!.findViewById<ImageView>(R.id.image)
                var name = view.findViewById<TextView>(R.id.name)
                var subTitle = view.findViewById<TextView>(R.id.sub_title)
                name.setText(learner.name)
                subTitle.setText(getString(R.string._234_learning_hours,learner.hours.toString(),learner.country))
                Glide.with(this).load(learner.imageUrl)
                    .into(imageView)



            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }.start()
}