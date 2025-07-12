package com.damian.paynext.group.repayments;

import com.damian.paynext.customer.Customer;
import com.damian.paynext.group.group.Group;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "group_repayments")
public class GroupRepayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "payer_customer_id")
    private Customer payer;

    @Column(precision = 15, scale = 2)
    private BigDecimal amount;

    @Column
    private Instant createdAt;

    @Column
    private Instant updatedAt;

    public GroupRepayment() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public GroupRepayment(Group group, Customer payer) {
        this();
        this.payer = payer;
        this.group = group;
    }

    public GroupRepayment(Group group, Customer payer, BigDecimal amount) {
        this(group, payer);
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "GroupRepayment {" +
               "id=" + id +
               ", Group=" + group.toString() +
               ", Payer=" + payer.toString() +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               "}";

    }

    public Customer getPayer() {
        return payer;
    }

    public void setPayer(Customer payer) {
        this.payer = payer;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
