package com.liverussia.dao;

import lombok.Getter;
import lombok.Setter;
//import org.hibernate.annotations.GenericGenerator;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import javax.persistence.Table;

@Getter
@Setter
//@Entity
//@Table(name = "accounts")
public class TestEntity {
//    @Id
    private String id;

    private String nickname;

    @Override
    public String toString() {
        return "TestEntity{" +
                "id='" + id + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
