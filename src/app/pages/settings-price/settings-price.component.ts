import { Component, inject, signal, effect } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  useTariffQuery,
  useUpdateTariffMutation,
  usePricingQuery,
  useUpdatePricingMutation
} from '../../core/domains/tariff/tariff.hooks';
import {
  usePartnershipsQuery,
  useCreatePartnershipMutation,
  useDeletePartnershipMutation
} from '../../core/domains/partnership/partnership.hooks';
import { ToastService } from '../../shared/services/toast.service';

@Component({
  selector: 'app-settings-price',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './settings-price.html',
  styleUrl: './settings-price.css',
})
export class SettingsPrice {
  private readonly toastService = inject(ToastService);

  // Queries e Mutações
  protected readonly tariffQuery = useTariffQuery();
  protected readonly pricingQuery = usePricingQuery();
  protected readonly partnershipsQuery = usePartnershipsQuery();

  private readonly updateTariffMutation = useUpdateTariffMutation();
  private readonly updatePricingMutation = useUpdatePricingMutation();
  private readonly createPartnershipMutation = useCreatePartnershipMutation();
  private readonly deletePartnershipMutation = useDeletePartnershipMutation();

  // Controle de Abas
  readonly activeTab = signal<'tariffs' | 'daily' | 'partnerships' | 'rules'>('tariffs');

  // Formulário Tarifas
  readonly firstHourRate = signal(10.00);
  readonly additionalHourRate = signal(8.00);
  readonly timeFractioningMinutes = signal(60);
  readonly gracePeriodMinutes = signal(10);

  // Formulário Diária & Mensalistas
  readonly dailyTriggerHours = signal(12);
  readonly dailyValue = signal(40.00);
  readonly monthlyMemberFee = signal(250.00);
  readonly overnightStayFee = signal(20.00);

  // Formulário Convênios
  readonly newPartnershipName = signal('');
  readonly newPartnershipDiscountType = signal('percentage');
  readonly newPartnershipValue = signal(10);

  constructor() {
    // Efeito para carregar dados das queries nos inputs do formulário de tarifas
    effect(() => {
      const tariff = this.tariffQuery.data();
      if (tariff) {
        this.firstHourRate.set(tariff.firstHourValue);
        this.additionalHourRate.set(tariff.additionalFractionValue);
        this.gracePeriodMinutes.set(tariff.toleranceMinutes);
      }
    });

    // Efeito para carregar dados de pricing
    effect(() => {
      const pricing = this.pricingQuery.data();
      if (pricing) {
        this.timeFractioningMinutes.set(pricing.dailyTriggerHours * 5); // Simulação ou valor real
        this.dailyValue.set(pricing.dailyValue);
        this.monthlyMemberFee.set(pricing.monthlyBaseValue);
      }
    });
  }

  protected changeTab(tab: 'tariffs' | 'daily' | 'partnerships' | 'rules'): void {
    this.activeTab.set(tab);
  }

  protected saveTariffs(): void {
    // Mutar tarifas
    this.updateTariffMutation.mutate(
      {
        firstHourRate: this.firstHourRate(),
        additionalHourRate: this.additionalHourRate(),
        gracePeriodMinutes: this.gracePeriodMinutes(),
      },
      {
        onSuccess: () => {
          // Mutar pricing
          this.updatePricingMutation.mutate(
            {
              timeFractioningMinutes: this.timeFractioningMinutes(),
              monthlyMemberFee: this.monthlyMemberFee(),
              overnightStayFee: this.overnightStayFee(),
            },
            {
              onSuccess: () => {
                this.toastService.success('Configurações salvas com sucesso!');
              },
              onError: () => {
                this.toastService.error('Erro ao atualizar configurações tarifárias adicionais.');
              }
            }
          );
        },
        onError: () => {
          this.toastService.error('Erro ao atualizar configurações tarifárias principais.');
        }
      }
    );
  }

  protected addPartnership(): void {
    const name = this.newPartnershipName().trim();
    const type = this.newPartnershipDiscountType();
    const val = this.newPartnershipValue();

    if (!name) {
      this.toastService.error('O nome do convênio/parceria é obrigatório.');
      return;
    }

    if (val <= 0) {
      this.toastService.error('O valor do desconto deve ser maior do que zero.');
      return;
    }

    this.createPartnershipMutation.mutate(
      {
        name,
        discountType: type,
        value: val,
      },
      {
        onSuccess: () => {
          this.toastService.success('Parceria cadastrada com sucesso!');
          this.newPartnershipName.set('');
          this.newPartnershipValue.set(10);
        },
        onError: () => {
          this.toastService.error('Erro ao cadastrar nova parceria.');
        }
      }
    );
  }

  protected deletePartnership(id: string): void {
    if (confirm('Tem certeza que deseja excluir esta parceria/convênio?')) {
      this.deletePartnershipMutation.mutate(id, {
        onSuccess: () => {
          this.toastService.success('Parceria excluída com sucesso!');
        },
        onError: () => {
          this.toastService.error('Erro ao excluir parceria.');
        }
      });
    }
  }
}
