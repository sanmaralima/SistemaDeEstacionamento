import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EstacionamentoService } from '../../core/services/estacionamento';
import { VeiculoEstacionado } from '../../core/models/veiculo-estacionado';
import { Vaga } from '../../core/models/vaga';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class Dashboard implements OnInit {
  veiculos: VeiculoEstacionado[] = [];
  vagasDisponiveis: Vaga[] = [];

  termoBusca = '';

  vagasLivres = 0;
  vagasOcupadas = 0;
  totalVeiculosDia = 0;
  faturamentoDiario = 0;

  modalEntradaAberto = false;
  modalSaidaAberto = false;

  veiculoSelecionado: VeiculoEstacionado | null = null;

  novaEntrada = {
    placa: '',
    modelo: '',
    cor: '',
    vaga: 0
  };

  constructor(private estacionamentoService: EstacionamentoService) {}

  ngOnInit(): void {
    this.carregarDados();
  }

  formatarDataAtual(): string {
    const data = new Date();

    return data.toLocaleDateString('pt-BR', {
      day: '2-digit',
      month: 'long',
      year: 'numeric'
    });
  }

  carregarDados(): void {
    this.veiculos = this.estacionamentoService.listarVeiculos();

    this.vagasLivres = this.estacionamentoService.getVagasLivres();
    this.vagasOcupadas = this.estacionamentoService.getVagasOcupadas();
    this.totalVeiculosDia = this.estacionamentoService.getTotalVeiculosHoje();
    this.faturamentoDiario = this.estacionamentoService.getFaturamentoDiario();

    this.vagasDisponiveis = this.estacionamentoService
      .listarVagas()
      .filter(vaga => vaga.status === 'Livre');
  }

  get veiculosFiltrados(): VeiculoEstacionado[] {
    const busca = this.termoBusca.trim().toLowerCase();

    if (!busca) {
      return this.veiculos;
    }

    return this.veiculos.filter(veiculo =>
      veiculo.placa.toLowerCase().includes(busca) ||
      veiculo.modelo.toLowerCase().includes(busca)
    );
  }

  abrirModalEntrada(): void {
    this.limparFormularioEntrada();
    this.modalEntradaAberto = true;
  }

  fecharModalEntrada(): void {
    this.modalEntradaAberto = false;
  }

  confirmarEntrada(): void {
    if (
      !this.novaEntrada.placa.trim() ||
      !this.novaEntrada.modelo.trim() ||
      !this.novaEntrada.cor.trim() ||
      !this.novaEntrada.vaga
    ) {
      return;
    }

    this.estacionamentoService.registrarEntrada({
      placa: this.novaEntrada.placa.toUpperCase(),
      modelo: this.novaEntrada.modelo,
      cor: this.novaEntrada.cor,
      vaga: Number(this.novaEntrada.vaga)
    });

    this.fecharModalEntrada();
    this.carregarDados();
  }

  limparFormularioEntrada(): void {
    this.novaEntrada = {
      placa: '',
      modelo: '',
      cor: '',
      vaga: 0
    };
  }

  abrirModalSaida(veiculo: VeiculoEstacionado): void {
    this.veiculoSelecionado = veiculo;
    this.modalSaidaAberto = true;
  }

  fecharModalSaida(): void {
    this.veiculoSelecionado = null;
    this.modalSaidaAberto = false;
  }

  registrarSaida(): void {
    if (!this.veiculoSelecionado) {
      return;
    }

    this.estacionamentoService.registrarSaida(this.veiculoSelecionado.id);
    this.fecharModalSaida();
    this.carregarDados();
  }

  formatarHorario(data: Date): string {
    return new Date(data).toLocaleTimeString('pt-BR', {
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  calcularTempo(dataEntrada: Date): string {
    const entrada = new Date(dataEntrada).getTime();
    const agora = new Date().getTime();

    const diferencaMinutos = Math.floor((agora - entrada) / 1000 / 60);

    const horas = Math.floor(diferencaMinutos / 60);
    const minutos = diferencaMinutos % 60;

    return `${horas}h ${minutos}m`;
  }

  calcularValor(dataEntrada: Date): number {
    return this.estacionamentoService.calcularValor(dataEntrada);
  }
}