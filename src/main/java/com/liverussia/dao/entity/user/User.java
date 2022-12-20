package com.liverussia.dao.entity.user;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "accounts")
public class User {

    @Id
    private String id;

    @Column(name="nickname")
    private String login;

    @Column(name="ppassword")
    private String password;

    @Column(name = "psalt")
    private String salt;

    @Formula("(select admins.level_admin from admins where admins.userid = id)")
    private Integer adminLevel;

    @Column(name = "prubles")
    private BigDecimal balance;
}
