//package com.example.secutity;
//
//import jakarta.persistence.*;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Data
//@NoArgsConstructor
//@Table(name = "users")
//public class User {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "login")
//     protected String login;
//
//    @Column(name = "password")
//    protected String password;
//
//    @Column(name = "role")
//    @Enumerated(EnumType.STRING)
//    private Role role;
//
//    public User(String login, String password, Role role) {
//        this.login = login;
//        this.password = password;
//        this.role = role;
//    }
//}
