package simulador3.cursoandroid.com.simulador3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class Grafico extends AppCompatActivity {
    LineGraphSeries<DataPoint> series;
    Double x,y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico);

        Bundle extra = getIntent().getExtras();
        Number[] vetorusuarios2= (Number[]) extra.get("vusers");
        Number[] tempo2= (Number[]) extra.get("vtempo");
        int amostras2= extra.getInt("amostras");


        GraphView grafico= (GraphView) findViewById(R.id.graph);
        series= new LineGraphSeries<DataPoint>();
        for (int i=0;i<amostras2;i++)
        {
          Double  x =(double)vetorusuarios2[i];
          Double  y=(double)tempo2[i];
            series.appendData(new DataPoint(x,y), true, amostras2);

        }

        grafico.addSeries(series);

    }
}
