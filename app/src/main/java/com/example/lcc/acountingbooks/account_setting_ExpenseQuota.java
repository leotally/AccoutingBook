package com.example.lcc.acountingbooks;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class account_setting_ExpenseQuota extends AppCompatActivity {

    static final String ExpenseQuotaName = "ExpenseQuota";
    EditText mTxtEQ,mYellowPercent,mRedPercent;
    SharedPreferences ExpenseQuota;
    Toast toast;
    Long EQ;
    int YP,RP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_setting_expensequota);
        toast = Toast.makeText(this,"",Toast.LENGTH_SHORT);

        ExpenseQuota = getSharedPreferences(ExpenseQuotaName, MODE_PRIVATE);

        mTxtEQ = (EditText) findViewById(R.id.EQ);
        mYellowPercent = (EditText) findViewById(R.id.yellowPercent);
        mRedPercent = (EditText) findViewById(R.id.redPercent);
    }

    public void save(View view){
        SharedPreferences.Editor editor = ExpenseQuota.edit();
        if (mRedPercent.getText().toString().length() == 0){
            toast.setText(R.string.redLightRange);
            toast.show();
        } else if (Integer.parseInt(mRedPercent.getText().toString()) < 1) {
            toast.setText(R.string.redLightRange);
            toast.show();
        } else if (mYellowPercent.getText().toString().length() == 0) {
            toast.setText(R.string.yellowLightRange);
            toast.show();
        } else if (Integer.parseInt(mYellowPercent.getText().toString()) < 1) {
            toast.setText(R.string.yellowLightRange);
            toast.show();
        } else if (mTxtEQ.getText().toString().length() == 0){
            EQ = 10000L;
            YP = Integer.parseInt(mYellowPercent.getText().toString());
            RP = Integer.parseInt(mRedPercent.getText().toString());

            editor.putLong("ExpenseQuota",EQ);
            editor.putInt("yellowPercent",YP);
            editor.putInt("RedPercent",RP);
            editor.commit();
            toast.setText(R.string.dateSaved);
            toast.show();
        } else {
            YP = Integer.parseInt(mYellowPercent.getText().toString());
            RP = Integer.parseInt(mRedPercent.getText().toString());
            EQ = Long.parseLong(mTxtEQ.getText().toString());

            editor.putLong("ExpenseQuota",EQ);
            editor.putInt("yellowPercent",YP);
            editor.putInt("RedPercent",RP);
            editor.commit();
            toast.setText(R.string.dateSaved);
            toast.show();
        }
    }
}
