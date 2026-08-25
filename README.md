# 🦄 Trio Mágico de Equestria

Site simples em Java, com tema My Little Pony, apresentando três amigas: **Leticia**, **Heloisa** e **Sara**.

- 100% Java puro (usa só `com.sun.net.httpserver.HttpServer`, da própria JDK)
- Sem frameworks, sem banco de dados
- Sem dependências externas no `pom.xml`
- Serve arquivos estáticos (HTML/CSS) empacotados dentro do próprio `.jar`

## Estrutura

```
mlp-site/
├── pom.xml
├── Dockerfile
└── src/main/
    ├── java/com/mlp/Main.java        # servidor HTTP
    └── resources/static/
        ├── index.html                 # página
        └── style.css                  # estilo (pastel, tema pônei)
```

## Rodando localmente

Requer Java 25 e Maven instalados.

```bash
mvn package
java -jar target/mlp-site.jar
```

Depois acesse: http://localhost:8080

O servidor também respeita a variável de ambiente `PORT`, caso o serviço de
hospedagem defina uma porta diferente automaticamente:

```bash
PORT=3000 java -jar target/mlp-site.jar
```

## Publicando no GitHub

```bash
cd mlp-site
git init
git add .
git commit -m "Site Trio Mágico de Equestria"
git branch -M main
git remote add origin https://github.com/SEU-USUARIO/SEU-REPOSITORIO.git
git push -u origin main
```

## Deploy em serviços de aplicativo (Render, Railway, etc.)

A maioria desses serviços consegue detectar um projeto Maven automaticamente.
Configure:

- **Build command:** `mvn package`
- **Start command:** `java -jar target/mlp-site.jar`

O `Dockerfile` incluso também funciona caso o serviço peça um container Docker
(ex: Render "Docker", Railway "Dockerfile", etc.) — nesse caso não é preciso
configurar build/start command, o serviço detecta o `Dockerfile` sozinho.

## Observação sobre a versão do Java

O `pom.xml` está configurado para `Java 25` (`maven.compiler.release`). Se o
serviço de hospedagem só oferecer Java 21 LTS, basta trocar esse número em
`pom.xml` — o código não usa nenhum recurso exclusivo do Java 25.
