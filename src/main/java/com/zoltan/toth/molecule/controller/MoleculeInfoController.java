package com.zoltan.toth.molecule.controller;

import com.zoltan.toth.molecule.model.api.BasicMoleculeInfo;
import com.zoltan.toth.molecule.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/info")
public class MoleculeInfoController {

    private final InfoService infoService;

    @Autowired
    public MoleculeInfoController(InfoService infoService) {
        this.infoService = infoService;
    }

    @GetMapping("/basic")
    public BasicMoleculeInfo calculate(@Valid @NotBlank @RequestParam(value = "molecule") String molecule) {
        return infoService.getBasicInfo(molecule);
    }

}
