package Models;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SistemaDeReservas {
    private List<Usuario> usuarios;
    private List<Sala> salas;

    public SistemaDeReservas() {
        this.usuarios = new ArrayList<>();
        this.salas = new ArrayList<>();
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public List<Sala> getSalas() {
        return salas;
    }

    public void carregarUsuarios(String caminhoArquivo) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo));
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length == 4) {
                    String tipo = partes[0].trim();
                    String nome = partes[1].trim();
                    String id = partes[2].trim();
                    String email = partes[3].trim();

                    Usuario usuario;
                    if (tipo.equalsIgnoreCase("Aluno")) {
                        usuario = new Aluno(nome, email, id);
                        System.out.println("Aluno carregado: " + usuario.getNome());
                    } else if (tipo.equalsIgnoreCase("Professor")) {
                        usuario = new Professor(nome, email, id);
                        System.out.println("Professor carregado: " + usuario.getNome());
                    } else {
                        throw new IllegalArgumentException("Tipo de usuário desconhecido: " + tipo);
                    }

                    usuarios.add(usuario);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void carregarSalas(String caminhoArquivo) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo));
            String linha;
            while ((linha = br.readLine()) != null) {
                String idSala = linha.trim();
                if (!idSala.isEmpty()) {
                    Sala sala = new Sala(idSala);
                    salas.add(sala);
                    System.out.println("Sala carregada: " + sala.getIdSala());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getNumeroSalas() {
        return  salas.size();
    }

    public void carregarReservas(String caminhoArquivo) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        try {
            BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo));
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length == 5) {
                    String idReserva = partes[0].trim();
                    LocalDateTime dataHoraInicio = LocalDateTime.parse(partes[1].trim(), formatter);
                    LocalDateTime dataHoraFim = LocalDateTime.parse(partes[2].trim(), formatter);
                    String idSala = partes[3].trim();
                    String idUsuario = partes[4].trim();

                    Sala sala = salas.stream()
                            .filter(s -> s.getIdSala().equals(idSala))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Sala não encontrada: " + idSala));

                    Reserva reserva = new Reserva(idReserva, idUsuario, idSala, dataHoraInicio, dataHoraFim);
                    sala.reservar(reserva);
                    System.out.println("Reserva carregada: " + reserva.getIdReserva());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Usuario buscarUsuarioPorId(String id) {
        return usuarios.stream()
                .filter(usuario -> usuario.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Reserva> ListarReservasDoUsuario(String idUsuario) {
        List<Reserva> reservasDoUsuario = new ArrayList<>();
        Usuario usuario = buscarUsuarioPorId(idUsuario);

        if (usuario == null) {
            System.out.println("Usuário não encontrado.");
        }

        System.out.println("Reservas do usuário " + usuario.getNome() + ":");
        for (Sala sala : salas) {
            List<Reserva> reservas = sala.getReservas();
            for (Reserva reserva : reservas) {
                if (reserva.getIdUsuario().equals(idUsuario)) {
                    System.out.println(reserva);
                    reservasDoUsuario.add(reserva);
                }
            }
        }
        return reservasDoUsuario;
    }

    //buscar reseervas  por periodo usando lambda
    public List<Reserva> bucarReservasPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        List<Reserva> reservasNoPeriodo = new ArrayList<>();
        for (Sala sala : salas) {
            List<Reserva> reservas = sala.getReservas();
            for (Reserva reserva : reservas) {
                if ((reserva.getDataHoraInicio().isAfter(dataInicio) || reserva.getDataHoraInicio().isEqual(dataInicio)) &&
                        (reserva.getDataHoraFim().isBefore(dataFim) || reserva.getDataHoraFim().isEqual(dataFim))) {
                    reservasNoPeriodo.add(reserva);
                }
            }
        }
        return reservasNoPeriodo;
    }

    public void cancelarReserva(String idReserva, Usuario solicitante) {
        for (Sala sala : salas) {
            List<Reserva> reservas = sala.getReservas();
            for (Reserva reserva : reservas) {
                if (reserva.getIdReserva().equals(idReserva)) {
                    if (reserva.getIdUsuario().equals(solicitante.getId()) || solicitante instanceof Professor) {
                        sala.cancelar(idReserva);
                        System.out.println("Reserva " + idReserva + " cancelada com sucesso.");
                        return;
                    } else {
                        System.out.println("Usuário não tem permissão para cancelar esta reserva.");
                        return;
                    }
                }
            }
        }
    }
    //gerar relataorio
    public String gerarRelatorio() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        StringBuilder relatorio = new StringBuilder();
        relatorio.append("=== Relatório Detalhado de Reservas ===\n");
        for (Sala sala : salas) {
            relatorio.append("Sala ID: ").append(sala.getIdSala()).append("\n");
            List<Reserva> reservas = sala.getReservas();
            if (reservas.isEmpty()) {
                relatorio.append("Nenhuma reserva feita.\n");
            } else {
                relatorio.append("Total de Reservas: ").append(reservas.size()).append("\n");
                for (Reserva reserva : reservas) {
                    Usuario usuario = buscarUsuarioPorId(reserva.getIdUsuario());
                    relatorio.append("Reserva ID: ").append(reserva.getIdReserva()).append("\n")
                            .append("Usuário: ").append(usuario != null ? usuario.getNome() : "Desconhecido").append("\n")
                            .append("Início: ").append(reserva.getDataHoraInicio().format(formatter)).append("\n")
                            .append("Fim: ").append(reserva.getDataHoraFim().format(formatter)).append("\n")
                            .append("-----------------------------\n");
                }
            }
        }
        return relatorio.toString();
    }
}

