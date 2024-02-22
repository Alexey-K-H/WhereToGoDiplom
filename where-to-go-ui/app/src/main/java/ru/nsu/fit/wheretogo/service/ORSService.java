package ru.nsu.fit.wheretogo.service;

import retrofit2.Call;
import retrofit2.http.POST;
import ru.nsu.fit.wheretogo.model.entity.ors.OrsDirectionResponse;

/**
 * Сервис по работе с ORS
 */
public interface ORSService {

    /**
     * Получить путь, используя способ передвижения: "машина"
     * @return данные пути: (геометрия, свойства)
     */
    @POST("/ors/route/driving")
    Call<OrsDirectionResponse> getRouteByDriving();

}
