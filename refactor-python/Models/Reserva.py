class Reserva:
    def __init__(self, idReserva, idUsuario, idSala, dataHoraInicio, dataHoraFim):
        self.idReserva = idReserva
        self.idUsuario = idUsuario
        self.idSala = idSala
        self.setDaraHoraInicioEFim(dataHoraInicio, dataHoraFim)

    def setDaraHoraInicioEFim(self, dataHoraInicio, dataHoraFim):
        if dataHoraInicio >= dataHoraFim:
            raise ValueError("Data e hora de início devem ser anteriores à data e hora de fim")
        self.dataHoraInicio = dataHoraInicio
        self.dataHoraFim = dataHoraFim

    def isConflito(self, outraReserva):
        return self.getDataHoraInicio() < outraReserva.getDataHoraFim() and outraReserva.getDataHoraInicio() < self.getDataHoraFim()

    def getDuracao(self):
        duracao = self.dataHoraFim - self.dataHoraInicio
        return duracao

    #Getters
    def getIdReserva(self):
        return self.idReserva
    def getIdUsuario(self):
        return self.idUsuario
    def getIdSala(self):
        return self.idSala
    def getDataHoraInicio(self):
        return self.dataHoraInicio
    def getDataHoraFim(self):
        return self.dataHoraFim
    #Setters
    def setIdReserva(self, idReserva):
        self.idReserva = idReserva
    def setIdUsuario(self, idUsuario):
        self.idUsuario = idUsuario
    def setIdSala(self, idSala):
        self.idSala = idSala
    def setDataHoraInicio(self, dataHoraInicio):
        if dataHoraInicio >= self.dataHoraFim:
            raise ValueError("Data e hora de início devem ser anteriores à data e hora de fim")
        self.dataHoraInicio = dataHoraInicio
    def setDataHoraFim(self, dataHoraFim):
        if dataHoraFim <= self.dataHoraInicio:
            raise ValueError("Data e hora de fim devem ser posteriores à data e hora de início")
        self.dataHoraFim = dataHoraFim