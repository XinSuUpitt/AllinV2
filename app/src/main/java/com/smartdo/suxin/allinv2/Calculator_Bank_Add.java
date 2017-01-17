package com.smartdo.suxin.allinv2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by suxin on 9/21/16.
 */
public class Calculator_Bank_Add extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    public static final String KEY_BANK = "key_bank";
    public static final String KEY_BANK_ID = "key_bank_id";

    public static final int REQ_CODE_BANK_EDIT = 103;
    public BankListAdapter adapter;
    String description;
    private String date;
    private String TAG = getClass().getSimpleName();

    private RecyclerView recyclerView;

    private Bank bank;

    int imageNo[] = {R.drawable.visa_debit, R.drawable.master_card};


    int countImage = imageNo.length;
    int month;
    int year;


    @BindView(R.id.slider) SliderLayout mDemoSlider;
    @BindView(R.id.holder_first_name) EditText holder_First_Name;
    @BindView(R.id.holder_last_name) EditText holder_Last_Name;
    @BindView(R.id.name_id) EditText name_ID;
    @BindView(R.id.bank_account_bank_name) EditText bank_Account_Bank_Name;
    @BindView(R.id.bank_account_number) EditText bank_Account_Bank_Number;
    @BindView(R.id.expiration_date) TextView expiration_Date;
    @BindView(R.id.bank_cvv_code) EditText bank_Cvv_Code;
    @BindView(R.id.bank_billing_address_1) EditText bank_Billing_Address_1;
    @BindView(R.id.bank_billing_address_2) EditText bank_Billing_address_2;
    @BindView(R.id.bank_add_delete) TextView deleteBtn;
    @BindView(R.id.fab_calculator_bank_add_save) FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator_bank);

        bank = getIntent().getParcelableExtra(KEY_BANK);

        //date = bank != null ? bank.expiration_Date : null;

        setupUI();


    }

    private void setupUI() {
        setContentView(R.layout.calculator_bank_new);
        ButterKnife.bind(this);

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Bank of America", R.drawable.bankofamerica);
        file_maps.put("Citi Bank", R.drawable.citibank);
        file_maps.put("Citizens Bank", R.drawable.citizensbank);
        file_maps.put("Chase", R.drawable.chasebank);
        file_maps.put("PNC Bank", R.drawable.pnc);
        file_maps.put("Wells Fargo", R.drawable.wellsfargo);
        file_maps.put("中国银行",R.drawable.bankofchina);
        file_maps.put("中国工商银行", R.drawable.industrialcommercialbankofchina);
        file_maps.put("中国建设银行", R.drawable.chinaconstructionbank);
        file_maps.put("中国农业银行", R.drawable.agriculturalbankofchina);
        file_maps.put("中信银行", R.drawable.chinaciticbank);
        file_maps.put("Other Banks", R.drawable.otherbanks);
        file_maps.put("浦发银行", R.drawable.spdbank);
        file_maps.put("Visa_debit",R.drawable.visa_debit);
        file_maps.put("Master Card",R.drawable.master_card);
        file_maps.put("Discover", R.drawable.discover);
        file_maps.put("银联", R.drawable.yinlian);


        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }

        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.stopAutoCycle();
        mDemoSlider.addOnPageChangeListener(this);

        mDemoSlider.setCurrentPosition(bank != null ? getPosition(bank.credit_categary) : 0);

