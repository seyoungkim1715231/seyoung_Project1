package com.medisook.app;

import static com.medisook.app.MenuFragmentSearch.IP_ADDRESS;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class JoinActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_nickname;
    private EditText et_password;
    private EditText warn_nk;
    private EditText warn_pw;
    private Button join_btn;
    private Button check_nk;
    private Context mContext;
    boolean nk_result=false;
    boolean nk_check=false;
    boolean pw_result = false;
    MenuFragmentSearch mf=new MenuFragmentSearch();
    String nk;
    String nk_final;
    String pw;
    String pw_final;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);
        mContext = this;

        warn_nk = (EditText) findViewById(R.id.warn_nk);
        warn_pw = (EditText) findViewById(R.id.warn_pw);
        et_password = (EditText) findViewById(R.id.password);
        et_password.setOnClickListener(this);
        et_nickname = (EditText) findViewById(R.id.nickname);
        et_nickname.setOnClickListener(this);
        join_btn = (Button) findViewById(R.id.join_btn);
        join_btn.setOnClickListener(this);
        check_nk = (Button) findViewById(R.id.check_nk);
        check_nk.setOnClickListener(this);



        et_nickname.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                nk =  et_nickname.getText().toString();
                check_nk.setBackgroundColor(Color.parseColor("#445E86"));
                switch (i){
                    case KeyEvent.KEYCODE_ENTER:
                        if (keyEvent.getAction() == keyEvent.ACTION_UP) {
                            check_nk.setBackground(check_nk.getBackground());

                            if(nk.length() >= 11 || nk.length()==0) {
                                et_nickname.setText(null);
                                warn_nk.setText("!????????? ???????????????.");
                                nk_result= false;
                            }
                            else{
                                warn_nk.setText(null);
                                nk_result= true;
                            }
                            Log.d("????????????", "result: " + nk_result);
                            Log.d("????????????", "?????????: " + nk);
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            //getWindow().sethideSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        return true;
                    case KeyEvent.KEYCODE_DEL:
                }
                return false;
            }
        });

        et_password.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                pw = et_password.getText().toString();
                switch (i){
                    case KeyEvent.KEYCODE_ENTER:
                        if (keyEvent.getAction() == keyEvent.ACTION_UP) {
                            if(Pattern.matches("^(?=.*[a-zA-z])(?=.*[0-9])(?!.*[^a-zA-z0-9]).{5,20}$", pw)) {
                                warn_pw.setText(null);
                                pw_result = true;
                            }
                            else {
                                et_password.setText(null);
                                warn_pw.setText("!????????? ???????????????.");
                                pw_result = false;
                            }
                            Log.d("????????????", "????????????: " + pw_result);
                            Log.d("????????????","????????????: " + pw);
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        } return true;
                    case KeyEvent.KEYCODE_DEL:
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        //Log.v("????????????", "?????????: " + nk_result);
        //Log.v("????????????", "?????????: " + pw_result);
        switch (v.getId()){
            case R.id.join_btn:
                Log.v("????????????", "?????????- " + "???????????????: " + nk_result + "????????????: " + pw_result + "?????????????????????: "+ nk_check);
                if(nk_result == true && pw_result == true && nk_check==true){//???????????? ?????? ?????? ??? ???????????? ???????????????.
                    nk_final = nk;
                    pw_final = pw;
                    mf = new MenuFragmentSearch();
                    mf.getNickname(nk_final, pw_final);
                    MenuFragmentSearch.InsertData insert = mf.new InsertData();
                    insert.execute("http://" + IP_ADDRESS + "/join.php", "0");
                    Log.d("?????????", "nk final :  "+ nk_final);
                    Toast.makeText(this.getApplicationContext(),"???????????? ??????!", Toast.LENGTH_SHORT).show();
                    Intent intentLoginActivity =
                            new Intent(JoinActivity.this, LoginActivity.class);
                    startActivity(intentLoginActivity);

                    Log.d("?????????", "???????????? ????????????");
                }
                else if(nk_check==false){
                    Toast.makeText(this.getApplicationContext(),"????????? ??????????????? ????????????.", Toast.LENGTH_SHORT).show();
                }
//                else{
//                    Toast.makeText(this.getApplicationContext(),"??????????????? ??????????????????.\n?????? ??????????????????.", Toast.LENGTH_SHORT).show();
//                    et_password.setText(null);
//                    et_nickname.setText(null);
//                }
                break;
            case R.id.check_nk:
                nk_final = nk;
                mf = new MenuFragmentSearch();
                mf.getNickname(nk_final, null);
                MenuFragmentSearch.ReadData read = mf.new ReadData();
                read.execute("http://" + IP_ADDRESS + "/login.php", "3");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("?????????", mf.getresult());

                        if(mf.getresult().contains("TRUE")){
                            nk_check = false;
                            Toast.makeText(JoinActivity.this, "????????????????????????.", Toast.LENGTH_SHORT).show();
                            Log.d("?????????", "????????????????????????");
                            et_nickname.setText(null);
                        }
                        else if(mf.getresult().contains("FALSE")){
                            nk_check = true;
                            check_nk.setBackgroundColor(Color.parseColor("#D3D3D3"));
//                            check_nk.setOnClickListener(new View.OnClickListener(){
//                                @Override
//                                public void onClick(View v){
//                                    check_nk.setBackgroundColor(Color.parseColor("#D3D3D3"));
//                                }
//                            });
                            Toast.makeText(JoinActivity.this, "??????????????? ??????????????????.", Toast.LENGTH_SHORT).show();
                            Log.d("?????????", "??????????????? ??????????????????.");
                            nk_final = nk;
                        }
                        Log.d("????????????", "???????????? " + nk_check + nk_final);
                    }

                }, 300);


                //????????? ???????????? ??????
        }

    }
}
