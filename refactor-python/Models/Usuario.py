import re
from abc import ABC, abstractmethod

class Usuario(ABC):
    def __init__(self, nome, id, email):
        self.nome = nome
        self.id = id
        self.email = self.verificarEmail(email)

    @abstractmethod
    def podeReservarComAntecedencia(self, dataHoraReserva):
        pass

    def verificarEmail(self, email):
        regex = r"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$"
        if re.match(regex, email):
            return email
        else:
            raise ValueError("Email inválido")

    def toString(self):
        retorno = f"Nome: {self.nome}\nID: {self.id}\nEmail: {self.email}"
        print(retorno)
        return retorno

    def getNome(self):
        return self.nome
    def getId(self):
        return self.id
    def getEmail(self):
        return self.email

    def setNome(self, nome):
        self.nome = nome
    def setId(self, id):
        self.id = id
    def setEmail(self, email):
        self.email = self.verificarEmail(email)