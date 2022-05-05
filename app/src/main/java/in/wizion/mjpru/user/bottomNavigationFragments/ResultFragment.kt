package `in`.wizion.mjpru.user.bottomNavigationFragments

import `in`.wizion.mjpru.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.fragment_result.*


class ResultFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val webSetting=webView.settings
        webSetting.javaScriptEnabled=true
        webView.loadUrl("https://ndl.iitkgp.ac.in./")
        webView.webViewClient= WebViewClient()
    }

}
