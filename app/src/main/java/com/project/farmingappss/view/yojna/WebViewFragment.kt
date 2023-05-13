package com.project.farmingappss.view.yojna

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.project.farmingappss.R
import com.project.farmingappss.utilities.URL
import com.project.farmingappss.viewmodel.YojnaViewModel
import kotlinx.android.synthetic.main.fragment_yojna.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WebViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WebViewFragment : Fragment() {

    private var param1: String? = null
    lateinit var yojnaViewModel: YojnaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(WebViewFragment::class.java.name, this.tag.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_yojna, container, false)
    }

    companion object {

        @JvmStatic
        fun newInstance(bundle: Bundle) =
            WebViewFragment().apply {
                arguments = bundle
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.apply {
            val url = this.getString(URL)
            yojnaWebView.apply {
                webViewClient = WebViewClient()
                loadUrl(url)
            }
        }
    }
}