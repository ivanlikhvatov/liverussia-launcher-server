package com.liverussia.dao.entity.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "roulette_prizes")
public class RoulettePrizes {

    @Column(name = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userID")
    private String userId;

    @Column(name = "type")
    private String type;

    @Column(name = "value")
    private String value;
}
