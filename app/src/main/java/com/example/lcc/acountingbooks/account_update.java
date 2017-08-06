package com.example.lcc.acountingbooks;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.annotation.IdRes;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class account_update extends AppCompatActivity
        implements View.OnClickListener,DatePickerDialog.OnDateSetListener {    //實作監聽日期交談事件的介面

    static final String TB_NAME = "AccountNote";    //資料表名稱
    static final String[] expense_data = new String[]{"year","month","day",
            "income","item","paymentMethods","money","info"}; //要儲的存的資料類型
    static String incomeitemName = "incomeItem" , payitemName = "payItem";  //儲存資料檔案的名子
    static String incomekey = "incomekey" , paykey = "paykey";
    static String incomefirst = "incomefirst" , payfirst = "payfirst";
    static String income = "income" , pay = "pay";
    int firstI=0,firstP=0; //用於判斷是否為新安裝第一次使用，預設0
    ArrayList<String> incomeItemList = new ArrayList<>();
    ArrayList<String> payItemList = new ArrayList<>();

    TextView mTxtUpdateDate;
    EditText mEdtMoneyUD, mEdtInfoUD;
    RadioButton rBtnIncomeUD, rBtnPayUD,rBtnCashUD,rBtnRemittanceUD,rBtnCreditCardUD;
    Button btnUpdate,btnDelete;
    Spinner mSpiItemUD, mSpiSelectCreditCardUD;
    RadioGroup rGupIPUD,rGupPaymentMethodsUD;
    Intent intent;
    int _id, year, month, day, money;   //暫存更改資料
    String getincomepay, item, info, paymentMethods;    //暫存更改資料
    ListView Updated, BeforeUpdating;    //顯示修改前後的資料
    Toast toast;    //建立提示訊息物件
    Calendar c = Calendar.getInstance();  //建立日曆物件
    MySQLiteOpenHelper helper;  //資料庫
    Cursor cur,cur2;             //游標
    SimpleCursorAdapter adapter,adapter2;    //將各資訊結合之載具
    static final String CreditCardName ="CreditCard";
    ArrayList<String> CreditCardList = new ArrayList<>();
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_update);
        myFindView();   //連結與監聽
        getSupportActionBar().hide();

        intent = getIntent();   //從上頁取得要修改資料
        _id = intent.getIntExtra("_id", 0);
        year = intent.getIntExtra("year", 0);
        month = intent.getIntExtra("month", 0);
        day = intent.getIntExtra("day", 0);
        getincomepay = intent.getStringExtra("income");
        item = intent.getStringExtra("item");
        paymentMethods = intent.getStringExtra("paymentMethods");
        money = intent.getIntExtra("money", 0);
        info = intent.getStringExtra("info");

        mTxtUpdateDate.setText("" + year + "/" + month + "/" + day);  //設定更新前消費日期
        mEdtInfoUD.setText("" + info);    //設定更新前消費內容
        mEdtMoneyUD.setText("" + money);  //設定更新前金額

        //設定(收入、支出)
        if (getincomepay.equals(getString(R.string.pay))){    //income = 支出
            rBtnPayUD.setChecked(true); //勾選支出
            rBtnCreditCardUD.setVisibility(View.VISIBLE);   //信用卡銀行選單可見
            if (payItemList == null){
                ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource
                        (account_update.this, R.array.expense_item , android.R.layout.simple_spinner_dropdown_item);
                mSpiItemUD.setAdapter(arrayAdapter);
            }else {
                ArrayAdapter arrayAdapter = new ArrayAdapter
                        (account_update.this, android.R.layout.simple_spinner_dropdown_item, payItemList);
                mSpiItemUD.setAdapter(arrayAdapter);

            }
        }else{
            rBtnIncomeUD.setChecked(true);  //勾選收入
            rBtnCreditCardUD.setVisibility(View.INVISIBLE); //信用卡銀行選單不可見
            if (incomeItemList == null){
                ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource
                        (account_update.this, R.array.income_item , android.R.layout.simple_spinner_dropdown_item);
                mSpiItemUD.setAdapter(arrayAdapter);
            } else {
                ArrayAdapter arrayAdapter = new ArrayAdapter
                        (account_update.this, android.R.layout.simple_spinner_dropdown_item,incomeItemList);
                mSpiItemUD.setAdapter(arrayAdapter);
            }
        }

        //設定(現金、匯款、信用卡)
        if(paymentMethods.equals(getString(R.string.cash))){    //paymentMethods = 現金
            rBtnCashUD.setChecked(true);    //勾選現金鈕
        }else if (paymentMethods.equals(getString(R.string.Remittance))){   //paymentMethods = 匯款
            rBtnRemittanceUD.setChecked(true);  //勾選匯款鈕
        }else{
            rBtnCreditCardUD.setChecked(true);  //勾選信用卡鈕
        }

        helper = MySQLiteOpenHelper.getInstance(this);  //開啟資料庫
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

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
        mSpiSelectCreditCardUD.setAdapter(arrayAdapter);
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
        //----取得支出項目資料

        //----取得收入項目資料
        SharedPreferences getIncomeDate = getSharedPreferences(incomeitemName,MODE_PRIVATE);
        int incomeitemKey = getIncomeDate.getInt(incomekey,-1);   //讀取檔案中 "載入次數"的數據
        firstI = getIncomeDate.getInt(incomefirst,0);  //戴入是否為第一次使用的判斷值，無值則為0
        int q = 0;  //提供改變 key-values中 key的名子使用
        for (int i = incomeitemKey; i >= 0; i--, q++) {
            String key = income + q;
            incomeItemList.add(getIncomeDate.getString(key, null)); //依檔案數量，依次寫入 CreditCardList
        }
        //----取得收入項目資料

        //----顯示更新前的資料↓
        cur = helper.getReadableDatabase().query(TB_NAME, null,
                "_id=?", new String[]{"" + _id}, null, null, null);

        adapter = new SimpleCursorAdapter(this,    //將游標內資料載內Adapter
                R.layout.listview_infomation, cur,
                expense_data,
                new int[]{R.id.year, R.id.month, R.id.day, R.id.incomepay, R.id.item, R.id.paymentMethods, R.id.money, R.id.info});
        BeforeUpdating.setAdapter(adapter);    //以ListView秀出Adapter內的資料
        //----顯示更新前的資料↑
    }

    private void myFindView() {  //連結與監聽
        mTxtUpdateDate = (TextView) findViewById(R.id.txtUpdateDate);
        mEdtMoneyUD = (EditText) findViewById(R.id.edtMoneyUD);
        mEdtInfoUD = (EditText) findViewById(R.id.edtInfoUD);
        rBtnIncomeUD = (RadioButton) findViewById(R.id.rBtnIncomeUD);
        rBtnPayUD = (RadioButton) findViewById(R.id.rBtnPayUD);
        rBtnCashUD = (RadioButton) findViewById(R.id.rBtnCashUD);
        rBtnRemittanceUD = (RadioButton) findViewById(R.id.rBtnRemittanceUD);
        rBtnCreditCardUD = (RadioButton) findViewById(R.id.rBtnCreditCardUD);
        mSpiItemUD = (Spinner) findViewById(R.id.spiExpenseItemUD);
        mSpiSelectCreditCardUD = (Spinner) findViewById(R.id.spiSelectCreditCardUD);
        BeforeUpdating = (ListView) findViewById(R.id.BeforeUpdating);
        Updated = (ListView) findViewById(R.id.Updated);
        rGupIPUD = (RadioGroup) findViewById(R.id.rGupIPUD);
        rGupPaymentMethodsUD = (RadioGroup) findViewById(R.id.rGupPaymentMethodsUD);
        btnUpdate = (Button) findViewById(R.id.btnUpdateI);
        btnDelete = (Button) findViewById(R.id.btnDeleteI);

        mTxtUpdateDate.setOnClickListener(this); //設定按下日期文字時的監聽物件
        rGupIPUD.setOnCheckedChangeListener(checkedIncome); //監聽收支群組
        rGupPaymentMethodsUD.setOnCheckedChangeListener(checkedCreditCard);  //監聽付費方式群組
}

    RadioGroup.OnCheckedChangeListener checkedIncome  = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            if (i == R.id.rBtnIncomeUD){    //點選收入鈕
                rBtnCreditCardUD.setVisibility(View.INVISIBLE); //信用卡鈕可見
                rBtnCreditCardUD.setChecked(false); //取消勾選信用卡
                rBtnCashUD.setChecked(true);    //勾選現金
                mSpiSelectCreditCardUD.setVisibility(View.INVISIBLE);   //信用卡銀行選單不可見
                if (incomeItemList.size() == 0){
                    ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource
                            (account_update.this, R.array.income_item , android.R.layout.simple_spinner_dropdown_item);
                    mSpiItemUD.setAdapter(arrayAdapter);
                } else {
                    ArrayAdapter arrayAdapter = new ArrayAdapter
                            (account_update.this, android.R.layout.simple_spinner_dropdown_item,incomeItemList);
                    mSpiItemUD.setAdapter(arrayAdapter);
                }
            }else {
                rBtnCreditCardUD.setVisibility(View.VISIBLE);   //信用卡鈕可見
                if (payItemList.size() == 0){
                    ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource
                        (account_update.this, R.array.expense_item , android.R.layout.simple_spinner_dropdown_item);
                    mSpiItemUD.setAdapter(arrayAdapter);
                }else {
                    ArrayAdapter arrayAdapter = new ArrayAdapter
                            (account_update.this, android.R.layout.simple_spinner_dropdown_item, payItemList);
                    mSpiItemUD.setAdapter(arrayAdapter);
                }
            }
        }
    };

    RadioGroup.OnCheckedChangeListener checkedCreditCard = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            if (i == R.id.rBtnCreditCardUD){    //點選信用卡鈕
                mSpiSelectCreditCardUD.setVisibility(View.VISIBLE); //信用卡銀行選單可見
            }else{
                mSpiSelectCreditCardUD.setVisibility(View.INVISIBLE);   //信用卡銀行選單不可見
            }
        }
    };
    public void pressDownExit(View view) {   //點擊離開按鈕
        finish();   //結束此頁
    }

    public void pressDownDelete(View view) {    //點擊刪除鈕
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(account_update.this);
        alertDialog.setTitle(R.string.confirmMessage)
                   .setMessage(R.string.confirmInfo)
                   .setCancelable(false)
                   .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {
                           helper.getWritableDatabase().delete(TB_NAME,"_id=?",new String []{""+_id});
                           finish();   //結束此頁
                       }
                   })
                   .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialogInterface, int i) {

                       }
                   })
                   .show();
    }

    public void pressDownUpdate(View view) { //點擊更新按鈕
        getData();  //取得資料

        if (mTxtUpdateDate.getText().toString() == getResources().getString(R.string.expense_date)) {       //沒有選擇日期
            toast.setText(R.string.noDate);
            toast.show();
        } else if (rBtnIncomeUD.isChecked() == rBtnPayUD.isChecked()) {     //未選收入或支出
            toast.setText(R.string.noIncomePay);
            toast.show();
        }else if  (!rBtnCashUD.isChecked() & !rBtnRemittanceUD.isChecked() & !rBtnCreditCardUD.isChecked()) {
            toast.setText(R.string.noPaymentMethods);
            toast.show();
        } else if (money == 0) {            //沒有輸入金額
            toast.setText(R.string.noExpense);
            toast.show();
        } else {
            //將各資訊帶入addData方法，啟動資料庫儲存資料
            updata(year, month, day, getincomepay, item, paymentMethods, money, info, _id);//資料齊全才新增資料
            updateAfter();  //載入修改後資料
        }
    }

    private void getData() {     //取得更新後的資料
        if (mEdtMoneyUD.getText().toString().length() == 0) {     //判斷是否有輸入金額
            money = 0;      //沒輸入則預設為0
        } else {
            money = Integer.parseInt(mEdtMoneyUD.getText().toString());   //有輸入則取得金額
        }

        info = mEdtInfoUD.getText().toString();   //取得消費資訊

        switch (rGupIPUD.getCheckedRadioButtonId()) {   //取得收支資訊
            case R.id.rBtnIncomeUD:
                getincomepay = getResources().getString(R.string.income).toString();
                item = mSpiItemUD.getSelectedItem().toString();   //取得項目
                break;
            case R.id.rBtnPayUD:
                getincomepay = getResources().getString(R.string.pay).toString();
                item = mSpiItemUD.getSelectedItem().toString();   //取得項目
                break;
        }

        switch (rGupPaymentMethodsUD.getCheckedRadioButtonId()){
            case R.id.rBtnCashUD:
                paymentMethods = getString(R.string.cash);
                break;
            case R.id.rBtnRemittanceUD:
                paymentMethods = getString(R.string.Remittance);
                break;
            case R.id.rBtnCreditCardUD:
                paymentMethods = getString(R.string.creditCard) +"-"+ mSpiSelectCreditCardUD.getSelectedItem();
        }
    }

    private void updata(int year, int month, int day, String income,        //進行資料庫更新
                        String item, String paymentMethods, int money, String info, int id) {
        ContentValues cv = new ContentValues(8); //建立含 8 個欄位的 ContentValues物件
        cv.put(expense_data[0], year);
        cv.put(expense_data[1], month);
        cv.put(expense_data[2], day);
        cv.put(expense_data[3], income);
        cv.put(expense_data[4], item);
        cv.put(expense_data[5], paymentMethods);
        cv.put(expense_data[6], money);
        cv.put(expense_data[7], info);

        helper.getWritableDatabase().update(TB_NAME, cv, "_id=" + id, null);    // 更新 id 所指的欄位
    }

    @Override
    public void onDateSet(DatePicker v, int y, int m, int d) {    // 將選定日期顯示在螢幕上
        mTxtUpdateDate.setText(y + "/" + (m + 1) + "/" + d);  // 設定顯示方式 yyyy/mm/dd
        year = y;
        month = m + 1;
        day = d;  //設定儲存入資料庫的年月日
    }

    @Override
    public void onClick(View view) {    //監控按下日期(TextView)可選擇年月日
        if (view == mTxtUpdateDate) {
            new DatePickerDialog(this, this,
                    c.get(Calendar.YEAR),             //從calendar物件取得目前的年
                    c.get(Calendar.MONTH),           //從calendar物件取得目前的月
                    c.get(Calendar.DAY_OF_MONTH))   //從calendar物件取得目前的日
                    .show();    //顯示出來
        }
    }

    private void updateAfter() {    //在 ListView2 上顯示修改後資料
        cur2 = helper.getReadableDatabase().query(TB_NAME, null,
                "_id=?", new String[]{"" + _id}, null, null, null);

        adapter2 = new SimpleCursorAdapter(this,    //將游標內資料載內Adapter
                R.layout.listview_infomation, cur2,
                expense_data,
                new int[]{R.id.year, R.id.month, R.id.day, R.id.incomepay, R.id.item, R.id.paymentMethods, R.id.money, R.id.info});
        Updated.setAdapter(adapter2);    //以ListView秀出Adapter內的資料
    }
}


