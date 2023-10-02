package org.miage.intervenantservice.boundary;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequestMapping(value="/intervenants", produces = MediaType.APPLICATION_JSON_VALUE)
public class IntervenantRepresentation {

    private final IntervenantResource ir;

    public IntervenantRepresentation(IntervenantResource ir) {
        this.ir = ir;
    }
    // GET http://localhost:8082/intervenants
    @GetMapping
    public ResponseEntity<?> getAllIntervenants() {
        return ResponseEntity.ok(ir.findAll());
    }
    
}
