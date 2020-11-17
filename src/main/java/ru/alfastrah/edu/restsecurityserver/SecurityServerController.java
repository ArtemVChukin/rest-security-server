package ru.alfastrah.edu.restsecurityserver;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SecurityServerController {
    private final ContractService contractService;

    @GetMapping("/contract")
    public String getAllContracts() {
        return contractService.getAllContracts().toString();
    }

}
