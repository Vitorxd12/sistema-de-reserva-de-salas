package Models;

import java.time.LocalDateTime;

public class Professor extends Usuario {

    public Professor(String nome, String email, String id) {
        super(nome, email, id);
    }

    @Override
    public boolean podeReservarComAntecedencia(LocalDateTime data) {
        return true;
    }

}
