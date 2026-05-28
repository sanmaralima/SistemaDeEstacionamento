export interface VeiculoEstacionado {
  id: number;
  placa: string;
  modelo: string;
  cor: string;
  horarioEntrada: Date;
  vaga: number;
  status: 'Ativo' | 'Finalizado';
}