package fr.upmf_grenoble.biofeedback;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import belt_connector.BeltConnectorFake;
import belt_connector.BeltConnectorZephyr;
import belt_connector.ZephyrRRPacket;
import belt_connector.ZephyrSummaryPacket;
import file_writer.FileWriter;

public class BiofeedbackActivity extends Activity implements Observer {

    private static final int WAITING_TIME_FAKE_BIOFEEDBACK = 60;

    private BeltConnectorZephyr beltConnectorZephyr;
    private BeltConnectorFake beltConnectorFake;
    private BiofeedbackActivityUpdater biofeedbackActivityUpdater;
    private RRWindow rrWindow;

    private FileWriter fileWriter;

    private final Context context = this;
    private Button buttonLogOn;
    private Button buttonLogOff;
    private Button buttonEvent;
    private Button buttonBioFeedback;
    private Button buttonFakeFeedback;
    private Button buttonRandom;

    private boolean booleanLogOn = true;
    private boolean booleanLogOff = false;
    private boolean booleanEvent = false;
    private boolean booleanBioFeedback = true;
    private boolean booleanFakeFeedback = true;
    private boolean booleanRandom = true;

    private TextView textProgression;
    private ProgressBar progressBar;
    private ImageView rectEmpty;
    private ImageView rectFull;
    private ImageView rectWhite;

    private int random = 0;
    private boolean log = false;
    private String event;
    private long time = 0;
    private long bioFeedbackTimer = 0;
    private int bioFeedbackLength = 0;
    private boolean bioFeedbackStarted = false;

