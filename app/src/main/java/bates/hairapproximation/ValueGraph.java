package bates.hairapproximation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class ValueGraph extends View {

    private ArrayList<Float> values = new ArrayList<>();

    private final float X_AXIS_LINE_MARGIN = 10f;
    private final float Y_AXIS_LINE_MARGIN = 10f;
    private final float AXIS_OVERLAP = 10f;
    private final float GRAPH_INSET = 3f;

    private final float MINIMUM_MAXIMUM_VALUE = 0.3f;

    private Paint axisLinePaint;
    private Paint graphPaint;
    private Path graphPath = new Path();

    public ValueGraph(Context context, AttributeSet attrs) {
        super(context, attrs);

        axisLinePaint = new Paint();
        axisLinePaint.setStrokeWidth(dpsToPixels(2));
        axisLinePaint.setColor(Color.BLACK);

        graphPaint = new Paint();
        graphPaint.setStyle(Paint.Style.STROKE);
        graphPaint.setStrokeWidth(dpsToPixels(2));
        graphPaint.setColor(Color.rgb(0x65, 0xbc, 0xd4));

        if (isInEditMode()) {
            for (float i = 0; i < 30; ++i) {
                values.add((float) Math.sin(i / 3) + 1);
            }
        }
    }

    @Override
    public void onDraw(Canvas c) {
        float xAxisYValue = (float) getHeight() - dpsToPixels(X_AXIS_LINE_MARGIN);
        float xAxisXStart = dpsToPixels(Y_AXIS_LINE_MARGIN - AXIS_OVERLAP);
        c.drawLine(xAxisXStart, xAxisYValue, (float) getWidth(), xAxisYValue, axisLinePaint);
        float yAxisXValue = dpsToPixels(Y_AXIS_LINE_MARGIN);
        float yAxisYEnd = (float) getHeight() - dpsToPixels(X_AXIS_LINE_MARGIN - AXIS_OVERLAP);
        c.drawLine(yAxisXValue, 0, yAxisXValue, yAxisYEnd, axisLinePaint);

        if (values.size() < 2) {
            return;
        }

        float usableHeight = xAxisYValue - dpsToPixels(GRAPH_INSET);
        float xPerValue = ((float) getWidth() - yAxisXValue - dpsToPixels(GRAPH_INSET)) /
                (float) values.size();
        float max = maximumValue();

        graphPath.reset();
        float lastXOffset = -1;
        float xOffset = yAxisXValue + dpsToPixels(GRAPH_INSET);
        for (int i = 0; i < values.size(); ++i) {
            if (lastXOffset + 1 <= xOffset) {
                float percentageOfMax = values.get(i) / max;
                float yOffset = usableHeight - (usableHeight * percentageOfMax);
                if (i == 0) {
                    graphPath.moveTo(xOffset, yOffset);
                } else {
                    graphPath.lineTo(xOffset, yOffset);
                }
                lastXOffset = xOffset;
            }
            xOffset += xPerValue;
        }
        c.drawPath(graphPath, graphPaint);
    }

    public void addValue(float value) {
        values.add(value);
        invalidate();
    }

    private float dpsToPixels(float dps) {
        return dps * getResources().getDisplayMetrics().density;
    }

    private float maximumValue() {
        float max = MINIMUM_MAXIMUM_VALUE;
        for (Float f : values) {
            if (f > max) {
                max = f;
            }
        }
        return max;
    }

}
