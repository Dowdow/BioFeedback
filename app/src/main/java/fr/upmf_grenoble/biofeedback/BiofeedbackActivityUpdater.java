package fr.upmf_grenoble.biofeedback;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import belt_connector.ZephyrSummaryPacket;
import file_writer.FileWriter;

public class BiofeedbackActivityUpdater implements Runnable {

    private TextView textProgression;

    private ImageView rectEmpty;
    private ImageView rectFull;
    private ImageView rectWhite;

    private Button buttonBioFeedback;
    private Button buttonFakeFeedback;
    private Button buttonRandom;

    private Context context;

    private boolean enableButtons = false;
    private boolean popUp = false;
    private boolean fake = false;

    private float hrv = 0;
    private int min = 0;
    private int max = 100;
    private int difference = 100;
    private int start = 50;
    private ZephyrSummaryPacket zephyrSummaryPacket;

    public BiofeedbackActivityUpdater(Context context, TextView textProgression, ImageView rectEmpty, ImageView rectFull, ImageView rectWhite,
                                      Button buttonBioFeedback, Button buttonFakeFeedback, Button buttonRandom) {
        this.context = context;
        this.textProgression = textProgression;
        this.rectEmpty = rectEmpty;
        this.rectFull = rectFull;
        this.rectWhite = rectWhite;
        this.buttonBioFeedback = buttonBioFeedback;
        this.buttonFakeFeedback = buttonFakeFeedback;
        this.buttonRandom = buttonRandom;
    }

    @Override
    public void run() {
        float empty, full, white;
        if(fake) {
            if(hrv > start) {
                empty = max - hrv;
                empty /= difference;
                empty *= 100;
                full = hrv - start;
                full /= difference;
                full *= 100;
                white = 50;
                setRectPercentage(empty, full, white);
            } else if(hrv < start) {
                empty = 50;
                full = start - hrv;
                full /= difference;
                full *= 100;
                white = hrv - min;
                white /= difference;
                white *= 100;
                setRectPercentage(empty, full, white);
            } else {
                empty = 50;
                full = 0;
                white = 50;
                setRectPercentage(empty, full, white);
            }
            updateProgression();
        } else {
            if(zephyrSummaryPacket != null) {
                if (zephyrSummaryPacket.getHeartRate() > 70) {
                    empty = 90 - zephyrSummaryPacket.getHeartRate();
                    empty /= 40;
                    empty *= 100;
                    full = zephyrSummaryPacket.getHeartRate() - 70;
                    full /= 40;
                    full *= 100;
                    white = 50;
                    setRectPercentage(empty, full, white);
                } else if (zephyrSummaryPacket.getHeartRate() < 70) {
                    empty = 50;
                    full = 70 - zephyrSummaryPacket.getHeartRate();
                    full /= 40;
                    full *= 100;
                    white = zephyrSummaryPacket.getHeartRate() - 50;
                    white /= 40;
                    white *= 100;
                    setRectPercentage(empty, full, white);
                } else {
                    empty = 50;
                    full = 0;
                    white = 50;
                    setRectPercentage(empty, full, white);
                }
            }
        }
        manageButtons();
        if(popUp){
            showPopUp();
        }
    }

    private void setRectPercentage(float empty, float full, float white) {
        rectEmpty.setLayoutParams(new LinearLayout.LayoutParams(250, 0, empty));
        rectFull.setLayoutParams(new LinearLayout.LayoutParams(250, 0, full));
        rectWhite.setLayoutParams(new LinearLayout.LayoutParams(250, 0, white));
        if(fake) {
            if(hrv > start) {
                rectFull.setBackgroundColor(Color.parseColor("#84e7ae"));
            } else {
                rectFull.setBackgroundColor(Color.parseColor("#f4a46f"));
            }
        } else {
            if(zephyrSummaryPacket != null) {
                if (zephyrSummaryPacket.getHeartRate() > 70) {
                    rectFull.setBackgroundColor(Color.parseColor("#84e7ae"));
                } else {
                    rectFull.setBackgroundColor(Color.parseColor("#f4a46f"));
                }
            }
        }
    }

    private void manageButtons() {
        if(enableButtons) {
            enable(buttonBioFeedback);
            enable(buttonFakeFeedback);
            enable(buttonRandom);
            enableButtons = false;
        }
    }

    private void updateProgression() {
        float progression = hrv - start;
        progression /= (difference / 2);
        progression *= 100;
        textProgression.setText("Progression : " + String.format("%.2f", progression) + "%");
        if(hrv > start) {
            textProgression.setTextColor(Color.parseColor("#84e7ae"));
        } else if(hrv < start) {
            textProgression.setTextColor(Color.parseColor("#f4a46f"));
        } else {
            textProgression.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }

    private void showPopUp() {
        popUp = false;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context)
                .setTitle("BioFeedback terminé")
                .setMessage("BioFeedback terminé")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void setZephyrSummaryPacket(ZephyrSummaryPacket zephyrSummaryPacket) {
        this.zephyrSummaryPacket = zephyrSummaryPacket;
    }

    public void setHrv(float hrv) {
        this.hrv = hrv;
    }

    public void setFake(boolean fake) {
        this.fake = fake;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void calculDifference() {
        this.difference = max -min;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setPopUp(boolean popUp) {
        this.popUp = popUp;
    }

    public void setTextProgression(TextView textProgression) {
        this.textProgression = textProgression;
    }

    public void setRectEmpty(ImageView rectEmpty) {
        this.rectEmpty = rectEmpty;
    }

    public void setRectFull(ImageView rectFull) {
        this.rectFull = rectFull;
    }

    public void setRectWhite(ImageView rectWhite) {
        this.rectWhite = rectWhite;
    }

    public void setButtonBioFeedback(Button buttonBioFeedback) {
        this.buttonBioFeedback = buttonBioFeedback;
    }

    public void setButtonFakeFeedback(Button buttonFakeFeedback) {
        this.buttonFakeFeedback = buttonFakeFeedback;
    }

    public void setButtonRandom(Button buttonRandom) {
        this.buttonRandom = buttonRandom;
    }

    public void setEnableButtons(boolean enableButtons) {
        this.enableButtons = enableButtons;
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
