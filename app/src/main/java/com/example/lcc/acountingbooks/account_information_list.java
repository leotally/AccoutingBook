package com.example.lcc.acountingbooks;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.IdRes;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class account_information_list extends AppCompatActivity {
    static final String TB_NAME = "AccountNote";    //資料表名稱
    static final String[] expense_data = new String[]{"year", "month", "day",
            "income", "item", "paymentMethods", "money", "info"}; //要儲的存的資料類型
    EditText mEdtYear1, mEdtMonth1, mEdtMonth2;
    RadioGroup mRGupRange, mRGupIPA;
    RadioButton mRBtnSingle, mRBtnRange, mRBtnI, mRBtnP, mRBtnA;
    android.support.constraint.ConstraintLayout mSearchLayout;
    ListView mLvInfo;
    TextView mTxtInfoDate;
    Cursor cur;             //游標
    SimpleCursorAdapter adapter;    //將各資訊結合之載具
    MySQLiteOpenHelper helper = MySQLiteOpenHelper.getInstance(this);
    Toast toast;
    Intent intent;
    int year1,year2,month1,month2,IncomePayALL;
    String ip;
    Calendar c = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_information_list);
        getSupportActionBar().hide();
        myFindView();
        getSystemDate();
        FistView();
        getAllDate();

        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
    }

    protected void onRestart(){
        super.onRestart();
        getAllDate();
    }

    //連結與監聽
    private void myFindView(){
        mEdtMonth1 = (EditText) findViewById(R.id.edtSearchMonth1);
        mEdtMonth2 = (EditText) findViewById(R.id.edtSearchMonth2);
        mEdtYear1 = (EditText) findViewById(R.id.edtSearchYear1);
        mRGupIPA = (RadioGroup) findViewById(R.id.rGupSearchIPA);
        mRGupRange = (RadioGroup) findViewById(R.id.rGupRange);
        mRBtnA = (RadioButton) findViewById(R.id.rBtnSearchA);
        mRBtnI = (RadioButton) findViewById(R.id.rBtnSearchI);
        mRBtnP = (RadioButton) findViewById(R.id.rBtnSearchP);
        mRBtnSingle = (RadioButton) findViewById(R.id.rBtnSingle);
        mRBtnRange = (RadioButton) findViewById(R.id.rBtnRange);
        mSearchLayout = (android.support.constraint.ConstraintLayout) findViewById(R.id.SearchLayout);
        mLvInfo = (ListView) findViewById(R.id.lVInfo);
        mTxtInfoDate = (TextView) findViewById(R.id.txtInfoDate);

        mLvInfo.setOnItemClickListener(listinfo);
        mRGupRange.setOnCheckedChangeListener(searchRange);
    }

    //首次載入此頁，隱藏暫不需要使用介面
    private void FistView(){
        mSearchLayout.setVisibility(View.GONE);
        mEdtMonth2.setEnabled(false);
        mEdtMonth2.setHint("");
    }

    //取得系統年、月 設定預設值
    private void getSystemDate(){
        year1 = c.get(Calendar.YEAR);
        year2 = c.get(Calendar.YEAR);
        month1 = c.get(Calendar.MONTH)+1;
        month2 = c.get(Calendar.MONTH)+1;
        IncomePayALL = 1;
    }

    //取得全部資料
    private void getAllDate(){
        cur = helper.getReadableDatabase().query(TB_NAME, null, null, null, null, null, "year,month,day");
        adapter = new SimpleCursorAdapter(this,    //將游標內資料載內Adapter
                R.layout.infomation_listview, cur,
                expense_data,
                new int[]{R.id.year, R.id.month, R.id.day, R.id.incomepay, R.id.item, R.id.paymentMethods, R.id.money, R.id.info});
        mLvInfo.setAdapter(adapter);    //以ListView秀出Adapter內的資料
        myToast();
        mTxtInfoDate.setText(getString(R.string.all_details));
    }

    //顯示搜尋筆數的訊息
    public void myToast() { //顯示搜尋到的筆數
        toast = Toast.makeText(this, getString(R.string.searchFor) + cur.getCount() + getString(R.string.piecesOfInformation), Toast.LENGTH_SHORT);
        toast.show();
    }

    //按下左箭頭 搜尋下個月資料
    public void nextMonth(View view){
        if (month1 >= 12) {          //設計可變更年份
            month1 = 1;
            year1 += 1;
        } else {
            month1 += 1;
        }

        if (IncomePayALL == 1) {
            showDateText(1);
        } else {
            showDateText(2);
        }
        SingleSearch();
        adapter.changeCursor(cur);
    }

    //按下右箭頭 搜尋上個月資料
    public void lastMonth(View view){
        if (month1 <= 1) {           //設計可變更年份
            month1 = 12;
            year1 -= 1;
        } else {
            month1 -= 1;
        }

        if (IncomePayALL == 1) {
            showDateText(1);
        } else {
            showDateText(2);
        }
        SingleSearch();
        adapter.changeCursor(cur);
    }

    //取得範圍資料變更顯示資訊
    private void showDateText(int i ){
        switch (i){
            case 1:
                mTxtInfoDate.setText("" + year1 + getString(R.string.year) + " " + month1 + getString(R.string.month) +
                        " " + getString(R.string.all_details));
                break;
            case 2:
                mTxtInfoDate.setText("" + year1 + getString(R.string.year) + " " + month1 + getString(R.string.month) +
                        " " + ip + getString(R.string.detail));
                break;
        }
    }

    //按下新增圖鈕
    public void add(View view){
        startActivity(new Intent(this, account_add.class));
    }

    //按下搜尋圖鈕 顯示搜尋 Layout
    public void search(View view) {
        mSearchLayout.setVisibility(View.VISIBLE);
    }

    RadioGroup.OnCheckedChangeListener searchRange = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            switch (i){
                case R.id.rBtnSingle:
                    mEdtMonth2.setEnabled(false);
                    mEdtMonth2.setHint("");
                    break;
                case R.id.rBtnRange:
                    mEdtMonth2.setEnabled(true);
                    mEdtMonth2.setHint("預設值為當月");
                    break;
            }
        }
    };

    //按下 搜尋 Layout 裡的確定
    public void confirm(View view){
        switch (mRGupRange.getCheckedRadioButtonId()){
            case R.id.rBtnSingle:
                getSingleDate();
                SingleSearch();
                if (IncomePayALL == 1){
                    mTxtInfoDate.setText(""+year1+getString(R.string.year)+" "+month1+getString(R.string.month)+" 全部明細");
                } else {
                    mTxtInfoDate.setText(""+year1+getString(R.string.year)+" "+month1+getString(R.string.month)+" "+ip+"明細");
                }
                myToast();
                adapter.changeCursor(cur);
                break;
            case R.id.rBtnRange:
                getRangeDate();
                if (IncomePayALL == 1){
                    cur = helper.getReadableDatabase().query(TB_NAME,null,
                            "year = ? AND month BETWEEN ? AND ? ",
                            new String[]{""+year1,""+month1,""+month2},null,null,"year,month,day");
                    mTxtInfoDate.setText(""+year1+getString(R.string.year)+" "+month1+getString(R.string.month)
                            +" 至 "+month2+getString(R.string.month)+" 全部明細");
                } else {
                    cur = helper.getReadableDatabase().query(TB_NAME, null,
                            "year = ? AND month BETWEEN ? AND ? AND income = ?",
                            new String[]{""+year1, ""+month1, ""+month2, ip}, null, null, "year,month,day");
                    mTxtInfoDate.setText(""+year1+getString(R.string.year)+" "+month1+getString(R.string.month)+" "
                            +" 至 "+month2+getString(R.string.month)+" "+ip+"明細");
                }
                myToast();
                adapter.changeCursor(cur);
                break;
        }
        mSearchLayout.setVisibility(View.GONE);
    }

    private void SingleSearch(){
        if (IncomePayALL == 1){
            cur = helper.getReadableDatabase().query(TB_NAME,null,
                    "year = ? and month = ?",new String[] {""+year1,""+month1},null,null,"year,month,day");
        } else {
            cur = helper.getReadableDatabase().query(TB_NAME, null,
                    "year = ? and month = ? and income = ? ", new String[]{""+year1, ""+month1, ip},
                    null, null, "year,month,day");
        }
    }


    //取得單筆搜索的資料
    private void getSingleDate(){
        if (mEdtYear1.getText().length() == 0){
            year1 = c.get(Calendar.YEAR);
        } else {
            year1 = Integer.parseInt(mEdtYear1.getText().toString());
        }
        if (mEdtMonth1.getText().length() == 0){
            month1 = c.get(Calendar.MONTH)+1;
        } else {
            month1 = Integer.parseInt(mEdtMonth1.getText().toString());
        }

        switch (mRGupIPA.getCheckedRadioButtonId()){
            case R.id.rBtnSearchI:
                ip=mRBtnI.getText().toString();
                IncomePayALL=0;
                break;
            case R.id.rBtnSearchP:
                ip=mRBtnP.getText().toString();
                IncomePayALL=0;
                break;
            case R.id.rBtnSearchA:
                IncomePayALL=1;
        }
    }

    //取得範圍搜索的資料
    private void getRangeDate(){
        if (mEdtYear1.getText().length() == 0){
            year1 = c.get(Calendar.YEAR);
        } else {
            year1 = Integer.parseInt(mEdtYear1.getText().toString());
        }
        if (mEdtMonth1.getText().length() == 0){
            month1 = c.get(Calendar.MONTH)+1;
        } else {
            month1 = Integer.parseInt(mEdtMonth1.getText().toString());
        }

        if (mEdtMonth2.getText().length() == 0){
            month2 = c.get(Calendar.MONTH)+1;
        } else {
            month2 = Integer.parseInt(mEdtMonth2.getText().toString());
        }

        switch (mRGupIPA.getCheckedRadioButtonId()){
            case R.id.rBtnSearchI:
                ip=mRBtnI.getText().toString();
                IncomePayALL=0;
                break;
            case R.id.rBtnSearchP:
                ip=mRBtnP.getText().toString();
                IncomePayALL=0;
                break;
            case R.id.rBtnSearchA:
                IncomePayALL=1;
        }
    }

    //按下 搜尋 Layout 裡的搜尋全部
    public void searchAll(View view){
        switch (mRGupIPA.getCheckedRadioButtonId()){
            case R.id.rBtnSearchI:
                ip=mRBtnI.getText().toString();
                IncomePayALL=0;
                break;
            case R.id.rBtnSearchP:
                ip=mRBtnP.getText().toString();
                IncomePayALL=0;
                break;
            case R.id.rBtnSearchA:
                IncomePayALL=1;
        }

        if (IncomePayALL == 1){
            cur = helper.getReadableDatabase().query(TB_NAME,null,null,null,null,null,null);
            mTxtInfoDate.setText(getString(R.string.all_details));
        } else {
            cur = helper.getReadableDatabase().query(TB_NAME,null,"income = ?",new String[]{ip},
                    null,null,null);
            mTxtInfoDate.setText(getString(R.string.all)+ip+getString(R.string.detail));
        }
        myToast();
        adapter.changeCursor(cur);
        mSearchLayout.setVisibility(View.GONE);
    }

    //按下 搜尋 Layout 裡的取消
    public void cancel(View view){
        mSearchLayout.setVisibility(View.GONE);
    }

    //點擊 ListView裡的資料 並夾帶資料到修改頁面
    AdapterView.OnItemClickListener listinfo = new AdapterView.OnItemClickListener() {
        @Override   //點選list項目取得id
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long i) {
            cur.moveToPosition(position); //	移動 Cursor 至使用者選取的項目

            intent = new Intent();
            intent.setClass(account_information_list.this,account_update.class);
            intent.putExtra("_id",cur.getInt(0));
            intent.putExtra("year",cur.getInt(1));
            intent.putExtra("month",cur.getInt(2));
            intent.putExtra("day",cur.getInt(3));
            intent.putExtra("income",cur.getString(4));
            intent.putExtra("item",cur.getString(5));
            intent.putExtra("paymentMethods",cur.getString(6));
            intent.putExtra("money",cur.getInt(7));
            intent.putExtra("info",cur.getString(8));
            startActivity(intent);
        }
    };
}