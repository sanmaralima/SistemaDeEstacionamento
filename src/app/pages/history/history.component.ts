import { Component, signal, computed, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { useTicketsQuery } from '../../core/domains/ticket/ticket.hooks';
import { TicketResponse } from '../../core/domains/ticket/ticket.types';
import { SpotAssignmentService } from '../../shared/services/spot-assignment.service';

@Component({
  selector: 'app-history',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './history.component.html',
  styleUrl: './history.component.css',
})
export class History {
  private readonly spotAssignmentService = inject(SpotAssignmentService);

  // Queries
  protected readonly ticketsQuery = useTicketsQuery();

  protected getSpotNumber(ticket: TicketResponse): number {
    return this.spotAssignmentService.getSpot(ticket);
  }

  // Filtros locais (Signals)
  readonly searchTerm = signal('');
  readonly filterDate = signal('');
  readonly filterStatus = signal(''); // '', 'active', 'finished'

  // Modal visualização detalhes (opcional)
  readonly selectedTicket = signal<TicketResponse | null>(null);
  readonly detailsOpen = signal(false);

  // Lista de registros filtrada e calculada
  protected readonly filteredTickets = computed(() => {
    const rawTickets = this.ticketsQuery.data() || [];
    const term = this.searchTerm().trim().toLowerCase();
    const dateVal = this.filterDate();
    const statusVal = this.filterStatus();

    return rawTickets.filter((t) => {
      // 1. Filtro termo busca (placa ou modelo)
      const matchesSearch =
        !term ||
        t.vehicle.plate.toLowerCase().includes(term) ||
        t.vehicle.model.toLowerCase().includes(term);

      // 2. Filtro de data
      let matchesDate = true;
      if (dateVal) {
        const ticketDate = new Date(t.enteredAt).toISOString().split('T')[0];
        matchesDate = ticketDate === dateVal;
      }

      // 3. Filtro de status
      let matchesStatus = true;
      const isFinished = !!t.exitedAt;
      if (statusVal === 'active') {
        matchesStatus = !isFinished;
      } else if (statusVal === 'finished') {
        matchesStatus = isFinished;
      }

      return matchesSearch && matchesDate && matchesStatus;
    });
  });

  protected calculateDuration(enteredAt: string, exitedAt?: string): string {
    const start = new Date(enteredAt).getTime();
    const end = exitedAt ? new Date(exitedAt).getTime() : new Date().getTime();
    
    const diffMin = Math.max(1, Math.floor((end - start) / 60000));
    const h = Math.floor(diffMin / 60);
    const m = diffMin % 60;
    
    return `${h}h ${m}m`;
  }

  protected openDetails(ticket: TicketResponse): void {
    this.selectedTicket.set(ticket);
    this.detailsOpen.set(true);
  }

  protected closeDetails(): void {
    this.detailsOpen.set(false);
    this.selectedTicket.set(null);
  }
}
