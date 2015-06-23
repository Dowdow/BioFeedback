package fr.upmf_grenoble.biofeedback;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import belt_connector.ZephyrSummaryPacket;
import file_writer.FileWriter;

public class BiofeedbackActivityUpdater implements Runnable {

    private TextView textProgression;

    private ImageView rectEmpty;
    private ImageView rectFull;
    private ImageView rectWhite;
    private ProgressBar progressBar;

    private Button buttonBioFeedback;
    private Button buttonFakeFeedback;
    private Button buttonRandom;

    private Context context;

    private boolean enableButtons = false;
    private boolean popUp = false;
    private boolean enableProgressBar = false;

    private float hrv = 0;
    private int min = 0;
    private int max = 100;
    private int difference = 100;
    private int start = 50;

    public BiofeedbackActivityUpdater(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        updateRect();
        updateProgression();
        if(enableProgressBar) {
            textProgression.setText("Calcul de la baseline en cours ...");
            textProgression.setTextColor(Color.parseColor("#FFFFFF"));
            hideRect();
            showProgressBar();
        } else {
            showRect();
            hideProgressBar();
        }
        if(enableButtons) {
            manageButtons();
        }
        if(popUp){
            showPopUp();
        }
    }

    private void setRectPercentage(float empty, float full, float white) {
        rectEmpty.setLayoutParams(new LinearLayout.LayoutParams(250, 0, empty));
        rectFull.setLayoutParams(new LinearLayout.LayoutParams(250, 0, full));
        rectWhite.setLayoutParams(new LinearLayout.LayoutParams(250, 0, white));
        if(hrv > start) {
            rectFull.setBackgroundColor(Color.parseColor("#84e7ae"));
        } else {
            rectFull.setBackgroundColor(Color.parseColor("#f4a46f"));
        }
    }

    private void manageButtons() {
        enable(buttonBioFeedback);
        enable(buttonFakeFeedback);
        enable(buttonRandom);
        enableButtons = false;
    }

    private void updateRect() {
        float empty, full, white;
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

    public void setHrv(float hrv) {
        this.hrv = hrv;
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

    public void setEnableProgressBar(boolean enableProgressBar) {
        this.enableProgressBar = enableProgressBar;
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

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
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

    private void showRect() {
        rectEmpty.setVisibility(View.VISIBLE);
        rectFull.setVisibility(View.VISIBLE);
        rectWhite.setVisibility(View.VISIBLE);
    }

    private void showProgression() {
        textProgression.setVisibility(View.VISIBLE);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideRect() {
        rectEmpty.setVisibility(View.GONE);
        rectFull.setVisibility(View.GONE);
        rectWhite.setVisibility(View.GONE);
    }

    private void hideProgression() {
        textProgression.setVisibility(View.GONE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
}
