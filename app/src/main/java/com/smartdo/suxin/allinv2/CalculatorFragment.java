package com.smartdo.suxin.allinv2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by suxin on 9/13/16.
 */
public class CalculatorFragment extends Fragment{

    @BindView(R.id.txtScreen) TextView _screen;
    private String display = "";
    private String currentOperator = "";
    private String result = "";
    private boolean flag = false;
    private String password = "";
    public static final String PASSWORD = "password";




    public static CalculatorFragment newInstance(int listType) {
        Bundle args = new Bundle();
        CalculatorFragment fragment = new CalculatorFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calculator, container, false);
        ButterKnife.bind(this,view);


        Context context = getActivity();
        SharedPreferences sharedPreferences = context.getSharedPreferences(PASSWORD, Context.MODE_PRIVATE);
        String defaultValue = getResources().getString(R.string.default_pw);
        String pw = sharedPreferences.getString(PASSWORD, defaultValue);
        if (pw.equals(defaultValue)) {
            display += getString(R.string.set_password_and_click);
        }
        _screen.setText(display);
        display = "";
        Button one = (Button) view.findViewById(R.id.btnOne);
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickNumber(view);
            }
        });
        Button two = (Button) view.findViewById(R.id.btnTwo);
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickNumber(view);
            }
        });
        Button three = (Button) view.findViewById(R.id.btnThree);
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickNumber(view);
            }
        });
        Button four = (Button) view.findViewById(R.id.btnFour);
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickNumber(view);
            }
        });
        Button five = (Button) view.findViewById(R.id.btnFive);
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickNumber(view);
            }
        });
        Button six = (Button) view.findViewById(R.id.btnSix);
        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickNumber(view);
            }
        });
        Button seven = (Button) view.findViewById(R.id.btnSeven);
        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickNumber(view);
            }
        });
        Button eight = (Button) view.findViewById(R.id.btnEight);
        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickNumber(view);
            }
        });
        Button nine = (Button) view.findViewById(R.id.btnNine);
        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickNumber(view);
            }
        });
        Button zero = (Button) view.findViewById(R.id.btnZero);
        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickNumber(view);
            }
        });
        Button dot = (Button) view.findViewById(R.id.btnDot);
        dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickNumber(view);
            }
        });

        Button plus = (Button) view.findViewById(R.id.btnAdd);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickOperator(view);
            }
        });
        Button minus = (Button) view.findViewById(R.id.btnSubtract);
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickOperator(view);
            }
        });
        Button multiply = (Button) view.findViewById(R.id.btnMultiply);
        multiply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickOperator(view);
            }
        });
        Button devide = (Button) view.findViewById(R.id.btnDivide);
        devide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickOperator(view);
            }
        });
        Button percent = (Button) view.findViewById(R.id.btnPercent);
        percent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getActivity();
                SharedPreferences sharedPreferences = context.getSharedPreferences(PASSWORD, Context.MODE_PRIVATE);
                String defaultValue = getResources().getString(R.string.default_pw);
                String pw = sharedPreferences.getString(PASSWORD,defaultValue);
                if (pw.equals(defaultValue)) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(PASSWORD, password);
                    editor.commit();
                    clear();
                    updateScreen();
                } else if (password.equals(pw)) {
                    Intent intent = new Intent(getContext(), CalculatorViewPager.class);
                    getActivity().startActivity(intent);
                    //Toast.makeText(getContext(), "you have password", Toast.LENGTH_LONG).show();
                    password = "";
                    clear();
                    updateScreen();
                }
                onClickOperator(view);
            }
        });

        Button equal = (Button) view.findViewById(R.id.btnEqual);
        equal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickEqual(view);
            }
        });

        Button clear = (Button) view.findViewById(R.id.btnClear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickClear(view);
            }
        });
        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private String passwordEncrypt(String password) {
        byte[] input = password.getBytes();
        String password_Encrypt;
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        sha.update(input);
        byte[] output = sha.digest();
        password_Encrypt = Base64.encodeToString(output, Base64.DEFAULT);
        return password_Encrypt;
    }


    private void updateScreen() {
        _screen.setText(display);
    }

    public void onClickNumber(View v){
        if(result != ""){
            clear();
            updateScreen();
        }
        Button b = (Button) v;
        if (b.getText() == ".") {
            flag = true;
        } else {
            flag = false;
        }
        display += b.getText();
        password += b.getText();
        updateScreen();
    }

    private void clear() {
        display = "";
        currentOperator = "";
        result = "";
        password = "";
    }

    private boolean isOperator(char op){
        switch (op){
            case '+':
            case '-':
            case 'x':
            case '÷':
            case '%':return true;
            default: return false;
        }
    }

    public void onClickOperator(View v){
        if (flag == true) {
            return;
        }
        if(display == "") return;

        Button b = (Button)v;


        if(result != ""){
            String _display = result;
            clear();
            display = _display;
        }
        if(currentOperator != ""){
            if(isOperator(display.charAt(display.length()-1))){
                display = display.replace(display.charAt(display.length()-1), b.getText().charAt(0));
                updateScreen();
                return;
            }else{
                getResult();
                display = result;
                result = "";
            }
            currentOperator = b.getText().toString();
        }
        display += b.getText();
        currentOperator = b.getText().toString();
        password = "";
        updateScreen();
    }

    public void onClickClear(View v){
        clear();
        updateScreen();
    }

    private double operate(String a, String b, String op){
        switch (op){
            case "+": return Double.valueOf(a) + Double.valueOf(b);
            case "-": return Double.valueOf(a) - Double.valueOf(b);
            case "x": return Double.valueOf(a) * Double.valueOf(b);
            case "÷": try{
                return Double.valueOf(a) / Double.valueOf(b);
            }catch (Exception e){
            }
            default: return -1;
        }
    }

    private boolean getResult(){
        if(currentOperator == "") return false;
        String[] operation = display.split(Pattern.quote(currentOperator));
        if(operation.length < 2) return false;
        result = String.valueOf(operate(operation[0], operation[1], currentOperator));
        return true;
    }

    public void onClickEqual(View v){
        if(display == "") return;
        if(!getResult()) return;
        _screen.setText(display + "\n" + String.valueOf(result));
        password = "";
    }
}
