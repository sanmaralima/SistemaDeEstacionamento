import { Component, EventEmitter, Input, OnChanges, Output, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TicketService } from '../../../core/domains/ticket/ticket.service';
import { TicketResponse, PaymentMethod } from '../../../core/domains/ticket/ticket.types';

interface ItemCobranca {
  descricao: string;
  valor: number;
}

interface FormaPagamentoOpcao {
  valor: string;
  label: string;
}

@Component({
  selector: 'app-modal-saida',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './modal-saida.html',
  styleUrl: './modal-saida.css',
})
export class ModalSaida implements OnChanges {
  @Input() veiculo: TicketResponse | null = null;
  @Output() fechar = new EventEmitter<void>();
  @Output() confirmado = new EventEmitter<void>();

  private readonly ticketService = inject(TicketService);

  agora = new Date();
  tempoDecorrido = '';
  itensCobranca: ItemCobranca[] = [];
  valorBase = 0;
  valorFinal = 0;
  convenio = '';
  formaPagamento = '';
  erro = '';

  formasPagamento: FormaPagamentoOpcao[] = [
    { valor: 'DINHEIRO', label: 'Dinheiro' },
    { valor: 'PIX', label: 'PIX' },
    { valor: 'CARD_CREDIT', label: 'Cartão de Crédito' },
    { valor: 'CARD_DEBIT', label: 'Cartão de Débito' },
  ];

  ngOnChanges(): void {
    if (this.veiculo) {
      this.agora = new Date();
      this.convenio = '';
      this.formaPagamento = '';
      this.erro = '';
      this.calcularTudo();
    }
  }

  calcularValorLocal(entradaStr: string): number {
    const entrada = new Date(entradaStr).getTime();
    const agora = new Date().getTime();
    const diffMs = agora - entrada;
    const diffMin = Math.max(1, Math.floor(diffMs / 60000));
    const horasCobradas = Math.ceil(diffMin / 60);
    return horasCobradas * 8; // R$ 8 por hora
  }

  calcularTudo(): void {
    if (!this.veiculo) return;

    const entrada = new Date(this.veiculo.enteredAt);
    const diffMs = this.agora.getTime() - entrada.getTime();
    const diffMin = Math.floor(diffMs / 60000);
    const h = Math.floor(diffMin / 60);
    const m = diffMin % 60;
    this.tempoDecorrido = `${h}h ${m}m`;

    // Composição da cobrança
    this.itensCobranca = [];
    const horas = Math.ceil(diffMin / 60);

    if (horas >= 8) {
      const dias = Math.ceil(horas / 8);
      this.valorBase = dias * 60;
      this.itensCobranca.push({ descricao: `Diária (8h ou mais)`, valor: this.valorBase });
    } else {
      this.valorBase = this.calcularValorLocal(this.veiculo.enteredAt);
      this.itensCobranca.push({ descricao: `${horas}h de permanência`, valor: this.valorBase });
    }

    this.recalcular();
  }

  recalcular(): void {
    const desconto = this.convenio ? Number(this.convenio) / 100 : 0;
    this.valorFinal = this.valorBase * (1 - desconto);
  }

  confirmar(): void {
    this.erro = '';

    if (!this.formaPagamento) {
      this.erro = 'Selecione a forma de pagamento.';
      return;
    }

    if (!this.veiculo) return;

    this.ticketService.checkOut(this.veiculo.id, this.formaPagamento as PaymentMethod).subscribe({
      next: () => {
        this.confirmado.emit();
        this.fechar.emit();
      },
      error: (err) => {
        this.erro = err?.error?.message || 'Erro ao registrar saída.';
      }
    });
  }
}