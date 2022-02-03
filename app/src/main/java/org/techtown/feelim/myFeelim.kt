package org.techtown.feelim

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.RatingBar
import android.widget.TextView
import androidx.annotation.Dimension
import androidx.core.content.withStyledAttributes
import org.techtown.feelim.DBManager
import org.techtown.feelim.MainActivity
import org.techtown.feelim.R

class myFeelim : AppCompatActivity() {
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase
    lateinit var layout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_feelim)

        dbManager = DBManager(this, "movieList", null, 1)
        sqlitedb = dbManager.readableDatabase


        layout = findViewById(R.id.myFeelimLL)


        var cursor: Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM movieList", null)

        var num: Int = 0
        var num2: Int = 0
        var num3: Int = 0

        while (cursor.moveToNext()) {

            var FdbMovieTitle = cursor.getString(cursor.getColumnIndex("mvTitle")).toString()
            var FdbStartDate = cursor.getString(cursor.getColumnIndex("mvStartDate")).toString()
            var FdbFinishDate = cursor.getString(cursor.getColumnIndex("mvFinishDate")).toString()
            var FdbGenre = cursor.getInt(cursor.getColumnIndex("mvGenre")).toInt()
            var FdbPlace = cursor.getInt(cursor.getColumnIndex("mvPlace")).toInt()
            var FdbScore = cursor.getString(cursor.getColumnIndex("mvScore")).toString()


            var layout_item: LinearLayout = LinearLayout(this)
            layout_item.orientation = LinearLayout.VERTICAL
            layout_item.id = num
            layout_item.setPadding(0,0,0,30)

            var layout_item_01: LinearLayout = LinearLayout(this)
            layout_item_01.orientation = LinearLayout.HORIZONTAL
            layout_item_01.id = num2
            layout_item.addView(layout_item_01)
            layout_item_01.setBackgroundColor(Color.parseColor("#333333"))

            var layout_item_02: LinearLayout = LinearLayout(this)
            layout_item_02.orientation = LinearLayout.HORIZONTAL
            layout_item_02.id = num3
            layout_item.addView(layout_item_02)
            layout_item_02.setBackgroundColor(Color.parseColor("#333333"))

            // context
            var tvMovieTitle: TextView = TextView(this)
            tvMovieTitle.text = FdbMovieTitle
            layout_item_01.addView(tvMovieTitle)
            tvMovieTitle.setTextColor(Color.parseColor("#46BC8F"))
            tvMovieTitle.setTextSize(Dimension.SP, 16F)
            tvMovieTitle.setPadding(40,20,0,0)
            tvMovieTitle.setTypeface(tvMovieTitle.typeface, Typeface.BOLD)
            num2++

            var tvGenre: TextView = TextView(this)
            var gr = resources.getStringArray(R.array.genreList)
            tvGenre.text = gr[FdbGenre]
            layout_item_01.addView(tvGenre)
            tvGenre.setTextColor(Color.parseColor("#CDCDCD"))
            tvGenre.setTextSize(Dimension.SP, 10F)
            tvGenre.setPadding(20,20,0,0)
            num2++

            //var tvScore = RatingBar(this)
            //tvScore.rating = FdbScore.toFloat()
            //layout_item_01.addView(tvScore)
            //tvScore.setPadding(20,20,0,0)
            //num2++


            var tvDate: TextView = TextView(this)
            tvDate.text = FdbStartDate + " ~ " + FdbFinishDate
            layout_item_02.addView(tvDate)
            tvDate.setTextColor(Color.WHITE)
            tvDate.setTextSize(Dimension.SP,12F)
            tvDate.setPadding(40,20,0,20)
            num3++

            // 감상평 개수 추가
            var tvReview: TextView = TextView(this)
            tvReview.text = "n개의 감상평"
            layout_item_02.addView(tvReview)
            tvReview.setTextColor(Color.WHITE)
            tvReview.setTextSize(Dimension.SP,12F)
            tvReview.setPadding(20,20,0,20)
            num3++



            layout_item.setOnClickListener {
                val intent = Intent(this, infoFeelim::class.java)
                intent.putExtra("intent_name", FdbMovieTitle)
                startActivity(intent)
            }

            layout.addView(layout_item)
            num++;

        }
        cursor.close()
        sqlitedb.close()
        dbManager.close()
    }




    //메뉴 추가
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.item1 -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.item2 -> {
                val intent = Intent(this, myFeelim::class.java)
                startActivity(intent)
                true
            }
            else ->super.onOptionsItemSelected(item)
        }
    }


}

