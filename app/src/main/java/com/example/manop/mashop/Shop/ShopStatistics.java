package com.example.manop.mashop.Shop;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.manop.mashop.Function.XYValue;
import com.example.manop.mashop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class ShopStatistics extends Activity {

    private static final Random RANDOM = new Random();
    private BarGraphSeries<DataPoint> series;
    private int lastX = 0;
    private ArrayList<Integer> yList;
    private ArrayList<Integer> xList;
    private Button moreHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_statistics);

        moreHistory = (Button) findViewById(R.id.moreHistory);


        // we get graph view instance
        final GraphView graph = (GraphView) findViewById(R.id.barChart);
        yList = new ArrayList<Integer>();
        xList = new ArrayList<Integer>();
        // data
        series = new BarGraphSeries<DataPoint>();
        graph.addSeries(series);
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setVerticalAxisTitle("Quantity");
        gridLabel.setHorizontalAxisTitle("Order number");
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    // show normal x values
                    if(value == (int)value) {
                        return super.formatLabel(value, isValueX);
                    }
                    else{
                        return "";
                    }
                } else {
                    // show currency for y values
                    return super.formatLabel(value, isValueX) + "";
                }
            }
        });

        // customize a little bit viewport
        graph.getGridLabelRenderer().setHumanRounding(true);
        final  Viewport viewport = graph.getViewport();
//        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);
//        viewport.setMaxY(10);
        viewport.setMinX(0);
//        viewport.setMaxX(5);
        viewport.setYAxisBoundsManual(true);
        viewport.setXAxisBoundsManual(true);
//        graph.getViewport().setScalable(true);
//        graph.getViewport().setScalableY(true);
        graph.getViewport().setScrollable(true); // enables horizontal scrolling
        graph.getViewport().setScrollableY(true); // enables vertical scrolling
//        graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
//        graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling
        FirebaseDatabase.getInstance().getReference().child("Shop").child(FirebaseAuth.getInstance().getCurrentUser()
                .getUid()).child("sell_history").addValueEventListener(new ValueEventListener() {
            int c  = 1;
            int mdp = 10;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    if (c <= dataSnapshot.getChildrenCount()) {
                        int x = c;
                        int y = (int) Integer.parseInt(ds.child("quantity").getValue().toString());
                        yList.add(y);
                        xList.add(x);
                        c++;
                        Log.d("Statis", Double.toString(x) + " : " + Double.toString(y));
                        if(c>= mdp) mdp *= 2;
                        addEntry(x, y,mdp);
                        //        viewport.setYAxisBoundsManual(true);
                        viewport.setMaxY(Collections.max(yList) + 3);
                        viewport.setMaxX(Collections.max(xList) + 1);
                        //graph.getGridLabelRenderer().setNumHorizontalLabels(c);
                        viewport.setYAxisBoundsManual(false);
                        viewport.setXAxisBoundsManual(false);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/8, (int) Math.abs(data.getY()*255/8), 255);
            }
        });

        series.setSpacing(60);

// draw values on top
        series.setDrawValuesOnTop(true);

        series.setValuesOnTopColor(Color.RED);

        moreHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //Write file
                    Bitmap bitmap = graph.takeSnapshot();
                    String filename = "bitmap.png";
                    FileOutputStream stream = ShopStatistics.this.openFileOutput(filename, Context.MODE_PRIVATE);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                    //Cleanup
                    stream.close();
                    bitmap.recycle();

                    //Pop intent
                    Intent more = new Intent(ShopStatistics.this,SellingHistory.class);
                    more.putExtra("image", filename);
                    startActivity(more);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    // add random data to graph
    private void addEntry(int x, int y, int mdp) {
        // here, we choose to display max 10 points on the viewport and we scroll to end
        series.appendData(new DataPoint(x, y),false,mdp);
    }

}