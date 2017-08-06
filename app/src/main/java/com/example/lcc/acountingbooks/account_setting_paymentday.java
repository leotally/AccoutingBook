package com.example.lcc.acountingbooks;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class account_setting_paymentday extends AppCompatActivity {
    static final String PaymentDayName = "PaymentDay";    //繳費日項目資料檔名稱
    ListView mLvPD;
    EditText mEdtBillName,mEdtPaymentDay;
    Button mBtnDeletePD,mBtnUpdatePD;
    Toast toast;
    SharedPreferences PaymentDay ;
    MyAdapter adapter = null;

    ArrayList<String> BillNameList = new ArrayList<>();
    ArrayList<Integer> PaymentDayList = new ArrayList<>();
    String [] billname;
    int [] paymentday;
    int index = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_setting_paymentday);
        toast = Toast.makeText(this, "" ,Toast.LENGTH_SHORT);
        myFindView();
        LoadingDate();
    }

    //監聽與連結
    private void myFindView(){
        mEdtBillName = (EditText) findViewById(R.id.edtBillName);
        mEdtPaymentDay = (EditText) findViewById(R.id.edtPaymentDay);
        mBtnDeletePD = (Button) findViewById(R.id.btnDeletePD);
        mBtnUpdatePD = (Button) findViewById(R.id.btnUpdatePD);
        mLvPD = (ListView) findViewById(R.id.lvPD);
        mLvPD.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                index = position;  //取索引值
                openAndCloseButton();
                mEdtBillName.setText(BillNameList.get(index));
                mEdtPaymentDay.setText(""+PaymentDayList.get(index));
            }
        });
    }

    //按下新增鈕將輸入資料存入ArrayList
    public void add(View view){
        if (mEdtBillName.getText().toString().length() == 0){
            toast.setText(R.string.pleaseEnterBillName);
            toast.show();
        } else if (mEdtPaymentDay.getText().toString().length() == 0){
            toast.setText(getString(R.string.pleaseEnter)+getString(R.string.monthOfDay));
            toast.show();
        } else if (Integer.parseInt(mEdtPaymentDay.getText().toString()) < 1 ||
                Integer.parseInt(mEdtPaymentDay.getText().toString()) > 32){
            toast.setText(getString(R.string.pleaseEnter)+getString(R.string.monthOfDay));
            toast.show();
        } else {
            BillNameList.add(mEdtBillName.getText().toString());
            PaymentDayList.add(Integer.parseInt(mEdtPaymentDay.getText().toString()));
            transArray();
            setAdapter();
        }
        index = -1;
        mEdtBillName.setText(null);
        mEdtPaymentDay.setText(null);
        openAndCloseButton();
    }

    //更新、刪除鈕開關判斷式
    private void openAndCloseButton() {
        if (index > -1) {
            mBtnUpdatePD.setEnabled(true);    //開啟更新按鈕
            mBtnDeletePD.setEnabled(true);   //開啟刪除按鈕
        } else {
            mBtnUpdatePD.setEnabled(false);    //關閉更新按鈕
            mBtnDeletePD.setEnabled(false);   //關閉刪除按鈕
        }
    }

    //將ArrayList 轉換成 StringArray與IntegerArray
    private void transArray(){
        billname = new String[BillNameList.size()];
        for (int a = 0 ; a < BillNameList.size() ; a++){
            billname[a] = BillNameList.get(a);
        }
        paymentday = new int [PaymentDayList.size()];
        for (int b = 0 ; b < PaymentDayList.size() ; b++){
            paymentday[b] = PaymentDayList.get(b);
        }
    }

    //載入ListView
    private void setAdapter(){
        adapter  = new MyAdapter(this);
        mLvPD.setAdapter(adapter);
    }

    //更新ArrayList裡的數據
    public void update(View view){
        if("".equals(mEdtBillName.getText().toString())){
            toast.setText(R.string.pleaseEnterBillName);
            toast.show();
        } else if ("".equals(mEdtPaymentDay.getText().toString())){
            toast.setText(getString(R.string.pleaseEnter)+getString(R.string.monthOfDay));
            toast.show();
        } else if (Integer.parseInt(mEdtPaymentDay.getText().toString()) < 1 ||
                Integer.parseInt(mEdtPaymentDay.getText().toString()) > 32) {
            toast.setText(getString(R.string.pleaseEnter) + getString(R.string.monthOfDay));
            toast.show();
        } else {
            BillNameList.set(index,mEdtBillName.getText().toString());
            PaymentDayList.set(index,Integer.parseInt(mEdtPaymentDay.getText().toString()));
            transArray();
            setAdapter();
        }
        index = -1;
        mEdtBillName.setText(null);
        mEdtPaymentDay.setText(null);
        openAndCloseButton();
    }

    //刪除ArrayList裡的數據
    public void delete(View view){
        BillNameList.remove(index);
        PaymentDayList.remove(index);
        transArray();
        setAdapter();
        mEdtBillName.setText(null);
        mEdtPaymentDay.setText(null);
        mBtnUpdatePD.setEnabled(false);    //關閉更新按鈕
        mBtnDeletePD.setEnabled(false);   //關閉刪除按鈕
    }

    public void save(View view){
        PaymentDay = getSharedPreferences(PaymentDayName,MODE_PRIVATE);
        SharedPreferences.Editor editor = PaymentDay.edit();
        int PaymentDayKey = -1;
        for (int a = 0 ; a < BillNameList.size() ; a++){
            String nameKey = "PDName" + a ;
            String dayKey = "PDDay" + a;
            PaymentDayKey = a;
            editor.putString(nameKey,BillNameList.get(a));
            editor.putInt(dayKey,PaymentDayList.get(a));
            editor.commit();
        }
        editor.putInt("PDKey",PaymentDayKey);
        editor.commit();
        toast.setText(R.string.dateSaved);
        toast.show();
    }

    private void LoadingDate() {
        SharedPreferences getDate = getSharedPreferences(PaymentDayName, MODE_PRIVATE);
        int PaymentDayKey = getDate.getInt("PDKey", -1);
        int j = 0;
        for (int a = PaymentDayKey; a >= 0; a--, j++) {
            String nameKey = "PDName" + j;
            String dayKey = "PDDay" + j;
            BillNameList.add(getDate.getString(nameKey, null));
            PaymentDayList.add(getDate.getInt(dayKey, 0));
        }
        if (BillNameList.size() > 0 ){
            transArray();
            setAdapter();
            openAndCloseButton();
        }
    }

    //自訂Adapter將資料載入ListView
    class MyAdapter extends BaseAdapter {
        LayoutInflater myInflater;

        public MyAdapter(account_setting_paymentday m) {
            myInflater = LayoutInflater.from(m);
        }

        @Override
        public int getCount() {
            return billname.length;
        }

        @Override
        public Object getItem(int position) {
            return billname[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            view = myInflater.inflate(R.layout.listview_payment_day_setting,null);

            TextView mTxtName = (TextView) view.findViewById(R.id.name);
            TextView mTxtDay = (TextView) view.findViewById(R.id.day);

            mTxtName.setText(billname[position]);
            mTxtDay.setText(""+paymentday[position]);
            return view;
        }
    }
}
