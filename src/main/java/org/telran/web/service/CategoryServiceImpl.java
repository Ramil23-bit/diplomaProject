package org.telran.web.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telran.web.entity.Category;
import org.telran.web.entity.Product;
import org.telran.web.exception.BadArgumentsException;
import org.telran.web.exception.CategoryNotFoundException;
import org.telran.web.repository.CategoryJpaRepository;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryJpaRepository categoryJpaRepository;

    @Autowired
    private ProductService productService;

    @Override
    public Category create(Category category) {
        try {
            return categoryJpaRepository.save(category);
        } catch (Exception exception) {
            throw new BadArgumentsException("Entered data is not corrected");
        }
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
        try {
            if (categoryJpaRepository.updateTitle(id, title) == 0) {
                throw new CategoryNotFoundException("Category with id " + id + " not found");
            }
        } catch (IllegalArgumentException ex) {
            throw new BadArgumentsException("Entered data is not corrected");
        }
    }

    @Override
    public Category getByName(String name) {
        return categoryJpaRepository.findByCategoryTitle(name)
                .orElseThrow(() -> new CategoryNotFoundException("Product with name " + name + " not found"));
    }

    @Override
    @Transactional
    public Category editListOfProductsAddProduct(Long categoryId, Long productId) {
        Category category = getById(categoryId);
        Product product = productService.getById(productId);
        productService.setCategory(productId, category);
        //productService.save(product);
        //category.getProducts().add(product);
        return category;
    }

    @Override
    @Transactional
    public Category editListOfProductsRemoveProduct(Long categoryId, Long productId) {
        Category category = getById(categoryId);
        Product product = productService.getById(productId);
        if (!product.getCategory().equals(category)) {
            throw new IllegalArgumentException("Product does not belong to the category");
        }
        productService.setCategory(productId, null);
        //productService.save(product);
        //category.getProducts().remove(productId);
        return category;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try {
            categoryJpaRepository.deleteById(id);
        } catch (Exception exception) {
            throw new CategoryNotFoundException("Category by " + id + " not found");
        }
    }
}
