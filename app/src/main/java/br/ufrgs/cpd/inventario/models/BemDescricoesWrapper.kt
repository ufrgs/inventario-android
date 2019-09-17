package br.ufrgs.cpd.inventario.models

import java.io.Serializable

class BemDescricoesWrapper(val bem : List<BemDescricao>)
class BemDescricao(val CodigoDescricao: String,
                   val DescricaoPadronizada: String) : Serializable