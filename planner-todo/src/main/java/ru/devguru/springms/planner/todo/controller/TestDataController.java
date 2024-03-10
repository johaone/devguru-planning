package ru.devguru.springms.planner.todo.controller;

// класс, предназначенный для заполнения тестовыми данными таблиц

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.devguru.springms.planner.todo.service.TestDataService;

@RestController
@RequestMapping("/testData")
@RequiredArgsConstructor
public class TestDataController {

    private final TestDataService testDataService;

    @PostMapping("/init")
    public ResponseEntity<Boolean> initTestData(@RequestBody Long userId) {

        testDataService.init(userId);

        return ResponseEntity.ok(true);

    }
}
