package org.yearup.models;

import jakarta.persistence.*;

import java.math.BigDecimal;


@Entity
@Table(name = "order_line_items")
public class OrderLineItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_line_item_id")
    private int orderLineItemId;
    @Column(name = "order_id")
    private int orderId;
    @Column(name = "product_id")
    private int productId;
    @Column(name = "sales_price")
    private BigDecimal salesPrice;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "discount")
    private BigDecimal discount = BigDecimal.valueOf(0);
    @ManyToOne
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;

    public OrderLineItem(){}

    public OrderLineItem(int orderLineItemId, int orderId, int productId, BigDecimal salesPrice, int quantity, BigDecimal discount) {
        this.orderLineItemId = orderLineItemId;
        this.orderId = orderId;
        this.productId = productId;
        this.salesPrice = salesPrice;
        this.quantity = quantity;
        this.discount = discount;
    }

    public int getOrderLineItemId() {
        return orderLineItemId;
    }

    public void setOrderLineItemId(int orderLineItemId) {
        this.orderLineItemId = orderLineItemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public BigDecimal getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(BigDecimal salesPrice) {
        this.salesPrice = salesPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "OrderLineItem{" +
                "orderLineItemId=" + orderLineItemId +
                ", orderId=" + orderId +
                ", productId=" + productId +
                ", salesPrice=" + salesPrice +
                ", quantity=" + quantity +
                ", discount=" + discount +
                '}';
    }
}
