package fr.upmf_grenoble.biofeedback;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import belt_connector.BeltConnectorFake;
import belt_connector.BeltConnectorZephyr;
import belt_connector.ZephyrSummaryPacket;

public class BiofeedbackActivity extends Activity implements Observer {

    private BeltConnectorZephyr beltConnectorZephyr;
    private BeltConnectorFake beltConnectorFake;
    private BiofeedbackActivityUpdater biofeedbackActivityUpdater;

    private TextView textZephyr;
    private TextView textFake;

    final Context context = this;
    private Button buttonLogOn;
    private Button buttonLogOff;
    private Button buttonBioFeedback;
    private Button buttonFakeFeedback;
    private Button buttonRandom;
    private int random = 0;

    /* FileWriter*/
    FileWriter writer;
    File root = Environment.getExternalStorageDirectory();
    File folder = new File(root + "/biofeedback");


    private void writeCsvData(String d, String e, String f) throws IOException {
        String line = String.format("%s,%s,%s\n", d, e, f);
        writer.write(line);
    }
    /* FileWriter */

    private void enable(Button button){
        button.setClickable(true);
        button.setAlpha(1f);
    }

    private void disable(Button button){
        button.setClickable(false);
        button.setAlpha(0.5f);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biofeedback);

        textZephyr = (TextView) findViewById(R.id.text_zephyr);
        textFake = (TextView) findViewById(R.id.text_fake);

        //Mise en place des bouttons
        buttonLogOn = (Button) findViewById(R.id.button_logOn);
        buttonLogOn.setOnClickListener(clickListener);
        enable(buttonLogOn);

        buttonLogOff = (Button) findViewById(R.id.button_logOff);
        buttonLogOff.setOnClickListener(clickListener);
        disable(buttonLogOff);

        buttonBioFeedback = (Button) findViewById(R.id.button_bioFeedback);
        buttonBioFeedback.setOnClickListener(clickListener);
        disable(buttonBioFeedback);

        buttonFakeFeedback = (Button) findViewById(R.id.button_fakeFeedback);
        buttonFakeFeedback.setOnClickListener(clickListener);
        disable(buttonFakeFeedback);

        buttonRandom = (Button) findViewById(R.id.button_random);
        buttonRandom.setOnClickListener(clickListener);
        disable(buttonRandom);

        biofeedbackActivityUpdater = new BiofeedbackActivityUpdater(textZephyr, textFake);

        if(folder.exists() && folder.isDirectory()) {
        }else{
            folder.mkdir();
        }

        beltConnectorFake = new BeltConnectorFake();
        beltConnectorFake.addObserver(this);
        beltConnectorFake.start();

        if(MainActivity.macAddress == null) {
            Toast.makeText(this, "Vous devez selectionner un appareil avant de faire le BioFeedback", Toast.LENGTH_LONG).show();
        } else {
            beltConnectorZephyr = new BeltConnectorZephyr(MainActivity.macAddress);
            beltConnectorZephyr.addObserver(this);
            beltConnectorZephyr.start();
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        if(observable instanceof BeltConnectorFake) {
            biofeedbackActivityUpdater.setHrv((int) data);
        } else if(observable instanceof BeltConnectorZephyr) {
           biofeedbackActivityUpdater.setZephyrSummaryPacket((ZephyrSummaryPacket) data);
        }
        BiofeedbackActivity.this.runOnUiThread(biofeedbackActivityUpdater);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                // Case Start Logs
                case R.id.button_logOn:
                    final EditText input = new EditText(context);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                            .setTitle("Entrez le numéro du participant")
                            .setMessage("Entrez le numéro de participant")
                            .setView(input)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String participant = input.getText().toString();

                                    /* FileWriter */
                                    File gpxfile = new File(folder, "biofeedback_" + participant + ".csv");
                                    try {
                                        writer = new FileWriter(gpxfile);
                                        writeCsvData("N° Participant:" + participant , " ", " ");
                                        writeCsvData("125","","baseline");
                                        writeCsvData("235", "", "");
                                        writeCsvData("125", "5min", "");
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    /* FileWriter */


                                    //Switch des bouttons
                                    disable(buttonLogOn);
                                    enable(buttonLogOff);
                                    enable(buttonBioFeedback);
                                    enable(buttonFakeFeedback);
                                    enable(buttonRandom);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert);

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    break;

                // Case Stop Logs
                case R.id.button_logOff:
                    /* FileWriter */
                    try {
                        writer.flush();
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    /* FileWriter */

                    //Switch des bouttons
                    disable(buttonLogOff);
                    disable(buttonBioFeedback);
                    disable(buttonFakeFeedback);
                    disable(buttonRandom);
                    enable(buttonLogOn);
                    break;

                // Case Start BioFeedback
                case R.id.button_bioFeedback:
                    break;

                // Case Start FakeFeedback
                case R.id.button_fakeFeedback:
                    break;

                // Case Start Random
                case R.id.button_random:
                    if(random==0){
                        /* generate random=1|2 */
                        /* start feedback 1|2 */
                    }else
                    if(random==1){ /* BioFeedback Done */
                        /* Start Fakefeedback */
                        random = 0;
                    }else
                    if(random==2){ /* FakeFeedback Done */
                        /* Start BioFeedback */
                        random = 0;
                    }
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        beltConnectorFake.stop();
        if(beltConnectorZephyr != null) {
            try {
                beltConnectorZephyr.stop();
            } catch (Exception e) {
            }
        }
    }

}
