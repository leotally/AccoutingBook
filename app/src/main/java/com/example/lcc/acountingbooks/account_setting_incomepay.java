package com.example.lcc.acountingbooks;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class account_setting_incomepay extends AppCompatActivity {
    EditText mEdtincome,mEdtpay;
    ListView mLvI,mLvP;
    Button mBtnAddI,mBtnDeleteI,mBtnUpdateI,mBtnDefaultI,mBtnAddP,mBtnDeleteP,mBtnUpdateP,mBtnDefaultP;
    int indexP = -1 , indexI =-1 ;
    ArrayList<String> incomeItemList = new ArrayList<>();
    ArrayList<String> payItemList = new ArrayList<>();
    ArrayAdapter adapterI,adapterP;
    static String incomeitemName = "incomeItem" , payitemName = "payItem";  //儲存資料檔案的名子
    static String incomekey = "incomekey" , paykey = "paykey";
    static String incomefirst = "incomefirst" , payfirst = "payfirst";
    static String income = "income" , pay = "pay";
    int firstI=0,firstP=0; //用於判斷是否為新安裝第一次使用，預設0
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_setting_incomepay);
        myFindView();
        mBtnUpdateI.setEnabled(false);
        mBtnDeleteI.setEnabled(false);
        mBtnUpdateP.setEnabled(false);
        mBtnDeleteP.setEnabled(false);

        toast = Toast.makeText(this , "" , Toast.LENGTH_SHORT);

        //建立從檔案 "incomeItem" 取得資料的物件
        SharedPreferences getPayDate = getSharedPreferences(payitemName,MODE_PRIVATE);
        int payitemKey = getPayDate.getInt(paykey,-1);
        firstP = getPayDate.getInt(payfirst,0);
        int p = 0;
        for (int i = payitemKey; i >= 0; i--, p++){
            String key = pay + p;
            payItemList.add(getPayDate.getString(key,null));
        }

        SharedPreferences getIncomeDate = getSharedPreferences(incomeitemName,MODE_PRIVATE);
        int incomeitemKey = getIncomeDate.getInt(incomekey,-1);   //讀取檔案中 "載入次數"的數據
        firstI = getIncomeDate.getInt(incomefirst,0);  //戴入是否為第一次使用的判斷值，無值則為0
        int j = 0;  //提供改變 key-values中 key的名子使用
        for (int i = incomeitemKey; i >= 0; i--, j++) {
            String key = income + j;
            incomeItemList.add(getIncomeDate.getString(key, null)); //依檔案數量，依次寫入 CreditCardList
        }

        //判斷 ArrayList 內是否有資料，進而選擇是將存檔或是預設內容載入 ArrayList
        if (firstI == 0 && firstP == 0){
            first();
        } else if (firstP == 0){
            firstP();
            setLvI(incomeItemList);
        } else if (firstI == 0) {
            firstI();
            setLvP(payItemList);
        } else{
            setLvI(incomeItemList);
            setLvP(payItemList);
        }
    }

    private void myFindView(){
        mEdtincome = (EditText) findViewById(R.id.edtIncome);
        mEdtpay = (EditText) findViewById(R.id.edtPay);
        mBtnAddI = (Button) findViewById(R.id.btnAddI);
        mBtnAddP = (Button) findViewById(R.id.btnAddP);
        mBtnDeleteI = (Button) findViewById(R.id.btnDeleteI);
        mBtnDeleteP = (Button) findViewById(R.id.btnDeleteP);
        mBtnUpdateI = (Button) findViewById(R.id.btnUpdateI);
        mBtnUpdateP = (Button) findViewById(R.id.btnUpdateP);
        mBtnDefaultI = (Button) findViewById(R.id.btnDefaultI);
        mBtnDefaultP = (Button) findViewById(R.id.btnDefaultP);


        mLvI = (ListView) findViewById(R.id.lvIncome);
        mLvI.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int p, long l) {
                indexI = p;
                mEdtincome.setText(((TextView) view).getText()); //取值放入edt
                openAndCloseButtonI();   //改變 更新、與刪除鈕的功能
            }
        });
        mLvP = (ListView) findViewById(R.id.lvPay);
        mLvP.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                indexP = i;
                mEdtpay.setText(((TextView) view).getText());   //取值放入edt
                openAndCloseButtonP();   //改變 更新、與刪除鈕的功能
            }
        });
    }

    private void first(){
        String[] incomeItem = getResources().getStringArray(R.array.income_item);
        incomeItemList = new ArrayList<>(Arrays.asList(incomeItem));
        adapterI =
                new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,incomeItemList);
        mLvI.setAdapter(adapterI);

        String[] payItme = getResources().getStringArray(R.array.expense_item);
        payItemList = new ArrayList<>(Arrays.asList(payItme));
        adapterP =
                new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,payItemList);
        mLvP.setAdapter(adapterP);
    }

    private void firstI(){
        String[] incomeItem = getResources().getStringArray(R.array.income_item);
        incomeItemList = new ArrayList<>(Arrays.asList(incomeItem));
        adapterI =
                new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,incomeItemList);
        mLvI.setAdapter(adapterI);
    }

    private void firstP(){
        String[] payItme = getResources().getStringArray(R.array.expense_item);
        payItemList = new ArrayList<>(Arrays.asList(payItme));
        adapterP =
                new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,payItemList);
        mLvP.setAdapter(adapterP);
    }

    private void setLvI(ArrayList<String> arraylist) { //更新收入ListView的方法
        adapterI = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arraylist);
        mLvI.setAdapter(adapterI);
    }

    private void setLvP(ArrayList<String> arraylist) { //更新支出ListView的方法
        adapterP = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arraylist);
        mLvP.setAdapter(adapterP);
    }

    private void openAndCloseButtonI() {   //改變收入欄的按鈕功能
        if (indexI > -1) {
            mBtnUpdateI.setEnabled(true);    //開啟更新按鈕
            mBtnDeleteI.setEnabled(true);   //開啟刪除按鈕
        } else {
            mBtnUpdateI.setEnabled(false);    //關閉更新按鈕
            mBtnDeleteI.setEnabled(false);   //關閉刪除按鈕
        }
    }

    private void openAndCloseButtonP(){     //改變支出欄的按鈕功能
        if (indexP > -1) {
            mBtnUpdateP.setEnabled(true);    //開啟更新按鈕
            mBtnDeleteP.setEnabled(true);   //開啟刪除按鈕
        } else {
            mBtnUpdateP.setEnabled(false);    //關閉更新按鈕
            mBtnDeleteP.setEnabled(false);   //關閉刪除按鈕
        }
    }

    protected void onDestroy(){
        super.onDestroy();
        int incomeitemKey = -1; //預設載入次數的變數
        //設定儲存收入項目的檔案名
        SharedPreferences incomeitem = getSharedPreferences(incomeitemName,MODE_PRIVATE);
        SharedPreferences.Editor editor = incomeitem.edit();
        for (int a = 0 ; a < incomeItemList.size() ; a++){  //以for迴圈跑(AyyayList)次數
            String key = income + a;  //設定key-values中 key的名子
            incomeitemKey = a;  //提供用來決定載入次數的數據
            editor.putString(key,incomeItemList.get(a));   //依次將ArrayList內的資料放入 key-values
            editor.commit();    //寫入檔案
        }
        if (incomeItemList.size() == 0){ //改變是否未從做過設定的判斷變數
            firstI = 0;
        } else {
            firstI = 1;
        }
        editor.putInt(incomefirst,firstI);  //放入未設定過的判斷變入
        editor.putInt(incomekey,incomeitemKey); //放入最後取得的載入次數
        editor.commit();    //寫入檔案

        int payitemKey = -1;    //預設載入次數的變數
        //設定儲存支出項目的檔案名
        SharedPreferences payitem = getSharedPreferences(payitemName,MODE_PRIVATE);
        SharedPreferences.Editor editor1 = payitem.edit();
        for (int a = 0 ; a < payItemList.size() ; a++){
            String key = pay + a;
            payitemKey = a;  //提供用來決定載入次數的數據
            editor1.putString(key,payItemList.get(a)); //依次將ArrayList內的資料放入 key-values
            editor1.commit();   //寫入檔案
        }
        if (payItemList.size() == 0){   //改變是否未從做過設定的判斷變數
            firstP = 0;
        } else {
            firstP = 1;
        }
        editor1.putInt(payfirst,firstP);    //放入未設定過的判斷變入
        editor1.putInt(paykey,payitemKey); //放入最後取得的載入次數
        editor1.commit();   //寫入檔案
    }

    //----收入的按鍵動作↓
    public void addI(View v) {        //新增自定選項到指定清單
        if (mEdtincome.getText().toString().matches("")){
            toast.setText(R.string.enter_info);
            toast.show();
        } else {
            incomeItemList.add(mEdtincome.getText().toString());   //新增輸入值在"收入項目列表 "
        }
        setLvI(incomeItemList);    //顯示新的"收入項目列表"
        indexI = -1; //改變判斷條件
        openAndCloseButtonI();   //改變 更新、與刪除鈕的功能
        mEdtincome.setText(null);    //將輸入項目的EditText清空
    }

    public void updateI(View view) {     //按下更新鈕
        incomeItemList.set(indexI, mEdtincome.getText().toString());  //將Edt上的資料更新至選定的Arraylist
        setLvI(incomeItemList);    //載入新的ListView
        indexI = -1; //改變判斷條件
        openAndCloseButtonI();   //改變 更新、與刪除鈕的功能
        mEdtincome.setText(null);    //將輸入項目的EditText清空
    }

    public void deleteI(View view) {     //按下刪除鈕
        incomeItemList.remove(indexI);   //從ArrayList中移除選定的項目
        setLvI(incomeItemList);    //載入新的ListView
        mBtnUpdateI.setEnabled(false);
        mBtnDeleteI.setEnabled(false);
        mEdtincome.setText(null);    //將輸入項目的EditText清空
    }

    public void DefaultI(View view) {       //按下預設鈕
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.confirmMessage)
                .setMessage(R.string.return_default_massage)
                .setCancelable(false)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        firstI();    //載入預設的String array
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .show();
        setLvI(incomeItemList);    //載入新的ListView
    }
    //----收入的按鍵動作↑

    //----支出的按鍵動作↓
    public void addP(View v) {        //新增自定選項到指定清單
        if (mEdtpay.getText().toString().matches("")){
            toast.setText(R.string.enter_info);
            toast.show();
        } else {
            payItemList.add(mEdtpay.getText().toString());   //新增輸入值在"收入項目列表 "
        }
        setLvP(payItemList);    //顯示新的"收入項目列表"
        indexP = -1; //改變判斷條件
        openAndCloseButtonP();   //改變 更新、與刪除鈕的功能
        mEdtpay.setText(null);    //將輸入項目的EditText清空
    }

    public void updateP(View view) {     //按下更新鈕
        payItemList.set(indexP, mEdtpay.getText().toString());  //將Edt上的資料更新至選定的Arraylist
        setLvP(payItemList);    //載入新的ListView
        indexP = -1; //改變判斷條件
        openAndCloseButtonP();   //改變 更新、與刪除鈕的功能
        mEdtpay.setText(null);    //將輸入項目的EditText清空
    }

    public void deleteP(View view) {     //按下刪除鈕
        payItemList.remove(indexP);   //從ArrayList中移除選定的項目
        setLvP(payItemList);    //載入新的ListView
        mBtnUpdateP.setEnabled(false);
        mBtnDeleteP.setEnabled(false);
        mEdtpay.setText(null);    //將輸入項目的EditText清空
    }

    public void DefaultP(View view) {       //按下預設鈕
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(R.string.confirmMessage)
                .setMessage(R.string.return_default_massage)
                .setCancelable(false)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        firstP();    //載入預設的String array
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .show();
        setLvP(payItemList);    //載入新的ListView
    }
    //----支出的按鍵動作↑
}
