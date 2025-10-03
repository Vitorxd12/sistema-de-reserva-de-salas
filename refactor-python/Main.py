from datetime import datetime

from Models.Reserva import Reserva
from Models.SistemaDeReservas import SistemaDeReservas

sistemaDeReservas = SistemaDeReservas()
sistemaDeReservas.carregarSalas('documents/salas.txt')
sistemaDeReservas.carregarUsuarios('documents/usuarios.txt')
sistemaDeReservas.carregarReservas('documents/reservas.txt')


def listarSalas():
    print("\n=== Salas Disponíveis ===")
    for sala in sistemaDeReservas.getSalas():
        print(f"Sala ID: {sala.getIdSala()}")
    print("=========================")

def fazerReserva(idUsuario):
    print("\n=== Fazer Reserva ===")
    formato = "%d/%m/%Y %H:%M"
    idSala = str(input("Digite o ID da sala que deseja reservar: "))
    for sala in sistemaDeReservas.getSalas():
        if sala.getIdSala() == idSala:
            print(f"Sala {sala.getIdSala()} encontrada.")
            break
    else:
        print("Sala não encontrada. Tente novamente.")
        return

    dataHoraInicioStr = str(input("Digite a data e hora de início (DD/MM/AAAA HH:MM): "))
    dataHoraFimStr = str(input("Digite a data e hora de fim (DD/MM/AAAA HH:MM): "))
    dataHoraInicio = datetime.strptime(dataHoraInicioStr, formato)
    dataHoraFim = datetime.strptime(dataHoraFimStr, formato)

    if sistemaDeReservas.buscarUsuarioPorId(idUsuario).podeReservarComAntecedencia(dataHoraInicio):
        print("Reserva permitida.")
    else:
        print("Reserva não permitida. Alunos só podem reservar com até 24 horas de antecedência.")
        return
    sala = next((s for s in sistemaDeReservas.getSalas() if s.getIdSala() == idSala), None)
    if not sala:
        print(f"Sala com ID {idSala} não encontrada")
        return
    idReserva = sala.gerarIdReserva(sistemaDeReservas)
    reserva = Reserva(idReserva, idUsuario, idSala, dataHoraInicio, dataHoraFim)
    try:
        sala.reservar(reserva)
        print(f"Reserva {idReserva} feita com sucesso para a sala {idSala} de {dataHoraInicioStr} a {dataHoraFimStr}.")
    except ValueError as e:
        print(f"Erro ao fazer reserva: {e}")

def listarMinhasReservas(idUsuario):
    print("\n=== Listar Minhas Reservas ===")
    reservas = sistemaDeReservas.listarReservasDoUsuario(idUsuario)
    if not reservas:
        print("Você não possui reservas.")
        return
    for reserva in reservas:
        print(f"Reserva ID: {reserva.getIdReserva()}, Sala ID: {reserva.getIdSala()}, Início: {reserva.getDataHoraInicio()}, Fim: {reserva.getDataHoraFim()}")

def cancelarReserva(idUsuario):
    print("\n=== Cancelar Reserva ===")
    idReserva = str(input("Digite o ID da reserva que deseja cancelar: "))
    try:
        sistemaDeReservas.cancelarReserva(idReserva, sistemaDeReservas.buscarUsuarioPorId(idUsuario))
        print(f"Reserva {idReserva} cancelada com sucesso.")
    except ValueError as e:
        print(f"Erro ao cancelar reserva: {e}")

def gerarRelatorio():
    print(sistemaDeReservas.gerarRelatorio())

def fazerLogin(sistemaDeReservas):
    id = str(input("Digite seu ID de usuário: "))
    for usuario in sistemaDeReservas.getUsuarios():
        if usuario.getId() == id:
            print(f"Login bem-sucedido. Bem-vindo, {usuario.getNome()}!")
            menuPrincipal(usuario.getId())
        else:
            print("ID de usuário não encontrado. Tente novamente.")
            fazerLogin(sistemaDeReservas)

def menuPrincipal(idUsuario):
    """
    Menu principal do usuário.
    """
    while True:
        print("\n=== MENU DO USUÁRIO ===")
        print("1. Listar salas disponíveis")
        print("2. Fazer reserva")
        print("3. Listar minhas reservas")
        print("4. Cancelar reserva")
        print("5. Gerar relatório")
        print("6. Logout")
        opcao = input("Escolha uma opção: ").strip()

        if opcao == "1":
            listarSalas()
            pass

        elif opcao == "2":
            fazerReserva(idUsuario)
            pass

        elif opcao == "3":
            listarMinhasReservas(idUsuario)
            pass

        elif opcao == "4":
            cancelarReserva(idUsuario)
            pass

        elif opcao == "5":
            gerarRelatorio()
            pass

        elif opcao == "6":
            print("Logout realizado com sucesso.")
            break

        else:
            # Caso a opção digitada seja inválida
            print("Opção inválida. Tente novamente.")

def menuInicial():
    print("Bem-vindo ao Sistema de Reservas de Salas")
    print("\n\nEscolha uma opção:")
    print("1. Fazer Login")
    print("\n2. Sair")
    opcao = int(input())
    match opcao:
        case 1:
            fazerLogin(sistemaDeReservas)
        case 2:
            print("Saindo do sistema...")
            exit(0)
        case _:
            print("Opção inválida. Tente novamente.")

menuInicial()