package br.ufrgs.cpd.inventario.ui.tutorial

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import br.ufrgs.cpd.inventario.App
import br.ufrgs.cpd.inventario.R
import kotlinx.android.synthetic.main.activity_tutorial.*
import org.jetbrains.anko.defaultSharedPreferences

class TutorialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        val pagerAdapter = PagerAdapter(supportFragmentManager)
        viewpager.adapter = pagerAdapter
        indicator.setViewPager(viewpager)

        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                if (position == 3) {
                    btn_next.text = "Finalizar"
                } else {
                    btn_next.text = "Próximo"
                }
            }

        })

        btn_next.setOnClickListener {
            if (viewpager.currentItem == 3) {
                endTutorial()
            } else {
                viewpager.setCurrentItem(viewpager.currentItem+1, true)
            }
        }

        btn_tutorial.setOnClickListener {
            endTutorial()
        }
    }

    private fun endTutorial(){
        val editor = defaultSharedPreferences.edit()
        editor.putBoolean(App.KEY_TUTORIAL, false)
        editor.apply()
        finish()
    }
    class PagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {

            val image = when (position) {
                0 -> R.drawable.intro1
                1 -> R.drawable.intro2
                2 -> R.drawable.intro3
                3 -> R.drawable.intro4
                else -> R.drawable.intro1
            }

            val text = when (position) {
                0 -> R.string.intro1
                1 -> R.string.intro2
                2 -> R.string.intro3
                3 -> R.string.intro4
                else -> R.string.intro1
            }

            return TutorialFragment.newInstance(image, text)
        }

        override fun getCount(): Int = 4

    }
}