package bates.hairapproximation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void runSimulation(View button) {
        Intent i = new Intent(this, SimulationActivity.class);
        i.putExtra("hairCount", parseInputForField(R.id.hair_count));
        i.putExtra("hairLoss", parseInputForField(R.id.hair_loss));
        i.putExtra("growthRate", parseInputForField(R.id.growth_rate));
        startActivity(i);
    }

    private float parseInputForField(int id) {
        try {
            String str = ((EditText) findViewById(id)).getText().toString();
            return Float.parseFloat(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

}
