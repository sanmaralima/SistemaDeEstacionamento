import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { useRegisterMutation } from '../../../core/domains/auth/auth.hooks';

@Component({
  selector: 'app-cadastro',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './cadastro.html',
  styleUrl: './cadastro.css',
})
export class Cadastro {
  senhaVisivel = false;
  confirmarSenhaVisivel = false;
  erro = '';

  form = {
    nomeEmpresa: '',
    cnpj: '',
    totalVagas: null as number | null,
    nomeUsuario: '',
    senha: '',
    confirmarSenha: '',
  };

  private readonly router = inject(Router);
  private readonly registerMutation = useRegisterMutation();

  concluir(): void {
    console.log('Cadastro.concluir chamado com form:', this.form);
    this.erro = '';

    const { nomeEmpresa, cnpj, totalVagas, nomeUsuario, senha, confirmarSenha } = this.form;

    if (!nomeEmpresa.trim() || !cnpj.trim() || !totalVagas || !nomeUsuario.trim() || !senha || !confirmarSenha) {
      this.erro = 'Preencha todos os campos obrigatórios.';
      return;
    }

    if (senha !== confirmarSenha) {
      this.erro = 'As senhas não coincidem.';
      return;
    }

    if (senha.length < 6) {
      this.erro = 'A senha deve ter pelo menos 6 caracteres.';
      return;
    }

    this.registerMutation.mutate(
      {
        username: nomeUsuario,
        password: senha,
        companyName: nomeEmpresa,
        name: nomeUsuario,
      },
      {
        onSuccess: () => {
          this.router.navigate(['/login']);
        },
        onError: () => {
          this.erro = 'Erro ao realizar o cadastro. Verifique as informações ou se o usuário já existe.';
        },
      }
    );
  }
}