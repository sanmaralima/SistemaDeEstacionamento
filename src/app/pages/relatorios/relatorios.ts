import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../core/domains/auth/auth.service';
import { useReportQuery } from '../../core/domains/report/report.hooks';

@Component({
  selector: 'app-relatorios',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './relatorios.html',
  styleUrl: './relatorios.css',
})
export class Relatorios {
  private readonly authService = inject(AuthService);

  readonly companyId = this.authService.companyId;
  readonly metricsQuery = useReportQuery(this.companyId);

  formatarMetodo(metodo: string): string {
    const map: Record<string, string> = {
      DINHEIRO: 'Dinheiro',
      PIX: 'PIX',
      CARD_CREDIT: 'Cartão de Crédito',
      CARD_DEBIT: 'Cartão de Débito',
    };
    return map[metodo] || metodo;
  }

  formatarMinutos(minutos?: number): string {
    if (!minutos) return '0h 0m';
    const h = Math.floor(minutos / 60);
    const m = minutos % 60;
    return `${h}h ${m}m`;
  }

  obterMaxReceita(): number {
    const summaries = this.metricsQuery.data()?.paymentMethodSummaries || [];
    if (summaries.length === 0) return 1;
    return Math.max(1, ...summaries.map((s) => s.revenue));
  }
}