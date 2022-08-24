package com.liverussia.dao.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import javax.persistence.*;

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

    @Formula("(select donate.rub from donate where donate.userid = id)")
    private String balance;
}
