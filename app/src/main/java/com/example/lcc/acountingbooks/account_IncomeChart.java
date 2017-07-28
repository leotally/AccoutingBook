package com.example.lcc.acountingbooks;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class account_IncomeChart extends Activity {

    int[] money ;//各項的總計數字
    String item[];
    ConstraintLayout layout;
    ArrayList<Integer> color = new ArrayList<>();//調色盤
    int total;//顯示總金額
    Toast toast;
    static final String TB_NAME="AccountNote";  //資料表名稱
    private MySQLiteOpenHelper helper = MySQLiteOpenHelper.getInstance(this);
    Cursor cur;
    Calendar c = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_income_chart);
        //layout = (ConstraintLayout)findViewById(R.id.mainLayout);
        toast = Toast.makeText(this,"",Toast.LENGTH_SHORT);

        getDate();


    }

    private void getDate(){
        cur = helper.getReadableDatabase().query(true,TB_NAME,new String[]{"item"},"year = ? and income = ?",
                new String[]{""+c.get(Calendar.YEAR),getString(R.string.income)},null,null,null,null);
        item = new String [cur.getCount()];
        cur.moveToFirst();
        for (int i =0 ; i <cur.getCount() ; i++){
            item[i] = cur.getString(0);
            cur.moveToNext();
        }
        money = new int[item.length];
        for (int j =0 ; j < item.length ; j++){
            cur = helper.getReadableDatabase().query(TB_NAME,new String[]{"SUM(money)"},"item = ?",
                    new String[]{item[j]},"item",null,null);
            cur.moveToFirst();
            money[j] = cur.getInt(0);
        }

        setChart();
    }

    public void setChart(){
        total = 0;
        for(int i=0;i<money.length;i++){
            total += money[i];
        }
        List<PieEntry> pieEntries = new ArrayList<>();
        for(int i=0;i<money.length;i++){
            pieEntries.add(new PieEntry(money[i],item[i]));
        }
        for(int c: ColorTemplate.JOYFUL_COLORS){color.add(c);}
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
        chart.setCenterText("總收入：\n"+total+"元");
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

        chart.invalidate();//刷新圖表

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                String msg = e.toString();
                msg = msg.substring(16,msg.length()-2);
                toast.setText("總收入："+msg+"元");
                toast.show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }



    public boolean onKeyDown(int keyCode, KeyEvent event) {//捕捉返回鍵
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
            overridePendingTransition(R.anim.up_to_down,R.anim.down_to_up);}//按返回鍵，則執行退出確認
        return true; }

    public void Back(View v){
        finish();   //關掉收入圓餅圖
        overridePendingTransition(R.anim.up_to_down,R.anim.down_to_up);}

    public void toPay(View v){
        startActivity(new Intent(this,account_paychart.class)); //跳轉支出圓餅圖
        finish();   //關掉收入圓餅圖
        overridePendingTransition(R.anim.zoom_in,0);
    }
}
