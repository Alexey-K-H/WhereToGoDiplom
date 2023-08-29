package ru.nsu.fit.wheretogo.controller.place;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.fit.wheretogo.dto.place.CategoryDTO;
import ru.nsu.fit.wheretogo.service.place.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @PostMapping()
    public ResponseEntity<Void> saveCategory(@RequestBody CategoryDTO categoryDTO) {
        service.addCategory(categoryDTO);
        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable(name = "id") Long id) {
        var dto = new CategoryDTO();
        dto.setId(id);

        service.deleteCategory(dto);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/list")
    public ResponseEntity<List<CategoryDTO>> getCategories() {
        return new ResponseEntity<>(service.getAllCategories(), HttpStatus.OK);
    }

}
