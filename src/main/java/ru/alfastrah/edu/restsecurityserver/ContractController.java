package ru.alfastrah.edu.restsecurityserver;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ContractController.CONTRACT_BASE_URL)
public class ContractController {
    public static final String CONTRACT_BASE_URL = "/contract";
    private final ContractService contractService;

    @GetMapping
    public List<Contract> getAllContracts() {
        return contractService.getAllContracts();
    }

    @GetMapping("/{contractId}")
    public Contract getContract(@PathVariable Long contractId) {
        return contractService.get(contractId);
    }

    @PostMapping
    @SuppressWarnings("java:S4684")
    public Contract saveContract(@RequestBody @Valid Contract contract) {
        return contractService.save(contract);
    }

    @DeleteMapping("/{contractId}")
    public void deleteContract(@PathVariable Long contractId) {
        contractService.delete(contractId);
    }
}
