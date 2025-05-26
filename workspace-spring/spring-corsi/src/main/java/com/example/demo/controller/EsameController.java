
package com.example.demo.controller;

import com.example.demo.dto.EsameDto;
import com.example.demo.facade.EsameFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class EsameController {

    private final EsameFacade esameFacade;

    @GetMapping("/esami")
    public String listaEsami(Model model) {
        model.addAttribute("esami", esameFacade.getAllEsami());
        return "esami/lista";
    }

    @GetMapping("/esami/nuovo")
    public String nuovoEsameForm(Model model) {
        model.addAttribute("esame", new EsameDto());
        return "esami/form";
    }

    @PostMapping("/esami")
    public String salvaEsame(@ModelAttribute EsameDto esame) {
        esameFacade.creaEsame(esame);
        return "redirect:/esami";
    }
}
