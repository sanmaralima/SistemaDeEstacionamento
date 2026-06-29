import { Directive, ElementRef, Input, OnChanges, Renderer2, SimpleChanges } from '@angular/core';

@Directive({
  selector: 'button[appLoading]',
  standalone: true,
})
export class LoadingDirective implements OnChanges {
  @Input('appLoading') isLoading = false;
  @Input() loadingText = 'Enviando...';

  private originalText = '';
  private originalDisabled = false;
  private isOriginalTextSaved = false;

  constructor(
    private el: ElementRef<HTMLButtonElement>,
    private renderer: Renderer2
  ) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['isLoading']) {
      this.updateState();
    }
  }

  private updateState(): void {
    const button = this.el.nativeElement;

    if (this.isLoading) {
      // Salva o texto/HTML original e o disabled original se ainda não foi salvo
      if (!this.isOriginalTextSaved) {
        this.originalText = button.innerHTML;
        this.originalDisabled = button.disabled;
        this.isOriginalTextSaved = true;
      }

      // Desabilita o botão
      this.renderer.setProperty(button, 'disabled', true);

      // Altera o texto para o estado de carregamento com um spinner SVG sutil
      this.renderer.setProperty(button, 'innerHTML', `
        <span class="loading-btn-content" style="display: inline-flex; align-items: center; justify-content: center; gap: 8px; width: 100%;">
          <svg style="width: 16px; height: 16px; animation: spin 1s linear infinite;" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
            <circle style="opacity: 0.25;" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
            <path style="opacity: 0.75;" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
          </svg>
          ${this.loadingText}
        </span>
      `);

      // Adiciona uma classe de estado de loading
      this.renderer.addClass(button, 'is-loading');

    } else {
      // Restaura o texto e o disabled originais
      if (this.isOriginalTextSaved) {
        this.renderer.setProperty(button, 'disabled', this.originalDisabled);
        this.renderer.setProperty(button, 'innerHTML', this.originalText);
        this.isOriginalTextSaved = false; // Permite capturar o estado correto caso haja novo clique futuro
      }

      // Remove a classe de estado de loading
      this.renderer.removeClass(button, 'is-loading');
    }
  }
}
