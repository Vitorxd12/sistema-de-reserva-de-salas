package Interfaces;

import Models.Reserva;

import java.util.List;

public interface Reservavel {
    public boolean reservar(Reserva r);
    public boolean cancelar(String idReserva);
    public List<Reserva> getReservas();
}
