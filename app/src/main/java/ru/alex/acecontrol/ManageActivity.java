package ru.alex.acecontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Callback;
import retrofit2.Call;
import retrofit2.Response;
import ru.alex.acecontrol.model.DelKey;
import ru.alex.acecontrol.model.KeyList;

public class ManageActivity extends AppCompatActivity {
    //    Объявление переменных
    EditText inputSearch;
    TextView textKey, textPackage, textHost, textDate, textClient;
    Spinner spinner1;
    Button btnSearch1, btnDeact, btnExit1;
    Switch switch1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        //        Инициализация
        inputSearch = (EditText) findViewById(R.id.editFind1);
        btnSearch1 = (Button) findViewById(R.id.buttonSearch1);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        textKey = (TextView) findViewById(R.id.textViewKey);
        textPackage = (TextView) findViewById(R.id.textViewPackage);
        textHost = (TextView) findViewById(R.id.textViewHost);
        textDate = (TextView) findViewById(R.id.textViewDate);
        textClient = (TextView) findViewById(R.id.textViewClient);
        btnDeact = (Button) findViewById(R.id.buttonDeact);
        switch1 = (Switch) findViewById(R.id.switch1);
        btnExit1 = (Button) findViewById(R.id.buttonExit1);

        FindKey("lastfive",""); //Сразу при старте показывает последние 5 активаций

        // Показать список активаций исходя из поисковой строки
        btnSearch1.setOnClickListener(view -> {
            String searchStr = inputSearch.getText().toString().trim();
            if (!searchStr.isEmpty()) {
                FindKey("findkey", searchStr);
            }
        });

        // Назначение слушателя кнопке Exit.
        btnExit1.setOnClickListener(view -> {
            super.finish();
        });

    }

    // Метод вызывающий запрос данных и их обработку
    public void FindKey(String option, String findStr) {
        RetroSrv.getInstance().getAceApi().getFindKey(option, findStr).enqueue(new Callback<List<KeyList>>() {
            @Override
            public void onResponse(Call<List<KeyList>> call, Response<List<KeyList>> response) {
                //Log.d("Manage Activity getLastList() response:", response.body().toString());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_spinner_item,
                        response.body().stream().map(k -> k.toString()).collect(Collectors.toList()));

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner1.setAdapter(adapter);

                //Настраиваем слушатель нажатий Spinner:
                spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        switch1.setChecked(false);
                        //Выбираем данные выпадающего списка:
                        textKey.setText(response.body().get(pos).getKey());
                        textPackage.setText(response.body().get(pos).getName());
                        textHost.setText(response.body().get(pos).getHost());
                        textDate.setText(response.body().get(pos).getDate());

                        btnDeact.setOnClickListener(v -> {
                            if (switch1.isChecked()){
                                //Toast.makeText(getApplicationContext(),"selected key:"+response.body().get(pos).getKey()+" / uuid:"+response.body().get(pos).getUuid(),Toast.LENGTH_SHORT).show();
                                RetroSrv.getInstance().getAceApi().getDeleteKey(response.body().get(pos).getUuid(),response.body().get(pos).getPort()).enqueue(new Callback<DelKey>() {
                                    @Override
                                    public void onResponse(Call<DelKey> call2, Response<DelKey> response2) {
                                        Toast.makeText(getApplicationContext(), "Статус удаления ключа "+response2.body().getKey()+": "+response2.body().getInfo(), Toast.LENGTH_SHORT).show();
                                        //Log.d("Deact log","response2 " + response2.body());
                                        switch1.setChecked(false);
                                    }
                                    @Override
                                    public void onFailure(Call<DelKey> call2, Throwable t) {
                                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                Toast.makeText(getApplicationContext(),"Не выбран контроль операции",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
            }

            @Override
            public void onFailure(Call<List<KeyList>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Ошибка получения данных. Сервер не доступен.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}