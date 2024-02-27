package ru.nsu.fit.wheretogo.utils.stub;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.nsu.fit.wheretogo.model.ors.direction.LatLong;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RouteData {
    @Getter
    private static final List<LatLong> data = new ArrayList<>(
            List.of(
                    LatLong
                            .builder()
                            .latitude("55.00626")
                            .longitude("82.93973")
                            .build(),
                    LatLong
                            .builder()
                            .latitude("55.01636")
                            .longitude("82.94845")
                            .build(),
                    LatLong
                            .builder()
                            .latitude("55.01383")
                            .longitude("82.97549")
                            .build(),
                    LatLong
                            .builder()
                            .latitude("55.02222")
                            .longitude("82.97971")
                            .build(),
                    LatLong
                            .builder()
                            .latitude("55.04506")
                            .longitude("82.95837")
                            .build(),
                    LatLong
                            .builder()
                            .latitude("55.04757")
                            .longitude("82.95274")
                            .build(),
                    LatLong
                            .builder()
                            .latitude("55.03242")
                            .longitude("82.91890")
                            .build(),
                    LatLong
                            .builder()
                            .latitude("55.02748")
                            .longitude("82.90380")
                            .build(),
                    LatLong
                            .builder()
                            .latitude("55.01336")
                            .longitude("82.92860")
                            .build(),
                    LatLong
                            .builder()
                            .latitude("54.98786")
                            .longitude("82.90916")
                            .build()
            )
    );

}
