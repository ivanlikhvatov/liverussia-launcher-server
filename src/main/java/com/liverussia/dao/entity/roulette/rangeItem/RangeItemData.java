package com.liverussia.dao.entity.roulette.rangeItem;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "r_range_item_data")
public class RangeItemData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "range_start")
    private Long rangeStart;

    @Column(name = "range_end")
    private Long rangeEnd;

    @Column(name = "range_element_type")
    private RangeElementType rangeElementType;

    @Column(name = "prize_image_file_name")
    private String prizeImageFileName;
}
