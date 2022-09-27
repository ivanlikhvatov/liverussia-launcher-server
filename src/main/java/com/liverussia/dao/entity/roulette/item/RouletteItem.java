package com.liverussia.dao.entity.roulette.item;

import com.liverussia.dao.entity.roulette.compositeItem.CompositeItemData;
import com.liverussia.dao.entity.roulette.rangeItem.RangeItemData;
import com.liverussia.dao.entity.roulette.singleItem.SingleItemData;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "r_roulette_item")
public class RouletteItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "r_item_category_id")
    private Category category;

    @Column(name = "image_file_name")
    private String imageFileName;

    @Column(name = "type")
    private RouletteItemType type;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "r_single_item_data_id")
    private SingleItemData singleItemData;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "r_range_item_data_id")
    private RangeItemData rangeItemData;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "r_composite_item_data_id")
    private CompositeItemData compositeItemData;
}
