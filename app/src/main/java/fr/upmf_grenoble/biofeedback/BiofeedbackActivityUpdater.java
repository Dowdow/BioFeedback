package fr.upmf_grenoble.biofeedback;

import android.graphics.Color;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import belt_connector.ZephyrSummaryPacket;

public class BiofeedbackActivityUpdater implements Runnable {

    private TextView textZephyr;
    private TextView textFake;

    private ImageView rectEmpty;
    private ImageView rectFull;
    private ImageView rectWhite;

    private Button buttonBioFeedback;
    private Button buttonFakeFeedback;
    private Button buttonRandom;

    private boolean enableButtons = false;

    private boolean fake = false;

    private float hrv = 0;
    private int min = 0;
    private int max = 100;
    private int difference = 100;
    private int start = 50;
    private ZephyrSummaryPacket zephyrSummaryPacket;

    public BiofeedbackActivityUpdater(TextView textZephyr, TextView textFake, ImageView rectEmpty, ImageView rectFull, ImageView rectWhite,
                                      Button buttonBioFeedback, Button buttonFakeFeedback, Button buttonRandom) {
        this.textZephyr = textZephyr;
        this.textFake = textFake;
        this.rectEmpty = rectEmpty;
        this.rectFull = rectFull;
        this.rectWhite = rectWhite;
        this.buttonBioFeedback = buttonBioFeedback;
        this.buttonFakeFeedback = buttonFakeFeedback;
        this.buttonRandom = buttonRandom;
    }

    @Override
    public void run() {
        if(zephyrSummaryPacket != null) {
            textZephyr.setText("HRV Zephyr : " + zephyrSummaryPacket.getHeartRate());
        }
        textFake.setText("HRV Fake : " + hrv);
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
            textFake.append(" Current");
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
            textZephyr.append(" Current");
        }
        manageButtons();
    }

    private void setRectPercentage(float empty, float full, float white) {
        rectEmpty.setLayoutParams(new LinearLayout.LayoutParams(150, 0, empty));
        rectFull.setLayoutParams(new LinearLayout.LayoutParams(150, 0, full));
        rectWhite.setLayoutParams(new LinearLayout.LayoutParams(150, 0, white));
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

    public void setTextZephyr(TextView textZephyr) {
        this.textZephyr = textZephyr;
    }

    public void setTextFake(TextView textFake) {
        this.textFake = textFake;
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
