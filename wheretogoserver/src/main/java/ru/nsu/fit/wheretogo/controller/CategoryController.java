package ru.nsu.fit.wheretogo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.fit.wheretogo.dto.CategoryDTO;
import ru.nsu.fit.wheretogo.service.CategoryService;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @PostMapping()
    public ResponseEntity<String> saveCategory(@RequestBody CategoryDTO categoryDTO) {
        service.addCategory(categoryDTO);
        return ResponseEntity.ok("OK");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable(name = "id") Integer id) {
        service.deleteCategory(new CategoryDTO().setId(id));
        return new ResponseEntity<>("Deleted", HttpStatus.NO_CONTENT);
    }

    @GetMapping("/list")
    public ResponseEntity<List<CategoryDTO>> getCategories() {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

}
