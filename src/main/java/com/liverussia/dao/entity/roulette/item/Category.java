package com.liverussia.dao.entity.roulette.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "r_item_category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "name")
    private CategoryName name;

    @Column(name = "probability")
    private String probability;
}
