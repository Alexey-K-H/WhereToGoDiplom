package ru.nsk.wheretogo.service;

import org.springframework.stereotype.Service;
import ru.nsk.wheretogo.dto.CategoryDTO;
import ru.nsk.wheretogo.entity.Category;
import ru.nsk.wheretogo.repository.CategoryRepository;

import javax.transaction.Transactional;
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
        if ( (categoryDTO.getId() == null) || (categoryDTO == null)) {
            return;
        }
        categoryRepository.deleteById(categoryDTO.getId());
    }

    public List<CategoryDTO> getAllcategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryDTO::getFromEntity)
                .collect(toList());
    }
}

