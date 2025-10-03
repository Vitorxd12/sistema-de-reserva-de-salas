import datetime

from Models.Usuario import Usuario


class Aluno(Usuario):
    def __init__(self, nome, id, email):
        super().__init__(id, nome, email)

    def podeReservarComAntecedencia(self, dataHoraReserva):
        dataLimite = datetime.now() + datetime.timedelta(hours=24)
        return dataHoraReserva < dataLimite