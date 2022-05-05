package `in`.wizion.mjpru.common

import `in`.wizion.mjpru.R
import `in`.wizion.mjpru.helperClasses.SliderAdapter
import `in`.wizion.mjpru.homeDashboard.UserDashboard
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager

class OnBoarding : AppCompatActivity(), ViewPager.OnPageChangeListener {

    //Variables
    private lateinit var viewPager: ViewPager
    private lateinit var dotsLayout: LinearLayout
    private var dots = arrayOf<TextView>()
    private lateinit var letsGetStarted: Button
    private lateinit var animation : Animation
    private lateinit var sliderAdapter: SliderAdapter
    private var currentPosition : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)

        //remove status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        //Hooks
        viewPager = findViewById(R.id.slider)
        dotsLayout = findViewById(R.id.dotsLayout)
        letsGetStarted = findViewById(R.id.get_started_btn)


        //call adapter
        sliderAdapter = SliderAdapter(this)
        viewPager.adapter = sliderAdapter

        //Dots
        addDots(0)
        viewPager.addOnPageChangeListener(this)
    }

    public fun skip(view : View){
       startActivity(Intent(this,UserDashboard::class.java))
       finish()
    }

    public fun next(view : View){
        viewPager.currentItem = currentPosition+1
    }

    public fun letsGetStarted(view : View){
        startActivity(Intent(this,UserDashboard::class.java))
        finish()
    }

    private fun addDots(position: Int) {
        dots = arrayOf(TextView(this), TextView(this), TextView(this), TextView(this))
        dotsLayout.removeAllViews()
        for (i in dots.indices) {
            dots[i] = TextView(this)
            dots[i].text = Html.fromHtml("&#8226")
            dots[i].textSize = 35F

            dotsLayout.addView(dots[i])
        }

        if (dots.isNotEmpty()) {
            dots[position].setTextColor(resources.getColor(R.color.purple_700))
        }
    }

    /**
     * This method will be invoked when the current page is scrolled, either as part
     * of a programmatically initiated smooth scroll or a homeDashboard initiated touch scroll.
     *
     * @param position Position index of the first page currently being displayed.
     * Page position+1 will be visible if positionOffset is nonzero.
     * @param positionOffset Value from [0, 1) indicating the offset from the page at position.
     * @param positionOffsetPixels Value in pixels indicating the offset from position.
     */
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    /**
     * This method will be invoked when a new page becomes selected. Animation is not
     * necessarily complete.
     *
     * @param position Position index of the new selected page.
     */
    override fun onPageSelected(position: Int) {
        addDots(position)
        currentPosition=position
        if (position == 0) {
            letsGetStarted.visibility= View.INVISIBLE
        } else if (position == 1) {
            letsGetStarted.visibility= View.INVISIBLE
        } else if (position == 2) {
            letsGetStarted.visibility= View.INVISIBLE
        } else {
            animation = AnimationUtils.loadAnimation(this,R.anim.bottom_anim)
            letsGetStarted.animation=animation
            letsGetStarted.visibility= View.VISIBLE
        }
    }

    /**
     * Called when the scroll state changes. Useful for discovering when the homeDashboard
     * begins dragging, when the pager is automatically settling to the current page,
     * or when it is fully stopped/idle.
     *
     * @param state The new scroll state.
     * @see ViewPager.SCROLL_STATE_IDLE
     *
     * @see ViewPager.SCROLL_STATE_DRAGGING
     *
     * @see ViewPager.SCROLL_STATE_SETTLING
     */
    override fun onPageScrollStateChanged(state: Int) {

    }


}










