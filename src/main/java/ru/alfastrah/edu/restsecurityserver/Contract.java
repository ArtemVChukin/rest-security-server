package ru.alfastrah.edu.restsecurityserver;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Data
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotEmpty
    private String number;
    @NotNull
    private LocalDate beginDate;
    @NotNull
    private LocalDate endDate;
    @NotEmpty
    private String department;
}
