# 📦 AntRoute

<!-- Badges de status, linguagem, licença, etc. -->
![Status](https://img.shields.io/badge/status-active-success.svg)
![License](https://img.shields.io/badge/license-MIT-blue.svg)
![Version](https://img.shields.io/badge/version-1.0.0-blue)
![GitHub issues](https://img.shields.io/github/issues/DouglasMonteles/antroute)
![GitHub stars](https://img.shields.io/github/stars/DouglasMonteles/antroute)
![GitHub forks](https://img.shields.io/github/forks/DouglasMonteles/antroute)

> O AntRoute é um projeto que aplica o algoritmo conhecido como Ant Colony Optimization (ACO). Ele é utilizado para se encontrar soluções aproximadas para problemas de otimização utilizando a forma como as formigas utilizam para encontrar o menor caminho entre a sua colônia e uma fonte de comida, aplicando uma trila de feromônio para comunicação.

---

## 📑 Índice

- [📦 AntRoute](#-antroute)
  - [📑 Índice](#-índice)
  - [📖 Sobre](#-sobre)
  - [✨ Funcionalidades](#-funcionalidades)
  - [🛠 Tecnologias](#-tecnologias)
  - [📋 Pré-requisitos](#-pré-requisitos)
  - [🚀 Instalação](#-instalação)
  - [📦 Como usar](#-como-usar)
  - [📂 Estrutura do projeto](#-estrutura-do-projeto)
  - [🤝 Contribuição](#-contribuição)
  - [📜 Licença](#-licença)
  - [👨‍💻 Autores](#-autores)
  - [📚 Referências](#-referências)

---

## 📖 Sobre

Neste projeto, foi implementado um sistema de simulação em tempo real que exibe as formigas percorrendo um grafo e marcando o melhor caminho com feromônio. Além do algoritmo ACO, cada formiga é um agente criado com Java Agent DEvelopment framework (JADE). Para viabilizar a comunicação entre a interface web e os agentes, foi adicionada uma API criada com Springboot.

![Demo do Projeto](docs/demo.gif)

---

## ✨ Funcionalidades

- ✅ Agentes:
  - Criação dinâmica de agentes conforme a demanda;
  - Remote Monitoring Agent (RMA): basicamente o "painel de controle" dos containers e agentes da plataforma;
  - Agente para o gerenciamento de formigas;
  - Agente para gerenciar a API com springboot;
  - Agente para se comunicar com a API via requisições que seguem o padrão REST.
- ✅ Frontend:
  - Comunicação via web socket com SockJs;
  - Comunicação via requisições REST com cliente fornecido pelo angular;
  - Geração dinâmica do grafo com cytoscape;
  - Formulário com envio dos dados para simulação;
  - Atualização constante da posição das formigas nos nós, conforme os dados recebidos da simulação;
  - Exibição do melhor caminho encontrado;
- ✅ Servidor:
  - Controlador para receber as requisições relacionadas as formigas;
  - Controlador para receber as requisições relacionadas ao grafo;
  - Configuração do servidor para suporte a conexões web socket;
  - Design pattern observer para viabilizar a atualização dos dados da simulação vindos do frontend;
  - Configuração de segurança do servidor;
- ✅ Implementação do algoritmo ACO em Java.

---

## 🛠 Tecnologias

Este projeto foi desenvolvido com as seguintes tecnologias:

- [Java](https://www.java.com/pt-BR)
- [JADE](https://jade-project.gitlab.io)
- [Maven](https://maven.apache.org)
- [Springboot](https://spring.io/projects/spring-boot)
- [Angular](https://angular.dev)
- [Angular Material](https://material.angular.dev)
- [Javascript](https://developer.mozilla.org/pt-BR/docs/Web/JavaScript)
- [Typescript](https://www.typescriptlang.org)
- [HTML5](https://developer.mozilla.org/pt-BR/docs/Web/HTML)
- [CSS3](https://developer.mozilla.org/pt-BR/docs/Web/CSS)

---

## 📋 Pré-requisitos

Antes de começar, você precisa ter instalado em sua máquina:

- [Git](https://git-scm.com/downloads)
- [Node.js](https://nodejs.org/) (versão 22.12.0)
- [Java](https://www.docker.com/) (versão 21.0.5)

---

## 🚀 Instalação

```bash
# Clone este repositório
git clone https://github.com/DouglasMonteles/antroute.git

# Acesse a pasta do projeto
cd antroute

# Instale as dependências (não executa os testes)
make build

# Execute o projeto
make up

# Dica: execute o comando abaixo para compilar o projeto e executá-lo em seguida
make build-up
```

---

## 📦 Como usar

Com o projeto executando, acesse <http://localhost:8080>, interaja com o formulário e clique no botão de "Iniciar simulação". Na tela seguinte, será exibida as formigas percorrendo os caminhos no grafo. Cada caminho exibe a quantidade de formigas que passaram por ele e uma maior intensidade na cor do caminho indica que mais formigas passaram por ele.

Ao final, o caminho com maior intensidade indica o melhor caminho encontrado na simulação.

---

## 📂 Estrutura do projeto

O projeto é estruturado utilizando o maven como gerenciador de dependências e de construção. O projeto possui três módulos principais, sendo eles:

```plaintext
├── .idea
├── .mvn
├── external-data
│   └── graph.json
├── agents
│   └── pom.xml
├── frontend
│   └── pom.xml
├── server
│   └── pom.xml
├── .gitattributes
├── .gitignore
├── APDescription.txt
├── HELP.md
├── Makefile
├── MTPs-Main-Container.txt
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

---

## 🤝 Contribuição

Contribuições são bem-vindas! Para contribuir:

1. **Fork** o projeto.
2. Crie uma **branch**: `git checkout -b minha-feature`
3. Faça suas alterações e **commit**: `git commit -m 'Minha nova feature'`
4. **Push** para a branch: `git push origin minha-feature`
5. Abra um **Pull Request**.

---

## 📜 Licença

Este projeto está sob a licença [MIT](LICENSE).

---

## 👨‍💻 Autores

- **Douglas** - _Desenvolvimento e documentação_ - [@DouglasMonteles](https://github.com/DouglasMonteles)

---

## 📚 Referências

- [The ant system: An autocatalytic optimizing process](https://www.academia.edu/download/39665098/Ant_System_An_Autocatalytic_Optimizing_P20151103-26864-13zyssn.pdf)