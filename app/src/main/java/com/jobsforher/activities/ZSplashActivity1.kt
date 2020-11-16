//package women.jobs.jobsforher.activities
//
//import android.app.Application
//import android.content.Context
//import android.os.Bundle
//
//import android.support.v4.view.ViewPager
//import android.support.v7.app.AppCompatActivity
//import android.view.View
//import android.widget.Button
//import android.widget.LinearLayout
//import android.widget.TextView
//import kotlinx.android.synthetic.main.zsplasha_activity1.*
//import women.jobs.jobsforher.R
//import android.content.Intent
//import android.support.v4.content.ContextCompat.getSystemService
//import android.support.v4.view.PagerAdapter
//import android.view.LayoutInflater
//import android.view.ViewGroup
//
//
//class ZSplashActivity1 : AppCompatActivity() {
//
//    var preferenceManager: PreferenceManager? = null
//    var Layout_bars: LinearLayout? = null
//    var bottomBars: Array<TextView>? = null
//    var screens: IntArray? = null
//    var Skip: Button? = null
//    var Next:Button? = null
//    var vp: ViewPager? = null
//    var myvpAdapter: MyViewPagerAdapter? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.zsplasha_activity1)
////        vp = (ViewPager) findViewById(R.id.view_pager);
////        Layout_bars = (LinearLayout) findViewById(R.id.layoutBars);
////        Skip = (Button) findViewById(R.id.skip);
////        Next = (Button) findViewById(R.id.next);
//        screens = intArrayOf(R.layout.intro_screen_one, R.layout.intro_screen_two, R.layout.intro_screen_three, R.layout.intro_screen_four,
//            R.layout.intro_screen_five, R.layout.intro_screen_six)
//        myvpAdapter = MyViewPagerAdapter(screens!!)
//        view_pager.setAdapter(myvpAdapter)
//
//        preferenceManager = PreferenceManager()
//        view_pager.addOnPageChangeListener(viewPagerPageChangeListener)
////        if (!preferenceManager!!.FirstLaunch()) {
////            launchMain()
////            finish()
////        }
//
//    }
//
//
//    fun next(v: View) {
//        val i = getItem(+1)
//        if (i < screens!!.size) {
//            view_pager.setCurrentItem(i)
//        } else {
////            launchMain()
//        }
//    }
//
//    fun skip(view: View) {
////        launchMain()
//    }
//
////    private fun ColoredBars(thisScreen: Int) {
////        val colorsInactive = resources.getIntArray(R.array.dot_on_page_not_active)
////        val colorsActive = resources.getIntArray(R.array.dot_on_page_active)
////        bottomBars = arrayOfNulls(screens.length)
////
////        Layout_bars.removeAllViews()
////        for (i in 0 until bottomBars.length) {
////            bottomBars[i] = TextView(this)
////            bottomBars[i].setTextSize(100)
////            bottomBars[i].setText(Html.fromHtml("¯"))
////            Layout_bars.addView(bottomBars[i])
////            bottomBars[i].setTextColor(colorsInactive[thisScreen])
////        }
////        if (bottomBars.length > 0)
////            bottomBars[thisScreen].setTextColor(colorsActive[thisScreen])
////    }
//
//    private fun getItem(i: Int): Int {
//        return view_pager.getCurrentItem() + i
//    }
//
//    private fun launchMain() {
//        preferenceManager!!.setFirstTimeLaunch(false)
//        startActivity(Intent(applicationContext, LoginActivity::class.java))
//        finish()
//    }
//
//    var viewPagerPageChangeListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {
//
//        override fun onPageSelected(position: Int) {
////            ColoredBars(position)
//            if (position == screens!!.size - 1) {
//                next.setText("start")
//                skip.setVisibility(View.GONE)
//            } else {
//                next.setText(getString(R.string.next))
//                skip.setVisibility(View.VISIBLE)
//            }
//        }
//
//        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {
//
//        }
//
//        override fun onPageScrollStateChanged(arg0: Int) {
//
//        }
//    }
//
//    public class MyViewPagerAdapter(val screens: IntArray) : PagerAdapter() {
//
//
//        private var inflater: LayoutInflater? = null
//
//        override fun instantiateItem(container: ViewGroup, position: Int): Any {
//           // inflater = ZSplashActivity1!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//            val view = inflater?.inflate(screens[position], container, false)
//            container.addView(view)
//            return view!!
//        }
//
//        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//            val v = `object` as View
//            container.removeView(v)
//        }
//
//        override fun isViewFromObject(v: View, `object`: Any): Boolean {
//            return v === `object`
//        }
//        override fun getCount(): Int {
//
//            return screens.size;
//        }
//    }
//}