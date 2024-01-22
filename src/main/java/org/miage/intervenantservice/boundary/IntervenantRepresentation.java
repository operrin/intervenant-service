package org.miage.intervenantservice.boundary;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.transaction.Transactional;

import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.miage.intervenantservice.entity.Intervenant;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Controller
@ResponseBody
@RequestMapping(value = "intervenants", produces = MediaType.APPLICATION_JSON_VALUE)
public class IntervenantRepresentation {

    private final IntervenantRepository ir;

    public IntervenantRepresentation(IntervenantRepository ir) {
        this.ir = ir;
    }

    // @GetMapping
    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(ir.findAll());
    }

    // get one
    @GetMapping("/{intervenantId}")
    public ResponseEntity<?> getOneIntervenant(@PathVariable("intervenantId") String id) {
        return Optional.of(ir.findById(id))
                .filter(Optional::isPresent)
                .map(i -> ResponseEntity.ok(i.get()))
                .orElse(ResponseEntity.notFound().build());
    }

    // POST
    @PostMapping()
    @Transactional
    public ResponseEntity<?> postIntervenant(@RequestBody Intervenant entity) {
        Intervenant toSave= new Intervenant(UUID.randomUUID().toString(),
                entity.getNom(),
                entity.getPrenom(),
                entity.getCommune(),
                entity.getCodepostal());
        Intervenant saved = ir.save(toSave);
        URI location = linkTo(IntervenantRepresentation.class).slash(saved.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    // PUT
    @PutMapping(value = "/{intervenantId}")
    @Transactional
    public ResponseEntity<?> update(@PathVariable("intervenantId") String id,
            @RequestBody Intervenant newIntervenant) {

        if (!ir.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Intervenant toSave = new Intervenant(id,
                newIntervenant.getNom(),
                newIntervenant.getPrenom(),
                newIntervenant.getCommune(),
                newIntervenant.getCodepostal());
        ir.save(toSave);
        return ResponseEntity.ok().build();
    }

    // DELETE
    @DeleteMapping(value = "/{intervenantId}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable("intervenantId") String id) {
        Optional<Intervenant> intervenant = ir.findById(id);
        intervenant.ifPresent(ir::delete);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/{intervenantId}")
    @Transactional
    public ResponseEntity<?> patch(@PathVariable("intervenantId") String id,
    @RequestBody Map<String,Object> changes) {

        Optional<Intervenant> body = ir.findById(id);
        if (body.isPresent()) {
            Intervenant existingIntervenant = body.get();
            changes.forEach(
                (attribute, value) -> {
                switch (attribute) {
                    case "nom" -> existingIntervenant.setNom((String) value);
                    case "prenom" -> existingIntervenant.setPrenom((String) value);
                    case "commune" ->  existingIntervenant.setCommune((String) value);
                    case "codepostal" -> existingIntervenant.setCodepostal((String) value);
                }
            });
            ir.save(existingIntervenant);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