//        if (date != null) {
//            expiration_Date.setText(date);
//        } else {
//            expiration_Date.setText(R.string.set_date);
//        }


        if (bank != null) {
            //imageSwitcher.setImageDrawable(getDrawable(R.drawable.visa_debit));
            holder_First_Name.setText(bank.holder_first_name);
            holder_Last_Name.setText(bank.holder_last_name);
            name_ID.setText(bank.name_Id);
            bank_Account_Bank_Name.setText(bank.bank_Name);
            bank_Account_Bank_Number.setText(bank.bank_Account_Number);
            expiration_Date.setText(bank.expiration_Date);
            bank_Cvv_Code.setText(bank.cvv_Code);
            bank_Billing_Address_1.setText(bank.address_1);
            bank_Billing_address_2.setText(bank.address_2);

            deleteBtn.setVisibility(View.VISIBLE);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    delete();
                }
            });
        } else {
            Calendar calendar = Calendar.getInstance();
            month = calendar.get(Calendar.MONTH);
            year = calendar.get(Calendar.YEAR);
            String string = Integer.toString(month) + "/" + Integer.toString(year);
            bank = new Bank(string);

            deleteBtn.setVisibility(View.GONE);
        }

        expiration_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mshowDialog();
            }
        });
        setupSaveButton();
    }

    private void mshowDialog() {
        if (bank.expiration_Date != null) {
            String[] parts = bank.expiration_Date.split("/");
            if (parts[0] == "") {
                Calendar calendar = Calendar.getInstance();
                month = calendar.get(Calendar.MONTH);
                year = calendar.get(Calendar.YEAR);
            } else {
                month = Integer.parseInt(parts[0]);
                year = Integer.parseInt(parts[1]);
            }
        } else {
            Calendar calendar = Calendar.getInstance();
            month = calendar.get(Calendar.MONTH);
            year = calendar.get(Calendar.YEAR);
        }

        MonthYearPickerDialog monthYearPickerDialog = new MonthYearPickerDialog(this, "Select Month and Year", month, year);
        monthYearPickerDialog.setMaxYear(2036);
        monthYearPickerDialog.setMinYear(2016);
        monthYearPickerDialog.setOnSetListener(new MonthYearPickerDialog.OnSet() {
            @Override
            public void onSet(int month, int year) {

                Log.d(TAG, "month: " + month + " year: " + year);
                date = month + "/" + year;
                expiration_Date.setText(date);
            }
        });
        monthYearPickerDialog.show();
    }


    private void setupSaveButton() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAndExit();
            }
        });
    }

    private void saveAndExit() {
        description = mDemoSlider.getCurrentSlider().getDescription().toString();

        if (bank == null) {
            bank = new Bank(
                            description,
                            holder_First_Name.getText().toString(),
                            holder_Last_Name.getText().toString(),
                            name_ID.getText().toString(),
                            bank_Account_Bank_Name.getText().toString(),
                            bank_Account_Bank_Number.getText().toString(),
                            expiration_Date.getText().toString(),
                            bank_Cvv_Code.getText().toString(),
                            bank_Billing_Address_1.getText().toString(),
                            bank_Billing_address_2.getText().toString(), getPosition(description));
        } else {
            bank.credit_categary = description;
            bank.holder_first_name = holder_First_Name.getText().toString();
            bank.holder_last_name = holder_Last_Name.getText().toString();
            bank.name_Id = name_ID.getText().toString();
            bank.bank_Name = bank_Account_Bank_Name.getText().toString();
            bank.bank_Account_Number = bank_Account_Bank_Number.getText().toString();
            bank.expiration_Date = expiration_Date.getText().toString();
            bank.cvv_Code = bank_Cvv_Code.getText().toString();
            bank.address_1 = bank_Billing_Address_1.getText().toString();
            bank.address_2 = bank_Billing_address_2.getText().toString();
            bank.position = getPosition(description);
        }

        Intent result = new Intent();
        result.putExtra(KEY_BANK, bank);
        setResult(Activity.RESULT_OK, result);
        finish();
    }

    private void delete() {
        Intent result = new Intent();
        result.putExtra(KEY_BANK_ID, bank.id);
        setResult(Activity.RESULT_OK, result);
        finish();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private int getPosition(String description) {

        int position;
        switch (description) {
            case "Chase":
                position = 0;
                break;
            case "Citizens Bank":
                position = 1;
                break;
            case "Citi Bank":
                position = 2;
                break;
            case "Visa_debit":
                position = 3;
                break;
            case "浦发银行":
                position = 4;
                break;
            case "PNC Bank":
                position = 5;
                break;
            case "Bank of America":
                position = 6;
                break;
            case "中国银行":
                position = 7;
                break;
            case "中国农业银行":
                position = 8;
                break;
            case "Discover":
                position = 9;
                break;
            case "Other Banks":
                position = 10;
                break;
            case "Wells Fargo":
                position = 11;
                break;
            case "中国工商银行":
                position = 12;
                break;
            case "中国建设银行":
                position = 13;
                break;
            case "银联":
                position = 14;
                break;
            case "Master Card":
                position = 15;
                break;
            case "中信银行":
                position = 16;
                break;
            default: position = 0;
                break;
        }
        return position;
    }
}
