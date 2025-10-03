import Models.Professor;
import Models.Reserva;
import Models.Sala;
import Models.SistemaDeReservas;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        SistemaDeReservas sistemaDeReservas = new SistemaDeReservas();
        sistemaDeReservas.carregarSalas("src/Documents/salas.txt");
        sistemaDeReservas.carregarUsuarios("src/Documents/usuarios.txt");
        sistemaDeReservas.carregarReservas("src/Documents/reservas.txt");

        System.out.println("Bem vindo ao Sistema de Reservas!");
        System.out.println("\n\nEscolha uma opção:");
        System.out.println("1. Fazer Login \n2. Sair");
        Scanner sc = new Scanner(System.in);
        String opcao = sc.nextLine();
        switch (opcao) {
            case "1":
                fazerLogin(sistemaDeReservas);
            case "2":
                System.out.println("Saindo do sistema...");
                break;
            default:
                System.out.println("Opção inválida. Tente novamente.");
        }
    }

    public static void fazerLogin(SistemaDeReservas sistemaDeReservas) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Digite seu ID:");
        String id = sc.nextLine();
        if (sistemaDeReservas.getUsuarios().stream().anyMatch(usuario -> usuario.getId().equalsIgnoreCase(id))) {
            System.out.println("Login bem-sucedido!");
            System.out.println("Bem-vindo, " + sistemaDeReservas.buscarUsuarioPorId(id).getNome() + "!");
            menuPrincipal(sistemaDeReservas, id);
        } else {
            System.out.println("ID não encontrado. Tente novamente.");
            fazerLogin(sistemaDeReservas);
        }
    }

    public static void menuPrincipal(SistemaDeReservas sistemaDeReservas, String idUsuario) {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n=== MENU DO USUÁRIO ===");
        System.out.println("1. Listar salas disponíveis");
        System.out.println("2. Fazer reserva");
        System.out.println("3. Listar minhas reservas");
        System.out.println("4. Cancelar reserva");
        System.out.println("5. Gerar relatório");
        System.out.println("6. Logout");
        System.out.print("Escolha uma opção: ");
        String opcao = sc.nextLine();

        switch (opcao) {
            case "1":
                listarSalas(sistemaDeReservas, idUsuario);
                break;
            case "2":
                fazerReserva(sistemaDeReservas, idUsuario);
                break;
            case "3":
                listarMinhasReservas(sistemaDeReservas, idUsuario);
                break;
            case "4":
                cancelarReserva(sistemaDeReservas, idUsuario);
                break;
            case "5":
                gerarRelatorio(sistemaDeReservas, idUsuario);
                break;
            case "6":
                System.out.println("Logout realizado com sucesso.");
                break;
            default:
                System.out.println("Opção inválida. Tente novamente.");
        }
    }

    public static void listarSalas(SistemaDeReservas sistemaDeReservas, String idUsuario) {
        System.out.println("Salas disponíveis:");
        sistemaDeReservas.getSalas().forEach(sala -> System.out.println("Sala ID: " + sala.getIdSala()));
        menuPrincipal(sistemaDeReservas, idUsuario);
    }

    public static void fazerReserva(SistemaDeReservas sistemaDeReservas, String idUsuario) {
        Scanner sc = new Scanner(System.in);
        //sescolher sala
        System.out.println("Digite o ID da sala que deseja reservar:");
        String idSala = sc.nextLine();

        if (sistemaDeReservas.getSalas().stream().noneMatch(sala -> sala.getIdSala().equals(idSala))) {
            System.out.println("Sala não encontrada. Tente novamente.");
            menuPrincipal(sistemaDeReservas, idUsuario);
        }

        //escolher hora de inicio
        System.out.println("Digite a data e hora de início da reserva (dd/MM/yyyy HH:mm):");
        String dataHoraInicioStr = sc.nextLine();

        //Ecolher data fim
        System.out.println("Digite a data e hora de fim da reserva (dd/MM/yyyy HH:mm):");
        String dataHoraFimStr = sc.nextLine();

        //formatar datas
        LocalDateTime dataHoraInicio = LocalDateTime.parse(dataHoraInicioStr, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        LocalDateTime dataHoraFim = LocalDateTime.parse(dataHoraFimStr, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        //Checar se pode reservar com antecedencia
        if (sistemaDeReservas.buscarUsuarioPorId(idUsuario).podeReservarComAntecedencia(dataHoraInicio)) {
            System.out.println("Reserva perimitida.");
        } else {
            System.out.println("Reserva negada - Alunos não podem fazer reserva para dias seguintes.");
            menuPrincipal(sistemaDeReservas, idUsuario);
            return;
        }

        //achar a sala pelo id
        Sala sala = sistemaDeReservas.getSalas().stream()
                .filter(s -> s.getIdSala().equals(idSala))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Sala não encontrada: " + idSala));
        String idReserva = sala.gerarIdReserva(sistemaDeReservas);

        //Criar reserva
        Reserva reserva = new Reserva(idReserva, idUsuario, idSala, dataHoraInicio, dataHoraFim);
        sala.reservar(reserva);
        System.out.println("Reserva realizada com sucesso! ID da reserva: " + idReserva +
                ", Sala: " + idSala +
                ", Início: " + dataHoraInicio +
                ", Fim: " + dataHoraFim);
        menuPrincipal(sistemaDeReservas, idUsuario);
    }

    public static void listarMinhasReservas(SistemaDeReservas sistemaDeReservas, String idUsuario) {
        Scanner sc = new Scanner(System.in);
        List<Reserva> reservasDoUsuario = sistemaDeReservas.ListarReservasDoUsuario(idUsuario);
        if (reservasDoUsuario.isEmpty()) {
            System.out.println("Você não possui reservas.");
        } else {
            System.out.println("Suas reservas:");
            reservasDoUsuario.forEach(reserva -> System.out.println("Reserva ID: " + reserva.getIdReserva() +
                    ", Sala: " + reserva.getIdSala() +
                    ", Início: " + reserva.getDataHoraInicio() +
                    ", Fim: " + reserva.getDataHoraFim()));
        }
        menuPrincipal(sistemaDeReservas, idUsuario);
    }

    public static void cancelarReserva(SistemaDeReservas sistemaDeReservas, String idUsuario) {
        System.out.println("Digite o id da reserva que deseja cancelar:");
        Scanner sc = new Scanner(System.in);
        String idReserva = sc.nextLine();
        boolean reservaEncontrada = false;

        for (Sala sala : sistemaDeReservas.getSalas()) {
            for (Reserva reserva : sala.getReservas()) {
                if (reserva.getIdReserva().equalsIgnoreCase(idReserva)) {
                    if (reserva.getIdUsuario().equals(idUsuario)) {
                        boolean cancelado = sala.cancelar(idReserva);
                        if (cancelado) {
                            System.out.println("Reserva cancelada com sucesso.");
                        } else {
                            System.out.println("Falha ao cancelar a reserva. Tente novamente.");
                        }
                    } else {
                        System.out.println("Você não tem permissão para cancelar esta reserva.");
                    }
                    reservaEncontrada = true;
                    break;
                }
            }
            if (reservaEncontrada) break;
        }

        if (!reservaEncontrada) {
            System.out.println("Reserva não encontrada.");
        }
        menuPrincipal(sistemaDeReservas, idUsuario);
    }

    public static void gerarRelatorio(SistemaDeReservas sistemaDeReservas, String idUsuario) {
        System.out.println(sistemaDeReservas.gerarRelatorio());
        menuPrincipal(sistemaDeReservas, idUsuario);
    }
}
