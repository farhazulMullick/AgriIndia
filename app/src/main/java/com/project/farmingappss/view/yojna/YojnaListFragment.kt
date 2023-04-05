package com.project.farmingappss.view.yojna

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentSnapshot
import com.project.farmingappss.R
import com.project.farmingappss.adapter.YojnaAdapter
import com.project.farmingappss.databinding.FragmentYojnaListBinding
import com.project.farmingappss.utilities.CellClickListener
import com.project.farmingappss.viewmodel.YojnaViewModel
import kotlinx.android.synthetic.main.fragment_yojna_list.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [YojnaListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class YojnaListFragment : Fragment(), CellClickListener<DocumentSnapshot> {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    val viewModel : YojnaViewModel by viewModels()
    lateinit var yojnaAdapter: YojnaAdapter

    lateinit var yojnaFragment: YojnaFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentYojnaListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentYojnaListBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.title = "Krishi Yojna"
        binding.setViewModelBindingData()
        viewModel.getAllYojna("krishi_yojnas")

    }

    private fun FragmentYojnaListBinding. setViewModelBindingData () {
        rcvYojna.apply {
            adapter = run {
                yojnaAdapter = YojnaAdapter(requireContext(), ArrayList(), this@YojnaListFragment)
                yojnaAdapter
            }
        }

        viewModel.message3.observe(viewLifecycleOwner, Observer {list ->
            if (list.isEmpty()) return@Observer
            yojnaAdapter.setData(list)
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment YojnaListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            YojnaListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    override fun onCellClickListener(data: DocumentSnapshot) {
//       yojnaFragment = YojnaFragment()
//        val bundle = Bundle()
//        yojnaFragment.setArguments(bundle)
//        activity!!.supportFragmentManager
//            .beginTransaction()
//            .replace(R.id.frame_layout, yojnaFragment, YojnaFragment::class.java.simpleName)
//            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//            .setReorderingAllowed(true)
//            .addToBackStack(YojnaFragment::class.java.simpleName)
//            .commitAllowingStateLoss()
    }

}