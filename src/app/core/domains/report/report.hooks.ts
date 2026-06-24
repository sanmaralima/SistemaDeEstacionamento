import { inject, Signal } from '@angular/core';
import { injectQuery } from '@tanstack/angular-query-experimental';
import { lastValueFrom } from 'rxjs';
import { ReportService } from './report.service';

export function useReportQuery(companyId: Signal<string | null>) {
  const service = inject(ReportService);
  return injectQuery(() => ({
    queryKey: ['reports', companyId()] as const,
    queryFn: () => lastValueFrom(service.getMetrics(companyId()!)),
    enabled: !!companyId(),
  }));
}
