package org.yearup.service;

import org.springframework.stereotype.Service;
import org.yearup.models.Category;
import org.yearup.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService
{
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository)
    {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories()
    {
        return categoryRepository.findAll();
    }

    public Optional<Category> getById(int categoryId)
    {
        return categoryRepository.findById(categoryId);
    }

    public Category create(Category category)
    {
        // create a new category
        return categoryRepository.save(category);

    }

    public Optional<Category> update(int categoryId, Category updatedState)
    {
        // update category and return the updated category
        return categoryRepository.findById(categoryId).map(
                category -> {
                 category.setName(updatedState.getName());
                 category.setDescription(updatedState.getDescription());

                 return categoryRepository.save(category);
                }
        );

    }

    public void delete(int categoryId)
    {
        // delete category
        categoryRepository.deleteById(categoryId);
    }
}
