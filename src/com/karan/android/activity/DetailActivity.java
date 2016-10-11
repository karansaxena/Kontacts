package com.karan.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.karan.android.R;

public class DetailActivity extends Activity {
    String email = "", phone = "", name = "";
    private TextView nameTV, emailTV, phoneTV;
    private ImageButton callButton, smsButton, emailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        nameTV = (TextView) findViewById(R.id.nameTV);
        phoneTV = (TextView) findViewById(R.id.mobileTV);
        emailTV = (TextView) findViewById(R.id.emailTV);
        callButton = (ImageButton) findViewById(R.id.callButton);
        smsButton = (ImageButton) findViewById(R.id.smsButton);
        emailButton = (ImageButton) findViewById(R.id.mailButton);
        parseIntent();
        setTextViewData();
        setOnClickListeners();
    }
    
    private void parseIntent() {
        try {
            Bundle b = getIntent().getExtras();
            name = b.getString("name");
            phone = b.getString("phone");
            email = b.getString("email");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTextViewData() {
        nameTV.setText(name);
        emailTV.setText(email);
        phoneTV.setText(phone);
    }

    private void setOnClickListeners() {
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri callNumUri = Uri.parse("tel:" + phone);
                Intent callIntent = new Intent(Intent.ACTION_DIAL, callNumUri);
                startActivity(callIntent);
            }
        });
        smsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri smsNumUri = Uri.parse("smsto:" + phone);
                Intent smsIntent = new Intent(Intent.ACTION_SENDTO, smsNumUri);
                startActivity(smsIntent);
            }
        });
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                String aEmailList[] = { email };
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, aEmailList);
                emailIntent.setType("plain/text");
                startActivity(emailIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
