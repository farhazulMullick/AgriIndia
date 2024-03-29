package com.project.farmingappss.view.dashboard

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.project.farmingappss.R
import com.project.farmingappss.adapter.DashboardEcomItemAdapter
import com.project.farmingappss.model.data.WeatherRootList
import com.project.farmingappss.utilities.ACTION_TYPE
import com.project.farmingappss.utilities.CROP_DISEASE_PREDICTION_SCREEN
import com.project.farmingappss.utilities.CellClickListener
import com.project.farmingappss.view.articles.ArticleListFragment
import com.project.farmingappss.view.articles.FruitsFragment
import com.project.farmingappss.view.ecommerce.EcommerceItemFragment
import com.project.farmingappss.view.plantml.MLBaseActivity
import com.project.farmingappss.view.weather.WeatherFragment
import com.project.farmingappss.view.yojna.YojnaListFragment
import com.project.farmingappss.viewmodel.EcommViewModel
import com.project.farmingappss.viewmodel.WeatherViewModel
import kotlinx.android.synthetic.main.fragment_dashboard.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [dashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class dashboardFragment : Fragment(), CellClickListener<String> {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var weatherFragment: WeatherFragment
    lateinit var fruitsFragment: FruitsFragment
    lateinit var yojnaListFragment: YojnaListFragment
    lateinit var articleListFragment: ArticleListFragment
    private lateinit var viewModel: WeatherViewModel
    private lateinit var viewModel2: EcommViewModel
    var data: WeatherRootList? = null
    lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        viewModel = ViewModelProviders.of(requireActivity())
            .get<WeatherViewModel>(WeatherViewModel::class.java)

        viewModel2 = ViewModelProviders.of(requireActivity())
            .get<EcommViewModel>(EcommViewModel::class.java)


        viewModel2.loadAllEcommItems()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel.getCoordinates().observe(viewLifecycleOwner, Observer {
            Log.d("DashFrag", it.toString())
            viewModel.updateNewData()
            val city = it.get(2) as String
            viewModel.newDataTrial.observe(viewLifecycleOwner, Observer {

                Log.d("Observed Here", "Yes")
                weathTempTextWeathFrag.text =
                    (it.list[0].main.temp - 273).toInt().toString() + "\u2103"
                humidityTextWeathFrag.text =
                    "Humidity: " + it!!.list[0].main.humidity.toString() + " %"
                windTextWeathFrag.text = "Wind: " + it!!.list[0].wind.speed.toString() + " km/hr"
                weatherCityTitle.text = city.toString()
                var iconcode = it!!.list[0].weather[0].icon
                var iconurl = "https://openweathermap.org/img/w/" + iconcode + ".png";
                Glide.with(activity!!.applicationContext).load(iconurl)
                    .into(weathIconImageWeathFrag)
            })
        })


        viewModel2.ecommLiveData.observe(viewLifecycleOwner, Observer {
            var itemsToShow = (0..it.size - 1).shuffled().take(4) as List<Int>
            val adapterEcomm =
                DashboardEcomItemAdapter(activity!!.applicationContext, it, itemsToShow, this)
            dashboardEcommRecycler.adapter = adapterEcomm
            dashboardEcommRecycler.layoutManager =
                GridLayoutManager(activity!!.applicationContext, 2)
        })

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.title = "Agri India"

        weatherCard.setOnClickListener {
            weatherFragment = WeatherFragment()

            val transaction = activity!!.supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame_layout, weatherFragment, "name2")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .setReorderingAllowed(true)
                .addToBackStack("name")
                .commit()
            data?.let { it1 -> viewModel.messageToB(it1) }
        }

        viewModel = ViewModelProviders.of(requireActivity())
            .get<WeatherViewModel>(WeatherViewModel::class.java)

        viewModel.getMessageA()
            .observe(viewLifecycleOwner, object : Observer<WeatherRootList?> {
                override fun onChanged(t: WeatherRootList?) {
                    Log.d("DashFrag Data Changed A", "B")
                }
            })

        cat1.setOnClickListener {

            articleListFragment = ArticleListFragment()
            val transaction = activity!!.supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame_layout, articleListFragment, "articlesListFrag")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .setReorderingAllowed(true)
                .addToBackStack("articlesListFrag")
                .commit()

        }


        cat3.setOnClickListener {
            yojnaListFragment = YojnaListFragment()
            val transaction = activity!!.supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame_layout, yojnaListFragment, "yojnaListFrag")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .setReorderingAllowed(true)
                .addToBackStack("yojnaListFrag")
                .commit()
        }

        btn_crop_disease.setOnClickListener {
            startActivity(
                Intent(requireContext(), MLBaseActivity::class.java)
                    .apply { putExtra(ACTION_TYPE, CROP_DISEASE_PREDICTION_SCREEN) }
            )
        }

//        cat5.setOnClickListener {
//            articleListFragment = ArticleListFragment()
//            val transaction = activity!!.supportFragmentManager
//                .beginTransaction()
//                .replace(R.id.frame_layout, articleListFragment, "articlesListFrag")
//                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                .setReorderingAllowed(true)
//                .addToBackStack("articlesListFrag")
//                .commit()
//        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment dashboardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            dashboardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onStop() {
        super.onStop()

    }

    override fun onCellClickListener(name: String) {
        val ecommerceItemFragment = EcommerceItemFragment()

        val transaction = activity!!.supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout, ecommerceItemFragment, name)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .setReorderingAllowed(true)
            .addToBackStack("name")
            .commit()
    }
}