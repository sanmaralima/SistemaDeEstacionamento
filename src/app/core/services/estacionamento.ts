import { Injectable } from '@angular/core';
import { VeiculoEstacionado } from '../models/veiculo-estacionado';
import { Vaga } from '../models/vaga';
import { RegistroEstacionamento } from '../models/registro-estacionamento';

@Injectable({
  providedIn: 'root'
})
export class EstacionamentoService {
  private totalVagas = 50;
  private valorHora = 8;

  private registros: RegistroEstacionamento[] = [
    {
      id: 1,
      placa: 'ABC-1234',
      modelo: 'Honda Civic',
      cor: 'Preto',
      vaga: 12,
      entrada: new Date(new Date().setHours(8, 30, 0, 0)),
      status: 'Aberto'
    },
    {
      id: 2,
      placa: 'XYZ-5678',
      modelo: 'Toyota Corolla',
      cor: 'Branco',
      vaga: 5,
      entrada: new Date(new Date().setHours(9, 15, 0, 0)),
      status: 'Aberto'
    },
    {
      id: 3,
      placa: 'DEF-9012',
      modelo: 'Volkswagen Gol',
      cor: 'Prata',
      vaga: 23,
      entrada: new Date(new Date().setHours(10, 0, 0, 0)),
      status: 'Aberto'
    },
    {
      id: 4,
      placa: 'GHI-3456',
      modelo: 'Fiat Argo',
      cor: 'Vermelho',
      vaga: 8,
      entrada: new Date(new Date().setHours(7, 0, 0, 0)),
      saida: new Date(new Date().setHours(9, 20, 0, 0)),
      tempoTotalMinutos: 140,
      valorTotal: 24,
      status: 'Finalizado'
    }
  ];

  listarVeiculos(): VeiculoEstacionado[] {
    return this.registros
      .filter(registro => registro.status === 'Aberto')
      .map(registro => ({
        id: registro.id,
        placa: registro.placa,
        modelo: registro.modelo,
        cor: registro.cor,
        horarioEntrada: registro.entrada,
        vaga: registro.vaga,
        status: 'Ativo'
      }));
  }

  listarRegistros(): RegistroEstacionamento[] {
    return this.registros;
  }

  listarVagas(): Vaga[] {
    const vagas: Vaga[] = [];

    for (let i = 1; i <= this.totalVagas; i++) {
      const ocupada = this.registros.some(
        registro => registro.vaga === i && registro.status === 'Aberto'
      );

      vagas.push({
        id: i,
        numero: i,
        status: ocupada ? 'Ocupada' : 'Livre'
      });
    }

    return vagas;
  }

  getTotalVagas(): number {
    return this.totalVagas;
  }

  getVagasOcupadas(): number {
    return this.registros.filter(registro => registro.status === 'Aberto').length;
  }

  getVagasLivres(): number {
    return this.totalVagas - this.getVagasOcupadas();
  }

  getTotalVeiculosHoje(): number {
    return this.registros.filter(registro => this.dataHoje(registro.entrada)).length;
  }

  getFaturamentoDiario(): number {
    return this.registros
      .filter(registro => registro.status === 'Finalizado' && registro.saida && this.dataHoje(registro.saida))
      .reduce((total, registro) => total + (registro.valorTotal ?? 0), 0);
  }

  registrarEntrada(novoVeiculo: Omit<VeiculoEstacionado, 'id' | 'horarioEntrada' | 'status'>): void {
    const registro: RegistroEstacionamento = {
      id: this.gerarNovoId(),
      placa: novoVeiculo.placa,
      modelo: novoVeiculo.modelo,
      cor: novoVeiculo.cor,
      vaga: novoVeiculo.vaga,
      entrada: new Date(),
      status: 'Aberto'
    };

    this.registros.push(registro);
  }

  registrarSaida(id: number): void {
    const registro = this.registros.find(item => item.id === id && item.status === 'Aberto');

    if (!registro) {
      return;
    }

    const saida = new Date();
    const tempoTotalMinutos = this.calcularTempoTotalMinutos(registro.entrada, saida);
    const valorTotal = this.calcularValorPorMinutos(tempoTotalMinutos);

    registro.saida = saida;
    registro.tempoTotalMinutos = tempoTotalMinutos;
    registro.valorTotal = valorTotal;
    registro.status = 'Finalizado';
  }

  calcularValor(dataEntrada: Date): number {
    const tempoTotalMinutos = this.calcularTempoTotalMinutos(dataEntrada, new Date());
    return this.calcularValorPorMinutos(tempoTotalMinutos);
  }

  private calcularTempoTotalMinutos(entrada: Date, saida: Date): number {
    const entradaMs = new Date(entrada).getTime();
    const saidaMs = new Date(saida).getTime();

    return Math.max(1, Math.floor((saidaMs - entradaMs) / 1000 / 60));
  }

  private calcularValorPorMinutos(minutos: number): number {
    const horasCobradas = Math.ceil(minutos / 60);
    return horasCobradas * this.valorHora;
  }

  private gerarNovoId(): number {
    if (this.registros.length === 0) {
      return 1;
    }

    return Math.max(...this.registros.map(registro => registro.id)) + 1;
  }

  private dataHoje(data: Date): boolean {
    const hoje = new Date();
    const dataComparada = new Date(data);

    return (
      dataComparada.getDate() === hoje.getDate() &&
      dataComparada.getMonth() === hoje.getMonth() &&
      dataComparada.getFullYear() === hoje.getFullYear()
    );
  }
}