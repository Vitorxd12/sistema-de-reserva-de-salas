from datetime import datetime

from Models.Aluno import Aluno
from Models.Professor import Professor
from Models.Reserva import Reserva
from Models.Sala import Sala


class SistemaDeReservas:
    def __init__(self):
        self.usuarios = []
        self.salas = []

    def carregarUsuarios(self, caminho):
            try:
                with open(caminho, 'r', encoding='utf-8') as file:
                    for linha in file:
                        linha = linha.strip()
                        if not linha:
                            continue
                        partes = linha.split(';')
                        if len(partes) == 4:
                            tipo = partes[0].strip()
                            nome = partes[1].strip()
                            id = partes[2].strip()
                            email = partes[3].strip()
                            if tipo.lower() == 'aluno':
                                usuario = Aluno(nome, id, email)
                            elif tipo.lower() == 'professor':
                                usuario = Professor(nome, id, email)
                            else:
                                print(f"Tipo de usuário desconhecido: {tipo}")
                                continue
                            self.usuarios.append(usuario)
            except IOError as e:
                print(f"Arquivo de usuários não encontrado.{e}")

    def carregarSalas(self, caminho):
        try:
            with open(caminho, 'r', encoding='utf-8') as file:
                for linha in file:
                    idSala = linha.strip()
                    if idSala != '':
                        sala = Sala(idSala)
                        self.salas.append(sala)
                        print (f"Sala {idSala} carregada com sucesso.")
        except IOError as e:
            print(f"Arquivo de salas não encontrado.{e}")


    def carregarReservas(self, caminho):
        formato_data = "%d/%m/%Y %H:%M"
        try:
            with open(caminho, 'r', encoding='utf-8') as arquivo:
                for linha in arquivo:
                    linha = linha.strip()
                    if not linha:
                        continue
                    partes = linha.split(';')
                    if len(partes) == 5:
                        idReserva = partes[0].strip()
                        dataInicio = partes[1].strip()
                        dataFim = partes[2].strip()
                        idSala = partes[3].strip()
                        idUsuario = partes[4].strip()

                        dataHoraInicio = datetime.strptime(dataInicio.strip(), "%d/%m/%Y %H:%M")
                        dataHoraFim = datetime.strptime(dataFim.strip(), "%d/%m/%Y %H:%M")
                        sala = next((s for s in self.salas if s.getIdSala() == idSala), None)
                        if not sala:
                            raise ValueError(f"Sala com ID {idSala} não encontrada")
                        reserva = Reserva(idReserva, idUsuario, idSala, dataHoraInicio, dataHoraFim)
                        sala.reservar(reserva)
                        print(f'Reserva {idReserva} carregada com sucesso.')
        except IOError as e:
            print(f"Arquivo de reservas não encontrado.{e}")

    def buscarUsuarioPorId(self, id):
        for usuario in self.usuarios:
            if usuario.getId() == id:
                return usuario
        return None

    def listarReservasDoUsuario(self, idUsuario):
        reservasDoUsuario = []
        usuario = self.buscarUsuarioPorId(idUsuario)
        if usuario == None:
            print("Usuário não encontrado")
        for sala in self.salas:
            for reserva in sala.getReservas():
                if reserva.getIdUsuario() == idUsuario:
                    reservasDoUsuario.append(reserva)
        return reservasDoUsuario

    def buscarReservasPorPeriodo(self,  dataInicio, dataFim):
        reservasNoPeriodo = []
        for sala in self.salas:
            for reserva in sala.getReservas():
                if reserva.getDataHoraInico() >= dataInicio and reserva.getDataHoraFim() <= dataFim:
                    reservasNoPeriodo.append(reserva)
        return reservasNoPeriodo

    def cancelarReserva(self, idReserva, solicitante):
        for sala in self.salas:
            for reserva in sala.reservas:
                if reserva.getIdUsuario() == solicitante.getId() or isinstance(solicitante, Professor):
                    if reserva.getIdReserva() == idReserva:
                        sala.cancelar(idReserva)
                        print("Reserva cancelaada com sucesso")
                        return True
                    else:
                        print("O usuario não tem permissão para cancelar esta reserva, ou a reserva não existe")
                        return False

    def gerarRelatorio(self, salas):
        formatter = "%d/%m/%Y %H:%M"
        relatorio = ["=== Relatório Detalhado de Reservas ===\n"]

        for sala in salas:
            relatorio.append(f"Sala ID: {sala.getIdSala()}")
            reservas = sala.getReservas()
            if not reservas:
                relatorio.append("Nenhuma reserva feita.\n")
            else:
                relatorio.append(f"Total de Reservas: {len(reservas)}")
                for reserva in reservas:
                    usuario = self.buscarUsuarioPorId(reserva.getIdUsuario())
                    nome_usuario = usuario.nome if usuario else "Desconhecido"
                    relatorio.append(
                        f"Reserva ID: {reserva.id_reserva}\n"
                        f"Usuário: {nome_usuario}\n"
                        f"Início: {reserva.inicio.strftime(formatter)}\n"
                        f"Fim: {reserva.fim.strftime(formatter)}\n"
                        "-----------------------------"
                    )

        return "\n".join(relatorio)

    def getNumeroSalas(self):
        return len(self.salas)

    def getUsuarios(self):
        return self.usuarios

    def getSalas(self):
        return self.salas