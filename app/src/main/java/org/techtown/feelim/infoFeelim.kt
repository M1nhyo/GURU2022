package org.techtown.feelim

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.core.widget.doBeforeTextChanged
import org.techtown.feelim.DBManager
import org.techtown.feelim.MainActivity
import org.techtown.feelim.R
import java.util.*

class infoFeelim : AppCompatActivity() {

    var dateString = ""
    var starNum = ""
    var genreS = 0
    var placeS = 0

    lateinit var removeFeelim: TextView
    lateinit var addFeelim: Button
    lateinit var detail_mv: TextView
    lateinit var myfeelimTitle: TextView

    lateinit var edtMovieTitle: EditText
    lateinit var edtStartDate: TextView
    lateinit var edtFinishDate: TextView
    lateinit var edtGenre: Spinner
    lateinit var edtScore: RatingBar
    lateinit var edtPlace: Spinner
   // lateinit var textview1:TextView
    //lateinit var editText1:EditText
    lateinit var Review:TextView

    lateinit var FdbMemo:String
    lateinit var FdbMovieTitle: String
    lateinit var FdbStartDate: String
    lateinit var FdbFinishDate: String
    var FdbGenre: Int = 0
    lateinit var FdbScore: String
    var FdbPlace: Int = 0

    lateinit var myHelper: DBManager
    lateinit var sqlDB: SQLiteDatabase



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_feelim)

        addFeelim = findViewById(R.id.addFeelim)
        removeFeelim = findViewById(R.id.removeFeelim)
        detail_mv = findViewById(R.id.detail_mv)
        myfeelimTitle = findViewById(R.id.myfeelimTitle)
        Review=findViewById(R.id.Review)

        edtMovieTitle = findViewById(R.id.edtMovieTitle)
        edtStartDate = findViewById(R.id.edtStartDate)
        edtFinishDate = findViewById(R.id.edtFinishDate)
        edtGenre = findViewById(R.id.edtGenre)
        edtScore = findViewById(R.id.edtScore)
        edtPlace = findViewById(R.id.edtPlace)

        val intent = intent
        FdbMovieTitle = intent.getStringExtra("intent_name").toString()

        // DB
        myHelper = DBManager(this, "movieList", null, 1)
        sqlDB = myHelper.readableDatabase

        var cursor: Cursor
        cursor = sqlDB.rawQuery("SELECT * FROM movieList WHERE mvTitle = '" + FdbMovieTitle + "';", null)

        if (cursor.moveToNext()) {
            FdbStartDate = cursor.getString(cursor.getColumnIndex("mvStartDate")).toString()
            FdbFinishDate = cursor.getString(cursor.getColumnIndex("mvFinishDate")).toString()
            FdbGenre = cursor.getInt(cursor.getColumnIndex("mvGenre")).toInt()
            FdbPlace = cursor.getInt(cursor.getColumnIndex("mvPlace")).toInt()
            FdbScore = cursor.getString(cursor.getColumnIndex("mvScore")).toString()
            FdbMemo = cursor.getString(cursor.getColumnIndex("myMemo")).toString()
        }

        cursor.close()
        sqlDB.close()
        myHelper.close()

        edtMovieTitle.setText(FdbMovieTitle)
        edtStartDate.text = FdbStartDate
        edtFinishDate.text = FdbFinishDate
        Review.text=FdbMemo



        // 시작 날짜
        edtStartDate.setOnClickListener {
            val cal = Calendar.getInstance()    //캘린더뷰 만들기
            val dateSetListener =
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    dateString = "${year}.${month + 1}.${dayOfMonth}"
                    edtStartDate.text = dateString
                }
            DatePickerDialog(
                this,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // 종료 날짜
        edtFinishDate.setOnClickListener {
            val cal = Calendar.getInstance()    //캘린더뷰 만들기
            val dateSetListener =
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    dateString = "${year}.${month + 1}.${dayOfMonth}"
                    edtFinishDate.text = dateString
                }
            DatePickerDialog(
                this,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // 장르
        var DataG = resources.getStringArray(R.array.genreList)
        var adapterGenre = ArrayAdapter<String> (this, R.layout.spinnerlayout, DataG)
        edtGenre.adapter = adapterGenre

        edtGenre.setSelection(FdbGenre)

        edtGenre.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                genreS = position
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        // 별점
        edtScore.rating = FdbScore.toFloat()
        starNum = "${edtScore.rating}"

        edtScore.setOnRatingBarChangeListener { _, rating, _ ->
            edtScore.rating = rating
            starNum = "${rating}"
        }

        // 장소

        var DataP = resources.getStringArray(R.array.placeList)
        var adapterPlace = ArrayAdapter<String> (this, R.layout.spinnerlayout, DataP)

        edtPlace.adapter = adapterPlace

        edtPlace.setSelection(FdbPlace)

        edtPlace.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                placeS = position
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }



        // 수정 버튼 설정
        addFeelim.setOnClickListener {
            sqlDB = myHelper.writableDatabase

            sqlDB.execSQL("DELETE FROM movieList WHERE mvTitle = '" + FdbMovieTitle + "';")
            sqlDB.execSQL("INSERT INTO movieList VALUES ('" + edtMovieTitle.text.toString() + "','"
                    + edtStartDate.text.toString() + "','"
                    + edtFinishDate.text.toString() + "','"
                    + genreS.toInt() + "','"
                    + starNum + "','"
                    + placeS.toInt() + "','"
                    + ""
                    + "');") // DB에 저장 (제목, 시작날짜, 종료날짜, 장르, 평점, 장소/플랫폼)
            sqlDB.close()

            FdbMovieTitle = edtMovieTitle.text.toString()

            Toast.makeText(applicationContext,"수정되었습니다.", Toast.LENGTH_SHORT).show()
        }

        // 삭제 버튼 설정
        removeFeelim.setOnClickListener {
            sqlDB = myHelper.writableDatabase

            sqlDB.execSQL("DELETE FROM movieList WHERE mvTitle = '" + FdbMovieTitle + "';")

            sqlDB.close()

            Toast.makeText(applicationContext,"삭제되었습니다.", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, myFeelim::class.java)
            startActivity(intent)
        }


        // 영화정보 더보기
        detail_mv.setOnClickListener {
            val intent = Intent(this, moreInfo::class.java)
            intent.putExtra("intent_name", edtMovieTitle.text.toString())
            startActivity(intent)
        }

        // myFeelim으로 이동
        myfeelimTitle.setOnClickListener {
            val intent = Intent(this, myFeelim::class.java)
            startActivity(intent)
        }
    }

    //메뉴 추가
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar items
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

