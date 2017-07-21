package com.example.lcc.acountingbooks;


import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class account_add extends AppCompatActivity
            implements View.OnClickListener,
            DatePickerDialog.OnDateSetListener{    //實作監聽日期交談事件的介面

    static final String TB_NAME="AccountNote";  //資料表名稱
    static final String[] expense_data = new String[]{"year","month","day",
            "income","item","paymentMethods","money","info"}; //要儲的存的資料類型
    static final String CreditCardName ="CreditCard";
    static String incomeitemName = "incomeItem" , payitemName = "payItem";  //儲存資料檔案的名子
    static String incomekey = "incomekey" , paykey = "paykey";
    static String incomefirst = "incomefirst" , payfirst = "payfirst";
    static String income = "income" , pay = "pay";
    int firstI=0,firstP=0; //用於判斷是否為新安裝第一次使用，預設0

    TextView mTxtDate;      //新增消費日期
    Spinner mSpiItem,mSpiSelectCreditCard;       //消費項目
    EditText mEdtMoney,mEdtInfo;    //消費金額、消費資訊
    RadioGroup rGupIP,rGupPaymentMethods;      //收支RadioGroup
    RadioButton rBtnIncome, rBtnPay, rBtnCash, rBtnCreditCard, rBtnRemittance;//收支RadioGroup與button
    Calendar c = Calendar.getInstance();  //建立日曆物件
    Toast toast;    //建立提示訊息物件
    CheckBox mCkBoxNext;    //用於勾選可不跳出頁面，直接建立多筆資料
    ArrayList<String> incomeItemList = new ArrayList<>();
    ArrayList<String> payItemList = new ArrayList<>();


    String getincomepay,item,info,paymentMethods;   //取得收支、項目、消費資訊用字串
    int year,month,day,money;   //取得年月日金額用之int
    private MySQLiteOpenHelper helper = MySQLiteOpenHelper.getInstance(this);
    ArrayList<String> CreditCardList = new ArrayList<>();
    ArrayAdapter arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_add);
        myFindView();   //連結關連與監聽的方法
        getSupportActionBar().hide(); //隱藏 ActionBar

        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        helper = MySQLiteOpenHelper.getInstance(this);

        //----取得信用卡項目資料↓
        SharedPreferences getDate = getSharedPreferences(CreditCardName,MODE_PRIVATE);   //建立存取資料檔物件
        int CreditCardKey = getDate.getInt("CreditCardKey",-1);   //讀取檔案中 "載入次數"的數據
        int j = 0;  //提供改變 key-values中 key的名子使用
        for (int i = CreditCardKey; i >= 0; i--, j++) {
            String k = "CreditCard" + j;
            CreditCardList.add(getDate.getString(k, null)); //依檔案數量，依次寫入 CreditCardList
        }
        if (CreditCardKey < 0){
            String[] CreditCardList = getResources().getStringArray(R.array.selectCreditCard);
            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, CreditCardList);
        }else {
            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, CreditCardList);
        }
        mSpiSelectCreditCard.setAdapter(arrayAdapter);
        //----取得信用卡項目資料↑

        //----取得支出項目資料↓
        SharedPreferences getPayDate = getSharedPreferences(payitemName,MODE_PRIVATE);
        int payitemKey = getPayDate.getInt(paykey,-1);
        firstP = getPayDate.getInt(payfirst,0);
        int p = 0;
        for (int i = payitemKey; i >= 0; i--, p++){
            String key = pay + p;
            payItemList.add(getPayDate.getString(key,null));
        }
        //----取得支出項目資料↑

        //----取得收入項目資料↓
        SharedPreferences getIncomeDate = getSharedPreferences(incomeitemName,MODE_PRIVATE);
        int incomeitemKey = getIncomeDate.getInt(incomekey,-1);   //讀取檔案中 "載入次數"的數據
        firstI = getIncomeDate.getInt(incomefirst,0);  //戴入是否為第一次使用的判斷值，無值則為0
        int q = 0;  //提供改變 key-values中 key的名子使用
        for (int i = incomeitemKey; i >= 0; i--, q++) {
            String key = income + q;
            incomeItemList.add(getIncomeDate.getString(key, null)); //依檔案數量，依次寫入 CreditCardList
        }
        //----取得收入項目資料↑
    }

    public void myFindView() {   //連結關連與監聽的方法
        mTxtDate = (TextView) findViewById(R.id.txtExpenseDate);
        mSpiItem = (Spinner) findViewById(R.id.spiExpenseItem);
        mSpiSelectCreditCard = (Spinner) findViewById(R.id.spiSelectCreditCard);
        mEdtMoney = (EditText) findViewById(R.id.edtMoney);
        mEdtInfo = (EditText) findViewById(R.id.edtInfo);
        mCkBoxNext = (CheckBox) findViewById(R.id.ckBoxNext);
        rGupIP = (RadioGroup) findViewById(R.id.rGupIP);
        rGupPaymentMethods = (RadioGroup) findViewById(R.id.rGupPaymentMethods);
        rBtnIncome = (RadioButton)findViewById(R.id.rBtnIncome);
        rBtnPay = (RadioButton)findViewById(R.id.rBtnPay);
        rBtnCash = (RadioButton)findViewById(R.id.rBtnCash);
        rBtnCreditCard = (RadioButton)findViewById(R.id.rBtnCreditCard);
        rBtnRemittance = (RadioButton)findViewById(R.id.rBtnRemittance);

        mTxtDate.setOnClickListener(this); //設定按下日期文字時的監聽物件
        rGupIP.setOnCheckedChangeListener(checkedIncome);
        rGupPaymentMethods.setOnCheckedChangeListener(checkedCreditCard);
    }

    RadioGroup.OnCheckedChangeListener checkedIncome = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            if (i == R.id.rBtnIncome){
                rBtnCreditCard.setVisibility(View.INVISIBLE);
                rBtnCreditCard.setChecked(false);
                rBtnCash.setChecked(true);
                mSpiSelectCreditCard.setVisibility(View.INVISIBLE);
                if (incomeItemList.size() == 0){
                    ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource
                            (account_add.this, R.array.income_item , android.R.layout.simple_spinner_dropdown_item);
                    mSpiItem.setAdapter(arrayAdapter);
                } else {
                    ArrayAdapter arrayAdapter = new ArrayAdapter
                            (account_add.this, android.R.layout.simple_spinner_dropdown_item,incomeItemList);
                    mSpiItem.setAdapter(arrayAdapter);
                }
            }else {
                rBtnCreditCard.setVisibility(View.VISIBLE);
                if (payItemList.size() == 0){
                    ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource
                            (account_add.this, R.array.expense_item , android.R.layout.simple_spinner_dropdown_item);
                    mSpiItem.setAdapter(arrayAdapter);
                }else {
                    ArrayAdapter arrayAdapter = new ArrayAdapter
                            (account_add.this, android.R.layout.simple_spinner_dropdown_item,payItemList);
                    mSpiItem.setAdapter(arrayAdapter);
                }
            }
        }
    };

    RadioGroup.OnCheckedChangeListener checkedCreditCard = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            if (i == R.id.rBtnCreditCard){
                mSpiSelectCreditCard.setVisibility(View.VISIBLE);
            }else{
                mSpiSelectCreditCard.setVisibility(View.INVISIBLE);
            }
        }
    };

    // 將選定日期顯示在螢幕上
    @Override
    public void onDateSet(DatePicker v, int y, int m, int d) {
        mTxtDate.setText(y + "/" + (m+1) + "/" + d);  // 將選定日期顯示在螢幕上
        year = y;   month = m+1;   day = d;  //設定儲存入資料庫的年月日
    }

    //監控按下日期(TextView)可選擇年月日
    @Override
    public void onClick(View view) {
        if (view == mTxtDate){
            new DatePickerDialog(this ,this ,
                    c.get(Calendar.YEAR),             //從calendar物件取得目前的年
                    c.get(Calendar.MONTH),           //從calendar物件取得目前的月
                    c.get(Calendar.DAY_OF_MONTH))   //從calendar物件取得目前的日
                    .show();    //顯示出來
        }
    }

    //按下清除(Button)的動作
    public void clear(View v){
        mEdtMoney.setText(null);    //清空金額
        mEdtInfo.setText(null);     //清空資訊內容
        mTxtDate.setText(getResources().getString(R.string.expense_date).toString());
    }

    private void getData(){
        if (mEdtMoney.getText().toString().length() == 0){     //判斷是否有輸入金額
            money = 0;      //沒輸入則預設為0
        }else{
            money = Integer.parseInt(mEdtMoney.getText().toString());   //有輸入則取得金額
        }

        info = mEdtInfo.getText().toString();   //取得消費資訊

        switch (rGupIP.getCheckedRadioButtonId()){   //取得收支資訊
            case R.id.rBtnIncome:
                getincomepay = getResources().getString(R.string.income).toString();
                item = mSpiItem.getSelectedItem().toString();   //取得項目
                break;
            case R.id.rBtnPay:
                getincomepay = getResources().getString(R.string.pay).toString();
                item = mSpiItem.getSelectedItem().toString();   //取得項目
                break;
        }

        switch (rGupPaymentMethods.getCheckedRadioButtonId()){
            case R.id.rBtnCash:
                paymentMethods = getString(R.string.cash);
                break;
            case R.id.rBtnRemittance:
                paymentMethods = getString(R.string.Remittance);
                break;
            case R.id.rBtnCreditCard:
                paymentMethods = getString(R.string.creditCard) +"-"+ mSpiSelectCreditCard.getSelectedItem();
        }
    }

    //按下確定(Button)儲存各數據
    public void send(View v){
        getData(); //取得數劇的方法

        if(mTxtDate.getText().toString() == getResources().getString(R.string.expense_date)){       //沒有選擇日期
            toast.setText("還沒設定日期喔！");
            toast.show();
        }else if (rBtnIncome.isChecked() == rBtnPay.isChecked()) {     //未選收入或支出
            toast.setText("還沒設定收入或支出喔！");
            toast.show();
        }else if (!rBtnCash.isChecked() & !rBtnRemittance.isChecked() & !rBtnCreditCard.isChecked()) {
            toast.setText("請設定支付方式現金、匯款或信用卡！");
            toast.show();
        }else if (money == 0){            //沒有輸入金額
            toast.setText("還沒輸入金額喔！");
            toast.show();
        }else {
            //將各資訊帶入addData方法，啟動資料庫儲存資料
            addData(year, month, day, getincomepay, item, paymentMethods, money, info);//資料齊全才新增資料
            if(mCkBoxNext.isChecked()){
                mEdtMoney.setText(null);    //清空金額
                mEdtInfo.setText(null);     //清空資訊內容
                mTxtDate.setText(getResources().getString(R.string.expense_date).toString());   //清空選取時間
            }else{
                finish(); //結束此新增頁面
            }
        }
    }

    //儲存每一筆新增資料
    public void addData(int year, int month, int day,String income, String item, String paymentMethods, int money, String info){
        ContentValues cv = new ContentValues(8); //建立含 7 個欄位的 ContentValues物件
        cv.put(expense_data[0], year);
        cv.put(expense_data[1], month);
        cv.put(expense_data[2], day);
        cv.put(expense_data[3], income);
        cv.put(expense_data[4], item);
        cv.put(expense_data[5], paymentMethods);
        cv.put(expense_data[6], money);
        cv.put(expense_data[7], info);

        helper.getWritableDatabase().insert(TB_NAME,null,cv);
    }
}
