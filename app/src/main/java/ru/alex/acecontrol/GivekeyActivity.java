package ru.alex.acecontrol;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.alex.acecontrol.model.ClientList;
import ru.alex.acecontrol.model.OneResult;
import ru.alex.acecontrol.model.PackList;
import ru.alex.acecontrol.model.SellKey;

public class GivekeyActivity extends AppCompatActivity {

    EditText inputPackage, inputClient, inputFIO, inputPhone1, inputPhone2, inputMail, inputCompany, inputCity, inputAdress;
    Button btnSearchPackage, btnSearchClient, btnGivekey, btnExit2;
    Spinner spinnerPackage, spinnerClient;
    TextView textFreeKey;
    Switch switch2;
    ClipboardManager myClipboard;
    ClipData myClip;
    SellKey sellKey;
    Date currentDate;
    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_givekey);

        //        Инициализация
        inputPackage = (EditText) findViewById(R.id.editFindPackage);
        btnSearchPackage = (Button) findViewById(R.id.buttonSearchPackage);
        spinnerPackage = (Spinner) findViewById(R.id.spinner2);
        inputClient = (EditText) findViewById(R.id.editFindClient);
        btnSearchClient = (Button) findViewById(R.id.buttonSearchClient);
        spinnerClient = (Spinner) findViewById(R.id.spinner3);
        inputFIO = (EditText) findViewById(R.id.editFIO);
        inputPhone1 = (EditText) findViewById(R.id.editPhone1);
        inputPhone2 = (EditText) findViewById(R.id.editPhone2);
        inputMail = (EditText) findViewById(R.id.editMail);
        inputCompany = (EditText) findViewById(R.id.editCompany);
        inputCity = (EditText) findViewById(R.id.editCity);
        inputAdress = (EditText) findViewById(R.id.editAdress);
        btnGivekey = (Button) findViewById(R.id.buttonGivekey);
        switch2 = (Switch) findViewById(R.id.switch2);
        textFreeKey = (TextView) findViewById(R.id.textViewFreeKey);
        btnExit2 = (Button) findViewById(R.id.buttonExit2);
        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

        // Показать список пакетов исходя из поисковой строки, если пусто то вывести все
        btnSearchPackage.setOnClickListener(view -> {
            String searchStr = inputPackage.getText().toString().trim();
            ListPackage(searchStr);
        });

        // Показать список покупателей исходя из поисковой строки, если пусто то вывести всех
        btnSearchClient.setOnClickListener(view -> {
            String searchStr = inputClient.getText().toString().trim();
            ListClient(searchStr);
        });

        // Назначение слушателя кнопке Exit. Инициализация нажатия на кнопку
        btnExit2.setOnClickListener(view -> {
            super.finish();
        });
    }
    // Метод вызывающий запрос списка пакетов и работу с ними
    public void ListPackage(String findStr) {
        RetroSrv.getInstance().getAceApi().getListPackage(findStr).enqueue(new Callback<List<PackList>>() {
            @Override
            public void onResponse(Call<List<PackList>> call, Response<List<PackList>> response) {
                //Log.d("Manage Activity getLastList() response:", response.body().toString());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_spinner_item,
                        response.body().stream().map(k -> k.getName()).collect(Collectors.toList()));

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerPackage.setAdapter(adapter);

                //слушатель нажатий SpinnerPackage:
                spinnerPackage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        switch2.setChecked(false);
                        String aceuid = response.body().get(pos).getAceuid();
                        String port = response.body().get(pos).getPort();

                        // Получить число непроданных ключей для данного пакета
                        RetroSrv.getInstance().getAceApi().getFreeCount(aceuid,port)
                                .enqueue(new Callback<OneResult>() {
                                    @Override
                                    public void onResponse(Call<OneResult> call2, Response<OneResult> response2) {
                                        textFreeKey.setText("Доступно ключей: "+response2.body().getResult());
                                    }

                                    @Override
                                    public void onFailure(Call<OneResult> call2, Throwable t) {
                                        Toast.makeText(getApplicationContext(),"Ошибка в получении числа ключей",Toast.LENGTH_SHORT).show();
                                    }
                                });

                        // Выдача свободного ключа по кнопке
                        btnGivekey.setOnClickListener(v -> {
                            if (inputCompany.getText().length()==0){
                                Toast.makeText(getApplicationContext(), "Наименование компании обязательный параметр", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (switch2.isChecked()){
                                sellKey = new SellKey();
                                currentDate = new Date();

                                // Получить первый свободный непроданный ключ для данного пакета
                                RetroSrv.getInstance().getAceApi().getFreeKey(aceuid,port)
                                        .enqueue(new Callback<OneResult>() {
                                            @RequiresApi(api = Build.VERSION_CODES.O)
                                            @Override
                                            public void onResponse(Call<OneResult> call3, Response<OneResult> response3) {
                                                sellKey.setKey(response3.body().getResult());
                                                sellKey.setInstance(spinnerPackage.getSelectedItem().toString());
                                                sellKey.setDate(dateFormat.format(currentDate));
                                                sellKey.setCompany(inputCompany.getText().toString());
                                                sellKey.setName(inputFIO.getText().toString());
                                                sellKey.setPhone1(inputPhone1.getText().toString());
                                                sellKey.setPhone2(inputPhone2.getText().toString());
                                                sellKey.setEmail(inputMail.getText().toString());
                                                sellKey.setCity(inputCity.getText().toString());
                                                sellKey.setAdress(inputAdress.getText().toString());
                                                // Вносим ключ и данные покупателя в базу данных
                                                RetroSrv.getInstance().getAceApi().getSellKey(new String(Base64.encode(gson.toJson(sellKey).getBytes(StandardCharsets.UTF_8),Base64.NO_WRAP))).enqueue(new Callback<OneResult>() {
                                                    @Override
                                                    public void onResponse(Call<OneResult> call4, Response<OneResult> response4) {
                                                        //Log.d("View info sellKey responce:", response4.body().getResult());
                                                        if (response4.body().getResult()=="true"){
                                                            // Проверям действительно ли данные внесены в базу
                                                            RetroSrv.getInstance().getAceApi().getCheckKey(sellKey.getKey()).enqueue(new Callback<OneResult>() {
                                                                @Override
                                                                public void onResponse(Call<OneResult> call5, Response<OneResult> response5) {
                                                                    //Log.d("View info CheckKey responce:", response5.body().getResult());
                                                                    if (response5.body().getResult()=="true"){
                                                                        textFreeKey.setText(sellKey.getKey());
                                                                        myClip = ClipData.newPlainText("text", sellKey.getKey());
                                                                        myClipboard.setPrimaryClip(myClip);
                                                                        Toast.makeText(getApplicationContext(), "Ключ "+sellKey.getKey()+" помещен в буфер", Toast.LENGTH_SHORT).show();
                                                                        ClearForm();
                                                                    }else{
                                                                        Toast.makeText(getApplicationContext(),"Ошибка проверки внесения данных покупателя в бд",Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }

                                                                @Override
                                                                public void onFailure(Call<OneResult> call5, Throwable t) {
                                                                    Toast.makeText(getApplicationContext(),"Ошибка проверки внесения данных покупателя в бд",Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                        }else{
                                                            Toast.makeText(getApplicationContext(),"Ошибка внесения данных покупателя в бд",Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<OneResult> call4, Throwable t) {
                                                        Toast.makeText(getApplicationContext(),"Ошибка внесения данных покупателя в бд",Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onFailure(Call<OneResult> call3, Throwable t) {
                                                Toast.makeText(getApplicationContext(),"Ошибка в получении свободного ключа",Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<List<PackList>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Ошибка получения данных. Сервер не доступен.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Метод вызывающий запрос списка клиентов
    public void ListClient(String findStr) {
        RetroSrv.getInstance().getAceApi().getListClient(findStr).enqueue(new Callback<List<ClientList>>() {
            @Override
            public void onResponse(Call<List<ClientList>> call, Response<List<ClientList>> response) {
                //Log.d("Manage Activity getListClient() response:", response.body().toString());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_spinner_item,
                        response.body().stream().map(k -> k.getCompany()).collect(Collectors.toList()));

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerClient.setAdapter(adapter);

                //слушатель нажатий SpinnerClient:
                spinnerClient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        switch2.setChecked(false);
                        // заполняем данные клиента найденные в БД
                        inputFIO.setText(response.body().get(pos).getName());
                        inputCompany.setText(response.body().get(pos).getCompany());
                        inputPhone1.setText(response.body().get(pos).getPhone1());
                        inputPhone2.setText(response.body().get(pos).getPhone2());
                        inputMail.setText(response.body().get(pos).getEmail());
                        inputCity.setText(response.body().get(pos).getCity());
                        inputAdress.setText(response.body().get(pos).getAdress());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
            }

            @Override
            public void onFailure(Call<List<ClientList>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Ошибка получения данных. Сервер не доступен.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Метод очистки формы
    public void ClearForm(){
        switch2.setChecked(false);
        inputFIO.setText("");
        inputCompany.setText("");
        inputPhone1.setText("");
        inputPhone2.setText("");
        inputMail.setText("");
        inputCity.setText("");
        inputAdress.setText("");
        spinnerPackage.setAdapter(null);
        spinnerClient.setAdapter(null);
    }
}