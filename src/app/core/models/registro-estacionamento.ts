export interface RegistroEstacionamento {
  id: number;
  placa: string;
  modelo: string;
  cor: string;
  vaga: number;
  entrada: Date;
  saida?: Date;
  tempoTotalMinutos?: number;
  valorTotal?: number;
  status: 'Aberto' | 'Finalizado';
}