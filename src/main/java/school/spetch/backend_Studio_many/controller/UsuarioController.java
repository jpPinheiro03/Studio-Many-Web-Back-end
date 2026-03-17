package school.spetch.backend_Studio_many.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.spetch.backend_Studio_many.dto.UsuarioMapper;
import school.spetch.backend_Studio_many.dto.UsuarioRequestDto;
import school.spetch.backend_Studio_many.dto.UsuarioResponseDto;
import school.spetch.backend_Studio_many.model.Usuario;
import school.spetch.backend_Studio_many.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponseDto> cadastrar(@RequestBody @Valid UsuarioRequestDto dto){
        Usuario u = UsuarioMapper.toModel(dto);

        Usuario uSalvar = usuarioService.cadastrar(u);

        UsuarioResponseDto responseDto = UsuarioMapper.toResponseDto(uSalvar);

        return ResponseEntity.status(201).body(responseDto);
    }
}
