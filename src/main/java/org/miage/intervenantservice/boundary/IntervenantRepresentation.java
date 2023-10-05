package org.miage.intervenantservice.boundary;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;
import org.miage.intervenantservice.entity.Intervenant;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.transaction.Transactional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

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
    
    // GET one
    @GetMapping(value="/{intervenantId}")
    public ResponseEntity<?> getIntervenant(@PathVariable("intervenantId") String id) {
        return Optional.of(ir.findById(id))
            .filter(Optional::isPresent)
            .map(i -> ResponseEntity.ok(i.get()))
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> postIntervenant(@RequestBody Intervenant intervenant) {
        Intervenant toSave = new Intervenant(UUID.randomUUID().toString(), 
        intervenant.getNom(), 
        intervenant.getPrenom(), 
        intervenant.getCommune(), 
        intervenant.getCodepostal());
        Intervenant saved = ir.save(toSave);
        URI uri = linkTo(IntervenantRepresentation.class).slash(saved.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

}
