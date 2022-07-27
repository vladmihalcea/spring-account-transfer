package com.vladmihalcea.spring.transfer.controller;

import com.vladmihalcea.spring.transfer.service.TransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * cURL:
 *
 * curl -X POST \
 * 	-H "Content-Type: application/x-www-form-urlencoded" \
 *  -d 'fromIban=63a0afea-4299-4dd4-8642-ee28d65ef526&toIban=c40d2e33-e7c2-4c2a-886f-13d4ed7ff31d&cents=5' \
 *  http://localhost:8080/transfer
 *
 * @author Vlad Mihalcea
 */
@RestController
public class TransferRestController {

    @Autowired
    private TransferService transferService;

    @PostMapping("/transfer")
    public long transfer(
        @RequestParam("fromIban") String fromIban,
        @RequestParam("toIban") String toIban,
        @RequestParam("cents") long cents) {
        return transferService.transfer(fromIban, toIban, cents);
    }
}
