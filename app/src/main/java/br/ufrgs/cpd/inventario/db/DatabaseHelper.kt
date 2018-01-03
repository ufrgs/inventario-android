package br.ufrgs.cpd.inventario.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import br.ufrgs.cpd.inventario.BuildConfig
import java.io.FileOutputStream
import java.io.IOException
import java.sql.SQLException

/**
 * Created by Theo on 29/09/17.
 */
class DatabaseHelper(private val mContext: Context) : SQLiteOpenHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION) {

    private var dataBase: SQLiteDatabase? = null


    init {

        try {
            createDataBase()
            openDataBase()
        } catch (e: IOException) {
            e.printStackTrace()
            Log.d(TAG, "DatabaseHelper: " + e.message)
        }

    }


    @Throws(IOException::class)
    fun createDataBase() {

        val dbExist = checkDataBase()

        if (dbExist) {
            val prefs = mContext.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
            if (prefs.getInt("DB_VERSION", 0) != DATABASE_VERSION) {
                mContext.deleteDatabase(DATABASE_NAME)
                val edit = prefs.edit()
                edit.putInt("DB_VERSION", DATABASE_VERSION)
                edit.commit()
                Log.d("DataBase Exist", "new db version")
                this.readableDatabase
                try {
                    copyDataBase()
                } catch (e: IOException) {
                    throw Error("Error copying Database")
                }

            }
        } else {

            this.readableDatabase

            try {

                copyDataBase()

            } catch (e: IOException) {

                throw Error("Error copying database: " + e.message)

            }

        }

    }

    private fun checkDataBase(): Boolean {

        var checkDB: SQLiteDatabase? = null

        try {
            val myPath = DB_PATH + DATABASE_NAME
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY)
            Log.d(TAG, "checkDataBase: abriu certo!")

        } catch (e: SQLiteException) {
            Log.d(TAG, "checkDataBase: " + e.message)
        }

        if (checkDB != null) {

            checkDB.close()

        }

        return if (checkDB != null) true else false
    }

    @Throws(IOException::class)
    private fun copyDataBase() {

        //Open your local db as the input stream
        val myInput = mContext.assets.open(DATABASE_NAME)

        // Path to the just created empty db
        val outFileName = DB_PATH + DATABASE_NAME

        //Open the empty db as the output stream
        val myOutput = FileOutputStream(outFileName)

        //transfer bytes from the inputfile to the outputfile
        val buffer = ByteArray(1024)
        var length: Int
        var loopControl = true
        //while ((length = myInput.read(buffer)) > 0) {
        while (loopControl) {
            length = myInput.read(buffer)
            if(length > 0)
                myOutput.write(buffer, 0, length)
            else
                loopControl = false


        }

        //Close the streams
        myOutput.flush()
        myOutput.close()
        myInput.close()

    }

    @Throws(SQLException::class)
    fun openDataBase() {
        //Open the database
        val myPath = DB_PATH + DATABASE_NAME
        dataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY)
    }

    fun getDataBase(): SQLiteDatabase? {
        if (dataBase!!.isOpen)
            return dataBase
        else
            return null
    }

    @Synchronized override fun close() {

        if (dataBase != null)
            dataBase!!.close()

        super.close()

    }

    override fun onCreate(db: SQLiteDatabase) {

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    companion object {
        private val TAG = "DatabaseHelper"

        /******************************* DB Description  */
        private val DATABASE_VERSION = 1
        val DATABASE_NAME = "db.sqlite"
        private val DB_PATH = "/data/data/br.ufrgs.cpd.inventario/"
    }
}