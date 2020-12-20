package com.example.lab11

import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var dbrw: SQLiteDatabase
    private var items:ArrayList<String> = ArrayList()
    lateinit var adapter:ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbrw = MyDBHelper(this).writableDatabase
        adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,items)
        listView.adapter = adapter

        btn_query.setOnClickListener {            // list button
            val c = dbrw.rawQuery(if(ed_book.length()<1)"SELECT * FROM  myTable" else "SELECT * FROM myTable WHERE book LIKE '${ed_book.text}'",null)

            c.moveToFirst()
            items.clear()
            Toast.makeText(this,"共有${c.count}筆資料",Toast.LENGTH_LONG).show()
            for(i in 0 until c.count){
                items.add("書名:${c.getString(0)}\t\t\t\t 價格:${c.getString(1)}")
                c.moveToNext()
            }
            adapter.notifyDataSetChanged()
            c.close()
        }

        btn_insert.setOnClickListener {          // add button
         if(ed_book.length()<1 || ed_price.length()<1){
             Toast.makeText(this,"欄位請勿留空",Toast.LENGTH_LONG).show()
         }else{
             try {
                 dbrw.execSQL("INSERT INTO myTable(book,price) VALUES(?,?)", arrayOf<Any?>(ed_book.text.toString(),ed_price.text.toString()))
                 Toast.makeText(this,"新增書名${ed_book.text} 價格${ed_price.text}",Toast.LENGTH_SHORT).show()

                 ed_price.setText("")
                 ed_book.setText("")

             }catch (e:Exception){
                 Toast.makeText(this,"新增失敗:$e",Toast.LENGTH_LONG).show()
             }
         }
        }

        btn_update.setOnClickListener {          //modify button
            if(ed_book.length()<1 || ed_price.length()<1){
                Toast.makeText(this,"欄位請勿留空",Toast.LENGTH_LONG).show()
            }else{
                try {
                    dbrw.execSQL("UPDATE myTable SET price=${ed_price.text} WHERE book LIKE'${ed_book.text}'")

                    Toast.makeText(this,"更新書名${ed_book.text}",Toast.LENGTH_SHORT).show()

                    ed_price.setText("")
                    ed_book.setText("")
                }catch (e:Exception){
                    Toast.makeText(this,"更新失敗:$e",Toast.LENGTH_LONG).show()
                }
            }
        }

        btn_delete.setOnClickListener {         //delete button
            if(ed_book.length()<1){
                Toast.makeText(this,"欄位請勿留空",Toast.LENGTH_LONG).show()
            }else{
                try {
                        Toast.makeText(this,"刪除書名${ed_book.text}",Toast.LENGTH_SHORT).show()
                        dbrw.execSQL("DELETE FROM myTable WHERE book LIKE '${ed_book.text}' ")

                        ed_price.setText("")
                        ed_book.setText("")
                    }catch (e:Exception){
                        Toast.makeText(this,"刪除失敗:$e",Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        dbrw.close()
    }
}
