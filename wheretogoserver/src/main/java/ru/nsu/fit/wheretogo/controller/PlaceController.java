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
import ru.nsu.fit.wheretogo.service.PlacePicturesService;
import ru.nsu.fit.wheretogo.service.PlaceService;
import ru.nsu.fit.wheretogo.service.UserService;
import ru.nsu.fit.wheretogo.service.fetch.PlaceFetchParameters;
import ru.nsu.fit.wheretogo.service.fetch.PlaceSortBy;
import ru.nsu.fit.wheretogo.utils.SortDirection;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/place")
@RequiredArgsConstructor
public class PlaceController {
    private final PlaceService service;
    private final PlacePicturesService picturesService;
    private final UserService userService;//нужен для работы с посещенными и понравившимися

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
    @PostMapping("/favourite")
    public ResponseEntity<String> addFavourite(
            @RequestParam(name = "placeId") Long placeId
    ) {
        try {
            userService.addFavourite(placeId);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/visited")
    public ResponseEntity<String> addVisited(
            @RequestParam(name = "placeId") Long placeId
    ) {
        try {
            userService.addVisited(placeId);
            return ResponseEntity.ok().build();
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

    @GetMapping("/list")
    public ResponseEntity<PagedListDTO<PlaceBriefDTO>> searchPlaces(
            @RequestParam(name = "name", defaultValue = "") String name,
            @RequestParam(name = "category", required = false) List<Integer> categoryId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(name = "sortDirection", defaultValue = "NONE") SortDirection sortDirection,
            @RequestParam(name = "sortBy", defaultValue = "NAME") PlaceSortBy sortBy
            ) {
        PagedListDTO<PlaceBriefDTO> placeList = service.getPlaceList(
                new PlaceFetchParameters(
                        name,
                        categoryId,
                        page,
                        pageSize,
                        sortDirection,
                        sortBy
                )
        );
        return new ResponseEntity<>(placeList, HttpStatus.OK);
    }
}
