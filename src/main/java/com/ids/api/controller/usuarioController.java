package com.ids.api.controller;

import com.ids.api.exception.ResourceNotFoundException;
import com.ids.api.models.Usuarios;
import com.ids.api.repository.usuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v1")
public class usuarioController {
    @Autowired
    private usuarioRepository _usuarioRepository;

    @GetMapping("/usuarios")
    public List<Usuarios> getAllUsuarios(@RequestHeader("Authorization") String auth){
        System.out.println("URI: /usuarios ");

        System.out.println(auth + " = Basic dXNlcnM6cGFzc3dk");
        if(auth.equals("Basic dXNlcnM6cGFzc3dk")){
            System.out.println("Credenciales correctas");
        }

        return _usuarioRepository.findAll();
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<Usuarios> getEmployeeById(
            @PathVariable(value = "id") Integer _id,
            @RequestHeader("Authorization") String auth)
            throws ResourceNotFoundException {
        Usuarios usuario = _usuarioRepository.findById(_id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + _id));

        if(auth.equals("Basic "+usuario.getPassword())){
            System.out.println("Credenciales correctas");
        }
        return ResponseEntity.ok().body(usuario);
    }

    @PostMapping("/usuarios")
    public ResponseEntity<Usuarios> createUsuarios(
            @RequestBody Usuarios usuario,
            @RequestHeader("Authorization") String auth){
        if(auth.equals("Basic "+usuario.getPassword())){
            final Usuarios saveUsuario = _usuarioRepository.save(usuario);
            return ResponseEntity.ok(saveUsuario);
        }

        return (ResponseEntity<Usuarios>) ResponseEntity.status(401);
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<Usuarios> updateUsuarios(
            @PathVariable(value = "id") Integer _id,
            @RequestBody Usuarios usuarioDetails,
            @RequestHeader("Authorization") String auth)
        throws ResourceNotFoundException{

        Usuarios usuario = _usuarioRepository.findById(_id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + _id));

        if(auth.equals("Basic "+usuario.getPassword())){
            usuario.setUsuario(usuarioDetails.getUsuario());
            final Usuarios updateUsuario = _usuarioRepository.save(usuario);
            return ResponseEntity.ok(updateUsuario);
        }
        return (ResponseEntity<Usuarios>) ResponseEntity.status(401);
    }

    @DeleteMapping("/usuarios/{id}")
    public Map<String, Boolean> deleteUsuario(@PathVariable(value = "id") Integer _id)
        throws ResourceNotFoundException{
        Usuarios usuario = _usuarioRepository.findById(_id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + _id));

        _usuarioRepository.delete(usuario);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;

    }
}
