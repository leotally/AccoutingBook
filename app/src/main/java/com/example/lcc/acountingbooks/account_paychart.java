package com.example.lcc.acountingbooks;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class account_paychart extends Activity {

    int[] money; //= {12000,20000,30450,40523,4563,78951};//各項的總計數字
    String[] item; //= {"飲食","服飾","房租","交通","娛樂","其它"};//getResources().getStringArray(R.array.expense_item);//項目名稱
    ArrayList<Integer> color = new ArrayList<>();//調色盤
    ConstraintLayout layout;
    Toast toast;
    int total;//顯示總金額
    MySQLiteOpenHelper helper = MySQLiteOpenHelper.getInstance(this);
    static final String TB_NAME = "AccountNote";
    Cursor cur;
    TextView txvdate;//標示當前圖表的資料日期
    Calendar calendar = Calendar.getInstance();
    int year,month,day;//日期用


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_pay_chart);
        txvdate = (TextView)findViewById(R.id.txvdate);
        // layout = (ConstraintLayout)findViewById(R.id.mainLayout);

        toast = Toast.makeText(this,"",Toast.LENGTH_SHORT);
        getDate();setChart();//設定圖表物件

    }
    public void setQuery(View v){//查詢某月的支出圖表資料
        //擇擇查詢月份
        new DatePickerDialog(this ,dateSetListener ,
                calendar.get(Calendar.YEAR),             //從calendar物件取得目前的年
                calendar.get(Calendar.MONTH),           //從calendar物件取得目前的月
                calendar.get(Calendar.DAY_OF_MONTH))   //從calendar物件取得目前的日
                .show();


    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            //選中的日期交給CUR做搜尋參數
            cur = helper.getReadableDatabase().query(true,TB_NAME,
                    new String[]{"item"},"year = ? and month = ? and income=?",
                    new String[]{""+year,""+(month+1),"支出"},null,null,null,null);
            item = new String[cur.getCount()];//當月找到的項目數量代入ITEM
            cur.moveToFirst();
            for(int i=0;i<cur.getCount();i++) {
                item[i] = cur.getString(0);
                cur.moveToNext();
            }

            money = new int [item.length];//查詢到的項目筆數和金額筆數同步
            for(int j=0;j<item.length;j++){
                cur = helper.getReadableDatabase().query(TB_NAME,
                        new String[]{"SUM(money)"},"year = ? and month = ? and  item=?",
                        new String[]{""+year,""+(month+1),item[j]},"item",null,null);
                cur.moveToFirst();
                money[j]=cur.getInt(0);
            }
            txvdate.setText(month+1+"月");
            setChart();//重新設置圖表
        }
    };


    private void getDate(){
        cur = helper.getReadableDatabase().query(true,TB_NAME,new String[]{"item"},
                "income = ?",new String[]{"支出"},null,null,null,null);
        item = new String[cur.getCount()];//使用CUR設定陣列的大小
        cur.moveToFirst();//游標移至第一筆後一一加入自定ITEM陣列
        for(int i=0;i<cur.getCount();i++){
            item[i]=cur.getString(0);
            cur.moveToNext();
        }
        txvdate.setText("");
        money = new int[item.length];//設定金額陣列數量與項目數量同步
        for(int j=0;j<item.length;j++){
            cur = helper.getReadableDatabase().query(
                    TB_NAME,new String[]{"SUM(money)"},"item=?",new String[]{item[j]},"item",null,null);
            cur.moveToFirst();
            money[j]= cur.getInt(0);
        }
    }

    private void setChart() {
        total = 0 ;
        for(int i=0;i<money.length;i++){
            total += money[i];}
        List<PieEntry> pieEntries = new ArrayList<>();

        //建立LIST將數值一一帶入
        for(int i=0;i<money.length;i++){
            pieEntries.add(new PieEntry(money[i],item[i]));
        }
        for(int c:ColorTemplate.JOYFUL_COLORS){color.add(c);}
        for(int c:ColorTemplate.COLORFUL_COLORS){color.add(c);}
        for(int c:ColorTemplate.LIBERTY_COLORS){color.add(c);}


        PieDataSet dataset = new PieDataSet(pieEntries,"");//放入自定LIST，圖表的名稱
        //dataset.setColors(ColorTemplate.JOYFUL_COLORS);//使用系設預設圖表區塊顏色
        dataset.setColors(color);//自定圖表區塊顏色，放入陣列
        dataset.setSliceSpace(4f);//區塊間的間隔
        dataset.setSelectionShift(16f);//點擊後的突出程度


        PieData data = new PieData(dataset);//建立PieData物件存取data資料
        data.setValueFormatter(new PercentFormatter());//顯示百分比
        data.setValueTextSize(20f);
        data.setValueTextColor(Color.BLACK);


        //建立圓餅圖
        final PieChart chart = (PieChart)findViewById(R.id.chart);
        chart.setData(data);//將設定好的PieData置入
        //設置屬性
        chart.setUsePercentValues(true);
        chart.animateX(2500);
        chart.setRotationEnabled(true);//可轉動餅圖
        chart.setRotationAngle(5);//鬆手後旋轉的角度
        chart.setHoleRadius(30);//設定中間空心大小
        chart.setTransparentCircleRadius(10);//空心外圈
        //圓餅圖中間屬性
        chart.setHoleColor(Color.rgb(235, 235, 235));
        chart.setDrawCenterText(true);
        chart.setCenterText("總花費：\n"+total+"元");
        chart.setCenterTextSize(16f);
        chart.setCenterTextColor(Color.RED);


        //設定圓餅圖的分配說明
        Legend legend = chart.getLegend();
        legend.setPosition(Legend.LegendPosition.ABOVE_CHART_LEFT);//顯示位置
        legend.setFormSize(16f);//字體大小
        legend.setXEntrySpace(2f);legend.setYEntrySpace(4f);//與圖的距離
        //設置比例塊換行
        legend.setWordWrapEnabled(true);
        legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        //設定字體顏色及圖塊樣式(預設為方塊)
        legend.setTextSize(20f);
        legend.setTextColor(Color.WHITE);
        legend.setForm(Legend.LegendForm.CIRCLE);

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e,Highlight h) {

                String msg = e.toString();
                msg = msg.substring(16,msg.length()-2);
                toast.setText("總花費："+msg+"元");
                toast.show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        chart.invalidate();//刷新圖表
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {//捕捉返回鍵
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
            overridePendingTransition(R.anim.up_to_down,R.anim.down_to_up);}//按返回鍵，則執行退出確認
        return true; }

    public void Back(View v){
        finish();       //關掉支出圓餅圖
        overridePendingTransition(R.anim.up_to_down,R.anim.down_to_up);}

    public void toIncome(View v){
        startActivity(new Intent(this,account_IncomeChart.class));    //跳轉收入圓餅圖
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();   //關掉支出圓餅圖
    }
}