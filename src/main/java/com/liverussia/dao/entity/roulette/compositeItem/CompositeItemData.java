package com.liverussia.dao.entity.roulette.compositeItem;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "r_composite_item_data")
public class CompositeItemData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "type")
    private CompositeElementType type;
}
