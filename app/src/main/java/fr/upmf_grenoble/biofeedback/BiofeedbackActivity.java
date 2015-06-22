package fr.upmf_grenoble.biofeedback;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import belt_connector.BeltConnectorFake;
import belt_connector.BeltConnectorZephyr;
import belt_connector.ZephyrSummaryPacket;
import file_writer.FileWriter;

public class BiofeedbackActivity extends Activity implements Observer {

    private BeltConnectorZephyr beltConnectorZephyr;
    private BeltConnectorFake beltConnectorFake;
    private BiofeedbackActivityUpdater biofeedbackActivityUpdater;

    private FileWriter fileWriter;

    final Context context = this;
    private Button buttonLogOn;
    private Button buttonLogOff;
    private Button buttonEvent;
    private Button buttonBioFeedback;
    private Button buttonFakeFeedback;
    private Button buttonRandom;

    private int random = 0;
    private boolean log = false;
    private String event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biofeedback);

        TextView textZephyr = (TextView) findViewById(R.id.text_zephyr);
        TextView textFake = (TextView) findViewById(R.id.text_fake);

        ImageView rectEmpty = (ImageView) findViewById(R.id.rect_empty);
        ImageView rectFull = (ImageView) findViewById(R.id.rect_full);
        ImageView rectWhite = (ImageView) findViewById(R.id.rect_white);

        biofeedbackActivityUpdater = new BiofeedbackActivityUpdater(textZephyr, textFake, rectEmpty, rectFull, rectWhite);

        //Mise en place des bouttons
        buttonLogOn = (Button) findViewById(R.id.button_logOn);
        buttonLogOn.setOnClickListener(clickListener);
        enable(buttonLogOn);

        buttonLogOff = (Button) findViewById(R.id.button_logOff);
        buttonLogOff.setOnClickListener(clickListener);
        disable(buttonLogOff);

        buttonEvent = (Button) findViewById(R.id.button_event);
        buttonEvent.setOnClickListener(clickListener);
        disable(buttonEvent);

        buttonBioFeedback = (Button) findViewById(R.id.button_bioFeedback);
        buttonBioFeedback.setOnClickListener(clickListener);
        disable(buttonBioFeedback);

        buttonFakeFeedback = (Button) findViewById(R.id.button_fakeFeedback);
        buttonFakeFeedback.setOnClickListener(clickListener);
        disable(buttonFakeFeedback);

        buttonRandom = (Button) findViewById(R.id.button_random);
        buttonRandom.setOnClickListener(clickListener);
        disable(buttonRandom);

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
            ZephyrSummaryPacket zephyrSummaryPacket = (ZephyrSummaryPacket) data;
            biofeedbackActivityUpdater.setZephyrSummaryPacket(zephyrSummaryPacket);
            if(log) {
                if(event == null) {
                    fileWriter.writeCsvData(String.valueOf(zephyrSummaryPacket.getHeartRate()), "", "");
                } else {
                    fileWriter.writeCsvData(String.valueOf(zephyrSummaryPacket.getHeartRate()), "", event);
                    event = null;
                }
            }
        }
        BiofeedbackActivity.this.runOnUiThread(biofeedbackActivityUpdater);
    }

    private void enable(Button button){
        button.setClickable(true);
        button.setAlpha(1f);
    }

    private void disable(Button button){
        button.setClickable(false);
        button.setAlpha(0.5f);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                // Case Start Logs
                case R.id.button_logOn:
                    final EditText inputParticipant = new EditText(context);
                    inputParticipant.setHint("N° participant");
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                            .setTitle("Entrez le numéro du participant")
                            .setMessage("Entrez le numéro de participant")
                            .setView(inputParticipant)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String participant = inputParticipant.getText().toString();
                                    fileWriter = new FileWriter(participant);
                                    log = true;
                                    //Switch des bouttons
                                    disable(buttonLogOn);
                                    enable(buttonLogOff);
                                    enable(buttonEvent);
                                    disable(buttonBioFeedback);
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
                    log = false;
                    fileWriter.close();
                    //Switch des bouttons
                    disable(buttonLogOff);
                    disable(buttonEvent);
                    disable(buttonBioFeedback);
                    disable(buttonFakeFeedback);
                    disable(buttonRandom);
                    enable(buttonLogOn);
                    break;

                case R.id.button_event:
                    final EditText inputEvent = new EditText(context);
                    inputEvent.setHint("Libellé de l'évènement");
                    AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(context)
                            .setTitle("Entrez le libellé de l'évènement")
                            .setMessage("Entrez le libellé de l'évènement")
                            .setView(inputEvent)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    event = inputEvent.getText().toString();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert);

                    AlertDialog alertDialog2 = alertDialogBuilder2.create();
                    alertDialog2.show();
                    break;

                // Case Start BioFeedback
                case R.id.button_bioFeedback:
                    final EditText inputTime = new EditText(context);
                    inputTime.setHint("Durée (secondes)");
                    AlertDialog.Builder alertDialogBuilderBioFeedback = new AlertDialog.Builder(context)
                            .setTitle("Entrez la durée du biofeedback")
                            .setMessage("Entrez la durée du biofeedback")
                            .setView(inputTime)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String time = inputTime.getText().toString();
                                    event = "biofeedback";
                                    biofeedbackActivityUpdater.setFake(false);
                                    disable(buttonBioFeedback);
                                    enable(buttonFakeFeedback);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert);

                    AlertDialog alertDialogBioFeedback = alertDialogBuilderBioFeedback.create();
                    alertDialogBioFeedback.show();
                    break;

                // Case Start FakeFeedback
                case R.id.button_fakeFeedback:
                    LayoutInflater factory = LayoutInflater.from(context);
                    final View textEntryView = factory.inflate(R.layout.text_entry, null);
                    final EditText inputTime2 = (EditText) textEntryView.findViewById(R.id.editText1);
                    final EditText inputProgression = (EditText) textEntryView.findViewById(R.id.editText2);
                    inputTime2.setHint("Durée (secondes)");
                    inputProgression.setHint("Progression (pourcentage)");

                    AlertDialog.Builder alertDialogBuilderFakeFeedback = new AlertDialog.Builder(context)
                            .setTitle("Entrez la durée/progression du biofeedback")
                            .setMessage("Entrez la durée/progression du biofeedback")
                            .setView(textEntryView)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String time = inputTime2.getText().toString();
                                    String progression = inputProgression.getText().toString();
                                    event = "fake biofeedback";
                                    biofeedbackActivityUpdater.setFake(true);
                                    disable(buttonFakeFeedback);
                                    enable(buttonBioFeedback);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert);

                    AlertDialog alertDialogFakeFeedback = alertDialogBuilderFakeFeedback.create();
                    alertDialogFakeFeedback.show();

                    break;

                // Case Start Random
                case R.id.button_random:
                    enable(buttonFakeFeedback);
                    enable(buttonBioFeedback);
                    if(random == 0){
                        Random rand = new Random();
                        random = rand.nextInt(2) + 1;
                        if(random == 1) {
                            event = "random biofeedback";
                            biofeedbackActivityUpdater.setFake(false);
                        } else if (random == 2) {
                            event = "random fake biofeedback";
                            biofeedbackActivityUpdater.setFake(true);
                        }
                    } else if(random == 1){
                        /* BioFeedback Done */
                        event = "random fake biofeedback";
                        biofeedbackActivityUpdater.setFake(true);
                        random = 0;
                    }
                    else if(random == 2){
                        /* FakeFeedback Done */
                        event = "random biofeedback";
                        biofeedbackActivityUpdater.setFake(false);
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
        if (beltConnectorZephyr != null) {
            try {
                beltConnectorZephyr.stop();
            } catch (Exception e) {
                System.out.println("Impossible de fermer la connexion à la ceinture");
            }
        }
        if (fileWriter != null) {
            fileWriter.close();
        }
    }

}
