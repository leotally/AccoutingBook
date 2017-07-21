package com.example.lcc.acountingbooks;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import java.util.Calendar;


public class calendar_main extends AppCompatActivity implements CalendarView.OnDateChangeListener, View.OnClickListener {

    CalendarView cal;
    TextView show,showdate;
    Button add,exit;
    int setyear,setmonth,setday;
    MySQLiteOpenHelper helper;
    String shownote;
    private Calendar calendar = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_main);
        helper = MySQLiteOpenHelper.getInstance(this);
        getSupportActionBar().hide();

        //預設先讀取資料
        cal = (CalendarView) this.findViewById(R.id.cal);
        show = (TextView) this.findViewById(R.id.show);
        add = (Button) this.findViewById(R.id.add);
        exit = (Button) this.findViewById(R.id.exit);
        showdate = (TextView) this.findViewById(R.id.showdate);

        //元件連結
        setyear = calendar.get(Calendar.YEAR);
        setmonth = calendar.get(Calendar.MONTH)+1;
        setday = calendar.get(Calendar.DAY_OF_MONTH);

        //獲取今天的日期
        long date = calendar.getTimeInMillis();
        //將月曆指標永遠指在今天

        cal.setDate(date,true,true);
        cal.setOnDateChangeListener(this);
        add.setOnClickListener(this);
        exit.setOnClickListener(this);

        shownote = helper.select(helper,setyear,setmonth,setday);
        showdate.setText(setyear+"年"+setmonth+"月"+setday+"日");
        show.setText(shownote);
        //設定顯示判別
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        shownote = helper.select(helper,setyear,setmonth,setday);
        showdate.setText(setyear+"年"+setmonth+"月"+setday+"日");
        show.setText(shownote);
    }
    //當建立或更新完時返回頁面後更新顯示資訊

    @Override
    public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {

        setyear = year;
        setmonth = month+1;
        setday = day;

        shownote = helper.select(helper,setyear,setmonth,setday);
        showdate.setText(setyear+"年"+setmonth+"月"+setday+"日");
        show.setText(shownote);
        //顯示查詢結果，如果沒有資料則顯示"目前尚未記事"字樣
        helper.close();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add:
                if (shownote.equals("目前尚無資料")){
                    shownote ="";
                }

                Intent it = new Intent(this,Add_New_NoteActivity.class);
                it.putExtra("年",setyear);
                it.putExtra("月",setmonth);
                it.putExtra("日",setday);
                it.putExtra("記事",shownote);
                startActivity(it);

                break;
            case R.id.exit:
                finish();
                break;
        }
    }
}
