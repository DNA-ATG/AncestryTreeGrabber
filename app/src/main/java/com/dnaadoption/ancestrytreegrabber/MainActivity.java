package com.dnaadoption.ancestrytreegrabber;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";        // This CLASS name
    String ATG_Version = " ";           // initialize
    EditText editTxt_treeNum;
    TextView txt_Desc, txt_HomePers, lbl_Desc, lbl_HomePers;
    Button btn_Grab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, "@@@@  Activity started  @@@@");
        try {
            ATG_Version = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        Toast toast = Toast.makeText(getBaseContext(), "Ancestry Tree Grabber ©2017  Ver." + ATG_Version, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txt_Desc = (TextView) findViewById(R.id.txt_Desc);
        txt_HomePers = (TextView) findViewById(R.id.txt_HomePers);
        lbl_Desc = (TextView) findViewById(R.id.lbl_Desc);
        lbl_HomePers = (TextView) findViewById(R.id.lbl_HomePers);
        txt_Desc.setVisibility(View.INVISIBLE);
        txt_HomePers.setVisibility(View.INVISIBLE);
        lbl_Desc.setVisibility(View.INVISIBLE);
        lbl_HomePers.setVisibility(View.INVISIBLE);

        Button btn_Grab = (Button) findViewById(R.id.btn_Grab);   // Listner defined in Layout XML
//        button_View.setOnClickListener(btn_Grab_Click);
        editTxt_treeNum = (EditText) findViewById(R.id.editTxt_treeNum);
        editTxt_treeNum.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Log.w(TAG, " editTxt_treeNum listener");
//                    Log.w(TAG, "Tree # = " + editText_treeNum.getText());
                    return true;
                }
                return false;

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

// Start Listeners
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
public void btn_Grab_Click(View view) {
    Log.w(TAG, "*** btn_Grab Click ***");
    txt_Desc.setVisibility(View.VISIBLE);
    txt_HomePers.setVisibility(View.VISIBLE);
    lbl_Desc.setVisibility(View.VISIBLE);
    lbl_HomePers.setVisibility(View.VISIBLE);

    txt_Desc.setText(editTxt_treeNum.getText());

}
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@



    //###################################################################
//###################################################################
//###################################################################
    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "onStart");
    }

    public void onPause() {
        super.onPause();
        Log.v(TAG, "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG, "onStop");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "OnDestroy");
        // ToDo - ??????
    }

}
