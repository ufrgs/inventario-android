package br.ufrgs.cpd.inventario.ui.tag

import android.content.Context
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import br.ufrgs.cpd.inventario.R
import br.ufrgs.cpd.inventario.models.TagColors
import br.ufrgs.cpd.inventario.utils.LayoutUtils
import kotlinx.android.synthetic.main.activity_tag.*
import org.jetbrains.anko.startActivity

class TagsActivity : AppCompatActivity() {

    private val color by lazy { intent.getSerializableExtra(COLOR) as TagColors }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag)
        LayoutUtils.setupToolbar(this, toolbar,"Etiqueta", false)
        tag_message.text = getString(R.string.tag, color.colorName)
        bg_color.setCardBackgroundColor(color.id)
        img_tag.setColorFilter(ContextCompat.getColor(this, color.id), android.graphics.PorterDuff.Mode.SRC_IN)

        ok_button.setOnClickListener {
            finish()
        }
    }

    companion object {
        private const val COLOR = "color"

        fun start(context: Context, color: TagColors) {
            context.startActivity<TagsActivity>(COLOR to color)
        }
    }
}