package com.dnaadoption.ancestrytreegrabber;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
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

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";        // This CLASS name
    String ATG_Version = " ";           // initialize
    String URL_OverView = "https://www.ancestry.com/family-tree/tree/";
    String Home_Person = " ";
    String HomPers_URL = "";
    String Num_People = " ";
    EditText editTxt_treeNum;
    TextView txt_Desc, txt_HomePers, txt_People, lbl_Desc, lbl_HomePers, lbl_People;
    Button btn_Grab;
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

        txt_Desc.setText(" ");          // clear data
        txt_HomePers.setText(" ");      //
        txt_People.setText(" ");        //

        getWebsite();       // Get Website data
    }

// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
// @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    private void getWebsite() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();
                try {
                    Document doc = Jsoup.connect(URL_OverView + editTxt_treeNum.getText() + "/recent").get();
                    Log.w(TAG, "URL = " + URL_OverView + editTxt_treeNum.getText() + "/recent");
                    Element tOview = doc.select("header.conHeader").first();
                    Log.w(TAG, tOview + "\n");
                    String title = doc.title();
                    Elements links = doc.select("h2");

                    builder.append(title).append("\n");

                    for (Element link : links) {
                        Log.w(TAG, "links " + link.text());
                        builder.append("\n").append("Link : ").append(link.attr("href"))
                                .append("\n").append("Text : ").append(link.text());
                        if (link.text() == "Tree Overview") {

                        }
                    }
                    builder.append("\n").append("********* END *********");
                    Home_Person = "John Doe";
                    HomPers_URL = "https://www.ancestry.com/family-tree/tree/16546820/person/1117640515";
                    Num_People = "1234";


        } catch (IOException e) {
                builder.append("Error : Invalid Tree #").append("\n");
                    builder.append("***** ").append(e.getMessage()).append("\n");
                    Home_Person = "***";
                    Num_People = "0";

                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        txt_Desc.setText(editTxt_treeNum.getText());
                        txt_Desc.setText(builder.toString());
                        txt_HomePers.setText(Home_Person);
                        txt_People.setText(Num_People);

                        txt_Desc.setVisibility(View.VISIBLE);
                        txt_HomePers.setVisibility(View.VISIBLE);
                        txt_People.setVisibility(View.VISIBLE);
                        lbl_Desc.setVisibility(View.VISIBLE);
                        lbl_HomePers.setVisibility(View.VISIBLE);
                        lbl_People.setVisibility(View.VISIBLE);

                        Show_Alert();

                    }
                });
            }
        }).start();
    }

    private void Show_Alert() {
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

//        Document tree = Jsoup.parse("UTF-8",HomPers_URL);
//        Element content = tree.getElementById("content");
//        Elements cards = content.getElementsByTag("h3");
//        for (Element link : cards) {
//            String linkHref = link.attr("class");
//            String linkText = link.text();
//        }

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
