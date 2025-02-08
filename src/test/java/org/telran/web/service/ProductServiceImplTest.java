//package org.telran.web.service;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.TypedQuery;
//import jakarta.persistence.criteria.*;
//import org.junit.jupiter.api.BeforeEach;
//import jakarta.persistence.criteria.Order;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.*;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.mockito.junit.jupiter.MockitoSettings;
//import org.mockito.quality.Strictness;
//import org.telran.web.dto.ProductCreateDto;
//import org.telran.web.entity.Category;
//import org.telran.web.entity.Product;
//import org.telran.web.entity.Storage;
//import org.telran.web.exception.ProductNotFoundException;
//import org.telran.web.repository.ProductJpaRepository;
//import jakarta.persistence.criteria.Path;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//import static org.mockito.ArgumentMatchers.any;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.anyList;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class ProductServiceImplTest {
//
//
//    @Mock
//    private EntityManager entityManager;
//
//    @Mock
//    private CriteriaBuilder criteriaBuilder;
//
//    @Mock
//    private CriteriaQuery<Product> criteriaQuery;
//
//    @Mock
//    private Root<Product> root;
//
//    @Mock
//    private TypedQuery<Product> typedQuery;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//    }
//
//    private static final Storage storage = new Storage(1L, 1L);
//    private static final List<Product> PRODUCTS = Arrays.asList(
//            new Product(
//                    3L,
//                    "Axe",
//                    BigDecimal.valueOf(1),
//                    "Hand tool for chopping wood",
//                    new Category(1L, "Tools and equipment", new ArrayList<>()),
//                    storage,
//                    BigDecimal.ZERO,
//                    null,
//                    null
//            ),
//
//            new Product(
//                    4L,
//                    "Drill",
//                    BigDecimal.valueOf(4),
//                    "Electric drill for construction work",
//                    new Category(1L, "Tools and equipment", new ArrayList<>()),
//                    storage,
//                    BigDecimal.ZERO,
//                    null,
//                    null
//            ),
//            new Product(
//                    5L,
//                    "Blower",
//                    BigDecimal.valueOf(2),
//                    "Electric blower for garden leaves",
//                    new Category(1L, "Tools and equipment", new ArrayList<>()),
//                    storage,
//                    BigDecimal.ZERO,
//                    null,
//                    null
//            ),
//            new Product(
//                    6L,
//                    "Excavator",
//                    BigDecimal.valueOf(5),
//                    "Mini excavator for landscaping",
//                    new Category(1L, "Tools and equipment", new ArrayList<>()),
//                    storage,
//                    BigDecimal.ZERO,
//                    null,
//                    null
//            ),
//            new Product(
//                    7L,
//                    "Chainsaw",
//                    BigDecimal.valueOf(3),
//                    "Powerful chainsaw for cutting trees",
//                    new Category(1L, "Tools and equipment", new ArrayList<>()),
//                    storage,
//                    BigDecimal.ZERO,
//                    null,
//                    null
//            ));
//
//    @Mock
//    private ProductJpaRepository repository;
//
//    @InjectMocks
//    private ProductServiceImpl service;
//
//    @Test
//    void testGetAllWithCriteriaBuilder() {
//        Long categoryId = 1L;
//        int direction = 1; // Сортировка по возрастанию
//        BigDecimal minPrice = BigDecimal.valueOf(2);
//        BigDecimal maxPrice = BigDecimal.valueOf(5);
//        BigDecimal discount = BigDecimal.valueOf(10);
//
//        // Настройка Criteria API
//        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
//        when(criteriaBuilder.createQuery(Product.class)).thenReturn(criteriaQuery);
//        when(criteriaQuery.from(Product.class)).thenReturn(root);
//
//        // Настройка моков для Path с использованием Answer
//        Path<Category> categoryPath = mock(Path.class);
//        Path<Long> categoryIdPath = mock(Path.class);
//        Path<BigDecimal> pricePath = mock(Path.class);
//        Path<BigDecimal> discountPath = mock(Path.class);
//
//        // Настройка root.get("category") и categoryPath.get("id")
//        when(root.get(eq("category"))).thenAnswer(invocation -> categoryPath);
//        when(categoryPath.get(eq("id"))).thenAnswer(invocation -> categoryIdPath);
//
//        // Настройка root.get("price") и root.get("discount")
//        when(root.get(eq("price"))).thenAnswer(invocation -> pricePath);
//        when(root.get(eq("discount"))).thenAnswer(invocation -> discountPath);
//
//        // Настройка Predicate
//        Predicate combinedPredicate = mock(Predicate.class);
//        Predicate categoryPredicate = mock(Predicate.class);
//        Predicate minPricePredicate = mock(Predicate.class);
//        Predicate maxPricePredicate = mock(Predicate.class);
//        Predicate discountPredicate = mock(Predicate.class);
//
//        when(criteriaBuilder.conjunction()).thenReturn(combinedPredicate);
//        when(criteriaBuilder.equal(categoryIdPath, categoryId)).thenReturn(categoryPredicate);
//        when(criteriaBuilder.and(combinedPredicate, categoryPredicate)).thenReturn(combinedPredicate);
//
//        when(criteriaBuilder.greaterThanOrEqualTo(pricePath, minPrice)).thenReturn(minPricePredicate);
//        when(criteriaBuilder.and(combinedPredicate, minPricePredicate)).thenReturn(combinedPredicate);
//
//        when(criteriaBuilder.lessThanOrEqualTo(pricePath, maxPrice)).thenReturn(maxPricePredicate);
//        when(criteriaBuilder.and(combinedPredicate, maxPricePredicate)).thenReturn(combinedPredicate);
//
//        when(criteriaBuilder.equal(discountPath, discount)).thenReturn(discountPredicate);
//        when(criteriaBuilder.and(combinedPredicate, discountPredicate)).thenReturn(combinedPredicate);
//
//        when(criteriaQuery.where(combinedPredicate)).thenReturn(criteriaQuery);
//
//        // Настройка сортировки
//        Order order = mock(Order.class);
//        if (direction == 1) {
//            when(criteriaBuilder.asc(pricePath)).thenReturn(order);
//        } else if (direction == -1) {
//            when(criteriaBuilder.desc(pricePath)).thenReturn(order);
//        }
//        when(criteriaQuery.orderBy(order)).thenReturn(criteriaQuery);
//
//        // Настройка TypedQuery
//        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
//        when(typedQuery.getResultList()).thenReturn(PRODUCTS.subList(0, 2));
//
//        // Вызов тестируемого метода
//        List<Product> result = service.getAll(categoryId, direction, minPrice, maxPrice, discount);
//
//        // Проверка результата
//        assertNotNull(result);
//        assertEquals(2, result.size());
//    }
//
//
//
//
//
//    @Test
//    public void getProductByIdWhenProductExists() {
//        Long productId = 1L;
//        Product productOne = PRODUCTS.get(0);
//        when(repository.findById(productId))
//                .thenReturn((Optional.of(productOne)));
//        Product productActual = service.getById(productId);
//        assertNotNull(productActual);
//        assertEquals(productOne, productActual);
//    }
//
//    @Test
//    public void getProductByIdWhenProductNotExists() {
//        Long productId = 1L;
//        when(repository.findById(productId))
//                .thenThrow(new ProductNotFoundException("Product not found"));
//        assertThrows(ProductNotFoundException.class,
//                () -> service.getById(productId));
//    }
//
//    @Test
//    void createProduct() {
//        Product product = PRODUCTS.get(0);
//        product.setId(null);
//        Product savedProduct = PRODUCTS.get(0);
//        savedProduct.setId(1L);
//
//        when(repository.save(product)).thenReturn(savedProduct);
//        Product createdProduct = service.create(product);
//
//        assertNotNull(createdProduct);
//        assertNotNull(createdProduct.getId());
//        assertEquals(1L, createdProduct.getId());
//    }
//
//    @Test
//    void editProductWhenProductExists() {
//        Long productId = 1L;
//        Product existingProduct = PRODUCTS.get(0);
//
//        ProductCreateDto updateDto = new ProductCreateDto();
//        updateDto.setPrice(BigDecimal.valueOf(300));
//        updateDto.setProductInfo("New Electric Trimmer");
//        updateDto.setDiscount(BigDecimal.valueOf(10));
//        updateDto.setUpdateAt(java.time.LocalDateTime.now());
//
//        when(repository.findById(productId))
//                .thenReturn(Optional.of(existingProduct));
//        when(repository.save(existingProduct))
//                .thenAnswer(invocation -> invocation.getArgument(0));
//
//        Product updatedProduct = service.editProducts(productId, updateDto);
//
//        assertNotNull(updatedProduct);
//        assertEquals(updateDto.getPrice(), updatedProduct.getPrice());
//        assertEquals(updateDto.getProductInfo(), updatedProduct.getProductInfo());
//        assertEquals(updateDto.getDiscount(), updatedProduct.getDiscount());
//        assertEquals(updateDto.getUpdateAt(), updatedProduct.getUpdatedAt());
//    }
//
//    @Test
//    void editProductWhenProductDoesNotExist() {
//        Long productId = 1L;
//        ProductCreateDto updateDto = new ProductCreateDto();
//        updateDto.setPrice(BigDecimal.valueOf(300));
//
//        when(repository.findById(productId)).thenReturn(Optional.empty());
//
//        assertThrows(ProductNotFoundException.class,
//                () -> service.editProducts(productId, updateDto));
//    }
//}