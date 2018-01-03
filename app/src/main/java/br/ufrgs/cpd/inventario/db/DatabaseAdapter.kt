package br.ufrgs.cpd.inventario.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import br.ufrgs.cpd.inventario.models.Orgao
import br.ufrgs.cpd.inventario.models.Predio

/**
 * Created by Theo on 29/09/17.
 */
class DatabaseAdapter(private val mContext: Context) {
    val dbHelper: DatabaseHelper = DatabaseHelper(mContext)
    var db: SQLiteDatabase

    init {
        db = dbHelper.getDataBase()!!
    }

    fun getAllPredios() : List<Predio> {
        if (!db.isOpen)
            db = dbHelper.getDataBase()!!

        val list = arrayListOf<Predio>()

        val selectQuery = "SELECT * from Predios"

        val c = db.rawQuery(selectQuery, null)
        if (c.moveToFirst()) {
            do {
                val predio = Predio()
                predio.CodEspacoFisico = c.getString(c.getColumnIndex("CodEspacoFisico"))
                predio.CodPredio = c.getString(c.getColumnIndex("CodPredio"))
                predio.NomePredio = c.getString(c.getColumnIndex("NomePredio"))

                list.add(predio)

            } while (c.moveToNext())
        }

        c.close()

        return list

    }

    fun getAllOrgao() : List<Orgao> {
        if (!db.isOpen)
            db = dbHelper.getDataBase()!!

        val list = arrayListOf<Orgao>()

        val selectQuery = "SELECT * from Orgaos"

        val c = db.rawQuery(selectQuery, null)
        if (c.moveToFirst()) {
            do {
                val orgao = Orgao()
                orgao.CodOrgao = c.getString(c.getColumnIndex("CodOrgao"))
                orgao.NomeOrgao = c.getString(c.getColumnIndex("NomeOrgao"))

                list.add(orgao)

            } while (c.moveToNext())
        }

        c.close()

        return list
    }

    companion object {
        private val TAG = "DatabaseAdapter"
    }


}