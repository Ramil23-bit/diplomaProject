package org.telran.web.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telran.web.entity.Category;
import org.telran.web.entity.Product;
import org.telran.web.exception.CategoryNotFoundException;
import org.telran.web.repository.CategoryJpaRepository;
import org.telran.web.repository.ProductJpaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryJpaRepository categoryJpaRepository;

    @Autowired
    private ProductService productService;

    @Override
    public Category create(Category category) {
        return categoryJpaRepository.save(category);
    }

    @Override
    public List<Category> getAll() {
        return categoryJpaRepository.findAll();
    }

    @Override
    public Category getById(Long id) {
        return categoryJpaRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category with id " + id + " not found"));
    }

    @Override
    @Transactional
    public void editTitle(Long id, String title) {
        if (0 == categoryJpaRepository.updateTitle(id, title)) {
            throw new CategoryNotFoundException("Category with id " + id + " not found");
        }
    }

    @Override
    @Transactional
    public Category editListOfProductsAddProduct(Long categoryId, Long productId) {
        Category category = getById(categoryId);
        Product product = productService.setCategory(productId, category);
        category.getProducts().add(product);
        return categoryJpaRepository.save(category);
    }

    @Override
    @Transactional
    public Category editListOfProductsRemoveProduct(Long categoryId, Long productId) {
        Category category = getById(categoryId);
        if(!productService.getById(productId).getCategory().equals(category)) {
            throw new IllegalArgumentException("Product does not belong to the category");
        }
        productService.setCategory(productId, null);
        category.getProducts().remove(productId);
        return categoryJpaRepository.save(category);
    }

    @Override
    public void delete(Long id) {
        categoryJpaRepository.deleteById(id);
    }
}
