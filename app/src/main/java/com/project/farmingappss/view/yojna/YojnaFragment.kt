package com.project.farmingappss.view.yojna

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.project.farmingappss.R
import com.project.farmingappss.utilities.YOJNA_URL
import com.project.farmingappss.viewmodel.YojnaViewModel
import kotlinx.android.synthetic.main.fragment_yojna.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [YojnaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class YojnaFragment : Fragment() {

    private var param1: String? = null
    lateinit var yojnaViewModel: YojnaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
        val tag = this.tag.toString()
        Log.d("YojnaFragment", this.tag.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_yojna, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(url: String) =
            YojnaFragment().apply {
                arguments = Bundle().apply {
                    putString(YOJNA_URL, url)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.apply {
            val yojnaUrl = this.getString(YOJNA_URL)
            yojnaWebView.apply {
                webViewClient = WebViewClient()
                loadUrl(yojnaUrl)
            }
        }
    }
}