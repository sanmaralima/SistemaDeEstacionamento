# LocusPark - Sistema Multi-Tenant de Gestão de Estacionamentos

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Angular](https://img.shields.io/badge/angular-%23DD0031.svg?style=for-the-badge&logo=angular&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)

--

## 📜 Descrição do Projeto

O **LocusPark** é uma plataforma de gestão B2B SaaS Multi-Tenant de alto desempenho voltada para o controle, monitoramento e precificação inteligente de estacionamentos e pátios comerciais. Desenvolvido com foco absoluto em qualidade técnica, o sistema utiliza a metodologia **TDD (Test-Driven Development)** na construção do backend e adota componentização reativa baseada em **Signals** no frontend.

A aplicação utiliza uma arquitetura totalmente desacoplada, dividida em duas branches principais no repositório:
* **`backend`**: Uma API RESTful em Java e Spring Boot governando o isolamento de dados, autenticação JWT Stateless e regras financeiras complexas.
* **`frontend`**: Uma aplicação SPA em Angular e Tailwind CSS contendo o painel administrativo e o configurador dinâmico de pátio e tarifas.
