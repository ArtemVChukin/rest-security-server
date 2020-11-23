package ru.alfastrah.edu.restsecurityserver;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ContractController {
    private final ContractService contractService;

    @GetMapping("/contract")
    public List<Contract> getAllContracts() {
        return contractService.getAllContracts();
    }

    @GetMapping("/contract/{contractId}")
    public Contract getContract(@PathVariable Long contractId) {
        return contractService.get(contractId);
    }

    @PostMapping("/contract")
    @SuppressWarnings("java:S4684")
    public Contract saveContract(@RequestBody @Valid Contract contract) {
        return contractService.save(contract);
    }

    @DeleteMapping("/contract/{contractId}")
    public void deleteContract(@PathVariable Long contractId) {
        contractService.delete(contractId);
    }
}
