
package com.example.demo.facade;

import com.example.demo.dto.EsameDto;
import com.example.demo.entity.Esame;
import com.example.demo.entity.Materia;
import com.example.demo.entity.Studente;
import com.example.demo.service.EsameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EsameFacade {

    private final EsameService esameService;

    public List<EsameDto> getAllEsami() {
        return esameService.getEsami().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    public EsameDto getEsame(Long id) {
        return esameService.getEsameById(id)
            .map(this::toDto)
            .orElse(null);
    }

    public EsameDto creaEsame(EsameDto dto) {
        Esame esame = toEntity(dto);
        return toDto(esameService.salvaEsame(esame));
    }

    private EsameDto toDto(Esame e) {
        return EsameDto.builder()
            .id(e.getId())
            .data(e.getData())
            .idStudente(e.getStudente().getId())
            .idMateria(e.getMateria().getId())
            .voto(e.getVoto())
            .build();
    }

    private Esame toEntity(EsameDto dto) {
        Studente s = new Studente(); s.setId(dto.getIdStudente());
        Materia m = new Materia(); m.setId(dto.getIdMateria());

        return Esame.builder()
            .id(dto.getId())
            .data(dto.getData())
            .studente(s)
            .materia(m)
            .voto(dto.getVoto())
            .build();
    }
}
