package com.liverussia.dao.entity;

import lombok.Getter;
import lombok.Setter;
//import org.hibernate.annotations.GenericGenerator;
//import org.springframework.data.annotation.CreatedDate;
//import org.springframework.data.annotation.LastModifiedDate;
//import org.springframework.data.jpa.domain.support.AuditingEntityListener;
//
//import javax.persistence.*;
//import java.time.LocalDateTime;
//import java.util.List;
import java.util.Set;

@Getter
@Setter
public class User {

    private String id;

    private String login;

    private String firstName;

    private String password;

    private Set<Role> roles;
}
