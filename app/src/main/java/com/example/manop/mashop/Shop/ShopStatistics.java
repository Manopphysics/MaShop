package com.example.manop.mashop.Shop;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
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
        moreHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent more = new Intent(ShopStatistics.this,SellingHistory.class);
                startActivity(more);
            }
        });

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
    }


    // add random data to graph
    private void addEntry(int x, int y, int mdp) {
        // here, we choose to display max 10 points on the viewport and we scroll to end
        series.appendData(new DataPoint(x, y),false,mdp);
    }

}
//import java.util.ArrayList;
//
//public class ShopStatistics extends AppCompatActivity {
//
//    private static final String TAG = "MainActivity";
//
//    //add PointsGraphSeries of DataPoint type
//    LineGraphSeries<DataPoint> xySeries;
//
////    private Button btnAddPt;
////
////    private EditText mX,mY;
//
//    GraphView mScatterPlot;
//
//    //make xyValueArray global
//    private ArrayList<XYValue> xyValueArray;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_shop_statistics);
//
//        //declare variables in oncreate
////        btnAddPt = (Button) findViewById(R.id.btnAddPt);
////        mX = (EditText) findViewById(R.id.numX);
////        mY = (EditText) findViewById(R.id.numY);
//        mScatterPlot = (GraphView) findViewById(R.id.scatterPlot);
//        xyValueArray = new ArrayList<>();
//
//        init();
//    }
//
//    private void init(){
//        //declare the xySeries Object
//        xySeries = new LineGraphSeries<>();
//
////        btnAddPt.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
//                //!mX.getText().toString().equals("") && !mY.getText().toString().equals("") ){
//                    //final double x = Double.parseDouble(mX.getText().toString());
//                    //final double y = Double.parseDouble(mY.getText().toString());
//                    //Log.d(TAG, "onClick: Adding a new point. (x,y): (" + x + "," + y + ")" );
//                    FirebaseDatabase.getInstance().getReference().child("Shop").child(FirebaseAuth.getInstance().getCurrentUser()
//                    .getUid()).child("sell_history").addValueEventListener(new ValueEventListener() {
//                        int c = 1;
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//
//                            for(DataSnapshot ds: dataSnapshot.getChildren()) {
//                                if(c <= dataSnapshot.getChildrenCount()){
//                                double x = c;
//                                double y = (double) Double.parseDouble(ds.child("quantity").getValue().toString());
//                                c++;
//                                xyValueArray.add(new XYValue(x, y));
//                                 //init();
//                            }
//                                else {
//                                    toastMessage("OUT OF RECURSIVE YAY! ");
//                                }
//                            }
//
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//
//
////            }
////        });
//
//        //little bit of exception handling for if there is no data.
//        if(xyValueArray.size() != 0){
//            createScatterPlot();
//        }else{
//            Log.d(TAG, "onCreate: No data to plot.");
//        }
//    }
//
//    private void createScatterPlot() {
//        Log.d(TAG, "createScatterPlot: Creating scatter plot.");
//
//        //sort the array of xy values
//        xyValueArray = sortArray(xyValueArray);
//
//        //add the data to the series
//        for(int i = 0;i <xyValueArray.size(); i++){
//            try{
//                double x = xyValueArray.get(i).getX();
//                double y = xyValueArray.get(i).getY();
//                xySeries.appendData(new DataPoint(x,y),true, 1000);
//            }catch (IllegalArgumentException e){
//                Log.e(TAG, "createScatterPlot: IllegalArgumentException: " + e.getMessage() );
//            }
//        }
//
//        //set some properties
////        xySeries.setShape(PointsGraphSeries.Shape.RECTANGLE);
//        xySeries.setColor(Color.BLUE);
////        xySeries.setSize(20f);
//
//        //set Scrollable and Scaleable
////        mScatterPlot.getViewport().setScalable(true);
////        mScatterPlot.getViewport().setScalableY(true);
////        mScatterPlot.getViewport().setScrollable(true);
////        mScatterPlot.getViewport().setScrollableY(true);
//        xySeries.setTitle("Random Curve 1");
//        xySeries.setColor(Color.GREEN);
//        xySeries.setDrawDataPoints(true);
//        xySeries.setDataPointsRadius(10);
//        xySeries.setThickness(8);
//        Paint paint = new Paint();
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeWidth(10);
//        paint.setPathEffect(new DashPathEffect(new float[]{8, 5}, 0));
//        xySeries.setCustomPaint(paint);
//        //set manual x bounds
//        mScatterPlot.getViewport().setYAxisBoundsManual(true);
//        mScatterPlot.getViewport().setMaxY(10);
//        mScatterPlot.getViewport().setMinY(0);
//
//        //set manual y bounds
//        mScatterPlot.getViewport().setXAxisBoundsManual(true);
//        mScatterPlot.getViewport().setMaxX(5);
//        mScatterPlot.getViewport().setMinX(0);
//
//        mScatterPlot.addSeries(xySeries);
//    }
//
//    /**
//     * Sorts an ArrayList<XYValue> with respect to the x values.
//     * @param array
//     * @return
//     */
//    private ArrayList<XYValue> sortArray(ArrayList<XYValue> array){
//        /*
//        //Sorts the xyValues in Ascending order to prepare them for the PointsGraphSeries<DataSet>
//         */
//        int factor = Integer.parseInt(String.valueOf(Math.round(Math.pow(array.size(),2))));
//        int m = array.size() - 1;
//        int count = 0;
//        Log.d(TAG, "sortArray: Sorting the XYArray.");
//
//
//        while (true) {
//            m--;
//            if (m <= 0) {
//                m = array.size() - 1;
//            }
//            Log.d(TAG, "sortArray: m = " + m);
//            try {
//                //print out the y entrys so we know what the order looks like
//                //Log.d(TAG, "sortArray: Order:");
//                //for(int n = 0;n < array.size();n++){
//                //Log.d(TAG, "sortArray: " + array.get(n).getY());
//                //}
//                double tempY = array.get(m - 1).getY();
//                double tempX = array.get(m - 1).getX();
//                if (tempX > array.get(m).getX()) {
//                    array.get(m - 1).setY(array.get(m).getY());
//                    array.get(m).setY(tempY);
//                    array.get(m - 1).setX(array.get(m).getX());
//                    array.get(m).setX(tempX);
//                } else if (tempX == array.get(m).getX()) {
//                    count++;
//                    Log.d(TAG, "sortArray: count = " + count);
//                } else if (array.get(m).getX() > array.get(m - 1).getX()) {
//                    count++;
//                    Log.d(TAG, "sortArray: count = " + count);
//                }
//                //break when factorial is done
//                if (count == factor) {
//                    break;
//                }
//            } catch (ArrayIndexOutOfBoundsException e) {
//                Log.e(TAG, "sortArray: ArrayIndexOutOfBoundsException. Need more than 1 data point to create Plot." +
//                        e.getMessage());
//                break;
//            }
//        }
//        return array;
//    }
//
//    /**
//     * customizable toast
//     * @param message
//     */
//    private void toastMessage(String message){
//        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
//    }
//}