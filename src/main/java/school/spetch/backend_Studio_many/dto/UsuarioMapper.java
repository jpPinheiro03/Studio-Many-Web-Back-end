package school.spetch.backend_Studio_many.dto;


import jakarta.validation.Valid;
import school.spetch.backend_Studio_many.model.Usuario;

public class UsuarioMapper {
    public static UsuarioResponseDto toResponseDto(Usuario usuario){
        if(usuario == null){
            return null;
        }

        UsuarioResponseDto dto = new UsuarioResponseDto();

        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setTelefone(usuario.getTelefone());
        dto.setEmail(usuario.getEmail());
        dto.setSenha(usuario.getSenha());
        dto.setDataCadastro(usuario.getDataCadastro());
        dto.setDataAtualizacao(usuario.getDataAtualizacao());

        return dto;
    }

    public static Usuario toModel(UsuarioRequestDto usuarioRequestDto) {
        Usuario u = new Usuario();

        u.setNome(usuarioRequestDto.getNome());
        u.setEmail(usuarioRequestDto.getEmail());
        u.setTelefone(usuarioRequestDto.getTelefone());
        u.setEmail(usuarioRequestDto.getEmail());
        u.setSenha(usuarioRequestDto.getSenha());
        u.setDataCadastro(usuarioRequestDto.getDataCadastro());
        u.setDataAtualizacao(usuarioRequestDto.getDataAtualizacao());

        return u;
    }
}