    private Handler timerHandler = new Handler();
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            if(bioFeedbackTimer <= 0) {
                timerHandler.removeCallbacks(this);
                startFakeBiofeedback();
            } else {
                bioFeedbackTimer--;
                timerHandler.postDelayed(this, 1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biofeedback);

        initUI();

        biofeedbackActivityUpdater = new BiofeedbackActivityUpdater(context);
        initAU();

        if(MainActivity.macAddress == null) {
            Toast.makeText(this, "Vous devez selectionner un appareil avant de faire le BioFeedback", Toast.LENGTH_LONG).show();
        } else {
            beltConnectorZephyr = new BeltConnectorZephyr(MainActivity.macAddress);
            beltConnectorZephyr.addObserver(this);
            beltConnectorZephyr.start();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setContentView(R.layout.activity_biofeedback);
        initUI();
        initAU();
    }

    @Override
    public void update(Observable observable, Object data) {
        if(observable instanceof BeltConnectorFake) {
            if(data instanceof Float) {
                biofeedbackActivityUpdater.setHrv((float) data);
            } else {
                stopFakeBiofeedback();
            }
        } else if(observable instanceof BeltConnectorZephyr) {
            if(data instanceof ZephyrSummaryPacket) {
                ZephyrSummaryPacket zephyrSummaryPacket = (ZephyrSummaryPacket) data;
                biofeedbackActivityUpdater.setHrv(zephyrSummaryPacket.getHeartRate());
                writeLog(zephyrSummaryPacket.getHeartRate());
            } else if (data instanceof ZephyrRRPacket) {
                ZephyrRRPacket zephyrRRPacket = (ZephyrRRPacket) data;
                if(zephyrRRPacket.getFinalRtoRSample() != 0) {
                    writeLog(zephyrRRPacket.getFinalRtoRSample());
                }
                if(rrWindow != null) {
                    boolean baselineDone = rrWindow.add(zephyrRRPacket.getFinalRtoRSample());
                    if(baselineDone && !bioFeedbackStarted) {
                        startBiofeedback(rrWindow.getSd1());
                    }
                    if(bioFeedbackStarted) {
                        double sd1 = rrWindow.getSd1();
                        biofeedbackActivityUpdater.setHrv(sd1);
                        long currentTime = System.currentTimeMillis() - bioFeedbackTimer;
                        if ((currentTime / 1000) >= bioFeedbackLength) {
                            stopBiofeedback();
                        }
                    }
                }
            }
        }
        BiofeedbackActivity.this.runOnUiThread(biofeedbackActivityUpdater);
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
                                    time = System.currentTimeMillis();
                                    //Switch des bouttons
                                    disable(buttonLogOn); booleanLogOn = false;
                                    enable(buttonLogOff); booleanLogOff = true;
                                    enable(buttonEvent); booleanEvent = true;
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
                    time = 0;
                    fileWriter.close();
                    //Switch des bouttons
                    enable(buttonLogOn); booleanLogOn = true;
                    disable(buttonLogOff); booleanLogOff = false;
                    disable(buttonEvent); booleanEvent = false;
                    break;

                // Case event
                case R.id.button_event:
                    event = "Event ...";
                    final EditText inputEvent = new EditText(context);
                    inputEvent.setHint("Libellé de l'évènement");
                    AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(context)
                            .setTitle("Entrez le libellé de l'évènement")
                            .setMessage("Entrez le libellé de l'évènement")
                            .setView(inputEvent)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    event = "... " + inputEvent.getText().toString();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    event = "... cancel";
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert);
                    AlertDialog alertDialog2 = alertDialogBuilder2.create();
                    alertDialog2.show();
                    break;

                // Case Start BioFeedback
                case R.id.button_bioFeedback:
                    final EditText inputTime = new EditText(context);
                    inputTime.setInputType(InputType.TYPE_CLASS_PHONE);
                    inputTime.setHint("Durée (secondes)");
                    AlertDialog.Builder alertDialogBuilderBioFeedback = new AlertDialog.Builder(context)
                            .setTitle("Entrez la durée du biofeedback")
                            .setMessage("Entrez la durée du biofeedback")
                            .setView(inputTime)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String time = inputTime.getText().toString();
                                    event = "biofeedback";
                                    prepareBiofeedback(Integer.parseInt(time));
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
                    inputTime2.setHint("Durée (secondes)");
                    inputTime2.setInputType(InputType.TYPE_CLASS_PHONE);
                    final EditText inputProgression = (EditText) textEntryView.findViewById(R.id.editText2);
                    inputProgression.setHint("Progression (pourcentage)");
                    inputProgression.setInputType(InputType.TYPE_CLASS_PHONE);

                    AlertDialog.Builder alertDialogBuilderFakeFeedback = new AlertDialog.Builder(context)
                            .setTitle("Entrez la durée/progression du biofeedback")
                            .setMessage("Entrez la durée/progression du biofeedback")
                            .setView(textEntryView)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String time = inputTime2.getText().toString();
                                    String progression = inputProgression.getText().toString();
                                    event = "fake biofeedback";
                                    prepareFakeBiofeedback(Integer.parseInt(progression), Integer.parseInt(time));
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
                    LayoutInflater factoryRandom = LayoutInflater.from(context);
                    final View textEntryViewRandom = factoryRandom.inflate(R.layout.text_entry, null);
                    final EditText inputTimeRandom = (EditText) textEntryViewRandom.findViewById(R.id.editText1);
                    inputTimeRandom.setHint("Durée (secondes)");
                    inputTimeRandom.setInputType(InputType.TYPE_CLASS_PHONE);
                    final EditText inputProgressionRandom = (EditText) textEntryViewRandom.findViewById(R.id.editText2);
                    inputProgressionRandom.setHint("Progression (pourcentage)");
                    inputProgressionRandom.setInputType(InputType.TYPE_CLASS_PHONE);

                    AlertDialog.Builder alertDialogBuilderRandomFeedback = new AlertDialog.Builder(context)
                            .setTitle("Entrez la durée/progression du biofeedback")
                            .setMessage("Entrez la durée/progression du biofeedback")
                            .setView(textEntryViewRandom)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    String time = inputTimeRandom.getText().toString();
                                    String progression = inputProgressionRandom.getText().toString();
                                    if(random == 0){
                                        Random rand = new Random();
                                        random = rand.nextInt(2) + 1;
                                        if(random == 1) {
                                            event = "random biofeedback";
                                            prepareBiofeedback(Integer.parseInt(time));
                                        } else if (random == 2) {
                                            event = "random fake biofeedback";
                                            prepareFakeBiofeedback(Integer.parseInt(progression), Integer.parseInt(time));
                                        }
                                    } else if(random == 1){
                                         /* BioFeedback Done */
                                        event = "random fake biofeedback";
                                        prepareFakeBiofeedback(Integer.parseInt(progression), Integer.parseInt(time));
                                        random = 0;
                                    }
                                    else if(random == 2){
                                         /* FakeFeedback Done */
                                        event = "random biofeedback";
                                        prepareBiofeedback(Integer.parseInt(time));
                                        random = 0;
                                    }
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert);

                    AlertDialog alertDialogRandomFeedback = alertDialogBuilderRandomFeedback.create();
                    alertDialogRandomFeedback.show();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timerHandler.removeCallbacks(timerRunnable);
        if (beltConnectorFake != null) {
            beltConnectorFake.stop();
        }
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

    private void prepareBiofeedback(int time) {
        rrWindow = new RRWindow();
        bioFeedbackLength = time;
        biofeedbackActivityUpdater.setEnableProgressBar(true);
        disable(buttonBioFeedback); booleanBioFeedback = false;
        disable(buttonFakeFeedback); booleanFakeFeedback = false;
        disable(buttonRandom); booleanRandom = false;
        BiofeedbackActivity.this.runOnUiThread(biofeedbackActivityUpdater);
    }

    private void startBiofeedback(double baseline) {
        bioFeedbackStarted = true;
        bioFeedbackTimer = System.currentTimeMillis();
        biofeedbackActivityUpdater.setEnableProgressBar(false);
        biofeedbackActivityUpdater.setMin(0);
        biofeedbackActivityUpdater.setMax(baseline * 2);
        biofeedbackActivityUpdater.setStart(baseline);
        biofeedbackActivityUpdater.calculDifference();
        event = "baseline done";
    }

