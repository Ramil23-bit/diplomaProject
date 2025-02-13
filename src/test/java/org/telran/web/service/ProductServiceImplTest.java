//package org.telran.web.service;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.TypedQuery;
//import jakarta.persistence.criteria.*;
//import org.junit.jupiter.api.BeforeEach;
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
//import java.util.*;
//
//import static org.mockito.ArgumentMatchers.any;
//
//import static org.junit.jupiter.api.Assertions.*;
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
//    @Mock
//    private ProductJpaRepository repository;
//
//    @Mock
//    private CategoryService categoryService;
//
//    @InjectMocks
//    private ProductServiceImpl service;
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
//    @Test
//    @MockitoSettings(strictness = Strictness.LENIENT)
//    void testGetAllWithCriteriaBuilder() {
//        Long categoryId = 1L;
//        int direction = 1;
//        BigDecimal minPrice = BigDecimal.valueOf(2);
//        BigDecimal maxPrice = BigDecimal.valueOf(5);
//        BigDecimal discount = BigDecimal.valueOf(10);
//
//        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
//        when(criteriaBuilder.createQuery(Product.class)).thenReturn(criteriaQuery);
//        when(criteriaQuery.from(Product.class)).thenReturn(root);
//
//        // Создаем моки для атрибутов
//        Path<Category> categoryPath = mock(Path.class);
//        Path<Long> categoryIdPath = mock(Path.class);
//        Path<BigDecimal> pricePath = mock(Path.class);
//        Path<BigDecimal> discountPath = mock(Path.class);
//        Path<Object> createdAtPath = mock(Path.class);
//
//        when(root.get("category")).thenReturn((Path) categoryPath);
//        when(categoryPath.get("id")).thenReturn((Path) categoryIdPath);
//        when(root.get("price")).thenReturn((Path) pricePath);
//        when(root.get("discount")).thenReturn((Path) discountPath);
//        when(root.get("createdAt")).thenReturn(createdAtPath);
//
//        // Мокируем создание предикатов
//        Predicate combinedPredicate = mock(Predicate.class);
//        when(criteriaBuilder.conjunction()).thenReturn(combinedPredicate);
//        when(criteriaBuilder.equal(categoryIdPath, categoryId)).thenReturn(mock(Predicate.class));
//        when(criteriaBuilder.greaterThanOrEqualTo(pricePath, minPrice)).thenReturn(mock(Predicate.class));
//        when(criteriaBuilder.lessThanOrEqualTo(pricePath, maxPrice)).thenReturn(mock(Predicate.class));
//        when(criteriaBuilder.equal(discountPath, discount)).thenReturn(mock(Predicate.class));
//
//        // **Исправленный мок для where()**
//        when(criteriaQuery.where(ArgumentMatchers.nullable(Predicate.class))).thenReturn(criteriaQuery);
//
//        // **Исправленный мок для orderBy()**
//        when(criteriaQuery.orderBy(anyList())).thenReturn(criteriaQuery);
//
//        // Мокируем выполнение запроса
//        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
//        when(typedQuery.getResultList()).thenReturn(new ArrayList<>(PRODUCTS));
//
//        // Вызов тестируемого метода
//        List<Product> result = service.getAll(categoryId, direction, minPrice, maxPrice, discount);
//
//        // Проверка результата
//        assertNotNull(result);
//        assertEquals(PRODUCTS.size(), result.size());
//    }
//
//    @Test
//    void getProductByIdWhenProductExists() {
//        Long productId = 3L;
//        Product expectedProduct = PRODUCTS.get(0);
//
//        when(repository.findById(productId)).thenReturn(Optional.of(expectedProduct));
//
//        Product actualProduct = service.getById(productId);
//
//        assertNotNull(actualProduct);
//        assertEquals(expectedProduct, actualProduct);
//    }
//
//    @Test
//    void getProductByIdWhenProductNotExists() {
//        Long productId = 10L;
//
//        when(repository.findById(productId)).thenReturn(Optional.empty());
//
//        assertThrows(ProductNotFoundException.class, () -> service.getById(productId));
//    }
//
//    @Test
//    void createProduct() {
//        Product product = PRODUCTS.get(0);
//        product.setId(null);
//        Product savedProduct = new Product(1L, "Axe", BigDecimal.valueOf(1), "Hand tool", new Category(1L, "Tools", new ArrayList<>()), storage, BigDecimal.ZERO, null, null);
//
//        when(repository.save(any(Product.class))).thenReturn(savedProduct);
//
//        Product createdProduct = service.create(product);
//
//        assertNotNull(createdProduct);
//        assertEquals(1L, createdProduct.getId());
//    }
//
//    @Test
//    void editProductWhenProductExists() {
//        Long productId = 3L;
//        Product existingProduct = PRODUCTS.get(0);
//
//        ProductCreateDto updateDto = new ProductCreateDto();
//        updateDto.setPrice(BigDecimal.valueOf(300));
//        updateDto.setDescription("New Electric Trimmer");
//        updateDto.setDiscount(BigDecimal.valueOf(10));
//        updateDto.setUpdateAt(java.time.LocalDateTime.now());
//        updateDto.setCategory("Tools");
//
//        when(repository.findById(productId)).thenReturn(Optional.of(existingProduct));
//        when(repository.save(any(Product.class))).thenAnswer(invocation -> {
//            Product savedProduct = invocation.getArgument(0);
//            savedProduct.setUpdatedAt(updateDto.getUpdateAt());
//            return savedProduct;
//        });
//        when(categoryService.getByName(updateDto.getCategory())).thenReturn(new Category(1L, "Tools"));
//
//        Product updatedProduct = service.editProducts(productId, updateDto);
//
//        assertNotNull(updatedProduct);
//        assertEquals(updateDto.getPrice(), updatedProduct.getPrice());
//        assertEquals(updateDto.getDescription(), updatedProduct.getProductInfo());
//        assertEquals(updateDto.getDiscount(), updatedProduct.getDiscount());
//        assertEquals(updateDto.getUpdateAt(), updatedProduct.getUpdatedAt());
//    }
//
//    @Test
//    void editProductWhenProductDoesNotExist() {
//        Long productId = 3L;
//        ProductCreateDto updateDto = new ProductCreateDto();
//        updateDto.setPrice(BigDecimal.valueOf(300));
//
//        when(repository.findById(productId)).thenReturn(Optional.empty());
//
//        assertThrows(ProductNotFoundException.class, () -> service.editProducts(productId, updateDto));
//    }
//}