
package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class EsameDto {
    private Long id;
    private LocalDate data;
    private Long idStudente;
    private Long idMateria;
    private Integer voto;
}
