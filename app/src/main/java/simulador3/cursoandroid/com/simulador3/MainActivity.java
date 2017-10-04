package simulador3.cursoandroid.com.simulador3;

import android.app.Activity;
import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.ufjf.mmc.jynacore.JynaSimulableModel;
import br.ufjf.mmc.jynacore.JynaSimulation;
import br.ufjf.mmc.jynacore.JynaSimulationData;
import br.ufjf.mmc.jynacore.JynaSimulationProfile;
import br.ufjf.mmc.jynacore.expression.Expression;
import br.ufjf.mmc.jynacore.expression.NumberOperator;
import br.ufjf.mmc.jynacore.expression.impl.DefaultExpression;
import br.ufjf.mmc.jynacore.expression.impl.DefaultNumberConstantExpression;
import br.ufjf.mmc.jynacore.expression.impl.DefaultReferenceExpression;
import br.ufjf.mmc.jynacore.impl.DefaultSimulationData;
import br.ufjf.mmc.jynacore.impl.DefaultSimulationData2;
import br.ufjf.mmc.jynacore.impl.DefaultSimulationProfile;
import br.ufjf.mmc.jynacore.systemdynamics.FiniteStock;
import br.ufjf.mmc.jynacore.systemdynamics.InfiniteStock;
import br.ufjf.mmc.jynacore.systemdynamics.Information;
import br.ufjf.mmc.jynacore.systemdynamics.Rate;
import br.ufjf.mmc.jynacore.systemdynamics.SystemDynamicsModel;
import br.ufjf.mmc.jynacore.systemdynamics.Variable;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultFiniteStock;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultInfiniteStock;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultInformation;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultRate;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultSystemDynamicsModel;
import br.ufjf.mmc.jynacore.systemdynamics.impl.DefaultVariable;
import br.ufjf.mmc.jynacore.systemdynamics.simulator.impl.DefaultSystemDynamicsEulerMethod;
import br.ufjf.mmc.jynacore.systemdynamics.simulator.impl.DefaultSystemDynamicsSimulation;


public class MainActivity extends Activity {
    private EditText tempo;
    private EditText namostras;
    private EditText valalem0;
    private EditText taxa;
    private JynaSimulationProfile profile;
    private JynaSimulationData data;
    private JynaSimulation simulation;
    private DefaultSystemDynamicsEulerMethod method;

    private Button start;
    private Button rodaSimulacao;

    int s=0;

    double valtime=0;
    int amostras=0;
    double valem0;
    double taxas=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = findViewById(R.id.botaoStart);
        rodaSimulacao = findViewById(R.id.rodaSimulacao);

        tempo = findViewById(R.id.mesesid);
        namostras = findViewById(R.id.namostrasid);
        valalem0 = findViewById(R.id.valorem0id);
        taxa = findViewById(R.id.taxaid);


        start.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View view) {
                                         String tempodig = tempo.getText().toString();
                                         if (tempodig.equals("") ) {
                                             Toast.makeText(getApplicationContext(),"Coloque Tempo em Meses",Toast.LENGTH_SHORT).show();
                                             s=1;
                                         }else
                                         {
                                             valtime = Double.parseDouble(tempodig);
                                         }

                                         String n = namostras.getText().toString();
                                         if (n.equals("")) {
                                             Toast.makeText(getApplicationContext(),"Coloque Numero de Amostras",Toast.LENGTH_SHORT).show();
                                             s=1;
                                         }else
                                         {
                                             amostras = Integer.parseInt(n);
                                         }

                                         String val = valalem0.getText().toString();
                                         if (val.equals("")) {
                                             Toast.makeText(getApplicationContext(),"Coloque Usuarios Iniciais",Toast.LENGTH_SHORT).show();
                                             s=1;
                                         }else
                                         {
                                             valem0 = Double.parseDouble(val);
                                         }

                                         String tax = taxa.getText().toString();
                                         if (tax.equals("")) {
                                             Toast.makeText(getApplicationContext(),"Coloque Fator de adesão",Toast.LENGTH_SHORT).show();
                                             s=1;
                                         }else
                                         {
                                             taxas = Double.parseDouble(tax);
                                         }



                    if (s==0) {
                        try {
                            profile = new DefaultSimulationProfile();
                            data = new DefaultSimulationData2();
                            simulation = new DefaultSystemDynamicsSimulation();
                            method = new DefaultSystemDynamicsEulerMethod();


                            SystemDynamicsModel modelSD = new DefaultSystemDynamicsModel();
                            modelSD.setName("Crescimento de Usuários");

                            FiniteStock users = new DefaultFiniteStock();
                            users.setName("Usuários");
                            users.setInitialValue(valem0);
                            modelSD.put(users);

                            InfiniteStock userPool = new DefaultInfiniteStock();
                            userPool.setName("Fonte de Usuários");
                            modelSD.put(userPool);

                            Variable incFactor = new DefaultVariable();
                            incFactor.setName("Taxa de Adesão");
                            incFactor.setExpression(new DefaultNumberConstantExpression(taxas));
                            modelSD.put(incFactor);

                            Rate userInc = new DefaultRate();
                            userInc.setName("Adesão");
                            userInc.setSource(userPool);
                            userInc.setTarget(users);
                            userInc.setSourceAndTarget(userPool, users);

                            Expression exp = new DefaultExpression();
                            exp.setOperator(NumberOperator.TIMES);
                            exp.setLeftOperand(new DefaultReferenceExpression(incFactor));
                            exp.setRightOperand(new DefaultReferenceExpression(users));
                            userInc.setExpression(exp);
                            modelSD.put(userInc);

                            Information info1 = new DefaultInformation(users, userInc);
                            modelSD.put(info1);
                            Information info2 = new DefaultInformation(incFactor, userInc);
                            modelSD.put(info2);


                            profile.setTimeLimits(amostras, valtime);

                            simulation.setProfile(profile);
                            simulation.setMethod(method);
                            data.addAll(((JynaSimulableModel) modelSD).getAllJynaValued());
                            simulation.setSimulationData(data);
                            simulation.setModel((JynaSimulableModel) modelSD);
                        } catch (Exception e) {
                            Log.e("Ruim:", e.getCause().toString());
                            System.err.println(e.getCause());
                            e.printStackTrace();
                        }
                    }

                                     }
                                 }
        );


            rodaSimulacao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        simulation.reset();
                        data.register(0.0);
                        simulation.run();

                        String n = namostras.getText().toString();
                        int amostras = Integer.parseInt(n);

                        int x;
                        Number[] nusuarios = new Number[amostras + 1];
                        Number[] time = new Number[amostras + 1];

                        for (x = 0; x <= amostras; x++) {
                            nusuarios[x] = data.getValue(0, x);

                        }


                        for (x = 0; x <= amostras; x++) {
                            time[x] = data.getTime(x);

                        }


                        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                        intent.putExtra("nusers", nusuarios);
                        intent.putExtra("temp", time);
                        intent.putExtra("amostras", amostras);
                        startActivity(intent);


                    } catch (Exception ex) {
                        Log.e("Ruim:", "Erro no run", ex);
                    }

                }
            });
    }


}
