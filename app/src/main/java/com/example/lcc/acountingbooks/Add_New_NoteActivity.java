package com.example.lcc.acountingbooks;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Add_New_NoteActivity extends AppCompatActivity implements View.OnClickListener {
    TextView title;
    EditText editnote;
    Button save,clean,cancle;
    Intent it;
    int getyear,getmonth,getday;
    MySQLiteOpenHelper helper;
    int count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__new__note);
        helper = MySQLiteOpenHelper.getInstance(this);

        title = (TextView) this.findViewById(R.id.title);
        editnote = (EditText) this.findViewById(R.id.editnote);
        save = (Button) this.findViewById(R.id.save);
        clean = (Button) this.findViewById(R.id.clean);
        cancle = (Button) this.findViewById(R.id.cancle);

        it = getIntent();
        getyear = it.getIntExtra("年",2017);
        getmonth = it.getIntExtra("月",1);
        getday = it.getIntExtra("日",1);

        title.setText("你在日期:"+getyear+"年"+getmonth+"月"+getday+"日下記事");

        if (it.getStringExtra("記事").equals("目前尚未設定記事")) {
            editnote.setText("");
        }
        else{
            editnote.setText(it.getStringExtra("記事"));
        }

        save.setOnClickListener(this);
        clean.setOnClickListener(this);
        cancle.setOnClickListener(this);
    }

    @Override
    public void onClick(final View view) {
        switch (view.getId()){
            case R.id.save:
                final AlertDialog.Builder builder =  new AlertDialog.Builder(Add_New_NoteActivity.this);
                builder.setMessage("請問是否確認儲存該日記事?")
                        .setTitle("儲存確認")
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Cursor cursor = helper.getReadableDatabase().query("CalenderNote",null,"year = ? AND month = ? AND day = ? ",new String[]{String.valueOf(getyear),String.valueOf(getmonth),String.valueOf(getday)},null,null,null);

                                if (cursor.moveToFirst()){
                                    count = cursor.getCount();
                                }

                                if (count != 0) {
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("year",getyear);
                                    contentValues.put("month",getmonth);
                                    contentValues.put("day",getday);
                                    contentValues.put("noteinfo",editnote.getText().toString());
                                    int code = helper.getWritableDatabase().update("CalenderNote",contentValues,"year = ? AND month = ? AND day = ? ",new String[]{String.valueOf(getyear),String.valueOf(getmonth),String.valueOf(getday)});
                                    helper.close();
                                    cursor.close();

                                    if (code != 0){
                                        Toast.makeText(view.getContext(),"更新成功",Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                    else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(Add_New_NoteActivity.this);
                                        builder.setTitle("更新通知")
                                                .setMessage("更新失敗!! 資料沒有變更!!")
                                                .setNegativeButton("確定",null)
                                                .show();
                                    }
                                }
                                else {
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put("year",getyear);
                                    contentValues.put("month",getmonth);
                                    contentValues.put("day",getday);
                                    contentValues.put("noteinfo",editnote.getText().toString());
                                    long id = helper.getWritableDatabase().insert("CalenderNote",null,contentValues);

                                    Toast.makeText(view.getContext(),"已成功送出 回傳值:"+id,Toast.LENGTH_LONG).show();
                                    helper.close();
                                    cursor.close();
                                    finish();
                                }
                            }
                        })
                        .setNegativeButton("返回修改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
                break;
            case R.id.clean:
                editnote.setText("");
                break;
            case R.id.cancle:
                finish();
                break;
        }
    }
}
