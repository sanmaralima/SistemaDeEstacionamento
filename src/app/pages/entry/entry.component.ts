import { Component, OnInit, OnDestroy, inject, signal, computed, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { useTicketsQuery, useCheckInMutation } from '../../core/domains/ticket/ticket.hooks';
import { useCreateVehicleMutation } from '../../core/domains/vehicle/vehicle.hooks';
import { useUserProfileQuery } from '../../core/domains/user/user.hooks';
import { ToastService } from '../../shared/services/toast.service';
import { SpotAssignmentService } from '../../shared/services/spot-assignment.service';

interface VagaOption {
  numero: number;
  label: string;
}

import { LoadingDirective } from '../../shared/directives/loading.directive';

@Component({
  selector: 'app-entry',
  standalone: true,
  imports: [CommonModule, FormsModule, LoadingDirective],
  templateUrl: './entry.component.html',
  styleUrl: './entry.component.css',
})
export class Entry implements OnInit, OnDestroy {
  private readonly toastService = inject(ToastService);
  private readonly spotAssignmentService = inject(SpotAssignmentService);

  // Queries
  protected readonly ticketsQuery = useTicketsQuery();
  protected readonly profileQuery = useUserProfileQuery();

  // Signals
  protected readonly companyId = computed(() => this.profileQuery.data()?.companyId || '');

  // Mutações
  protected readonly checkInMutation = useCheckInMutation();
  protected readonly createVehicleMutation = useCreateVehicleMutation(this.companyId);

  protected readonly isConfirming = computed(() => 
    this.createVehicleMutation.isPending() || this.checkInMutation.isPending()
  );

  // Estados locais do relógio
  readonly timeString = signal('');
  readonly dateString = signal('');
  private clockInterval: ReturnType<typeof setInterval> | null = null;

  // Total de vagas do pátio
  readonly totalSpots = 120;

  // Formulário
  readonly plate = signal('');
  readonly modelName = signal('');
  readonly vehicleType = signal('Carro');
  readonly selectedSpot = signal(0);
  readonly isMonthly = signal(false);

  // Vagas ocupadas atualmente
  protected readonly occupiedSpotsCount = computed(() => {
    return this.ticketsQuery.data()?.length || 0;
  });

  // Vagas livres atualmente
  protected readonly freeSpotsCount = computed(() => {
    return Math.max(0, this.totalSpots - this.occupiedSpotsCount());
  });

  // Lista de vagas disponíveis para o select (1 a 120 filtrando as ocupadas)
  protected readonly availableSpots = computed<VagaOption[]>(() => {
    const activeTickets = this.ticketsQuery.data() || [];
    const occupiedNumbers = new Set(activeTickets.map(t => this.spotAssignmentService.getSpot(t)));
    
    const options: VagaOption[] = [];
    for (let i = 1; i <= this.totalSpots; i++) {
      if (!occupiedNumbers.has(i)) {
        options.push({ numero: i, label: `Vaga ${i}` });
      }
    }
    return options;
  });

  ngOnInit(): void {
    this.updateClock();
    this.clockInterval = setInterval(() => this.updateClock(), 1000);
  }

  ngOnDestroy(): void {
    if (this.clockInterval) {
      clearInterval(this.clockInterval);
    }
  }

  private updateClock(): void {
    const now = new Date();
    this.timeString.set(
      now.toLocaleTimeString('pt-BR', {
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
      })
    );
    
    const formattedDate = now.toLocaleDateString('pt-BR', {
      weekday: 'long',
      day: '2-digit',
      month: 'long',
    });
    // Capitalizar a primeira letra
    this.dateString.set(formattedDate.charAt(0).toUpperCase() + formattedDate.slice(1));
  }

  protected confirmEntry(): void {
    const rawPlate = this.plate().toUpperCase().trim();
    const model = this.modelName().trim();
    const spot = this.selectedSpot();

    if (!rawPlate) {
      this.toastService.error('Placa do veículo é obrigatória.');
      return;
    }

    if (!model) {
      this.toastService.error('Modelo/Marca do veículo é obrigatório.');
      return;
    }

    if (spot <= 0) {
      this.toastService.error('Selecione uma vaga disponível.');
      return;
    }

    // Para fazer o check-in na API do backend:
    // Primeiro cadastramos o veículo na empresa usando a mutação do vehicle
    this.createVehicleMutation.mutate(
      {
        plate: rawPlate,
        model: model,
        color: 'N/A', // Cor padrão
      },
      {
        onSuccess: (vehicleResponse) => {
          // Após cadastrar o veículo, fazemos o check-in (usando o id do veículo)
          this.checkInMutation.mutate(vehicleResponse.id, {
            onSuccess: (ticketResponse) => {
              this.spotAssignmentService.assignSpot(ticketResponse.id, spot);
              this.toastService.success(`Entrada do veículo ${rawPlate} registrada com sucesso na Vaga ${spot}!`);
              this.resetForm();
            },
            onError: () => {
              this.toastService.error('Erro ao registrar entrada (check-in) do veículo.');
            }
          });
        },
        onError: () => {
          // Caso falhe por já existir o veículo ou erro de cadastro, tentamos fazer o check-in diretamente se for possível.
          // Como em muitos sistemas o veículo pode já estar cadastrado, listamos os veículos para achar o id,
          // mas para simplificar, tratamos o fluxo de erro avisando o operador.
          this.toastService.error('Erro ao cadastrar veículo. Verifique se a placa já está no sistema.');
        }
      }
    );
  }

  private resetForm(): void {
    this.plate.set('');
    this.modelName.set('');
    this.vehicleType.set('Carro');
    this.selectedSpot.set(0);
    this.isMonthly.set(false);
  }
}
