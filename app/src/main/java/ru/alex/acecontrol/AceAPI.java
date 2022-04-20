package ru.alex.acecontrol;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.alex.acecontrol.model.ClientList;
import ru.alex.acecontrol.model.DelKey;
import ru.alex.acecontrol.model.KeyList;
import ru.alex.acecontrol.model.OneResult;
import ru.alex.acecontrol.model.PackList;

public interface AceAPI {
    @GET("/vm/api.php?")
    Call<List<KeyList>> getFindKey(@Query("opt") String option, @Query("str") String findStr);

    @GET("/vm/api.php?opt=delkey")
    Call<DelKey> getDeleteKey(@Query("str") String uuid, @Query("port") String port);

    @GET("/vm/api.php?opt=packages")
    Call<List<PackList>> getListPackage(@Query("str") String str);

    @GET("/vm/api.php?opt=freecount")
    Call<OneResult> getFreeCount(@Query("str") String aceuid, @Query("port") String port);

    @GET("/vm/api.php?opt=freekey")
    Call<OneResult> getFreeKey(@Query("str") String aceuid, @Query("port") String port);

    @GET("/vm/api.php?opt=listclients")
    Call<List<ClientList>> getListClient(@Query("str") String str);

    @GET("/vm/api.php?opt=sellkey")
    Call<OneResult> getSellKey(@Query("str") String str);

    @GET("/vm/api.php?opt=checkkey")
    Call<OneResult> getCheckKey(@Query("str") String str);
}
