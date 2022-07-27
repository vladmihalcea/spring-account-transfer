package com.vladmihalcea.spring.transfer.controller;

import com.vladmihalcea.spring.transfer.service.TransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Vlad Mihalcea
 */
@RestController
public class TransferRestController {

    @Autowired
    private TransferService transferService;

    @PostMapping("/transfer")
    public void transfer(
        @RequestParam("fromIban") String fromIban,
        @RequestParam("toIban") String toIban,
        @RequestParam("cents") long cents) {
        transferService.transfer(fromIban, toIban, cents);
    }
}
