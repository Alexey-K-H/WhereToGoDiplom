package ru.nsu.fit.wheretogo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.fit.wheretogo.dto.CategoryDTO;
import ru.nsu.fit.wheretogo.entity.Category;
import ru.nsu.fit.wheretogo.repository.CategoryRepository;

import javax.validation.ValidationException;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public void addCategory(CategoryDTO categoryDTO) {
        if (categoryRepository.existsByName(categoryDTO.getName())) {
            throw new ValidationException("Category already exist");
        }
        categoryRepository.save(Category.getFromDTO(categoryDTO));
    }

    @Transactional
    public void deleteCategory(CategoryDTO categoryDTO) {
        if (categoryDTO == null || categoryDTO.getId() == null) {
            return;
        }
        categoryRepository.deleteById(categoryDTO.getId());
    }

    public List<CategoryDTO> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryDTO::getFromEntity)
                .collect(toList());
    }
}
