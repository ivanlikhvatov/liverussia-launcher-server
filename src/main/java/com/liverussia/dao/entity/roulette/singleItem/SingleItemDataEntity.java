package com.liverussia.dao.entity.roulette.singleItem;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "r_single_item_data")
public class SingleItemDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "samp_element_id")
    private String sampElementId;

    @Column(name = "single_element_type")
    @Enumerated(EnumType.STRING)
    private SingleElementType singleElementType;

    @Column(name = "prize_image_file_name")
    private String prizeImageFileName;
}
