package `in`.wizion.mjpru.helperClasses

import `in`.wizion.mjpru.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter

class SliderAdapter(private val context : Context) : PagerAdapter() {

    lateinit var layoutInflater : LayoutInflater

    val images : Array<Int> = arrayOf(R.drawable.splash_screen,
                         R.drawable.splash_screen,
                         R.drawable.splash_screen,
                         R.drawable.splash_screen)

    private val headings : Array<Int> = arrayOf(R.string.first_slide_title,
                                       R.string.second_slide_title,
                                       R.string.third_slide_title,
                                       R.string.fourth_slide_title)

    val descriptions : Array<Int> = arrayOf(R.string.first_slide_desc,
                                            R.string.second_slide_desc,
                                            R.string.third_slide_desc,
                                            R.string.fourth_slide_desc)
    /**
     * Return the number of views available.
     */
    override fun getCount(): Int {
        return headings.size
    }

    /**
     * Determines whether a page View is associated with a specific key object
     * as returned by [.instantiateItem]. This method is
     * required for a PagerAdapter to function properly.
     *
     * @param view Page View to check for association with `object`
     * @param object Object to check for association with `view`
     * @return true if `view` is associated with the key object `object`
     */
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object` as ConstraintLayout
    }

    /**
     * Create the page for the given position.  The adapter is responsible
     * for adding the view to the container given here, although it only
     * must ensure this is done by the time it returns from
     * [.finishUpdate].
     *
     * @param container The containing View in which the page will be shown.
     * @param position The page position to be instantiated.
     * @return Returns an Object representing the new page.  This does not
     * need to be a View, but can be some other container of the page.
     */
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater= context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view : View = layoutInflater.inflate(R.layout.slides_layout,container,false)

        var imageView :ImageView = view.findViewById(R.id.slider_image)
        var heading : TextView = view.findViewById(R.id.slider_heading)
        var desc : TextView = view.findViewById(R.id.slider_dec)

        imageView.setImageResource(images[position])
        heading.setText(headings[position])
        desc.setText(descriptions[position])

        container.addView(view)
        return view
    }

    /**
     * Remove a page for the given position.  The adapter is responsible
     * for removing the view from its container, although it only must ensure
     * this is done by the time it returns from [.finishUpdate].
     *
     * @param container The containing View from which the page will be removed.
     * @param position The page position to be removed.
     * @param object The same object that was returned by
     * [.instantiateItem].
     */
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }
}












