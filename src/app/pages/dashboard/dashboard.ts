import { Component, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { useTicketsQuery } from '../../core/domains/ticket/ticket.hooks';
import { useReportQuery } from '../../core/domains/report/report.hooks';
import { useUserProfileQuery } from '../../core/domains/user/user.hooks';
import { TicketResponse } from '../../core/domains/ticket/ticket.types';
import { ModalExit } from '../../shared/components/modal-exit/modal-exit.component';
import { SpotAssignmentService } from '../../shared/services/spot-assignment.service';

interface GridSpot {
  number: number;
  ticket: TicketResponse | null;
  status: 'Livre' | 'Ocupada';
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, ModalExit],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard {
  private readonly router = inject(Router);
  private readonly spotAssignmentService = inject(SpotAssignmentService);

  // Queries
  protected readonly ticketsQuery = useTicketsQuery();
  protected readonly profileQuery = useUserProfileQuery();

  // Signals
  protected readonly companyId = computed(() => this.profileQuery.data()?.companyId || null);
  protected readonly reportQuery = useReportQuery(this.companyId);

  // Constantes
  readonly totalSpots = 120;

  // Modal checkout
  readonly modalSaidaAberto = signal(false);
  readonly veiculoSelecionado = signal<TicketResponse | null>(null);

  // Vagas ocupadas atualmente
  protected readonly occupiedSpotsCount = computed(() => {
    return this.ticketsQuery.data()?.length || 0;
  });

  // Vagas livres atualmente
  protected readonly freeSpotsCount = computed(() => {
    return Math.max(0, this.totalSpots - this.occupiedSpotsCount());
  });

  // Mapeamento dinâmico das vagas de 1 a 120 para o mapa visual
  protected readonly gridSpots = computed<GridSpot[]>(() => {
    const activeTickets = this.ticketsQuery.data() || [];
    this.spotAssignmentService.cleanInactiveTickets(activeTickets);

    const ticketMap = new Map<number, TicketResponse>();
    activeTickets.forEach((t) => {
      const spot = this.spotAssignmentService.getSpot(t);
      if (spot) {
        ticketMap.set(spot, t);
      }
    });

    const spots: GridSpot[] = [];
    for (let i = 1; i <= this.totalSpots; i++) {
      const ticket = ticketMap.get(i) || null;
      spots.push({
        number: i,
        ticket,
        status: ticket ? 'Ocupada' : 'Livre',
      });
    }
    return spots;
  });

  // Detalhes das vagas ocupadas (apenas os tickets ativos)
  protected readonly occupiedTickets = computed(() => {
    return this.ticketsQuery.data() || [];
  });

  protected getSpotNumber(ticket: TicketResponse): number {
    return this.spotAssignmentService.getSpot(ticket);
  }



  protected formatarDataAtual(): string {
    const data = new Date();
    return data.toLocaleDateString('pt-BR', {
      weekday: 'long',
      day: '2-digit',
      month: 'long',
      year: 'numeric',
    });
  }

  protected irParaEntrada(): void {
    this.router.navigate(['/entry']);
  }

  protected abrirModalSaida(ticket: TicketResponse): void {
    this.veiculoSelecionado.set(ticket);
    this.modalSaidaAberto.set(true);
  }

  protected fecharModalSaida(): void {
    this.modalSaidaAberto.set(false);
    this.veiculoSelecionado.set(null);
  }

  protected handleCheckoutConfirmed(): void {
    this.ticketsQuery.refetch();
    if (this.companyId() && this.companyId() !== 'null' && this.companyId() !== 'undefined') {
      this.reportQuery.refetch();
    }
  }
}