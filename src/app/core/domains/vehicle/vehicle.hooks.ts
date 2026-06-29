import { inject, Signal } from '@angular/core';
import { injectQuery, injectMutation, QueryClient } from '@tanstack/angular-query-experimental';
import { lastValueFrom } from 'rxjs';
import { VehicleService } from './vehicle.service';
import { VehicleRequest, UpdateVehicleParams } from './vehicle.types';

export function useVehiclesQuery(companyId: Signal<string>) {
  const service = inject(VehicleService);
  return injectQuery(() => ({
    queryKey: ['vehicles', companyId()] as const,
    queryFn: () => lastValueFrom(service.listAll(companyId())),
    enabled: !!companyId() && companyId() !== 'null' && companyId() !== 'undefined',
  }));
}

export function useVehicleByIdQuery(companyId: Signal<string>, id: Signal<string>) {
  const service = inject(VehicleService);
  return injectQuery(() => ({
    queryKey: ['vehicles', companyId(), id()] as const,
    queryFn: () => lastValueFrom(service.getById(companyId(), id())),
    enabled: !!companyId() && companyId() !== 'null' && companyId() !== 'undefined' && !!id(),
  }));
}

export function useCreateVehicleMutation(companyId: Signal<string>) {
  const service = inject(VehicleService);
  const queryClient = inject(QueryClient);
  return injectMutation(() => ({
    mutationFn: (request: VehicleRequest) => lastValueFrom(service.create(companyId(), request)),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['vehicles', companyId()] }),
  }));
}

export function useUpdateVehicleMutation() {
  const service = inject(VehicleService);
  const queryClient = inject(QueryClient);
  return injectMutation(() => ({
    mutationFn: (params: UpdateVehicleParams) =>
      lastValueFrom(service.update(params.companyId, params.id, params.request)),
    onSuccess: (_data, params) =>
      queryClient.invalidateQueries({ queryKey: ['vehicles', params.companyId] }),
  }));
}

export function useDeleteVehicleMutation() {
  const service = inject(VehicleService);
  const queryClient = inject(QueryClient);
  return injectMutation(() => ({
    mutationFn: (params: { companyId: string; id: string }) =>
      lastValueFrom(service.delete(params.companyId, params.id)),
    onSuccess: (_data, params) =>
      queryClient.invalidateQueries({ queryKey: ['vehicles', params.companyId] }),
  }));
}
