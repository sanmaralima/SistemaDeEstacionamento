import { Component, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { useTicketsQuery } from '../../core/domains/ticket/ticket.hooks';
import { TicketResponse } from '../../core/domains/ticket/ticket.types';
import { ModalExit } from '../../shared/components/modal-exit/modal-exit.component';
import { SpotAssignmentService } from '../../shared/services/spot-assignment.service';

interface GridSpot {
  number: number;
  ticket: TicketResponse | null;
  status: 'Livre' | 'Ocupada';
}

@Component({
  selector: 'app-parking-spots',
  standalone: true,
  imports: [CommonModule, ModalExit],
  templateUrl: './parking-spots.component.html',
  styleUrl: './parking-spots.component.css',
})
export class ParkingSpots {
  private readonly spotAssignmentService = inject(SpotAssignmentService);

  // Queries
  protected readonly ticketsQuery = useTicketsQuery();

  // Constantes
  readonly totalSpots = 120;

  // Modal checkout
  readonly modalOpen = signal(false);
  readonly selectedTicket = signal<TicketResponse | null>(null);

  // Mapeamento dinâmico das vagas de 1 a 120
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

  protected handleSpotClick(spot: GridSpot): void {
    if (spot.ticket) {
      this.selectedTicket.set(spot.ticket);
      this.modalOpen.set(true);
    }
  }

  protected closeCheckoutModal(): void {
    this.modalOpen.set(false);
    this.selectedTicket.set(null);
  }

  protected handleCheckoutConfirmed(): void {
    this.ticketsQuery.refetch();
  }
}
