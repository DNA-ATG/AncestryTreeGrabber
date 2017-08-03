package com.dnaadoption.ancestrytreegrabber;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
// JSoup
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

// TEST & DEBUG
import android.widget.Toast;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";        // This CLASS name
    String ATG_Version = " ";           // initialize
    String URL_OverView = "https://www.ancestry.com/family-tree/tree/";
    String tree_Num;                    // Tree # entered
    String Home_Person = " ";
    String HomPers_URL = "";
    String Num_People = " ";
    EditText editTxt_treeNum;
    TextView txt_Desc, txt_HomePers, txt_People, lbl_Desc, lbl_HomePers, lbl_People;
    Button btn_Grab, btn_GEDCOM;
    ProgressBar progressBar1;

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
        preReqs();                        // Check for pre-requisites
        
        Toast toast = Toast.makeText(getBaseContext(), "Ancestry Tree Grabber ©2017  Ver." + ATG_Version, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar1.setVisibility(View.INVISIBLE);
        txt_Desc = (TextView) findViewById(R.id.txt_Desc);
        txt_HomePers = (TextView) findViewById(R.id.txt_HomePers);
        txt_People = (TextView) findViewById(R.id.txt_People);
        lbl_Desc = (TextView) findViewById(R.id.lbl_Desc);
        lbl_HomePers = (TextView) findViewById(R.id.lbl_HomePers);
        lbl_People = (TextView) findViewById(R.id.lbl_People);
        txt_Desc.setVisibility(View.INVISIBLE);
        txt_HomePers.setVisibility(View.INVISIBLE);
        txt_People.setVisibility(View.INVISIBLE);
        lbl_Desc.setVisibility(View.INVISIBLE);
        lbl_HomePers.setVisibility(View.INVISIBLE);
        lbl_People.setVisibility(View.INVISIBLE);
        txt_Desc.setMovementMethod(new ScrollingMovementMethod());

        Button btn_Grab = (Button) findViewById(R.id.btn_Grab);         // Listner defined in Layout XML
//        button_View.setOnClickListener(btn_Grab_Click);
        Button btn_GEDCOM = (Button) findViewById(R.id.btn_GEDCOM);     // Listner defined in Layout XML
//        button_View.setOnClickListener(btn_GEDCOM_Click);
        btn_GEDCOM.setVisibility(View.GONE);
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

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private void preReqs() {
        boolean isSdPresent;
        isSdPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        Log.w(TAG, "SD card: " + isSdPresent);
        if (isSdPresent) { 		// Make sure ATG directory is there
            File extStore = Environment.getExternalStorageDirectory();
            File directFRC = new File(Environment.getExternalStorageDirectory() + "/download/ATG");
            if(!directFRC.exists())  {
                if(directFRC.mkdir())
                { }        //directory is created;
            }
            Log.w(TAG, "ATG file(s) created");
        }  else {
            Toast.makeText(getBaseContext(), "There is no SD card available", Toast.LENGTH_LONG).show();
        }
    }

// #######################################################################
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
        if (id == R.id.action_about) {
            Toast toast = Toast.makeText(getBaseContext(), "Ancestry Tree Grabber ©2017  Ver." + ATG_Version + "\n             by DNA Adoption.com", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

// Start Listeners
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    public void btn_Grab_Click(View view) {
        Log.w(TAG, "*** btn_Grab Click ***");

        tree_Num = String.valueOf(editTxt_treeNum.getText());
        tree_Num = "16546820";                              // **DEBUG**
        editTxt_treeNum.setText(tree_Num);                  // **DEBUG**
        txt_Desc.setText(" ");          // clear data
        txt_HomePers.setText(" ");      //
        txt_People.setText(" ");        //

        getWebsite();       // Get Website data

    }

    // --------------------------------------------------
    public void btn_GEDCOM_Click(View view) {
        Log.w(TAG, "@@@ btn_GEDCOM Click @@@");
        Button btn_GEDCOM = (Button) findViewById(R.id.btn_GEDCOM);
        Show_Alert();

        btn_GEDCOM.setVisibility(View.GONE);
    }

// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private int getWebsite() {
        Log.w(TAG, ">>>  getWebsite  <<< " + tree_Num);
        final Button btn_GEDCOM = (Button) findViewById(R.id.btn_GEDCOM);
        new Thread(new Runnable() {
            @Override
            public void run() {
            final StringBuilder builder = new StringBuilder();
            String J_URL = URL_OverView + tree_Num + "/recent";
            Log.w(TAG, "JSoup URL = " + J_URL);

            try {
                Document doc = Jsoup.connect(J_URL).get();
//--------------------------------------------------------------------------------------------
//              Elements select = doc.select("table[width=80%] tr:has(td:matchesOwn(^Name:$)) td:eq(1)")
                String srchCSS = "div.page";
//                Elements select = doc.getElementsByIndexGreaterThan(0);
                Elements select = doc.select(srchCSS);
                Iterator<Element> iterator = select.iterator();
                Log.w(TAG, "'" + srchCSS + "' # " + select.size());
                while (iterator.hasNext()) {
                    Element x = iterator.next();
                    System.out.println("'" + srchCSS + "':  " + x.text());
                }
                Log.e(TAG, "%%% COMPLETE %%%");

//_____________________________________________________________________________________________
                builder.append("\n").append("________________________________");

                Home_Person = "John Doe";
                HomPers_URL = "https://www.ancestry.com/family-tree/tree/16546820/person/1117640515";
                Num_People = "1234";


    } catch (IOException e) {
            builder.append("Error : Invalid Tree #").append("\n");
                builder.append("***** ").append(e.getMessage()).append("\n");
                Home_Person = "***";
                Num_People = "0";
                Log.e(TAG, "****  Error : Invalid URL  ****");

            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                txt_Desc.setText(builder.toString());
                txt_HomePers.setText(Home_Person);
                txt_People.setText(Num_People);

                txt_Desc.setVisibility(View.VISIBLE);
                txt_HomePers.setVisibility(View.VISIBLE);
                txt_People.setVisibility(View.VISIBLE);
                lbl_Desc.setVisibility(View.VISIBLE);
                lbl_HomePers.setVisibility(View.VISIBLE);
                lbl_People.setVisibility(View.VISIBLE);
//                    SystemClock.sleep(5000);        // Wait for them to see data
                btn_GEDCOM.setVisibility(View.VISIBLE);

                }
            });
            }
        }).start();
        return 0;
    }

    private void Show_Alert() {
//        SystemClock.sleep(5000);        // Wait for them to see data
        AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        alertbox.setMessage("Do you want to create a GEDCOM for this tree? (fees will apply)");
        alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Log.w(TAG, "User chose YES");
                new GEDCOM_Async().execute();     // Load data Asyncronously

//                    finish();
            }
        });
        alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Log.e(TAG, "User chose NO");
                Toast.makeText(getBaseContext(), "No GEDCOMs will be generated", Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog dialog = alertbox.create();
        alertbox.show();
    }


