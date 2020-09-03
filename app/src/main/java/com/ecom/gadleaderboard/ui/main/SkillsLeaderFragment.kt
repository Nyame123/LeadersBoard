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
import com.ecom.gadleaderboard.models.SkillsLeaders
import kotlinx.android.synthetic.main.fragment_learning.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * A placeholder fragment containing a simple view.
 */
class SkillsLeaderFragment : Fragment(), CustomRecyclerView.BindViewsListener {

    private lateinit var pageViewModel: PageViewModel
    private lateinit var apiService: ApiService
    private lateinit var skills: ArrayList<SkillsLeaders>
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
        val root = inflater.inflate(R.layout.fragment_skills, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiService = ApiClient.liveAdapter().create(ApiService::class.java)
        skills = arrayListOf()
        recyclerview.setBindViewsListener(this)
        progressBar.visibility = View.VISIBLE
        val call = apiService.fetchIQSkillsLeaders()
        call.enqueue(object : Callback<List<SkillsLeaders>> {

            override fun onResponse(
                call: Call<List<SkillsLeaders>>,
                response: Response<List<SkillsLeaders>>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        skills.addAll(response.body()!!)

                        progressBar.visibility = View.GONE
                        if (skills.isNotEmpty()) {
                            recyclerview.addModels(skills)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<SkillsLeaders>>, t: Throwable) {
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
        fun newInstance(sectionNumber: Int): SkillsLeaderFragment {
            return SkillsLeaderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    override fun bindViews(view: View?, objects: MutableList<*>?, position: Int) = Thread {
        mHandler.post {
            try {

                val learner = skills.get(position)
                var imageView = view!!.findViewById<ImageView>(R.id.image)
                var name = view.findViewById<TextView>(R.id.name)
                var subTitle = view.findViewById<TextView>(R.id.sub_title)
                name.setText(learner.name)
                subTitle.setText(getString(R.string.skills_iq,learner.score.toString(),learner.country))
                Glide.with(this).load(learner.imageUrl)
                    .into(imageView)



            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }.start()
}