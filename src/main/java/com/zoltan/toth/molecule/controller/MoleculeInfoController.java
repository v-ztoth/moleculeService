package com.zoltan.toth.molecule.controller;

import com.zoltan.toth.molecule.model.api.BasicMoleculeInfo;
import com.zoltan.toth.molecule.service.BasicMoleculeInfoRetriever;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/info")
public class MoleculeInfoController {

    private final BasicMoleculeInfoRetriever basicMoleculeInfoRetriever;

    @Autowired
    public MoleculeInfoController(BasicMoleculeInfoRetriever basicMoleculeInfoRetriever) {
        this.basicMoleculeInfoRetriever = basicMoleculeInfoRetriever;
    }

    @GetMapping("/basic")
    public BasicMoleculeInfo calculate(@RequestParam(value = "molecule") String name) {
        return new BasicMoleculeInfo();
    }

}
