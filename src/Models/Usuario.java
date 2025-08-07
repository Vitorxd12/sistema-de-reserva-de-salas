package Models;

import java.time.LocalDateTime;

public abstract class Usuario{
    private String nome;
    private String email;
    private String id;

    public Usuario(String nome, String email, String id) {
        this.nome = nome;
        setEmail(email);
        this.id = id;
    }
    public abstract boolean podeReservarComAntecedencia(LocalDateTime data);

    public String toString() {
        String retorno = "nome: " + nome + "\n" +
                         "email: " + email + "\n" +
                         "id: " + id + "\n";
        System.out.println(retorno);
        return retorno;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        this.email = email.matches(regex) ? email : (null);
        if (this.email == null) {
            throw new IllegalArgumentException("Email inválido");
        }
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

}
