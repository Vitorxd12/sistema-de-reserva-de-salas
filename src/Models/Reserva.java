package Models;

import java.time.Duration;
import java.time.LocalDateTime;

public class Reserva{
    private String idReserva;
    private String idUsuario;
    private String idSala;
    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;

    public Reserva(String idReserva, String idUsuario, String idSala, LocalDateTime DataHoraInicio, LocalDateTime DataHoraFim) {
        this.idReserva = idReserva;
        this.idUsuario = idUsuario;
        this.idSala = idSala;
        if (DataHoraInicio.isAfter(DataHoraFim)) {
            throw new IllegalArgumentException("Data de início não pode ser após a data de fim.");
        } else {
            this.dataHoraInicio = DataHoraInicio;
            this.dataHoraFim = DataHoraFim;
        }
    }
    public boolean isConflito(Reserva outraReserva) {
        return this.dataHoraInicio.isBefore(outraReserva.dataHoraFim) && outraReserva.dataHoraInicio.isBefore(this.dataHoraFim);
    }
    public Duration getDuracao() {
        return  Duration.between(this.getDataHoraInicio(), this.getDataHoraFim());
    }

    public String getIdReserva() {
        return idReserva;
    }
    public void setIdReserva(String idReserva) {
        this.idReserva = idReserva;
    }
    public String getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
    public String getIdSala() {
        return idSala;
    }
    public void setIdSala(String idSala) {
        this.idSala = idSala;
    }
    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }
    public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
        if (dataHoraInicio.isAfter(dataHoraFim)) {
            throw new IllegalArgumentException("Data de início não pode ser após a data de fim.");
        }
        this.dataHoraInicio = dataHoraInicio;
    }
    public LocalDateTime getDataHoraFim() {
        return dataHoraFim;
    }
    public void setDataHoraFim(LocalDateTime dataHoraFim) {
        if (dataHoraFim.isBefore(dataHoraInicio)) {
            throw new IllegalArgumentException("Data de fim não pode ser antes da data de início.");
        }
        this.dataHoraFim = dataHoraFim;
    }
}
