package ru.alfastrah.edu.restsecurityserver;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractService {
    private final ContractRepository contractRepository;

    List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

    Contract get(Long id) {
        return contractRepository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    Contract save(@Valid Contract contract) {
        return contractRepository.save(contract);
    }

    public void delete(Long contractId) {
        contractRepository.deleteById(contractId);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    static class NotFoundException extends RuntimeException {
    }
}
