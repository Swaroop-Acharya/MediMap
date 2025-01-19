package com.medimap.order.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_orders")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Order {
    
}
