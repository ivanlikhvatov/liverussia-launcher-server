package com.liverussia.dao.entity.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "donate")
public class Donate {
    @Id
    private String id;

    @Column(name = "rub")
    private BigDecimal balance;

    @Column(name = "userid")
    private String userId;
}
