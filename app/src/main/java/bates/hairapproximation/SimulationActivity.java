package bates.hairapproximation;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class SimulationActivity extends Activity {

    private final long MILLISECONDS_PER_FRAME = 42;

    private float growthRate;
    private int lossRate;
    private int hairCount;

    private float[] hairs;
    private float daysElapsed = 0;

    Random random;

    private TextView daysElapsedTextView;
    private TextView averageLengthTextView;
    private ValueGraph graph;
    private SeekBar speedBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation);

        // TODO: use time-based seed here.
        random = new Random(123456789L);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            throw new RuntimeException("SimulationActivity is useless without extras.");
        }

        hairCount = (int) extras.getFloat("hairCount");
        growthRate = extras.getFloat("growthRate") / 28;
        lossRate = (int) extras.getFloat("hairLoss");

        hairs = new float[hairCount];

        daysElapsedTextView = (TextView) findViewById(R.id.days_elapsed);
        averageLengthTextView = (TextView) findViewById(R.id.average_hair_length);
        graph = (ValueGraph) findViewById(R.id.graph);
        graph.addValue(0);
        speedBar = (SeekBar) findViewById(R.id.speed);

        this.beginRunning();
    }

    private void beginRunning() {
        final Handler handler = new Handler();
        final Runnable runNextDay = new Runnable() {
            @Override
            public void run() {
                int lastDays = (int)daysElapsed;
                daysElapsed += (float)speedBar.getProgress() / 10f;
                int currentDays = (int)daysElapsed;
                for (; lastDays < currentDays; ++lastDays) {
                    runDay();
                }
                averageLengthTextView.setText("Average length: " + averageLength() + " inches");
                daysElapsedTextView.setText("Days elapsed: " + currentDays);
                handler.postDelayed(this, MILLISECONDS_PER_FRAME);
            }
        };
        handler.post(runNextDay);
    }

    private void runDay() {
        ++daysElapsed;
        growHairs();
        removeRandomHairs();
        float average = averageLength();
        graph.addValue(average);
    }

    private void removeRandomHairs() {
        for (int j = 0; j < lossRate; ++j) {
            hairs[random.nextInt(hairs.length)] = 0;
        }
    }

    private void growHairs() {
        for (int i = 0; i < hairs.length; ++i) {
            hairs[i] += growthRate;
        }
    }

    private float averageLength() {
        float sum = 0;
        for (int i = 0; i < hairs.length; ++i) {
            sum += hairs[i];
        }
        return sum / (float)hairs.length;
    }

}
