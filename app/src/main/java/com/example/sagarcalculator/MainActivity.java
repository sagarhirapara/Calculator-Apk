package com.example.sagarcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    TextView display,history;
    String value = "";
    String result;
    String historyValue ="";
    Button b1,b2,b3,b4,b5,b6,b7,b8,b9,b0,b00,plus,minus,multi,div,ans,clear,bpoint,clearHistory,delete;
    public boolean flag = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1 = findViewById(R.id.n1);
        b2 = findViewById(R.id.n2);
        b3 = findViewById(R.id.n3);
        b4 = findViewById(R.id.n4);
        b5 = findViewById(R.id.n5);
        b6 = findViewById(R.id.n6);
        b7 = findViewById(R.id.n7);
        b8 = findViewById(R.id.n8);
        b9 = findViewById(R.id.n9);
        b0 = findViewById(R.id.n0);
        bpoint = findViewById(R.id.npoint);
        b00 = findViewById(R.id.n00);
        plus = findViewById(R.id.plus);
        minus = findViewById(R.id.min);
        multi = findViewById(R.id.mul);
        div = findViewById(R.id.div);
        ans = findViewById(R.id.ans);
        clear = findViewById(R.id.clear);
        clearHistory = findViewById(R.id.clearHistory);
        delete = findViewById(R.id.delete);

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        b5.setOnClickListener(this);
        b6.setOnClickListener(this);
        b7.setOnClickListener(this);
        b8.setOnClickListener(this);
        b9.setOnClickListener(this);
        b0.setOnClickListener(this);
        b00.setOnClickListener(this);

        plus.setOnClickListener(this);
        minus.setOnClickListener(this);
        multi.setOnClickListener(this);
        ans.setOnClickListener(this);
        clear.setOnClickListener(this);
        div.setOnClickListener(this);
        bpoint.setOnClickListener(this);
        clearHistory.setOnClickListener(this);
        delete.setOnClickListener(this);

    }
    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }
    @Override
    public void onClick(View v) {
        display = findViewById(R.id.display);
        history = findViewById(R.id.history);
        String displayText = display.getText().toString();
        if(displayText.length() > 50)
        {
            return;
        }
        if(v.getId() == R.id.clearHistory)
        {
            historyValue = "";
            history.setText(" ");
            return;
        }
        if(flag == false)
        {

            value = "";
            display.setText(" ");
            flag = true;
        }
        if(v.getId()==R.id.clear)
        {
            value = "";
            display.setText(value);
            return;
        }
        if(v.getId() == R.id.ans)
        {
            String finalValue = value;
            if(value.length()==0)
            {
                return;
            }
            char first = finalValue.charAt(0);
            char last = finalValue.charAt(finalValue.length()-1);

            while(first == '*' || first == '+' || first == '/')
            {
                if(finalValue.length() >1)
                {
                    finalValue = finalValue.substring(1);
                    first = finalValue.charAt(0);
                }

            }

            while(last == '*' || last == '+' || last == '/' || last== '-')
            {
                if(finalValue.length() >1)
                {
                    finalValue = finalValue.substring(0,(finalValue.length())-1);
                    last = finalValue.charAt(finalValue.length()-1);
                }

            }

            String m = "";

            try {
                result = String.valueOf(eval(finalValue));
                display.setText(result);
            }
            catch (Exception e)
            {
                 m = "invalid input";
                display.setText("invalid input");
            }

            flag = false;
            if(m != "invalid input")
            {
                historyValue += value + "=" + result +"\n";
                history.setText(historyValue );
                ScrollView scrollView = findViewById(R.id.scroll);
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }

            return;
        }
        if(v.getId() == R.id.delete)
        {
            if(value.length()>0)
            {
                value = value.substring(0,value.length()-1);
                display.setText(value);
            }
            return;
        }
        Button b = findViewById(v.getId());
        String text = b.getText().toString();
        if(value.length() == 0)
        {
            if(text.contains("*") || text.contains("/") || text.contains("+"))
            {
                return;
            }

        }
        else
        {
            if(text.contains("*") || text.contains("/") || text.contains("+"))
            {
                int len = value.length();

                char x = value.charAt(len-1);
                if(x == '+' || x == '*' || x == '/')
                {
                    display.setText(value + "");
                    return;
                }
            }


        }

        value = value + text;
        display.setText(value);

    }
}