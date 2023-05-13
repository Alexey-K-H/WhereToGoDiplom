package ru.nsu.fit.wheretogo.controller.place;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.fit.wheretogo.dto.PagedListDTO;
import ru.nsu.fit.wheretogo.dto.place.CategoryDTO;
import ru.nsu.fit.wheretogo.dto.place.PlaceBriefDTO;
import ru.nsu.fit.wheretogo.dto.place.PlaceDescriptionDTO;
import ru.nsu.fit.wheretogo.service.place.CategoryService;
import ru.nsu.fit.wheretogo.service.place.PlaceService;
import ru.nsu.fit.wheretogo.service.user.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/place")
public class PlaceController {
    private static final String DELETED_STR = "Deleted";
    private static final String ADDED_STR = "Added";

    private final PlaceService placeService;
    private final UserService userService;
    private final CategoryService categoryService;

    public PlaceController(
            PlaceService placeService,
            UserService userService,
            CategoryService categoryService) {
        this.placeService = placeService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<Void> addPlace(@RequestBody @Valid PlaceDescriptionDTO place) {
        placeService.addPlace(place);
        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlace(@PathVariable(name = "id") Long id) {
        var dto = new PlaceDescriptionDTO();
        dto.setId(id);

        placeService.deletePlace(dto);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaceDescriptionDTO> getPlaceById(@PathVariable(name = "id") Long id) {
        PlaceDescriptionDTO dto = placeService.getPlaceById(id);
        if (dto != null) {
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/favourite/{id}")
    public ResponseEntity<String> addFavourite(
            @PathVariable(name = "id") Long placeId
    ) {
        try {
            userService.addFavourite(placeId);
            return ResponseEntity.ok(ADDED_STR);
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
            return ResponseEntity.ok(ADDED_STR);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/favourite")
    public ResponseEntity<List<PlaceBriefDTO>> getFavourite() {
        try {
            return ResponseEntity.ok(userService.getFavourites());
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
    public ResponseEntity<Boolean> existVisitedById(@PathVariable(name = "id") Long id) {
        try {
            return ResponseEntity.ok(userService.findVisitedById(id));
        } catch (Exception exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/favourite/{id}")
    public ResponseEntity<Boolean> existFavouriteById(@PathVariable(name = "id") Long id) {
        try {
            return ResponseEntity.ok(userService.findFavouriteById(id));
        } catch (Exception exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/visited/{id}")
    public ResponseEntity<String> deleteVisitedById(@PathVariable(name = "id") Long id) {
        try {
            userService.deleteVisited(id);
            return new ResponseEntity<>(DELETED_STR, HttpStatus.OK);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/favourite/{id}")
    public ResponseEntity<String> deleteFavouriteById(@PathVariable(name = "id") Long id) {
        try {
            userService.deleteFavourite(id);
            return new ResponseEntity<>(DELETED_STR, HttpStatus.OK);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/list")
    public ResponseEntity<PagedListDTO<PlaceBriefDTO>> searchPlaces(
            @RequestParam(name = "category", required = false) List<Integer> categoryId,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {
        var categoryIds = "";
        categoryIds = Objects.requireNonNullElseGet(
                        categoryId,
                        () -> categoryService.getAllCategories().stream().map(CategoryDTO::getId).toList())
                .stream().map(String::valueOf).collect(Collectors.joining(","));

        return new ResponseEntity<>(
                placeService.getPlaces(
                        categoryIds,
                        page,
                        pageSize),
                HttpStatus.OK);
    }

    @PostMapping("/category")
    public ResponseEntity<Void> addCategory(
            @RequestParam(name = "placeId") Long placeId,
            @RequestParam(name = "categoryId") Long categoryId
    ) {
        try {
            placeService.addPlaceCategory(placeId, categoryId);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            return ResponseEntity.badRequest().build();
        }
    }
}