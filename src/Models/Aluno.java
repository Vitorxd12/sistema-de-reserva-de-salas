package Models;

import java.time.LocalDateTime;

public class Aluno extends Usuario{
    public Aluno(String nome, String email, String id) {
        super(nome, email, id);
    }

    @Override
    public boolean podeReservarComAntecedencia(LocalDateTime data) {
        return data.isBefore(LocalDateTime.now().plusHours(24));
    }
}
