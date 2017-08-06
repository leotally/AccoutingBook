package com.example.lcc.acountingbooks;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

public class account_setting_indexview extends AppCompatActivity {
    static final String IndexViewName = "IndexView";
    SharedPreferences IndexView,getDate;
    Switch SumOfMoneySwitch,ExpenseQuotaSwitch,PayOfDaySwitch,CreditCardSwitch,PaymentDaySwitch;
    String SMS,EQS,PDS,CCS,PD;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_setting_indexview);
        getSupportActionBar().hide();
        toast = Toast.makeText(this, "" ,Toast.LENGTH_SHORT);
        myFindView();
        LoadingDate();
    }

    private void myFindView(){
        SumOfMoneySwitch = (Switch) findViewById(R.id.SumOfMoneySwitch);
        ExpenseQuotaSwitch = (Switch) findViewById(R.id.ExpenseQuotaSwitch);
        PayOfDaySwitch = (Switch) findViewById(R.id.PayOfDaySwitch);
        CreditCardSwitch = (Switch) findViewById(R.id.CreditCardSwitch);
        PaymentDaySwitch = (Switch) findViewById(R.id.PaymentDaySwitch);
    }

    private void LoadingDate(){
        getDate = getSharedPreferences(IndexViewName,MODE_PRIVATE);
        SMS = getDate.getString("SMS","true");
        EQS = getDate.getString("EQS","true");
        PDS = getDate.getString("PDS","true");
        CCS = getDate.getString("CCS","true");
        PD = getDate.getString("PD","true");
        SumOfMoneySwitch.setChecked(Boolean.parseBoolean(SMS));
        ExpenseQuotaSwitch.setChecked(Boolean.parseBoolean(EQS));
        PayOfDaySwitch.setChecked(Boolean.parseBoolean(PDS));
        CreditCardSwitch.setChecked(Boolean.parseBoolean(CCS));
        PaymentDaySwitch.setChecked(Boolean.parseBoolean(PD));
    }

    public void save(View view){
        getDate();
        IndexView = getSharedPreferences(IndexViewName, MODE_PRIVATE);
        SharedPreferences.Editor editor = IndexView.edit();
        editor.putString("SMS",SMS);
        editor.putString("EQS",EQS);
        editor.putString("PDS",PDS);
        editor.putString("CCS",CCS);
        editor.putString("PD",PD);
        editor.commit();
        toast.setText(R.string.dateSaved);
        toast.show();
    }

    private void getDate(){
        if (SumOfMoneySwitch.isChecked() == true){
            SMS = getString(R.string.infoTrue);
        } else {
            SMS = getString(R.string.infoFalse);
        }

        if(ExpenseQuotaSwitch.isChecked() == true){
            EQS = getString(R.string.infoTrue);
        } else {
            EQS = getString(R.string.infoFalse);
        }

        if (PayOfDaySwitch.isChecked() == true){
            PDS = getString(R.string.infoTrue);
        } else {
            PDS = getString(R.string.infoFalse);
        }

        if (CreditCardSwitch.isChecked() == true){
            CCS = getString(R.string.infoTrue);
        } else {
            CCS = getString(R.string.infoFalse);
        }

        if (PaymentDaySwitch.isChecked() == true){
            PD = getString(R.string.infoTrue);
        } else {
            PD = getString(R.string.infoFalse);
        }
    }
}
