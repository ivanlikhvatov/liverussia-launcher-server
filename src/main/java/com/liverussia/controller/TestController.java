package com.liverussia.controller;

import com.liverussia.domain.JwtUser;
import com.liverussia.service.impl.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class TestController {

//    private final TestRepository testRepository;
    private final AuthService authService;

    @GetMapping("/test/helloworld")
    public String helloWorld() {
        return "hello world!";
    }

//    @GetMapping("/test/getFromDb")
//    public TestEntity getFromDb() {
//        return testRepository.getById("163");
//    }

    @GetMapping("/android/hello")
    public ResponseEntity<String> helloUser(@AuthenticationPrincipal JwtUser user) {
        return ResponseEntity.ok("Hello android user " + user.getLogin() + "!");
    }

    @GetMapping("/admin/hello")
    public ResponseEntity<String> helloAdmin(@AuthenticationPrincipal JwtUser user) {
        return ResponseEntity.ok("Hello admin " + user.getLogin() + "!");
    }


}
