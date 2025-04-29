package com.damian.debtto.activity.debt;

import com.damian.debtto.customer.Customer;

import java.math.BigDecimal;
import java.time.Instant;

//@Entity
//@Table(name = "customer_debts")
public class Debt {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Customer customerDebitor;
    private Customer customerToPayDebt;


    private BigDecimal initialDebt;
    private BigDecimal redeemedDebt;

//    @Column
    private Instant createdAt;

//    @Column
    private Instant updatedAt;

    public Debt() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public DebtDTO toDTO() {
//        return DTOMapper.build(this);
//    }

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
}
