from Models.Usuario import Usuario


class Professor(Usuario):
    def __init__(self, nome, id, email):
        super().__init__(nome, id, email)

    def podeReservarComAntecedencia(self, dataHoraReserva):
        return True