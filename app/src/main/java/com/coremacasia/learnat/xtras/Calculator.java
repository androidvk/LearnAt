package com.coremacasia.learnat.xtras;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.coremacasia.learnat.R;

import java.text.DecimalFormat;

public class Calculator extends AppCompatActivity {
    private static final String TAG = "Calculator";
    private EditText eN, eCashback, eMarketing, eCourseFee, eMentorFee;
    private Button bSubmit;
    private int iN;
    private int iCashBack;
    private int iMarketing;
    private int iCourseFee;
    private int iMentorFee;
    String result;
    private TextView tResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        eN = findViewById(R.id.eN);
        eCashback = findViewById(R.id.eCashBack);
        eMarketing = findViewById(R.id.eMarketingCost);
        eCourseFee = findViewById(R.id.eCourse);
        eMentorFee = findViewById(R.id.eMentorFee);
        bSubmit = findViewById(R.id.bSubmit);
        tResult = findViewById(R.id.tResult);

        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });
    }

    private void getData() {
        if (eN.getText().toString().equals("") || eCashback.getText().toString().equals("")
                || eCourseFee.getText().toString().equals("")) {
            Toast.makeText(this, "Input Data to Calculate", Toast.LENGTH_SHORT).show();
            return;

        }

        iN = Integer.parseInt(eN.getText().toString());
        iCashBack = Integer.parseInt(eCashback.getText().toString());
        iCourseFee = Integer.parseInt(eCourseFee.getText().toString());
        iMarketing = Integer.parseInt(eMarketing.getText().toString());
        iMentorFee = Integer.parseInt(eMentorFee.getText().toString());


        double totalStudent = (Math.pow(2, (iN + 1))) - 1;
        double totalReceivers = (Math.pow(2, iN)) - 1;

        float amountIn = (float) (totalStudent * iCourseFee);
        float amountOut = (float) ((iCashBack / 100f) * (iCourseFee * totalReceivers));

        float profitWithoutExpenses = amountIn - amountOut;
        float profitPerStudentWithoutExpense = (float) (profitWithoutExpenses / totalStudent);
        float profitPercentWithoutExpense = (profitPerStudentWithoutExpense / iCourseFee) * 100;


        float totalExpenses = (float) ((iMentorFee + iMarketing) * totalStudent);
        double profitWithExpenses = amountIn - (amountOut + totalExpenses);
        float profitPerStudentWithExpenses = (float) (profitWithExpenses / totalStudent);
        float profitPercentWithExpense = (profitPerStudentWithExpenses / iCourseFee) * 100;
        DecimalFormat df = new DecimalFormat("0.00");
        NumberToWordConverter n;

        result = "==========\n" +
                "N: " + iN + "\n" +
                "Total Subscription: " + totalStudent + "\n" +
                "Total Receivers: " + totalReceivers + "\n" +
                "CashBack: " + iCashBack + "%\n" + "--------\n" +
                "Amount in: Rs." + amountIn + "\n" +
                "[ " + NumberToWordConverter.convert((int) amountIn) + " ]\n\n" +
                "Amount Out: Rs." + amountOut + "\n" +
                "[ " + NumberToWordConverter.convert((int) amountOut) + " ]\n\n --------\n" +

                /*"Without Expenses:-\n" +
                "Profit: Rs." + profitWithoutExpenses + "\n" +
                "[ " + NumberToWordConverter.convert((int) profitWithoutExpenses) + " ]\n\n" +

                "Profit Per Subscription: Rs." + df.format(profitPerStudentWithoutExpense) + "\n" +
                "Profit Percentage: " + df.format(profitPercentWithoutExpense) + "%\n" +

                "--------\n" +*/
                "With Expenses:-\n" +
                "Amount in: Rs." + amountIn + "\n" +
                "[ " + NumberToWordConverter.convert((int) amountIn) + " ]\n\n" +

                "Total Amount Out: Rs." + ((totalStudent * iMentorFee)
                + (totalStudent * iMarketing) + amountOut) + "\n" +

                "[ " + NumberToWordConverter.convert((int) ((totalStudent * iMentorFee)
                + (totalStudent * iMarketing) + amountOut)) + " ]\n\n" +

                "Profit: Rs." + profitWithExpenses + "\n" +
                "[ " + NumberToWordConverter.convert((int) profitWithExpenses) + " ]\n\n" +

                "Profit Per Subscription: Rs." + df.format(profitPerStudentWithExpenses) + "\n" +

                "[ " + NumberToWordConverter.convert((int) profitPerStudentWithExpenses) + " ]\n\n" +
                "Profit Percentage: " + df.format(profitPercentWithExpense) + "%\n" + "--------\n" +

                "Total Expenses: Rs." + ((totalStudent * iMentorFee)
                + (totalStudent * iMarketing)) + "\n" +

                "[ " + NumberToWordConverter.convert((int) ((totalStudent * iMentorFee)
                + (totalStudent * iMarketing))) + " ]\n\n" +

                "Total Mentor Expense: Rs." + (totalStudent * iMentorFee) + "\n" +
                "[ " + NumberToWordConverter.convert((int) (totalStudent * iMentorFee)) + " ]\n\n" +

                "Total Marketing Expense: Rs." + (totalStudent * iMarketing) + "\n" +
                "[ " + NumberToWordConverter.convert((int) (totalStudent * iMarketing)) + " ]" ;

        tResult.setText(result);
        eN.setText((iN + 1) + "");
        closeKeyboard();
    }

    private void closeKeyboard() {
        // this will give us the view
        // which is currently focus
        // in this layout
        View view = this.getCurrentFocus();

        // if nothing is currently
        // focus then this will protect
        // the app from crash
        if (view != null) {

            // now assign the system
            // service to InputMethodManager
            InputMethodManager manager
                    = (InputMethodManager)
                    getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            manager
                    .hideSoftInputFromWindow(
                            view.getWindowToken(), 0);
        }
    }
}