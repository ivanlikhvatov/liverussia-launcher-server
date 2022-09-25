package com.liverussia.dao.entity.roulette.singleItem;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "r_single_item_data")
public class SingleItemData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "samp_element_id")
    private String sampElementId;

    @Column(name = "single_element_type")
    private SingleElementType singleElementType;

    @Column(name = "prize_image_file_name")
    private String prizeImageFileName;
}
