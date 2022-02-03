package org.techtown.feelim

import android.app.DatePickerDialog
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
//import com.example.guru2022.databinding.ActivityMainBinding
import java.util.*

class newFeelim : AppCompatActivity() {

    var dateString = ""
    var starNum = ""
    var genreS = 0
    var placeS = 0


    lateinit var edtMovieTitle: EditText
    lateinit var edtStartDate: TextView
    lateinit var edtFinishDate: TextView
    lateinit var edtGenre: Spinner
    lateinit var edtScore: RatingBar
    lateinit var edtPlace: Spinner

    lateinit var addFeelim: Button

    lateinit var myHelper: DBManager
    lateinit var sqlDB: SQLiteDatabase




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_feelim)


        edtMovieTitle = findViewById(R.id.edtMovieTitle)
        edtStartDate = findViewById(R.id.edtStartDate)
        edtFinishDate = findViewById(R.id.edtFinishDate)
        edtGenre = findViewById(R.id.edtGenre)
        edtScore = findViewById(R.id.edtScore)
        edtPlace = findViewById(R.id.edtPlace)

        addFeelim = findViewById(R.id.addFeelim)




        // DB
        myHelper = DBManager(this, "movieList", null, 1)


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
        // 스피너

        var DataG = resources.getStringArray(R.array.genreList)
        var adapterGenre = ArrayAdapter<String> (this, R.layout.spinnerlayout, DataG)

        edtGenre.adapter = adapterGenre

        edtGenre.setSelection(0)

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
        edtScore.setOnRatingBarChangeListener { _, rating, _ ->
            edtScore.rating = rating
            starNum = "${rating}"
        }


        // 장소
        // edtPlace.adapter =
        //    ArrayAdapter.createFromResource(this, R.array.placeList, R.layout.spinnerlayout)

        var DataP = resources.getStringArray(R.array.placeList)
        var adapterPlace = ArrayAdapter<String> (this, R.layout.spinnerlayout, DataP)

        edtPlace.adapter = adapterPlace

        edtPlace.setSelection(0)

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


        // 정보 DB로 이동
        addFeelim.setOnClickListener {
            sqlDB = myHelper.writableDatabase

            sqlDB.execSQL("INSERT INTO movieList VALUES ('" + edtMovieTitle.text.toString() + "','"
                    + edtStartDate.text.toString() + "','"
                    + edtFinishDate.text.toString() + "','"
                    + genreS.toInt() + "','"
                    + starNum + "','"
                    + placeS.toInt()
                    + "');") // DB에 저장 (제목, 시작날짜, 종료날짜, 장르, 평점, 장소/플랫폼)
            sqlDB.close()

            Toast.makeText(applicationContext,"저장되었습니다.", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, infoFeelim::class.java)
            intent.putExtra("intent_name", edtMovieTitle.text.toString())
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
