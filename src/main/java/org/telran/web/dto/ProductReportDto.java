package org.telran.web.dto;

import org.telran.web.entity.Product;

/**
 * Data Transfer Object (DTO) representing product reports.
 * This class is used to store and transfer data about a product and its associated count,
 * such as the number of times it was purchased or canceled.
 */
public class ProductReportDto {

    /**
     * The product associated with the report.
     */
    private Product product;

    /**
     * The count of occurrences related to the product (e.g., purchase count or cancellation count).
     */
    private Long count;

    /**
     * Constructs a new ProductReportDto with the specified product and count.
     *
     * @param product The product for which the report is generated.
     * @param count   The count of occurrences related to the product.
     */
    public ProductReportDto(Product product, Long count) {
        this.product = product;
        this.count = count;
    }

    /**
     * Retrieves the product associated with the report.
     *
     * @return The product.
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Sets the product associated with the report.
     *
     * @param product The product to be set.
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * Retrieves the count of occurrences related to the product.
     *
     * @return The count of occurrences.
     */
    public Long getCount() {
        return count;
    }

    /**
     * Sets the count of occurrences related to the product.
     *
     * @param count The count to be set.
     */
    public void setCount(Long count) {
        this.count = count;
    }
}