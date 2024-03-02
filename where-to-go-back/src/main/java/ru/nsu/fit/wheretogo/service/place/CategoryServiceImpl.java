package ru.nsu.fit.wheretogo.service.place;

import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.fit.wheretogo.dto.place.CategoryDTO;
import ru.nsu.fit.wheretogo.entity.place.Category;
import ru.nsu.fit.wheretogo.repository.place.CategoryRepository;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public void addCategory(CategoryDTO categoryDTO) {
        if (categoryRepository.existsByName(categoryDTO.getName())) {
            throw new ValidationException("Category already exist");
        }
        categoryRepository.save(Category.getFromDTO(categoryDTO));
    }

    @Override
    @Transactional
    public void deleteCategory(CategoryDTO categoryDTO) {
        categoryRepository.deleteById(categoryDTO.getId());
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryDTO::getFromEntity)
                .toList();
    }
}
