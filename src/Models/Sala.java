package Models;

import Interfaces.Reservavel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Sala implements Reservavel {
    private String id;
    private List<Reserva> reservas;

    public Sala(String id) {
        this.id = id;
        this.reservas = new ArrayList<>();
    }
    public boolean reservar(Reserva r){
        for(Reserva rr : reservas){
            if(r.isConflito(rr)){
               throw new IllegalArgumentException("Conflito de reserva: " + r.getIdReserva() + " com " + rr.getIdReserva());
            }
        }
        reservas.add(r);
        return true; // Reserva bem-sucedida
    }
    public boolean cancelar(String idReserva) {
        boolean removido = reservas.removeIf(reserva -> reserva.getIdReserva().equals(idReserva));
        if (!removido) {
            System.out.println("Reserva com ID " + idReserva + " não encontrada.");
        }
        return removido;
    }

    public List<Reserva> verReservasFuturas(){
        return reservas.stream()
                .filter(r -> r.getDataHoraInicio().isAfter(java.time.LocalDateTime.now()))
                .collect(Collectors.toList());
    }

    public String getIdSala() {
        return id;
    }
    public void setIdSala(String id) {
        this.id = id;
    }

    public List<Reserva> getReservas(){
        return new ArrayList<>(reservas);
    }
    public String gerarIdReserva(SistemaDeReservas sistemaDeReservas) {
        return "RES-" + reservas.size() + sistemaDeReservas.getNumeroSalas() + 1;
    }
}
