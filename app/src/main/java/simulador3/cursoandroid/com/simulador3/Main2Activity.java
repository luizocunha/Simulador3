package simulador3.cursoandroid.com.simulador3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Main2Activity extends AppCompatActivity {
    private EditText times;
    private Button botao;
    private Button grafico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        times= (EditText) findViewById(R.id.tempoid);
        botao=(Button) findViewById(R.id.voltarid);
        grafico=(Button) findViewById(R.id.botgrafid);

        Bundle extra = getIntent().getExtras();
        final Number[] vetorusuarios= (Number[]) extra.get("nusers");
        final Number[] tempo= (Number[]) extra.get("temp");
        final int amostras= extra.getInt("amostras");


        int x;
        Number a;
        Number b;
        String ch;
        String ch2;
        StringBuilder sb = new StringBuilder();
        for(x=0;x<=amostras;x++)
        {
            sb.append(String.format("%6.2f\t->\t%8.2f\n", tempo[x], vetorusuarios[x]));

        }
            times.setText(sb.toString());

        botao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Main2Activity.this,MainActivity.class));
            }
        });
        grafico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Main2Activity.this, Grafico.class);
                intent.putExtra("vusers", vetorusuarios);
                intent.putExtra("vtempo", tempo);
                intent.putExtra("amostras", amostras);
                startActivity(intent);

            }
        });


    }
}
