package com.liverussia.dao.entity.roulette.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "r_item_category")
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private CategoryName name;

    @Column(name = "percent_probability")
    private Long percentProbability;
}
