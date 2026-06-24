import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TicketService } from '../../core/domains/ticket/ticket.service';
import { TicketResponse } from '../../core/domains/ticket/ticket.types';
import { ModalSaida } from '../../shared/components/modal-saida/modal-saida';

@Component({
  selector: 'app-saida',
  standalone: true,
  imports: [CommonModule, FormsModule, ModalSaida],
  templateUrl: './saida.html',
  styleUrl: './saida.css',
})
export class Saida implements OnInit {
  veiculos: TicketResponse[] = [];
  resultados: TicketResponse[] = [];
  termoBusca = '';
  buscaFocada = false;

  modalAberto = false;
  veiculoSelecionado: TicketResponse | null = null;

  private readonly ticketService = inject(TicketService);

  ngOnInit(): void {
    this.carregarVeiculos();
  }

  carregarVeiculos(): void {
    this.ticketService.getAll().subscribe({
      next: (data) => {
        this.veiculos = data;
      },
      error: () => {
        this.veiculos = [];
      }
    });
  }

  onBusca(): void {
    const busca = this.termoBusca.trim().toLowerCase();
    if (!busca) {
      this.resultados = [];
      return;
    }

    this.resultados = this.veiculos.filter(
      (v) =>
        v.vehicle.plate.toLowerCase().includes(busca) ||
        v.vehicle.model.toLowerCase().includes(busca)
    );
  }

  selecionarVeiculo(veiculo: TicketResponse): void {
    this.veiculoSelecionado = veiculo;
    this.modalAberto = true;
  }

  fecharModal(): void {
    this.modalAberto = false;
    this.veiculoSelecionado = null;
  }

  onConfirmado(): void {
    this.limparBusca();
    this.carregarVeiculos();
  }

  limparBusca(): void {
    this.termoBusca = '';
    this.resultados = [];
  }

  onBlur(): void {
    // Pequeno delay para permitir clique no resultado antes de perder foco
    setTimeout(() => { this.buscaFocada = false; }, 150);
  }
}