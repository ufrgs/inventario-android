package br.ufrgs.cpd.inventario.models

import br.ufrgs.cpd.inventario.R

enum class TagColors(val colorName: String?, val id: Int) {
    WHITE_AND_RED("Branca com vermelho", R.color.red_and_white_tag_background),
    BLUE("Azul", R.color.blue_tag_background)
}
