
package com.example.demo.service;

import com.example.demo.entity.Esame;
import com.example.demo.repository.EsameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EsameService {

    private final EsameRepository esameRepository;

    public List<Esame> getEsami() {
        return esameRepository.findAll();
    }

    public Optional<Esame> getEsameById(Long id) {
        return esameRepository.findById(id);
    }

    public Esame salvaEsame(Esame esame) {
        return esameRepository.save(esame);
    }

    public void eliminaEsame(Long id) {
        esameRepository.deleteById(id);
    }
}
