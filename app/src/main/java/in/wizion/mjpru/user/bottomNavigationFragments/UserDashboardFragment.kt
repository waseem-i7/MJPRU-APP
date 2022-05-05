package `in`.wizion.mjpru.user.bottomNavigationFragments

import `in`.wizion.mjpru.R
import `in`.wizion.mjpru.user.UserDashboardActivities.*
import `in`.wizion.mjpru.user.UserDashboardActivities.faculty.UpdateFaculty
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_user_dashboard.*


class UserDashboardFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addNotice.setOnClickListener {
            startActivity(Intent(activity,UploadNotice::class.java))
        }

        addGalleryImage.setOnClickListener {
            startActivity(Intent(activity,UploadImage::class.java))
        }

        addEbook.setOnClickListener {
            startActivity(Intent(activity,UploadEbook::class.java))
        }

        faculty.setOnClickListener {
            startActivity(Intent(activity, UpdateFaculty::class.java))
        }

        deleteNotice.setOnClickListener {
            startActivity(Intent(activity,DeleteNotice::class.java))
        }
    }


}