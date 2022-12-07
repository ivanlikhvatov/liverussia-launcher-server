package com.liverussia.dao.entity.roulette.compositeItem;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "r_composite_items")
public class CompositeItemsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private CompositeElementType type;

    @Column(name = "image_file_name")
    private String imageFileName;

    @Column(name = "samp_element_id")
    private String sampElementId;
}
