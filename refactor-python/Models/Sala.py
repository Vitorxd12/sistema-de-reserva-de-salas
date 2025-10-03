import datetime


class Sala:
    def __init__(self, idSala):
        self.idSala = idSala
        self.reservas = []

    def reservar(self, novaReserva):
        for reservaExistente in self.reservas:
            if novaReserva.isConflito(reservaExistente):
                raise ValueError("Conflito de reserva detectado")
        self.reservas.append(novaReserva)
        return True

    def cancelar(self, idReserva):
        for reserva in self.reservas:
            if reserva.getIdReserva() == idReserva:
                self.reservas.remove(reserva)
                return True
        raise ValueError("Reserva não encontrada")

    def verReservasFuturas(self):
        return [r for r in self.reservas if r.getDataHoraInico() > datetime.now()]

    def getIdSala(self):
        return self.idSala
    def getReservas(self):
        return self.reservas

    def setIdSala(self, idSala):
        self.idSala = idSala
    def gerarIdReserva(self, sistemaDeReservas):
        return f"RES-{len(self.getReservas()) + 1}{sistemaDeReservas.getNumeroSalas() + 1}"