    public void stopBiofeedback() {
        rrWindow = null;
        bioFeedbackStarted = false;
        biofeedbackActivityUpdater.setPopUp(true);
        biofeedbackActivityUpdater.setEnableButtons(true);
        booleanBioFeedback = true;
        booleanFakeFeedback = true;
        booleanRandom = true;
        event = "biofeedback done";
    }

    private void prepareFakeBiofeedback(int progression, int time) {
        biofeedbackActivityUpdater.setEnableProgressBar(true);
        biofeedbackActivityUpdater.setMin(0);
        biofeedbackActivityUpdater.setMax(100);
        biofeedbackActivityUpdater.setStart(50);
        biofeedbackActivityUpdater.calculDifference();
        beltConnectorFake = new BeltConnectorFake(progression, time);
        beltConnectorFake.addObserver(BiofeedbackActivity.this);
        disable(buttonBioFeedback); booleanBioFeedback = false;
        disable(buttonFakeFeedback); booleanFakeFeedback = false;
        disable(buttonRandom); booleanRandom = false;
        bioFeedbackTimer = WAITING_TIME_FAKE_BIOFEEDBACK;
        timerHandler.postDelayed(timerRunnable, 1000);
        BiofeedbackActivity.this.runOnUiThread(biofeedbackActivityUpdater);
    }

    private void startFakeBiofeedback() {
        biofeedbackActivityUpdater.setEnableProgressBar(false);
        beltConnectorFake.start();
        event = "baseline done";
    }

    private void stopFakeBiofeedback() {
        biofeedbackActivityUpdater.setPopUp(true);
        biofeedbackActivityUpdater.setEnableButtons(true);
        booleanBioFeedback = true;
        booleanFakeFeedback = true;
        booleanRandom = true;
        event = "fake biofeedback done";
    }

    private void writeLog(double hrv) {
        if (log) {
            if (event == null) {
                fileWriter.writeCsvData(String.valueOf(hrv), "", "");
            } else {
                fileWriter.writeCsvData(String.valueOf(hrv), String.valueOf((System.currentTimeMillis() - time) / 1000), event);
                event = null;
            }
        }
    }

    private void initUI() {
        textProgression = (TextView) findViewById(R.id.text_progression);

        rectEmpty = (ImageView) findViewById(R.id.rect_empty);
        rectFull = (ImageView) findViewById(R.id.rect_full);
        rectWhite = (ImageView) findViewById(R.id.rect_white);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        buttonLogOn = (Button) findViewById(R.id.button_logOn);
        buttonLogOn.setOnClickListener(clickListener);
        if(booleanLogOn)
            enable(buttonLogOn);
        else
            disable(buttonLogOn);

        buttonLogOff = (Button) findViewById(R.id.button_logOff);
        buttonLogOff.setOnClickListener(clickListener);
        if(booleanLogOff)
            enable(buttonLogOff);
        else
            disable(buttonLogOff);

        buttonEvent = (Button) findViewById(R.id.button_event);
        buttonEvent.setOnClickListener(clickListener);
        if(booleanEvent)
            enable(buttonEvent);
        else
            disable(buttonEvent);

        buttonBioFeedback = (Button) findViewById(R.id.button_bioFeedback);
        buttonBioFeedback.setOnClickListener(clickListener);
        if(booleanBioFeedback)
            enable(buttonBioFeedback);
        else
            disable(buttonBioFeedback);

        buttonFakeFeedback = (Button) findViewById(R.id.button_fakeFeedback);
        buttonFakeFeedback.setOnClickListener(clickListener);
        if(booleanFakeFeedback)
            enable(buttonFakeFeedback);
        else
            disable(buttonFakeFeedback);

        buttonRandom = (Button) findViewById(R.id.button_random);
        buttonRandom.setOnClickListener(clickListener);
        if(booleanRandom)
            enable(buttonRandom);
        else
            disable(buttonRandom);
    }

    private void initAU() {
        biofeedbackActivityUpdater.setTextProgression(textProgression);
        biofeedbackActivityUpdater.setRectEmpty(rectEmpty);
        biofeedbackActivityUpdater.setRectFull(rectFull);
        biofeedbackActivityUpdater.setRectWhite(rectWhite);
        biofeedbackActivityUpdater.setProgressBar(progressBar);
        biofeedbackActivityUpdater.setButtonBioFeedback(buttonBioFeedback);
        biofeedbackActivityUpdater.setButtonFakeFeedback(buttonFakeFeedback);
        biofeedbackActivityUpdater.setButtonRandom(buttonRandom);
    }

    private void enable(Button button){
        button.setClickable(true);
        button.setAlpha(1f);
    }

    private void disable(Button button){
        button.setClickable(false);
        button.setAlpha(0.5f);
    }

}