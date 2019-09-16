package br.ufrgs.cpd.inventario.ui.tutorial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.ufrgs.cpd.inventario.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_tutorial.view.*

class TutorialFragment : Fragment() {

    private val imageRes by lazy { arguments!!.getInt(KEY_IMAGE) }
    private val textRes by lazy { arguments!!.getInt(KEY_TEXT) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tutorial, container, false)

        Picasso.get().load(imageRes).into(view.intro_img)
        view.intro_tx.setText(textRes)

        return view
    }

    companion object {
        private const val KEY_IMAGE = "Image"
        private const val KEY_TEXT = "Text"

        fun newInstance(image: Int, text: Int) : TutorialFragment {
            val fragment = TutorialFragment()
            val arg = Bundle()
            arg.putInt(KEY_IMAGE, image)
            arg.putInt(KEY_TEXT, text)
            fragment.arguments = arg
            return fragment
        }
    }
}