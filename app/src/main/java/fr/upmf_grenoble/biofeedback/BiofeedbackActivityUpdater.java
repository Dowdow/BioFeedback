package fr.upmf_grenoble.biofeedback;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
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

    private boolean fake = false;

    private int hrv = 0;
    private ZephyrSummaryPacket zephyrSummaryPacket;

    public BiofeedbackActivityUpdater(TextView textZephyr, TextView textFake, ImageView rectEmpty, ImageView rectFull, ImageView rectWhite) {
        this.textZephyr = textZephyr;
        this.textFake = textFake;
        this.rectEmpty = rectEmpty;
        this.rectFull = rectFull;
        this.rectWhite = rectWhite;
    }

    @Override
    public void run() {
        if(zephyrSummaryPacket != null) {
            textZephyr.setText("HRV Zephyr : " + zephyrSummaryPacket.getHeartRate());
        }
        textFake.setText("HRV Fake : " + hrv);
        float empty, full, white;
        if(fake) {
            if(hrv > 70) {
                empty = 90 - hrv;
                empty /= 40;
                empty *= 100;
                full = hrv - 70;
                full /= 40;
                full *= 100;
                white = 50;
                setRectPercentage(empty, full, white);
            } else if(hrv < 70) {
                empty = 50;
                full = 70 - hrv;
                full /= 40;
                full *= 100;
                white = hrv - 50;
                white /= 40;
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
    }

    private void setRectPercentage(float empty, float full, float white) {
        rectEmpty.setLayoutParams(new LinearLayout.LayoutParams(150, 0, empty));
        rectFull.setLayoutParams(new LinearLayout.LayoutParams(150, 0, full));
        rectWhite.setLayoutParams(new LinearLayout.LayoutParams(150, 0, white));
        if(fake) {
            if(hrv > 70) {
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

    public void setZephyrSummaryPacket(ZephyrSummaryPacket zephyrSummaryPacket) {
        this.zephyrSummaryPacket = zephyrSummaryPacket;
    }

    public void setHrv(int hrv) {
        this.hrv = hrv;
    }

    public void setFake(boolean fake) {
        this.fake = fake;
    }
}
