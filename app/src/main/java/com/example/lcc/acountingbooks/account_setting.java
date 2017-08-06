package com.example.lcc.acountingbooks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class account_setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_setting);
        getSupportActionBar().hide();
    }

    public void IndexView(View view){
        startActivity(new Intent(this, account_setting_indexview.class));
    }

    public void IncomePayItemSetting(View view){
        startActivity(new Intent(this, account_setting_incomepay.class));
    }

    public void CreditCardSetting(View view){
        startActivity(new Intent(this, account_setting_creditcard.class));
    }

    public void ExpenseQuota(View view){
        startActivity(new Intent(this, account_setting_ExpenseQuota.class));
    }

    public void PaymentDay(View view){
        startActivity(new Intent(this, account_setting_paymentday.class));
    }
}
