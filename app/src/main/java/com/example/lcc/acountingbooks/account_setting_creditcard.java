package com.example.lcc.acountingbooks;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.MovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Arrays;

public class account_setting_creditcard extends AppCompatActivity {
    static final String CreditCardName = "CreditCard";    //信用卡項目資料檔名稱

    TextView mTxtC1, mTxtC2, mTxtC3, mTxtC4, mTxtC5, mTxtC6, mTxtC7, mTxtC8, mTxtC9, mTxtC10;
    EditText mEdtNewCreditCard, mEdtC1, mEdtC2, mEdtC3, mEdtC4, mEdtC5, mEdtC6, mEdtC7, mEdtC8, mEdtC9, mEdtC10;
    ListView mLvCreditCard;
    android.support.constraint.ConstraintLayout mLyeoutC1, mLyeoutC2, mLyeoutC3, mLyeoutC4, mLyeoutC5,
            mLyeoutC6, mLyeoutC7, mLyeoutC8, mLyeoutC9, mLyeoutC10;
    Button mBtnAddC, mBtnDeleteC, mBtnUpdateC, mBtnDefaultC;
    ToggleButton mTBtnC1, mTBtnC2, mTBtnC3, mTBtnC4, mTBtnC5, mTBtnC6, mTBtnC7, mTBtnC8, mTBtnC9, mTBtnC10;
    int index = -1, tBtnindex;
    ArrayList<String> CreditCardList = new ArrayList<>();
    ArrayList<Long> MoneyOfMessageList = new ArrayList<>();
    ArrayList<String> SwitchOfMessageList = new ArrayList<>();
    ArrayAdapter adapter;
    int firstopen = 0; //用於判斷是否為新安裝第一次使用，預設0
    SharedPreferences CreditCard;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_setting_creditcard);
        myFindView();
        mBtnDeleteC.setEnabled(false);  //關閉刪除鈕
        mBtnUpdateC.setEnabled(false);  //關閉更新鈕
        //建立儲存物件，但將檔案取名為 "CreditCard"
        CreditCard = getSharedPreferences(CreditCardName, MODE_PRIVATE);
        toast = Toast.makeText(this,"",Toast.LENGTH_SHORT);

        LoadingDate();
    }

    //取得連結與監聽
    private void myFindView() {
        mEdtC1 = (EditText) findViewById(R.id.edtC1);
        mEdtC2 = (EditText) findViewById(R.id.edtC2);
        mEdtC3 = (EditText) findViewById(R.id.edtC3);
        mEdtC4 = (EditText) findViewById(R.id.edtC4);
        mEdtC5 = (EditText) findViewById(R.id.edtC5);
        mEdtC6 = (EditText) findViewById(R.id.edtC6);
        mEdtC7 = (EditText) findViewById(R.id.edtC7);
        mEdtC8 = (EditText) findViewById(R.id.edtC8);
        mEdtC9 = (EditText) findViewById(R.id.edtC9);
        mEdtC10 = (EditText) findViewById(R.id.edtC10);
        mEdtNewCreditCard = (EditText) findViewById(R.id.edtIncome);

        mTxtC1 = (TextView) findViewById(R.id.txtViewC1);
        mTxtC2 = (TextView) findViewById(R.id.txtViewC2);
        mTxtC3 = (TextView) findViewById(R.id.txtViewC3);
        mTxtC4 = (TextView) findViewById(R.id.txtViewC4);
        mTxtC5 = (TextView) findViewById(R.id.txtViewC5);
        mTxtC6 = (TextView) findViewById(R.id.txtViewC6);
        mTxtC7 = (TextView) findViewById(R.id.txtViewC7);
        mTxtC8 = (TextView) findViewById(R.id.txtViewC8);
        mTxtC9 = (TextView) findViewById(R.id.txtViewC9);
        mTxtC10 = (TextView) findViewById(R.id.txtViewC10);

        mBtnAddC = (Button) findViewById(R.id.btnAddC);
        mBtnDeleteC = (Button) findViewById(R.id.btnDeleteI);
        mBtnDefaultC = (Button) findViewById(R.id.btnDefaultC);
        mBtnUpdateC = (Button) findViewById(R.id.btnUpdateI);

        mLyeoutC1 = (android.support.constraint.ConstraintLayout) findViewById(R.id.creditcard1);
        mLyeoutC2 = (android.support.constraint.ConstraintLayout) findViewById(R.id.creditcard2);
        mLyeoutC3 = (android.support.constraint.ConstraintLayout) findViewById(R.id.creditcard3);
        mLyeoutC4 = (android.support.constraint.ConstraintLayout) findViewById(R.id.creditcard4);
        mLyeoutC5 = (android.support.constraint.ConstraintLayout) findViewById(R.id.creditcard5);
        mLyeoutC6 = (android.support.constraint.ConstraintLayout) findViewById(R.id.creditcard6);
        mLyeoutC7 = (android.support.constraint.ConstraintLayout) findViewById(R.id.creditcard7);
        mLyeoutC8 = (android.support.constraint.ConstraintLayout) findViewById(R.id.creditcard8);
        mLyeoutC9 = (android.support.constraint.ConstraintLayout) findViewById(R.id.creditcard9);
        mLyeoutC10 = (android.support.constraint.ConstraintLayout) findViewById(R.id.creditcard10);

        mTBtnC1 = (ToggleButton) findViewById(R.id.tBtnC1);
        mTBtnC2 = (ToggleButton) findViewById(R.id.tBtnC2);
        mTBtnC3 = (ToggleButton) findViewById(R.id.tBtnC3);
        mTBtnC4 = (ToggleButton) findViewById(R.id.tBtnC4);
        mTBtnC5 = (ToggleButton) findViewById(R.id.tBtnC5);
        mTBtnC6 = (ToggleButton) findViewById(R.id.tBtnC6);
        mTBtnC7 = (ToggleButton) findViewById(R.id.tBtnC7);
        mTBtnC8 = (ToggleButton) findViewById(R.id.tBtnC8);
        mTBtnC9 = (ToggleButton) findViewById(R.id.tBtnC9);
        mTBtnC10 = (ToggleButton) findViewById(R.id.tBtnC10);

        mLvCreditCard = (ListView) findViewById(R.id.lvCreditCard);
        mLvCreditCard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override   //點選 ListView 清單的動作
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                index = i;  //取索引值
                tBtnindex = i;  //取ToggleButton判斷式用的值
                mEdtNewCreditCard.setText(((TextView) view).getText()); //取值放入edt
                openAndCloseButton();   //改變 更新、與刪除鈕的功能
            }
        });
    }

    private void LoadingDate(){
        //建立從檔案 "CreditCard" 取得資料的物件
        SharedPreferences getDate = getSharedPreferences(CreditCardName, MODE_PRIVATE);
        int CreditCardKey = getDate.getInt("CreditCardKey", -1); //讀取檔案中 "載入次數"的數據
        firstopen = getDate.getInt("FirstOpen", 0);  //戴入是否為第一次使用的判斷值，無值則為0
        int j = 0;  //提供改變 key-values中 key的名子使用
        for (int i = CreditCardKey; i >= 0; i--, j++) {
            String k = "CreditCard" + j;
            String k1 = "MoneyOfMessage" + j;
            String k2 = "SwitchOfMessage" + j;
            CreditCardList.add(getDate.getString(k, null)); //依檔案數量，依次寫入 CreditCardList
//            MoneyOfMessageList.add(getDate.getLong(k1, 1L));
//            SwitchOfMessageList.add(getDate.getString(k2, null));
//            String sw;
//            switch (j){
//                case 0:
////                    mEdtC1.setText(MoneyOfMessageList.get(j).toString());
//                    sw = SwitchOfMessageList.get(j);
//                    if (sw.length() == 3){
//                        mTBtnC1.setChecked(false);
//                    } else
//                        mTBtnC1.setChecked(true);
//                    break;
//                case 1:
////                    mEdtC2.setText(MoneyOfMessageList.get(j).toString());
//                    sw = SwitchOfMessageList.get(j);
//                    if (sw.length() == 3){
//                        mTBtnC2.setChecked(false);
//                    } else
//                        mTBtnC2.setChecked(true);
//                    break;
//                case 2:
////                    mEdtC3.setText(MoneyOfMessageList.get(j).toString());
//                    sw = SwitchOfMessageList.get(j);
//                    if (sw.length() == 3){
//                        mTBtnC3.setChecked(false);
//                    } else
//                        mTBtnC3.setChecked(true);
//                    break;
//                case 3:
////                    mEdtC4.setText(MoneyOfMessageList.get(j).toString());
//                    sw = SwitchOfMessageList.get(j);
//                    if (sw.length() == 3){
//                        mTBtnC4.setChecked(false);
//                    } else
//                        mTBtnC4.setChecked(true);
//                    break;
//                case 4:
//                    mEdtC5.setText(MoneyOfMessageList.get(j).toString());
//                    sw = SwitchOfMessageList.get(j);
//                    if (sw.length() == 3){
//                        mTBtnC5.setChecked(false);
//                    } else
//                        mTBtnC5.setChecked(true);
//                    break;
//                case 5:
//                    mEdtC6.setText(MoneyOfMessageList.get(j).toString());
//                    sw = SwitchOfMessageList.get(j);
//                    if (sw.length() == 3){
//                        mTBtnC6.setChecked(false);
//                    } else
//                        mTBtnC6.setChecked(true);
//                    break;
//                case 6:
//                    mEdtC7.setText(MoneyOfMessageList.get(j).toString());
//                    sw = SwitchOfMessageList.get(j);
//                    if (sw.length() == 3){
//                        mTBtnC7.setChecked(false);
//                    } else
//                        mTBtnC7.setChecked(true);
//                    break;
//                case 7:
//                    mEdtC8.setText(MoneyOfMessageList.get(j).toString());
//                    sw = SwitchOfMessageList.get(j);
//                    if (sw.length() == 3){
//                        mTBtnC8.setChecked(false);
//                    } else
//                        mTBtnC8.setChecked(true);
//                    break;
//                case 8:
//                    mEdtC9.setText(MoneyOfMessageList.get(j).toString());
//                    sw = SwitchOfMessageList.get(j);
//                    if (sw.length() == 3){
//                        mTBtnC9.setChecked(false);
//                    } else
//                        mTBtnC9.setChecked(true);
//                    break;
//                case 9:
//                    mEdtC10.setText(MoneyOfMessageList.get(j).toString());
//                    sw = SwitchOfMessageList.get(j);
//                    if (sw.length() == 3){
//                        mTBtnC10.setChecked(false);
//                    } else
//                        mTBtnC10.setChecked(true);
//                    break;
//            }
        }

        if (firstopen == 0) {    //第一次安裝APP，沒有 ArrayList 時
            first();
        } else {
            setCreditCardInfoName();     //依ListView上項目的名稱，戴入至訊息欄上的TextView
            setCreditCardInfoLayoutVisible();   //依ListView的長度來打開設定資訊的Layout
            setListView(CreditCardList);    //載入ListView
        }
    }

    //未做過項目更新，則取得預設值
    private void first() {
        String[] CreditCard = getResources().getStringArray(R.array.selectCreditCard);
        CreditCardList = new ArrayList<>(Arrays.asList(CreditCard));
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.selectCreditCard, android.R.layout.simple_spinner_dropdown_item);
        mLvCreditCard.setAdapter(arrayAdapter);
        setCreditCardInfoName();     //依ListView上項目的名稱，戴入至訊息欄上的TextView
        setCreditCardInfoLayoutVisible();   //依ListView的長度來打開設定資訊的Layout
    }

    //判定刪除、更新鈕開關與限制信用卡的數量
    private void openAndCloseButton() {
        if (CreditCardList.size() == 10) {   //設定最大上限為10
            mBtnAddC.setEnabled(false);     //達到10則關閉新增鈕
        } else {
            mBtnAddC.setEnabled(true);      //反之打開新增鈕
        }

        if (index > -1) {
            mBtnUpdateC.setEnabled(true);    //開啟更新按鈕
            mBtnDeleteC.setEnabled(true);   //開啟刪除按鈕
        } else {
            mBtnUpdateC.setEnabled(false);    //關閉更新按鈕
            mBtnDeleteC.setEnabled(false);   //關閉刪除按鈕
        }
    }

    //更新ListView的方法
    private void setListView(ArrayList<String> arraylist) {
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arraylist);
        mLvCreditCard.setAdapter(adapter);
    }

    //新增自定選項到指定清單
    public void add(View v) {
        if (mEdtNewCreditCard.getText().toString().matches("")){
            toast.setText("請輸入信用卡名稱");
            toast.show();
        } else {
            CreditCardList.add(mEdtNewCreditCard.getText().toString());   //新增輸入值在"收入項目列表 "
        }
        setListView(CreditCardList);    //顯示新的"收入項目列表"
        index = -1; //改變判斷條件
        openAndCloseButton();   //改變 更新、與刪除鈕的功能
        mEdtNewCreditCard.setText(null);    //將輸入項目的EditText清空
        setCreditCardInfoName();     //依ListView上項目的名稱，戴入至訊息欄上的TextView
        setCreditCardInfoLayoutVisible();   //依ListView的長度來打開設定資訊的Layout
    }

    //按下更新鈕
    public void update(View view) {
        if ("".equals(mEdtNewCreditCard.getText().toString())){
            toast.setText("請輸入信用卡名稱");
            toast.show();
        } else {
            CreditCardList.set(index, mEdtNewCreditCard.getText().toString());  //將Edt上的資料更新至選定的Arraylist
        }
        setListView(CreditCardList);    //載入新的ListView
        index = -1; //改變判斷條件
        openAndCloseButton();   //改變 更新、與刪除鈕的功能
        mEdtNewCreditCard.setText(null);    //將輸入項目的EditText清空
        setCreditCardInfoName();     //依ListView上項目的名稱，戴入至訊息欄上的TextView
        setCreditCardInfoLayoutVisible();   //依ListView的長度來打開設定資訊的Layout
    }

    //按下刪除鈕
    public void delete(View view) {
        CreditCardList.remove(index);   //從ArrayList中移除選定的項目
        setListView(CreditCardList);    //載入新的ListView
        setCreditCardInfoLayoutInvisible(); //關閉訊息Layout
        mEdtNewCreditCard.setText(null);    //將輸入項目的EditText清空
        setToggleButtonOnOrOff();
    }

    //按下預設鈕
    public void Default(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);  //建立AlertDialog
        alertDialog.setTitle(R.string.confirmMessage)
                .setMessage(R.string.return_default_massage)
                .setCancelable(false)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        first();    //載入預設的String array
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .show();

        setListView(CreditCardList);    //載入新的ListView
        setCreditCardInfoName();        //依ListView上項目的名稱，戴入至訊息欄上的TextView
        setCreditCardInfoLayoutVisible();   //依ListView的長度來打開設定資訊的Layout
        mLyeoutC5.setVisibility(View.GONE); //關閉不顯示的Layout
        mLyeoutC6.setVisibility(View.GONE);
        mLyeoutC7.setVisibility(View.GONE);
        mLyeoutC8.setVisibility(View.GONE);
        mLyeoutC9.setVisibility(View.GONE);
        mLyeoutC10.setVisibility(View.GONE);

        tBtnindex = 0;  //關閉全部ToggleButton用的判斷值
        setToggleButtonOnOrOff();
    }

    //此頁面關閉時啟動
    public void save(View view){
        getPromptMessageDate();
        int CreditCardKey = -1;
        SharedPreferences.Editor editor = CreditCard.edit();    //簡碼的變型
        for (int a = 0; a < CreditCardList.size(); a++) { //以for迴圈跑 CreditCardList(ArrayList)次數
            String key = "CreditCard" + a ;   //設定key-values中 key的名子
            String moneykey = "MoneyOfMessage" + a ;
            String buttonkey = "SwitchOfMessage" +a ;
            CreditCardKey = a;  //提供用來決定載入次數的數據
            editor.putString(key, CreditCardList.get(a));  //放入 信用卡名稱
            editor.putLong(moneykey, MoneyOfMessageList.get(a)); //放入 信用卡訊息金額
            editor.putString(buttonkey, SwitchOfMessageList.get(a)); //收入 信用卡訊息開關
            editor.commit();    //寫入檔案
        }
        if (CreditCardList.size() == 0) {
            firstopen = 0;
        } else {
            firstopen = 1;
        }
        editor.putInt("FirstOpen", firstopen);
        editor.putInt("CreditCardKey", CreditCardKey);   //放入 最終要載入次數的數據
        editor.commit();    //寫入檔案

        toast.setText(R.string.date_seved);
        toast.show();
    }

    private void getPromptMessageDate(){
        for ( int a = 0 ; a < CreditCardList.size() ; a++){
            switch (a){
                case 0:
                    if ("".equals(mEdtC1.getText().toString())){
                        MoneyOfMessageList.add(0L);
                    }else {
                        MoneyOfMessageList.add(Long.parseLong(mEdtC1.getText().toString()));
                    }
                    SwitchOfMessageList.add(mTBtnC1.getText().toString());
                    break;
                case 1:
                    if ("".equals(mEdtC2.getText().toString())){
                        MoneyOfMessageList.add(0L);
                    }else {
                        MoneyOfMessageList.add(Long.parseLong(mEdtC2.getText().toString()));
                    }
                    SwitchOfMessageList.add(mTBtnC2.getText().toString());
                    break;
                case 2:
                    if ("".equals(mEdtC3.getText().toString())){
                        MoneyOfMessageList.add(0L);
                    }else {
                        MoneyOfMessageList.add(Long.parseLong(mEdtC3.getText().toString()));
                    }
                    SwitchOfMessageList.add(mTBtnC3.getText().toString());
                    break;
                case 3:
                    if ("".equals(mEdtC4.getText().toString())){
                        MoneyOfMessageList.add(0L);
                    }else {
                        MoneyOfMessageList.add(Long.parseLong(mEdtC4.getText().toString()));
                    }
                    SwitchOfMessageList.add(mTBtnC4.getText().toString());
                    break;
                case 4:
                    if ("".equals(mEdtC5.getText().toString())){
                        MoneyOfMessageList.add(0L);
                    }else {
                        MoneyOfMessageList.add(Long.parseLong(mEdtC5.getText().toString()));
                    }
                    SwitchOfMessageList.add(mTBtnC5.getText().toString());
                    break;
                case 5:
                    if ("".equals(mEdtC6.getText().toString())){
                        MoneyOfMessageList.add(0L);
                    }else {
                        MoneyOfMessageList.add(Long.parseLong(mEdtC6.getText().toString()));
                    }
                    SwitchOfMessageList.add(mTBtnC6.getText().toString());
                    break;
                case 6:
                    if ("".equals(mEdtC7.getText().toString())){
                        MoneyOfMessageList.add(0L);
                    }else {
                        MoneyOfMessageList.add(Long.parseLong(mEdtC7.getText().toString()));
                    }
                    SwitchOfMessageList.add(mTBtnC7.getText().toString());
                    break;
                case 7:
                    if ("".equals(mEdtC8.getText().toString())){
                        MoneyOfMessageList.add(0L);
                    }else {
                        MoneyOfMessageList.add(Long.parseLong(mEdtC8.getText().toString()));
                    }
                    SwitchOfMessageList.add(mTBtnC8.getText().toString());
                    break;
                case 8:
                    if ("".equals(mEdtC9.getText().toString())){
                        MoneyOfMessageList.add(0L);
                    }else {
                        MoneyOfMessageList.add(Long.parseLong(mEdtC9.getText().toString()));
                    }
                    SwitchOfMessageList.add(mTBtnC9.getText().toString());
                    break;
                case 9:
                    if ("".equals(mEdtC10.getText().toString())){
                        MoneyOfMessageList.add(0L);
                    }else {
                        MoneyOfMessageList.add(Long.parseLong(mEdtC10.getText().toString()));
                    }
                    SwitchOfMessageList.add(mTBtnC10.getText().toString());
                    break;
            }
        }
    }

    //按下Navigation Bar上的返回鍵時跳出確認訊息
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this).setTitle(R.string.confirmMessage)
                    .setMessage(R.string.creditcard_check_info)
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();
        }
        return true;
    }

    //依ListView的長度來打開設定資訊的Layout
    private void setCreditCardInfoLayoutVisible(){
        for (int a = 0 ; a < CreditCardList.size() ; a++ ){
            switch (a){
                case 0:
                    mLyeoutC1.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    mLyeoutC2.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    mLyeoutC3.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    mLyeoutC4.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    mLyeoutC5.setVisibility(View.VISIBLE);
                    break;
                case 5:
                    mLyeoutC6.setVisibility(View.VISIBLE);
                    break;
                case 6:
                    mLyeoutC7.setVisibility(View.VISIBLE);
                    break;
                case 7:
                    mLyeoutC8.setVisibility(View.VISIBLE);
                    break;
                case 8:
                    mLyeoutC9.setVisibility(View.VISIBLE);
                    break;
                case 9:
                    mLyeoutC10.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    //依ListView上項目的名稱，戴入至訊息欄上的TextView
    private void setCreditCardInfoName(){
        for (int a = 0 ; a < CreditCardList.size() ; a++ ){
            switch (a){
                case 0:
                    mTxtC1.setText(CreditCardList.get(a));
                    break;
                case 1:
                    mTxtC2.setText(CreditCardList.get(a));
                    break;
                case 2:
                    mTxtC3.setText(CreditCardList.get(a));
                    break;
                case 3:
                    mTxtC4.setText(CreditCardList.get(a));
                    break;
                case 4:
                    mTxtC5.setText(CreditCardList.get(a));
                    break;
                case 5:
                    mTxtC6.setText(CreditCardList.get(a));
                    break;
                case 6:
                    mTxtC7.setText(CreditCardList.get(a));
                    break;
                case 7:
                    mTxtC8.setText(CreditCardList.get(a));
                    break;
                case 8:
                    mTxtC9.setText(CreditCardList.get(a));
                    break;
                case 9:
                    mTxtC10.setText(CreditCardList.get(a));
                    break;
            }
        }
    }

    //依ListView的長度來開閉設定資訊的Layout
    private void setCreditCardInfoLayoutInvisible(){
        for (int a = 9 ; a >= CreditCardList.size() ; a-- ){
            switch (a) {
                case 0:
                    mLyeoutC1.setVisibility(View.GONE);
                    break;
                case 1:
                    mLyeoutC2.setVisibility(View.GONE);
                    break;
                case 2:
                    mLyeoutC3.setVisibility(View.GONE);
                    break;
                case 3:
                    mLyeoutC4.setVisibility(View.GONE);
                    break;
                case 4:
                    mLyeoutC5.setVisibility(View.GONE);
                    break;
                case 5:
                    mLyeoutC6.setVisibility(View.GONE);
                    break;
                case 6:
                    mLyeoutC7.setVisibility(View.GONE);
                    break;
                case 7:
                    mLyeoutC8.setVisibility(View.GONE);
                    break;
                case 8:
                    mLyeoutC9.setVisibility(View.GONE);
                    break;
                case 9:
                    mLyeoutC10.setVisibility(View.GONE);
                    break;
            }
        }
        index = -1;
        openAndCloseButton();
        setCreditCardInfoName();
    }

    //依刪除的索引值將排序之後的 ToggleButton 關閉
    private void setToggleButtonOnOrOff(){
        for (int a =tBtnindex ; a <= 9 ; a++){
            switch (a) {
                case 0:
                    mTBtnC1.setChecked(false);
                    break;
                case 1:
                    mTBtnC2.setChecked(false);
                    break;
                case 2:
                    mTBtnC3.setChecked(false);
                    break;
                case 3:
                    mTBtnC4.setChecked(false);
                    break;
                case 4:
                    mTBtnC5.setChecked(false);
                    break;
                case 5:
                    mTBtnC6.setChecked(false);
                    break;
                case 6:
                    mTBtnC7.setChecked(false);
                    break;
                case 7:
                    mTBtnC8.setChecked(false);
                    break;
                case 8:
                    mTBtnC9.setChecked(false);
                    break;
                case 9:
                    mTBtnC10.setChecked(false);
                    break;

            }
        }
    }
}
