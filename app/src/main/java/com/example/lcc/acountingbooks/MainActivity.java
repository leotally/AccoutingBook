package com.example.lcc.acountingbooks;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import java.util.Calendar;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static final String TB_NAME = "AccountNote";    //資料表名稱
    static final String CreditCardName = "CreditCard";    //信用卡項目資料檔名稱
    static final String ExpenseQuotaName = "ExpenseQuota";  //每月警示資料檔名稱
    static final String IndexViewName = "IndexView";    //首頁樣式資料檔名稱
    static final String [] Day_Information = new String []{"item","money","info"};
    TextView mThisYearPay, mThisYearIncome, mThisYearTotalView,
            mThisMonthPay, mThisMonthIncome,mThisMonthTotalView;
    Long mThisYearTotalPay, mThisYearTotalIncome, mThisYearTotal,
            mThisMonthTotalPay,mThisMonthTotalIncome,mThisMonthTotal;
    String SMS,EQS,PDS,CCS;

    MySQLiteOpenHelper helper;
    Cursor cursor,cursor1;
    Calendar c = Calendar.getInstance();

    TextView mTxtC1Name, mTxtC1Money, mTxtC2Name, mTxtC2Money,
            mTxtC3Name, mTxtC3Money, mTxtC4Name, mTxtC4Money,
            mTxtC5Name, mTxtC5Money, mTxtC6Name, mTxtC6Money,
            mTxtC7Name, mTxtC7Money, mTxtC8Name, mTxtC8Money,
            mTxtC9Name, mTxtC9Money, mTxtC10Name, mTxtC10Money,
            mTxtC1_1, mTxtC1_2, mTxtC2_1, mTxtC2_2, mTxtC3_1, mTxtC3_2, mTxtC4_1, mTxtC4_2,
            mTxtC5_1, mTxtC5_2, mTxtC6_1, mTxtC6_2, mTxtC7_1, mTxtC7_2, mTxtC8_1, mTxtC8_2,
            mTxtC9_1, mTxtC9_2, mTxtC10_1, mTxtC10_2;
    ImageView mIvC1Up, mIvC2Up, mIvC1Down, mIvC2Down, mIvC3Up, mIvC4Up, mIvC3Down, mIvC4Down,
            mIvC5Up, mIvC6Up, mIvC5Down, mIvC6Down, mIvC7Up, mIvC8Up, mIvC7Down, mIvC8Down,
            mIvC9Up, mIvC10Up, mIvC9Down, mIvC10Down;
    android.support.constraint.ConstraintLayout
            mCreditCardLayout1, mCreditCardLayout2, mCreditCardLayout3, mCreditCardLayout4,
            mCreditCardLayout5, mCreditCardLayout6, mCreditCardLayout7, mCreditCardLayout8,
            mCreditCardLayout9, mCreditCardLayout10,
            mCLayout1_1, mCLayout1_2, mCLayout2_1, mCLayout2_2, mCLayout3_1, mCLayout3_2, mCLayout4_1, mCLayout4_2,
            mCLayout5_1, mCLayout5_2, mCLayout6_1, mCLayout6_2, mCLayout7_1, mCLayout7_2, mCLayout8_1, mCLayout8_2,
            mCLayout9_1, mCLayout9_2, mCLayout10_1, mCLayout10_2;
    int index1 = 0, index2 = 0, index3 = 0, index4 = 0, index5 = 0,
            index6 = 0, index7 = 0, index8 = 0, index9 = 0, index10 = 0;

    TextView mMoneyOfDay,mNoConsumption;
    ScrollView mInfoOfDay;
    ListView mLvOfDay;

    ImageView mIvRed,mIvGreen,mIvYellow;
    TextView mExpenseQuotaMonth;

    android.support.constraint.ConstraintLayout mSumOfMoney,mExpenseQuota,mPayOfDay,mCreditCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper = MySQLiteOpenHelper.getInstance(this);
        getSupportActionBar().hide();   //隱藏ActionBar
        myFindView();   //連結與監聽
        FirstInView();  //程式開啟時信用卡訊息欄位顯示方式

        //----顯示本年度支出、收入與總合的方法----//
        thisYearTotalPay();
        thisYearTotalIncome();
        thisYearTotal();
        //----顯示本年度收入、支出與總合的方法----//

        //----顯示本年度當月收入、支出與總合的方法----//
        thisMonthPay();
        thisMonthIncome();
        thisMonthTotal();
        //----顯示本年度當月收入、支出與總合的方法----//

        //----顯本每日消費資訊----//
        PayOfDay();
        //----顯本每日消費資訊----//

        //----顯示每月消費警示----//
        ExpenseQuotaOfMonth();
        //----顯示每月消費警示----//

        //----首頁欄位顯示樣式----//
        IndexView();
        //----首頁欄位顯示樣式----//
    }

    private void myFindView() {
        mThisYearPay = (TextView) findViewById(R.id.txtThisYearPay);
        mThisYearIncome = (TextView) findViewById(R.id.txtThisYearIncome);
        mThisYearTotalView = (TextView) findViewById(R.id.txtThisYearTatal);
        mThisMonthPay = (TextView) findViewById(R.id.txtThisMonthPay);
        mThisMonthIncome = (TextView) findViewById(R.id.txtThisMonthIncome);
        mThisMonthTotalView = (TextView) findViewById(R.id.txtThisMonthTatal);

        //--以下為首頁顯示欄位所用連結
        mSumOfMoney = (android.support.constraint.ConstraintLayout) findViewById(R.id.SumOfMoney);
        mExpenseQuota = (android.support.constraint.ConstraintLayout) findViewById(R.id.ExpenseQuota);
        mPayOfDay = (android.support.constraint.ConstraintLayout) findViewById(R.id.PayOfDay);
        mCreditCard = (android.support.constraint.ConstraintLayout) findViewById(R.id.CreditCard);

        //--以下為每日消費資訊所用連結
        mMoneyOfDay = (TextView) findViewById(R.id.MoneyOfDay);
        mNoConsumption = (TextView) findViewById(R.id.noConsumption);
        mInfoOfDay = (ScrollView) findViewById(R.id.infoOfDay);
        mLvOfDay = (ListView) findViewById(R.id.LvOfDay);

        //---以下為每月消費警示所用連結
        mIvRed = (ImageView) findViewById(R.id.red);
        mIvGreen = (ImageView) findViewById(R.id.green);
        mIvYellow = (ImageView) findViewById(R.id.yellow);
        mExpenseQuotaMonth = (TextView) findViewById(R.id.txtExpenseQuotaMonth);

        //----以下為信用卡提示所用連結
        //-----第一張卡(依欄位順序)
        mCreditCardLayout1 = (android.support.constraint.ConstraintLayout) findViewById(R.id.CreditCardLayout1);
        mCLayout1_1 = (android.support.constraint.ConstraintLayout) findViewById(R.id.CLayout1_1);
        mCLayout1_1.setOnClickListener(this);
        mTxtC1Name = (TextView) findViewById(R.id.txtC1Name);
        mTxtC1Money = (TextView) findViewById(R.id.txtC1Money);
        mIvC1Up = (ImageView) findViewById(R.id.c1Up);
        mIvC1Down = (ImageView) findViewById(R.id.c1Down);
        mCLayout1_2 = (android.support.constraint.ConstraintLayout) findViewById(R.id.CLayout1_2);
        mTxtC1_1 = (TextView) findViewById(R.id.txtC1_1Money);
        mTxtC1_2 = (TextView) findViewById(R.id.txtC1_2Money);

        //-----第二張卡(依欄位順序)
        mCreditCardLayout2 = (android.support.constraint.ConstraintLayout) findViewById(R.id.CreditCardLayout2);
        mCLayout2_1 = (android.support.constraint.ConstraintLayout) findViewById(R.id.CLayout2_1);
        mCLayout2_1.setOnClickListener(this);
        mTxtC2Name = (TextView) findViewById(R.id.txtC2Name);
        mTxtC2Money = (TextView) findViewById(R.id.txtC2Money);
        mIvC2Up = (ImageView) findViewById(R.id.c2Up);
        mIvC2Down = (ImageView) findViewById(R.id.c2Down);
        mCLayout2_2 = (android.support.constraint.ConstraintLayout) findViewById(R.id.CLayout2_2);
        mTxtC2_1 = (TextView) findViewById(R.id.txtC2_1Money);
        mTxtC2_2 = (TextView) findViewById(R.id.txtC2_2Money);

        //-----第三張卡(依欄位順序)
        mCreditCardLayout3 = (android.support.constraint.ConstraintLayout) findViewById(R.id.CreditCardLayout3);
        mCLayout3_1 = (android.support.constraint.ConstraintLayout) findViewById(R.id.CLayout3_1);
        mCLayout3_1.setOnClickListener(this);
        mTxtC3Name = (TextView) findViewById(R.id.txtC3Name);
        mTxtC3Money = (TextView) findViewById(R.id.txtC3Money);
        mIvC3Up = (ImageView) findViewById(R.id.c3Up);
        mIvC3Down = (ImageView) findViewById(R.id.c3Down);
        mCLayout3_2 = (android.support.constraint.ConstraintLayout) findViewById(R.id.CLayout3_2);
        mTxtC3_1 = (TextView) findViewById(R.id.txtC3_1Money);
        mTxtC3_2 = (TextView) findViewById(R.id.txtC3_2Money);

        //-----第四張卡(依欄位順序)
        mCreditCardLayout4 = (android.support.constraint.ConstraintLayout) findViewById(R.id.CreditCardLayout4);
        mCLayout4_1 = (android.support.constraint.ConstraintLayout) findViewById(R.id.CLayout4_1);
        mCLayout4_1.setOnClickListener(this);
        mTxtC4Name = (TextView) findViewById(R.id.txtC4Name);
        mTxtC4Money = (TextView) findViewById(R.id.txtC4Money);
        mIvC4Up = (ImageView) findViewById(R.id.c4Up);
        mIvC4Down = (ImageView) findViewById(R.id.c4Down);
        mCLayout4_2 = (android.support.constraint.ConstraintLayout) findViewById(R.id.CLayout4_2);
        mTxtC4_1 = (TextView) findViewById(R.id.txtC4_1Money);
        mTxtC4_2 = (TextView) findViewById(R.id.txtC4_2Money);

        //-----第五張卡(依欄位順序)
        mCreditCardLayout5 = (android.support.constraint.ConstraintLayout) findViewById(R.id.CreditCardLayout5);
        mCLayout5_1 = (android.support.constraint.ConstraintLayout) findViewById(R.id.CLayout5_1);
        mCLayout5_1.setOnClickListener(this);
        mTxtC5Name = (TextView) findViewById(R.id.txtC5Name);
        mTxtC5Money = (TextView) findViewById(R.id.txtC5Money);
        mIvC5Up = (ImageView) findViewById(R.id.c5Up);
        mIvC5Down = (ImageView) findViewById(R.id.c5Down);
        mCLayout5_2 = (android.support.constraint.ConstraintLayout) findViewById(R.id.CLayout5_2);
        mTxtC5_1 = (TextView) findViewById(R.id.txtC5_1Money);
        mTxtC5_2 = (TextView) findViewById(R.id.txtC5_2Money);

        //-----第六張卡(依欄位順序)
        mCreditCardLayout6 = (android.support.constraint.ConstraintLayout) findViewById(R.id.CreditCardLayout6);
        mCLayout6_1 = (android.support.constraint.ConstraintLayout) findViewById(R.id.CLayout6_1);
        mCLayout6_1.setOnClickListener(this);
        mTxtC6Name = (TextView) findViewById(R.id.txtC6Name);
        mTxtC6Money = (TextView) findViewById(R.id.txtC6Money);
        mIvC6Up = (ImageView) findViewById(R.id.c6Up);
        mIvC6Down = (ImageView) findViewById(R.id.c6Down);
        mCLayout6_2 = (android.support.constraint.ConstraintLayout) findViewById(R.id.CLayout6_2);
        mTxtC6_1 = (TextView) findViewById(R.id.txtC6_1Money);
        mTxtC6_2 = (TextView) findViewById(R.id.txtC6_2Money);

        //-----第七張卡(依欄位順序)
        mCreditCardLayout7 = (android.support.constraint.ConstraintLayout) findViewById(R.id.CreditCardLayout7);
        mCLayout7_1 = (android.support.constraint.ConstraintLayout) findViewById(R.id.CLayout7_1);
        mCLayout7_1.setOnClickListener(this);
        mTxtC7Name = (TextView) findViewById(R.id.txtC7Name);
        mTxtC7Money = (TextView) findViewById(R.id.txtC7Money);
        mIvC7Up = (ImageView) findViewById(R.id.c7Up);
        mIvC7Down = (ImageView) findViewById(R.id.c7Down);
        mCLayout7_2 = (android.support.constraint.ConstraintLayout) findViewById(R.id.CLayout7_2);
        mTxtC7_1 = (TextView) findViewById(R.id.txtC7_1Money);
        mTxtC7_2 = (TextView) findViewById(R.id.txtC7_2Money);

        //-----第八張卡(依欄位順序)
        mCreditCardLayout8 = (android.support.constraint.ConstraintLayout) findViewById(R.id.CreditCardLayout8);
        mCLayout8_1 = (android.support.constraint.ConstraintLayout) findViewById(R.id.CLayout8_1);
        mCLayout8_1.setOnClickListener(this);
        mTxtC8Name = (TextView) findViewById(R.id.txtC8Name);
        mTxtC8Money = (TextView) findViewById(R.id.txtC8Money);
        mIvC8Up = (ImageView) findViewById(R.id.c8Up);
        mIvC8Down = (ImageView) findViewById(R.id.c8Down);
        mCLayout8_2 = (android.support.constraint.ConstraintLayout) findViewById(R.id.CLayout8_2);
        mTxtC8_1 = (TextView) findViewById(R.id.txtC8_1Money);
        mTxtC8_2 = (TextView) findViewById(R.id.txtC8_2Money);

        //-----第九張卡(依欄位順序)
        mCreditCardLayout9 = (android.support.constraint.ConstraintLayout) findViewById(R.id.CreditCardLayout9);
        mCLayout9_1 = (android.support.constraint.ConstraintLayout) findViewById(R.id.CLayout9_1);
        mCLayout9_1.setOnClickListener(this);
        mTxtC9Name = (TextView) findViewById(R.id.txtC9Name);
        mTxtC9Money = (TextView) findViewById(R.id.txtC9Money);
        mIvC9Up = (ImageView) findViewById(R.id.c9Up);
        mIvC9Down = (ImageView) findViewById(R.id.c9Down);
        mCLayout9_2 = (android.support.constraint.ConstraintLayout) findViewById(R.id.CLayout9_2);
        mTxtC9_1 = (TextView) findViewById(R.id.txtC9_1Money);
        mTxtC9_2 = (TextView) findViewById(R.id.txtC9_2Money);

        //-----第十張卡(依欄位順序)
        mCreditCardLayout10 = (android.support.constraint.ConstraintLayout) findViewById(R.id.CreditCardLayout10);
        mCLayout10_1 = (android.support.constraint.ConstraintLayout) findViewById(R.id.CLayout10_1);
        mCLayout10_1.setOnClickListener(this);
        mTxtC10Name = (TextView) findViewById(R.id.txtC10Name);
        mTxtC10Money = (TextView) findViewById(R.id.txtC10Money);
        mIvC10Up = (ImageView) findViewById(R.id.c10Up);
        mIvC10Down = (ImageView) findViewById(R.id.c10Down);
        mCLayout10_2 = (android.support.constraint.ConstraintLayout) findViewById(R.id.CLayout10_2);
        mTxtC10_1 = (TextView) findViewById(R.id.txtC10_1Money);
        mTxtC10_2 = (TextView) findViewById(R.id.txtC10_2Money);
    }

    @Override   //按下母欄位可展開子欄位
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.CLayout1_1:
                if (index1 == 0) {
                    mIvC1Down.setVisibility(View.GONE);
                    mIvC1Up.setVisibility(View.VISIBLE);
                    mCLayout1_2.setVisibility(View.VISIBLE);
                    index1 = 1;
                } else {
                    mIvC1Down.setVisibility(View.VISIBLE);
                    mIvC1Up.setVisibility(View.GONE);
                    mCLayout1_2.setVisibility(View.GONE);
                    index1 = 0;
                }
                break;
            case R.id.CLayout2_1:
                if (index2 == 0) {
                    mIvC2Down.setVisibility(View.GONE);
                    mIvC2Up.setVisibility(View.VISIBLE);
                    mCLayout2_2.setVisibility(View.VISIBLE);
                    index2 = 1;
                } else {
                    mIvC2Down.setVisibility(View.VISIBLE);
                    mIvC2Up.setVisibility(View.GONE);
                    mCLayout2_2.setVisibility(View.GONE);
                    index2 = 0;
                }
                break;
            case R.id.CLayout3_1:
                if (index3 == 0) {
                    mIvC3Down.setVisibility(View.GONE);
                    mIvC3Up.setVisibility(View.VISIBLE);
                    mCLayout3_2.setVisibility(View.VISIBLE);
                    index3 = 1;
                } else {
                    mIvC3Down.setVisibility(View.VISIBLE);
                    mIvC3Up.setVisibility(View.GONE);
                    mCLayout3_2.setVisibility(View.GONE);
                    index3 = 0;
                }
                break;
            case R.id.CLayout4_1:
                if (index4 == 0) {
                    mIvC4Down.setVisibility(View.GONE);
                    mIvC4Up.setVisibility(View.VISIBLE);
                    mCLayout4_2.setVisibility(View.VISIBLE);
                    index4 = 1;
                } else {
                    mIvC4Down.setVisibility(View.VISIBLE);
                    mIvC4Up.setVisibility(View.GONE);
                    mCLayout4_2.setVisibility(View.GONE);
                    index4 = 0;
                }
                break;
            case R.id.CLayout5_1:
                if (index5 == 0) {
                    mIvC5Down.setVisibility(View.GONE);
                    mIvC5Up.setVisibility(View.VISIBLE);
                    mCLayout5_2.setVisibility(View.VISIBLE);
                    index5 = 1;
                } else {
                    mIvC5Down.setVisibility(View.VISIBLE);
                    mIvC5Up.setVisibility(View.GONE);
                    mCLayout5_2.setVisibility(View.GONE);
                    index5 = 0;
                }
                break;
            case R.id.CLayout6_1:
                if (index6 == 0) {
                    mIvC6Down.setVisibility(View.GONE);
                    mIvC6Up.setVisibility(View.VISIBLE);
                    mCLayout6_2.setVisibility(View.VISIBLE);
                    index6 = 1;
                } else {
                    mIvC6Down.setVisibility(View.VISIBLE);
                    mIvC6Up.setVisibility(View.GONE);
                    mCLayout6_2.setVisibility(View.GONE);
                    index6 = 0;
                }
                break;
            case R.id.CLayout7_1:
                if (index7 == 0) {
                    mIvC7Down.setVisibility(View.GONE);
                    mIvC7Up.setVisibility(View.VISIBLE);
                    mCLayout7_2.setVisibility(View.VISIBLE);
                    index7 = 1;
                } else {
                    mIvC7Down.setVisibility(View.VISIBLE);
                    mIvC7Up.setVisibility(View.GONE);
                    mCLayout7_2.setVisibility(View.GONE);
                    index7 = 0;
                }
                break;
            case R.id.CLayout8_1:
                if (index8 == 0) {
                    mIvC8Down.setVisibility(View.GONE);
                    mIvC8Up.setVisibility(View.VISIBLE);
                    mCLayout8_2.setVisibility(View.VISIBLE);
                    index8 = 1;
                } else {
                    mIvC8Down.setVisibility(View.VISIBLE);
                    mIvC8Up.setVisibility(View.GONE);
                    mCLayout8_2.setVisibility(View.GONE);
                    index8 = 0;
                }
                break;
            case R.id.CLayout9_1:
                if (index9 == 0) {
                    mIvC9Down.setVisibility(View.GONE);
                    mIvC9Up.setVisibility(View.VISIBLE);
                    mCLayout9_2.setVisibility(View.VISIBLE);
                    index9 = 1;
                } else {
                    mIvC9Down.setVisibility(View.VISIBLE);
                    mIvC9Up.setVisibility(View.GONE);
                    mCLayout9_2.setVisibility(View.GONE);
                    index9 = 0;
                }
                break;
            case R.id.CLayout10_1:
                if (index10 == 0) {
                    mIvC10Down.setVisibility(View.GONE);
                    mIvC10Up.setVisibility(View.VISIBLE);
                    mCLayout10_2.setVisibility(View.VISIBLE);
                    index10 = 1;
                } else {
                    mIvC10Down.setVisibility(View.VISIBLE);
                    mIvC10Up.setVisibility(View.GONE);
                    mCLayout10_2.setVisibility(View.GONE);
                    index10 = 0;
                }
                break;
        }
    }

    //第一次進入畫面的方法，將畫面回復到預設，並載入資料
    private void FirstInView() {
        mCreditCardLayout1.setVisibility(View.GONE);
        mIvC1Down.setVisibility(View.VISIBLE);
        mIvC1Up.setVisibility(View.GONE);
        mCLayout1_2.setVisibility(View.GONE);
        mCreditCardLayout2.setVisibility(View.GONE);
        mIvC2Down.setVisibility(View.VISIBLE);
        mIvC2Up.setVisibility(View.GONE);
        mCLayout2_2.setVisibility(View.GONE);
        mCreditCardLayout3.setVisibility(View.GONE);
        mIvC3Down.setVisibility(View.VISIBLE);
        mIvC3Up.setVisibility(View.GONE);
        mCLayout3_2.setVisibility(View.GONE);
        mCreditCardLayout4.setVisibility(View.GONE);
        mIvC4Down.setVisibility(View.VISIBLE);
        mIvC4Up.setVisibility(View.GONE);
        mCLayout4_2.setVisibility(View.GONE);
        mCreditCardLayout5.setVisibility(View.GONE);
        mIvC5Down.setVisibility(View.VISIBLE);
        mIvC5Up.setVisibility(View.GONE);
        mCLayout5_2.setVisibility(View.GONE);
        mCreditCardLayout6.setVisibility(View.GONE);
        mIvC6Down.setVisibility(View.VISIBLE);
        mIvC6Up.setVisibility(View.GONE);
        mCLayout6_2.setVisibility(View.GONE);
        mCreditCardLayout7.setVisibility(View.GONE);
        mIvC7Down.setVisibility(View.VISIBLE);
        mIvC7Up.setVisibility(View.GONE);
        mCLayout7_2.setVisibility(View.GONE);
        mCreditCardLayout8.setVisibility(View.GONE);
        mIvC8Down.setVisibility(View.VISIBLE);
        mIvC8Up.setVisibility(View.GONE);
        mCLayout8_2.setVisibility(View.GONE);
        mCreditCardLayout9.setVisibility(View.GONE);
        mIvC9Down.setVisibility(View.VISIBLE);
        mIvC9Up.setVisibility(View.GONE);
        mCLayout9_2.setVisibility(View.GONE);
        mCreditCardLayout10.setVisibility(View.GONE);
        mIvC10Down.setVisibility(View.VISIBLE);
        mIvC10Up.setVisibility(View.GONE);
        mCLayout10_2.setVisibility(View.GONE);
        getCreditCardInfo();
    }

    //第二次進入畫面的方法關閉與清除不使用的Layout，並載入資料
    private void SecondInView() {
        mCreditCardLayout1.setVisibility(View.GONE);
        mTxtC1Name.setText(null);
        mTxtC1_1.setText(null);
        mTxtC1_2.setText(null);
        mCreditCardLayout2.setVisibility(View.GONE);
        mTxtC2Name.setText(null);
        mTxtC2_1.setText(null);
        mTxtC2_2.setText(null);
        mCreditCardLayout3.setVisibility(View.GONE);
        mTxtC3Name.setText(null);
        mTxtC3_1.setText(null);
        mTxtC3_2.setText(null);
        mCreditCardLayout4.setVisibility(View.GONE);
        mTxtC4Name.setText(null);
        mTxtC4_1.setText(null);
        mTxtC4_2.setText(null);
        mCreditCardLayout5.setVisibility(View.GONE);
        mTxtC5Name.setText(null);
        mTxtC5_1.setText(null);
        mTxtC5_2.setText(null);
        mCreditCardLayout6.setVisibility(View.GONE);
        mTxtC6Name.setText(null);
        mTxtC6_1.setText(null);
        mTxtC6_2.setText(null);
        mCreditCardLayout7.setVisibility(View.GONE);
        mTxtC7Name.setText(null);
        mTxtC7_1.setText(null);
        mTxtC7_2.setText(null);
        mCreditCardLayout8.setVisibility(View.GONE);
        mTxtC8Name.setText(null);
        mTxtC8_1.setText(null);
        mTxtC8_2.setText(null);
        mCreditCardLayout9.setVisibility(View.GONE);
        mTxtC9Name.setText(null);
        mTxtC9_1.setText(null);
        mTxtC9_2.setText(null);
        mCreditCardLayout10.setVisibility(View.GONE);
        mTxtC10Name.setText(null);
        mTxtC10_1.setText(null);
        mTxtC10_2.setText(null);
        getCreditCardInfo();
    }

    //此Activity重啓做更新動作
    protected void onRestart() {
        super.onRestart();

        //----顯示今年度收入、支出與總合的方法----//
        thisYearTotalPay();
        thisYearTotalIncome();
        thisYearTotal();

        //----顯示本月收入、支出與總合的方法----//
        thisMonthIncome();
        thisMonthPay();
        thisMonthTotal();

        PayOfDay(); //更新每日消費資訊

        ExpenseQuotaOfMonth();//更新每月警示資訊

        IndexView();//更新首頁顯示欄位

        SecondInView(); //使用第二次進入畫面的方法關閉與清除不使用的Layout，並載入資料
    }

    //從信用卡設定檔案中載入資料
    private void getCreditCardInfo() {
        ArrayList<Long> MoneyOfMessageList = new ArrayList<>();
        ArrayList<String> SwitchOfMessageList = new ArrayList<>();
        ArrayList<String> CreditCardList = new ArrayList<>();
        SharedPreferences getDate = getSharedPreferences(CreditCardName, MODE_PRIVATE);
        int CreditCardKey = getDate.getInt("CreditCardKey", -1); //讀取檔案中 "載入次數"的數據
        int j = 0;  //提供改變 key-values中 key的名子使用
        for (int i = CreditCardKey; i >= 0; i--, j++) {
            String k = "CreditCard" + j;
            String k1 = "MoneyOfMessage" + j;
            String k2 = "SwitchOfMessage" + j;
            CreditCardList.add(getDate.getString(k, null)); //依檔案數量，依次寫入 CreditCardList
            MoneyOfMessageList.add(getDate.getLong(k1, 1L));
            SwitchOfMessageList.add(getDate.getString(k2, null));
            String sw;
            switch (j) {
                case 0:
                    mTxtC1Name.setText(CreditCardList.get(j));
                    mTxtC1_1.setText(MoneyOfMessageList.get(j).toString());
                    cursor = helper.getReadableDatabase().query(TB_NAME,new String[]{"SUM(money)"},
                            "year = ? and month = ? and paymentMethods LIKE ?",
                            new String[]{""+c.get(Calendar.YEAR),""+(c.get(Calendar.MONTH)+1),"%"+mTxtC1Name.getText()},
                            "paymentMethods",null,null);
                    cursor.moveToFirst();
                    Long c1;
                    if(cursor.getCount() == 0){
                        c1 = 0L;
                    } else {
                        c1 = cursor.getLong(0);
                    }
                    mTxtC1_2.setText(""+c1);
                    cursor.close();
                    sw = SwitchOfMessageList.get(j);
                    if (sw.length() == 3) {
                        mCreditCardLayout1.setVisibility(View.GONE);
                    } else {
                        mCreditCardLayout1.setVisibility(View.VISIBLE);
                    }
                    mTxtC1Money.setText(""+(Long.parseLong(mTxtC1_1.getText().toString()) -
                            (Long.parseLong(mTxtC1_2.getText().toString()))));
                    break;
                case 1:
                    mTxtC2Name.setText(CreditCardList.get(j));
                    mTxtC2_1.setText(MoneyOfMessageList.get(j).toString());
                    cursor = helper.getReadableDatabase().query(TB_NAME,new String[]{"SUM(money)"},
                            "year = ? and month = ? and paymentMethods LIKE ?",
                            new String[]{""+c.get(Calendar.YEAR),""+(c.get(Calendar.MONTH)+1),"%"+mTxtC2Name.getText()},
                            "paymentMethods",null,null);
                    cursor.moveToFirst();
                    Long c2;
                    if(cursor.getCount() == 0){
                        c2 = 0L;
                    } else {
                        c2 = cursor.getLong(0);
                    }
                    mTxtC2_2.setText(""+c2);
                    cursor.close();
                    sw = SwitchOfMessageList.get(j);
                    if (sw.length() == 3) {
                        mCreditCardLayout2.setVisibility(View.GONE);
                    } else {
                        mCreditCardLayout2.setVisibility(View.VISIBLE);
                    }
                    mTxtC2Money.setText(""+(Long.parseLong(mTxtC2_1.getText().toString()) -
                            (Long.parseLong(mTxtC2_2.getText().toString()))));
                    break;
                case 2:
                    mTxtC3Name.setText(CreditCardList.get(j));
                    mTxtC3_1.setText(MoneyOfMessageList.get(j).toString());
                    cursor = helper.getReadableDatabase().query(TB_NAME,new String[]{"SUM(money)"},
                            "year = ? and month = ? and paymentMethods LIKE ?",
                            new String[]{""+c.get(Calendar.YEAR),""+(c.get(Calendar.MONTH)+1),"%"+mTxtC3Name.getText()},
                            "paymentMethods",null,null);
                    cursor.moveToFirst();
                    Long c3;
                    if(cursor.getCount() == 0){
                        c3 = 0L;
                    } else {
                        c3 = cursor.getLong(0);
                    }
                    mTxtC3_2.setText(""+c3);
                    cursor.close();
                    sw = SwitchOfMessageList.get(j);
                    if (sw.length() == 3) {
                        mCreditCardLayout3.setVisibility(View.GONE);
                    } else {
                        mCreditCardLayout3.setVisibility(View.VISIBLE);
                    }
                    mTxtC3Money.setText(""+(Long.parseLong(mTxtC3_1.getText().toString()) -
                            (Long.parseLong(mTxtC3_2.getText().toString()))));
                    break;
                case 3:
                    mTxtC4Name.setText(CreditCardList.get(j));
                    mTxtC4_1.setText(MoneyOfMessageList.get(j).toString());
                    cursor = helper.getReadableDatabase().query(TB_NAME,new String[]{"SUM(money)"},
                            "year = ? and month = ? and paymentMethods LIKE ?",
                            new String[]{""+c.get(Calendar.YEAR),""+(c.get(Calendar.MONTH)+1),"%"+mTxtC4Name.getText()},
                            "paymentMethods",null,null);
                    cursor.moveToFirst();
                    Long c4;
                    if(cursor.getCount() == 0){
                        c4 = 0L;
                    } else {
                        c4 = cursor.getLong(0);
                    }
                    mTxtC4_2.setText(""+c4);
                    cursor.close();
                    sw = SwitchOfMessageList.get(j);
                    if (sw.length() == 3) {
                        mCreditCardLayout4.setVisibility(View.GONE);
                    } else {
                        mCreditCardLayout4.setVisibility(View.VISIBLE);
                    }
                    mTxtC4Money.setText(""+(Long.parseLong(mTxtC4_1.getText().toString()) -
                            (Long.parseLong(mTxtC4_2.getText().toString()))));
                    break;
                case 4:
                    mTxtC5Name.setText(CreditCardList.get(j));
                    mTxtC5_1.setText(MoneyOfMessageList.get(j).toString());
                    cursor = helper.getReadableDatabase().query(TB_NAME,new String[]{"SUM(money)"},
                            "year = ? and month = ? and paymentMethods LIKE ?",
                            new String[]{""+c.get(Calendar.YEAR),""+(c.get(Calendar.MONTH)+1),"%"+mTxtC5Name.getText()},
                            "paymentMethods",null,null);
                    cursor.moveToFirst();
                    Long c5;
                    if(cursor.getCount() == 0){
                        c5 = 0L;
                    } else {
                        c5 = cursor.getLong(0);
                    }
                    mTxtC5_2.setText(""+c5);
                    cursor.close();
                    sw = SwitchOfMessageList.get(j);
                    if (sw.length() == 3) {
                        mCreditCardLayout5.setVisibility(View.GONE);
                    } else {
                        mCreditCardLayout5.setVisibility(View.VISIBLE);
                    }
                    mTxtC5Money.setText(""+(Long.parseLong(mTxtC5_1.getText().toString()) -
                            (Long.parseLong(mTxtC5_2.getText().toString()))));
                    break;
                case 5:
                    mTxtC6Name.setText(CreditCardList.get(j));
                    mTxtC6_1.setText(MoneyOfMessageList.get(j).toString());
                    cursor = helper.getReadableDatabase().query(TB_NAME,new String[]{"SUM(money)"},
                            "year = ? and month = ? and paymentMethods LIKE ?",
                            new String[]{""+c.get(Calendar.YEAR),""+(c.get(Calendar.MONTH)+1),"%"+mTxtC6Name.getText()},
                            "paymentMethods",null,null);
                    cursor.moveToFirst();
                    Long c6;
                    if(cursor.getCount() == 0){
                        c6 = 0L;
                    } else {
                        c6 = cursor.getLong(0);
                    }
                    mTxtC6_2.setText(""+c6);
                    cursor.close();
                    sw = SwitchOfMessageList.get(j);
                    if (sw.length() == 3) {
                        mCreditCardLayout6.setVisibility(View.GONE);
                    } else {
                        mCreditCardLayout6.setVisibility(View.VISIBLE);
                    }
                    mTxtC6Money.setText(""+(Long.parseLong(mTxtC6_1.getText().toString()) -
                            (Long.parseLong(mTxtC6_2.getText().toString()))));
                    break;
                case 6:
                    mTxtC7Name.setText(CreditCardList.get(j));
                    mTxtC7_1.setText(MoneyOfMessageList.get(j).toString());
                    cursor = helper.getReadableDatabase().query(TB_NAME,new String[]{"SUM(money)"},
                            "year = ? and month = ? and paymentMethods LIKE ?",
                            new String[]{""+c.get(Calendar.YEAR),""+(c.get(Calendar.MONTH)+1),"%"+mTxtC7Name.getText()},
                            "paymentMethods",null,null);
                    cursor.moveToFirst();
                    Long c7;
                    if(cursor.getCount() == 0){
                        c7 = 0L;
                    } else {
                        c7 = cursor.getLong(0);
                    }
                    mTxtC7_2.setText(""+c7);
                    cursor.close();
                    sw = SwitchOfMessageList.get(j);
                    if (sw.length() == 3) {
                        mCreditCardLayout7.setVisibility(View.GONE);
                    } else {
                        mCreditCardLayout7.setVisibility(View.VISIBLE);
                    }
                    mTxtC7Money.setText(""+(Long.parseLong(mTxtC7_1.getText().toString()) -
                            (Long.parseLong(mTxtC7_2.getText().toString()))));
                    break;
                case 7:
                    mTxtC8Name.setText(CreditCardList.get(j));
                    mTxtC8_1.setText(MoneyOfMessageList.get(j).toString());
                    cursor = helper.getReadableDatabase().query(TB_NAME,new String[]{"SUM(money)"},
                            "year = ? and month = ? and paymentMethods LIKE ?",
                            new String[]{""+c.get(Calendar.YEAR),""+(c.get(Calendar.MONTH)+1),"%"+mTxtC8Name.getText()},
                            "paymentMethods",null,null);
                    cursor.moveToFirst();
                    Long c8;
                    if(cursor.getCount() == 0){
                        c8 = 0L;
                    } else {
                        c8 = cursor.getLong(0);
                    }
                    mTxtC8_2.setText(""+c8);
                    cursor.close();
                    sw = SwitchOfMessageList.get(j);
                    if (sw.length() == 3) {
                        mCreditCardLayout8.setVisibility(View.GONE);
                    } else {
                        mCreditCardLayout8.setVisibility(View.VISIBLE);
                    }
                    mTxtC8Money.setText(""+(Long.parseLong(mTxtC8_1.getText().toString()) -
                            (Long.parseLong(mTxtC8_2.getText().toString()))));
                    break;
                case 8:
                    mTxtC9Name.setText(CreditCardList.get(j));
                    mTxtC9_1.setText(MoneyOfMessageList.get(j).toString());
                    cursor = helper.getReadableDatabase().query(TB_NAME,new String[]{"SUM(money)"},
                            "year = ? and month = ? and paymentMethods LIKE ?",
                            new String[]{""+c.get(Calendar.YEAR),""+(c.get(Calendar.MONTH)+1),"%"+mTxtC9Name.getText()},
                            "paymentMethods",null,null);
                    cursor.moveToFirst();
                    Long c9;
                    if(cursor.getCount() == 0){
                        c9 = 0L;
                    } else {
                        c9 = cursor.getLong(0);
                    }
                    mTxtC9_2.setText(""+c9);
                    cursor.close();
                    sw = SwitchOfMessageList.get(j);
                    if (sw.length() == 3) {
                        mCreditCardLayout9.setVisibility(View.GONE);
                    } else {
                        mCreditCardLayout9.setVisibility(View.VISIBLE);
                    }
                    mTxtC9Money.setText(""+(Long.parseLong(mTxtC9_1.getText().toString()) -
                            (Long.parseLong(mTxtC9_2.getText().toString()))));
                    break;
                case 9:
                    mTxtC10Name.setText(CreditCardList.get(j));
                    mTxtC10_1.setText(MoneyOfMessageList.get(j).toString());
                    cursor = helper.getReadableDatabase().query(TB_NAME,new String[]{"SUM(money)"},
                            "year = ? and month = ? and paymentMethods LIKE ?",
                            new String[]{""+c.get(Calendar.YEAR),""+(c.get(Calendar.MONTH)+1),"%"+mTxtC10Name.getText()},
                            "paymentMethods",null,null);
                    cursor.moveToFirst();
                    Long c10;
                    if(cursor.getCount() == 0){
                        c10 = 0L;
                    } else {
                        c10 = cursor.getLong(0);
                    }
                    mTxtC10_2.setText(""+c10);
                    cursor.close();
                    sw = SwitchOfMessageList.get(j);
                    if (sw.length() == 3) {
                        mCreditCardLayout10.setVisibility(View.GONE);
                    } else {
                        mCreditCardLayout10.setVisibility(View.VISIBLE);
                    }
                    mTxtC10Money.setText(""+(Long.parseLong(mTxtC10_1.getText().toString()) -
                            (Long.parseLong(mTxtC10_2.getText().toString()))));
                    break;
            }
        }


    }

    //顯示本年度收入、支出與總合的方法
    private void thisYearTotalPay() {
        int year = c.get(Calendar.YEAR);
        cursor = helper.getReadableDatabase().query(TB_NAME, new String[]{"SUM(money)"},
                "year = ? and income = ? ", new String[]{""+year, "支出"},
                "year", null, null);

        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            mThisYearTotalPay = 0L;
        } else {
            mThisYearTotalPay = cursor.getLong(0);
        }
        mThisYearPay.setText("" + mThisYearTotalPay);
        cursor.close();
    }
    private void thisYearTotalIncome() {
        int year = c.get(Calendar.YEAR);
        cursor = helper.getReadableDatabase().query(TB_NAME, new String[]{"SUM(money)"},
                "year = ? and income = ?", new String[]{""+year, "收入"}, "year", null, null);
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            mThisYearTotalIncome = 0L;
        } else {
            mThisYearTotalIncome = cursor.getLong(0);
        }
        mThisYearIncome.setText("" + mThisYearTotalIncome);
        cursor.close();
    }
    private void thisYearTotal() {
        mThisYearTotal = (Long) mThisYearTotalIncome - (Long) mThisYearTotalPay;
        if (mThisYearTotal < 0) {
            mThisYearTotalView.setTextColor(0xbbff1500);
        } else {
            mThisYearTotalView.setTextColor(0xff33b5e5);
        }
        mThisYearTotalView.setText("" + (mThisYearTotalIncome - mThisYearTotalPay));
    }

    //顯示本年度本月收入、支出與總合的方法
    private void thisMonthPay(){
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        cursor = helper.getReadableDatabase().query(TB_NAME,new String[]{"SUM(money)"},
                "year = ? and month = ? and income = ?",new String[]{""+year,""+month,
                        "支出"},"month",null,null);
        cursor.moveToFirst();
        if (cursor.getCount() == 0){
            mThisMonthTotalPay = 0L;
        } else {
            mThisMonthTotalPay = cursor.getLong(0);
        }
        mThisMonthPay.setText(""+mThisMonthTotalPay);
        cursor.close();
    }
    private void thisMonthIncome(){
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        cursor = helper.getReadableDatabase().query(TB_NAME,new String[]{"SUM(money)"},
                "year = ? and month = ? and income = ?",new String[]{""+year, ""+month,
                        "收入"},"month",null,null);
        cursor.moveToFirst();
        if (cursor.getCount() == 0){
            mThisMonthTotalIncome = 0L;
        } else {
            mThisMonthTotalIncome = cursor.getLong(0);
        }
        mThisMonthIncome.setText(""+mThisMonthTotalIncome);
        cursor.close();
    }
    private void thisMonthTotal(){
        mThisMonthTotal = (Long) mThisMonthTotalIncome - (Long) mThisMonthTotalPay;
        if (mThisMonthTotal < 0){
            mThisMonthTotalView.setTextColor(0xbbff1500);
        } else {
            mThisMonthTotalView.setTextColor(0xff33b5e5);
        }
        mThisMonthTotalView.setText("" + (mThisMonthTotalIncome - mThisMonthTotalPay));
    }

    //顯示每日消費資訊的方法
    private void PayOfDay(){
        cursor = helper.getReadableDatabase().query(TB_NAME,new String[]{"SUM(money)"},
                "year = ? and month = ? and day = ? and income = ? ",
                new String[]{""+c.get(Calendar.YEAR),""+(c.get(Calendar.MONTH)+1),
                ""+c.get(Calendar.DAY_OF_MONTH),getString(R.string.pay)},
                "day",null,null);
        cursor.moveToFirst();
        Long PayOfDay;
        if(cursor.getCount() == 0){
            PayOfDay = 0L;
        } else {
            PayOfDay = cursor.getLong(0);
        }
        mMoneyOfDay.setText(""+PayOfDay);
        cursor.close();

        cursor1 = helper.getReadableDatabase().query(TB_NAME,new String[]{"_id,item,money,info"},
                "year = ? and month = ? and day = ? and income = ? ",
                new String[]{""+c.get(Calendar.YEAR),""+(c.get(Calendar.MONTH)+1),
                        ""+c.get(Calendar.DAY_OF_MONTH),getString(R.string.pay)},null,null,null);

        if(cursor1.getCount() == 0){
            mNoConsumption.setVisibility(View.VISIBLE);
            mInfoOfDay.setVisibility(View.GONE);
        }else{
            mNoConsumption.setVisibility(View.GONE);
            mInfoOfDay.setVisibility(View.VISIBLE);
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,R.layout.info_of_day_listview,
                    cursor1, Day_Information, new int[]{R.id.dItem,R.id.dMoney,R.id.dInfo});
            mLvOfDay.setAdapter(adapter);
        }
    }

    //顯示每月消費警示的方法
    private void ExpenseQuotaOfMonth(){
        SharedPreferences getDate = getSharedPreferences(ExpenseQuotaName, MODE_PRIVATE);
        Long EQ; int YP,RP;
        EQ = getDate.getLong("ExpenseQuota",10000L);
        YP = getDate.getInt("yellowPercent",50);
        RP = getDate.getInt("RedPercent",20);
        cursor = helper.getReadableDatabase().query(TB_NAME,new String[]{"SUM(money)"},
                "year = ? and month = ? and income = ? ",new String[]{""+c.get(Calendar.YEAR),
                ""+(c.get(Calendar.MONTH)+1),getString(R.string.pay)},"month",null,null);
        cursor.moveToFirst();
        Long ExpenseQuota;
        if (cursor.getCount() == 0){
            ExpenseQuota = 0L;
        } else {
            ExpenseQuota = cursor.getLong(0);
        }

        if ((EQ - ExpenseQuota) * 0.01 < RP ){
            mIvYellow.setVisibility(View.GONE);
            mIvGreen.setVisibility(View.GONE);
            mIvRed.setVisibility(View.VISIBLE);
        } else if ((EQ - ExpenseQuota) * 0.01 < YP ){
            mIvYellow.setVisibility(View.VISIBLE);
            mIvGreen.setVisibility(View.GONE);
            mIvRed.setVisibility(View.GONE);
        } else {
            mIvYellow.setVisibility(View.GONE);
            mIvGreen.setVisibility(View.VISIBLE);
            mIvRed.setVisibility(View.GONE);
        }
        mExpenseQuotaMonth.setText(""+(EQ - ExpenseQuota));
    }

    //顯示首頁欄位樣式的方法
    private void IndexView(){
        SharedPreferences getDate = getSharedPreferences(IndexViewName,MODE_PRIVATE);
        SMS = getDate.getString("SMS","true");
        EQS = getDate.getString("EQS","true");
        PDS = getDate.getString("PDS","true");
        CCS = getDate.getString("CCS","true");

        if (SMS.length() == 4){
            mSumOfMoney.setVisibility(View.VISIBLE);
        } else {
            mSumOfMoney.setVisibility(View.GONE);
        }

        if (EQS.length() == 4){
            mExpenseQuota.setVisibility(View.VISIBLE);
        } else {
            mExpenseQuota.setVisibility(View.GONE);
        }

        if (PDS.length() == 4){
            mPayOfDay.setVisibility(View.VISIBLE);
        } else {
            mPayOfDay.setVisibility(View.GONE);
        }

        if (CCS.length() == 4){
            mCreditCard.setVisibility(View.VISIBLE);
        } else {
            mCreditCard.setVisibility(View.GONE);
        }
    }

    //以下為跳轉頁面
    public void Analysis(View view) {
        startActivity(new Intent(this, account_paychart.class));
    }

    public void AllSetting(View view) {
        startActivity(new Intent(this, account_setting.class));
    }

    public void AddInfo(View v) {
        startActivity(new Intent(this, account_add.class));
    }

    public void SearchInfo(View v) {
        startActivity(new Intent(this, account_information_list.class));
    }

    public void Calender(View view) {
        startActivity(new Intent(this, calendar_main.class));
    }
}
