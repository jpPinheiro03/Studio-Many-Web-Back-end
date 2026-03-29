package com.studio.core.validator;

import com.studio.core.exception.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class CpfValidator {

    public void validar(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            return;
        }

        cpf = cpf.replaceAll("[^\\d]", "");

        if (cpf.length() != 11) {
            throw new BadRequestException("CPF deve conter 11 dígitos");
        }

        if (cpf.matches("(\\d)\\1{10}")) {
            throw new BadRequestException("CPF inválido");
        }

        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += (cpf.charAt(i) - '0') * (10 - i);
        }
        int resto = soma % 11;
        int digito1 = resto < 2 ? 0 : 11 - resto;

        if ((cpf.charAt(9) - '0') != digito1) {
            throw new BadRequestException("CPF inválido");
        }

        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += (cpf.charAt(i) - '0') * (11 - i);
        }
        resto = soma % 11;
        int digito2 = resto < 2 ? 0 : 11 - resto;

        if ((cpf.charAt(10) - '0') != digito2) {
            throw new BadRequestException("CPF inválido");
        }
    }
}
