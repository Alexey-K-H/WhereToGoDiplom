package ru.nsu.fit.wheretogo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.nsu.fit.wheretogo.dto.PagedListDTO;
import ru.nsu.fit.wheretogo.dto.PlaceBriefDTO;
import ru.nsu.fit.wheretogo.dto.PlaceDescriptionDTO;
import ru.nsu.fit.wheretogo.service.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/place")
@RequiredArgsConstructor
public class PlaceController {
    private final PlaceService service;
    private final PlacePicturesService picturesService;
    private final UserService userService;//нужен для работы с посещенными и понравившимися

    private final StayPointService stayPointService;
    private final ScoreService scoreService;

    @PostMapping
    public ResponseEntity<String> savePlace(@RequestBody @Valid PlaceDescriptionDTO place) {
        service.savePlace(place);
        return ResponseEntity.ok("OK");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePlace(@PathVariable(name = "id") Long id) {
        service.deletePlace(new PlaceDescriptionDTO().setId(id));
        return new ResponseEntity<>("Deleted", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaceDescriptionDTO> getPlaceById(@PathVariable(name = "id") Long id) {
        PlaceDescriptionDTO dto = service.getPlaceById(id);
        if (dto != null) {
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/picture")
    public ResponseEntity<String> addPicture(
            @RequestParam(name = "file") MultipartFile file,
            @RequestParam(name = "placeId") Long placeId
            ) {
        try {
            picturesService.savePicture(file, placeId);
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/picture")
    public ResponseEntity<Resource> getPicture(
            @RequestParam(name = "url") String url
    ) {
        try {
            return ResponseEntity.ok(picturesService.loadPicture(url));
        } catch (Exception exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    //TODO:Запросы для работы с посещенными, избранными
    @PostMapping("/favourite/{id}")
    public ResponseEntity<String> addFavourite(
            @PathVariable(name = "id") Long placeId
    ) {
        try {
            userService.addFavourite(placeId);
            return ResponseEntity.ok("Added");
        } catch (Exception exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/visited/{id}")
    public ResponseEntity<String> addVisited(
            @PathVariable(name = "id") Long placeId
    ) {
        try {
            userService.addVisited(placeId);
            return ResponseEntity.ok("Added");
        } catch (Exception exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/favourite")
    public ResponseEntity<List<PlaceBriefDTO>> getFavourite() {
        try {
            return ResponseEntity.ok(userService.getFavourite());
        } catch (Exception exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/visited")
    public ResponseEntity<List<PlaceBriefDTO>> getVisited() {
        try {
            return ResponseEntity.ok(userService.getVisited());
        } catch (Exception exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/visited/{id}")
    public ResponseEntity<Boolean> existVisitedById(@PathVariable(name = "id") Long id){
        try {
            return ResponseEntity.ok(userService.findVisitedById(id));
        }catch (Exception exception){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/favourite/{id}")
    public ResponseEntity<Boolean> existFavouriteById(@PathVariable(name = "id") Long id){
        try{
            return ResponseEntity.ok(userService.findFavouriteById(id));
        }catch (Exception exception){
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/visited/{id}")
    public ResponseEntity<String> deleteVisitedById(@PathVariable(name = "id") Long id){
        try {
            userService.deleteVisited(id);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        }
        catch (Exception exception){
            exception.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/favourite/{id}")
    public ResponseEntity<String> deleteFavouriteById(@PathVariable(name = "id") Long id) {
        try{
            userService.deleteFavourite(id);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        }
        catch (Exception exception){
            return ResponseEntity.badRequest().build();
        }
    }

//    @GetMapping("/list")
//    public ResponseEntity<PagedListDTO<PlaceBriefDTO>> searchPlaces(
//            @RequestParam(name = "name", defaultValue = "") String name,
//            @RequestParam(name = "category", required = false) List<Integer> categoryId,
//            @RequestParam(name = "page", defaultValue = "0") Integer page,
//            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
//            @RequestParam(name = "sortDirection", defaultValue = "NONE") SortDirection sortDirection,
//            @RequestParam(name = "sortBy", defaultValue = "NAME") PlaceSortBy sortBy
//            ) {
//        PagedListDTO<PlaceBriefDTO> placeList = service.getPlaceList(
//                new PlaceFetchParameters(
//                        name,
//                        categoryId,
//                        page,
//                        pageSize,
//                        sortDirection,
//                        sortBy
//                )
//        );
//        return new ResponseEntity<>(placeList, HttpStatus.OK);
//    }

    @GetMapping("/list")
    public ResponseEntity<PagedListDTO<PlaceBriefDTO>> searchPlaces(
            @RequestParam(name = "category", required = false) List<Integer> categoryId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        String categoryIds = "";
        if(categoryId != null){
            categoryIds = categoryId.stream().map(String::valueOf).collect(Collectors.joining(","));
        }
        return new ResponseEntity<>(
                service.getPlaces(
                        categoryIds,
                        page,
                        pageSize),
                HttpStatus.OK);
    }

    //Запрос на получение ближаших мест к определнной точке на карте, заданной своими координатами
    //(1-ая часть контент-ориентированной рекомендательной системы)
    @GetMapping("/nearest/category")
    public ResponseEntity<PagedListDTO<PlaceBriefDTO>> getNearestPlaces(
            @RequestParam(name = "_my_lat") Double myLat,
            @RequestParam(name = "_my_lon") Double myLon,
            @RequestParam(name = "_start_dist") Double startDist,
            @RequestParam(name = "_max_dist") Double maxDist,
            @RequestParam(name = "_limit") Integer limit,
            @RequestParam(name = "category", required = false) List<Integer> categoryId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize
    ){
        String categoryIds = "";
        if(categoryId != null){
            categoryIds = categoryId.stream().map(String::valueOf).collect(Collectors.joining(","));
        }
        return new ResponseEntity<>(
                service.getNearestPlacesByCategory(
                        myLat,
                        myLon,
                        startDist,
                        maxDist,
                        limit,
                        categoryIds,
                        page,
                        pageSize),
                HttpStatus.OK);
    }

    //Запрос на получение рекомендаций с учетом stay-point-ов
    //(2.1-ая часть контент-ориентирвоанной рекомендательной системы)
    @GetMapping("/recommend/stay_points")
    public ResponseEntity<PagedListDTO<PlaceBriefDTO>> getStayPointRecommendations(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize
    ){
        if(stayPointService.ifUserHasStayPoints()){
            return new ResponseEntity<>(
                    service.getNearestPlacesByStayPoints(
                            page,
                            pageSize),
                    HttpStatus.OK);
        }else {
            //Запрос на получение рекомендаций с учетом посещенных мест
            //(2-ая часть контент-ориентированной рекомендательной системы)
            return new ResponseEntity<>(
                    service.getNearestPlacesByVisited(
                            page,
                            pageSize),
                    HttpStatus.OK);
        }
    }

    //Запрос на получение рекомедаций на основе личных оценок пользователя
    //3-я (заключительная) часть контент-ориентированной рекомендательной системы
    @GetMapping("/recommend/content_based")
    public ResponseEntity<PagedListDTO<PlaceBriefDTO>> getContentBasedRecommendations(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize
    ){
        return new ResponseEntity<>(
                service.getContentBasedRecommendations(
                        page,
                        pageSize),
                HttpStatus.OK);
    }

    //Запрос на получение рекомендаций на основе предпочтений других пользователей
    //(Вторая рекомендательная система)
    @GetMapping("/recommend/collaborative_filter")
    public ResponseEntity<PagedListDTO<PlaceBriefDTO>> getCollaborativeFilteringRecommendations(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize
    ){
        if(scoreService.ifUserHasScores(userService.getCurrentUserDto().getId())){
            return new ResponseEntity<>(
                    service.getCollaborativeRecommendationsByScores(
                            page,
                            pageSize
                    ),
                    HttpStatus.OK
            );
        }else{
            return new ResponseEntity<>(
                    service.getCollaborativeRecommendationsByFavourites(
                            page,
                            pageSize
                    ),
                    HttpStatus.OK
            );
        }
    }

    @PostMapping("/category")
    public ResponseEntity<String> addCategory(
            @RequestParam(name = "placeId") Long placeId,
            @RequestParam(name = "categoryId") Integer categoryId

    ) {
        try {
            service.addPlaceCategory(placeId, categoryId);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            return ResponseEntity.badRequest().build();
        }
    }
}