//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private class GEDCOM_Async extends AsyncTask<Void, Integer, String> {
    int progress = 0;
    int progIncrement = 100 / 8;
    ProgressDialog pd;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.i("GEDCOM_Async", "***  onPreExecute  ***");
        progressBar1.setVisibility(ProgressBar.VISIBLE);

        pd = new ProgressDialog(MainActivity.this);
        pd.setTitle("Loading data for tree of " + Home_Person);
        pd.setMessage("Please wait for data");
//            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            pd.setMax(100);
        pd.setCancelable(false);
//        pd.setButton(ProgressDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //Cancel download task
//                pd.cancel();
//                pd.dismiss();
//            }
//        });
        pd.show();
    }

    @Override
    protected String doInBackground(Void... params) {
        Log.i("GEDCOM_Async", "***  doInBackground  *** " + progIncrement);
        progress += 2;
        Log.w(TAG, "URL = " + HomPers_URL);

        try {
            Document tree = Jsoup.connect(HomPers_URL).timeout(0).ignoreHttpErrors(true).get();
            Element hPers = tree.getElementById("ctl05_ctl00_homePerson");
            Log.w(TAG, "attr= " + hPers.attributes());
            Log.w(TAG, "HTML= " + hPers.html());
            Log.e(TAG, "HP= " + hPers.text());

        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int x = 0; x < 8; x++) {
            // ToDo - process all people in the tree
            SystemClock.sleep(1000);

            progress = progress + progIncrement;
            publishProgress(progress);
        }
        progress = 100;
        publishProgress(progress);
        return null;        // end of Asynch Task
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        Log.i("GEDCOM_Async", "***  onProgressUpdate  ***  " + progress);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar1.setProgress(values[0]);
//        pd.setMessage("Please wait for Blue Alliance data - Team: " + tnum);
    }

    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.i("GEDCOM_Async", "***  onPostExecute  ***  " + result);
        progressBar1.setProgress(100);
        SystemClock.sleep(3000);
        if (pd != null) {
            if (pd.isShowing()) {
                pd.dismiss();
            }
        } else {
            Log.e("GEDCOM_Async", "***  pd = NULL  ***  ");
        }
        progressBar1.setVisibility(View.INVISIBLE);
    }
}

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
