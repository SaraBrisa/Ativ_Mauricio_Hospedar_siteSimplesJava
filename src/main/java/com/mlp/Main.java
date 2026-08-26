package com.mlp;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLConnection;

/**
 * Servidor web simples e sem dependências para o site "Trio Mágico de Equestria".
 * Serve os arquivos estáticos de src/main/resources/static (empacotados no jar).
 * Não usa banco de dados nem frameworks externos.
 * teste azure
 */
public class Main {

    private static final String STATIC_ROOT = "/static";

    public static void main(String[] args) throws IOException {
        int port = resolvePort();

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new StaticFileHandler());
        server.setExecutor(null); // executor padrão
        server.start();

        System.out.println("Trio Mágico de Equestria squad girls rodando em http://localhost:" + port);
    }

    private static int resolvePort() {
        String envPort = System.getenv("PORT");
        if (envPort != null && !envPort.isBlank()) {
            try {
                return Integer.parseInt(envPort.trim());
            } catch (NumberFormatException ignored) {
                // cai para a porta padrão abaixo
            }
        }
        return 8080;
    }

    static class StaticFileHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();

            if (path.equals("/") || path.isBlank()) {
                path = "/index.html";
            }

            // impede tentativas de sair da pasta static (ex: ../../etc/passwd)
            String safePath = path.replace("..", "");
            String resourcePath = STATIC_ROOT + safePath;

            InputStream resource = Main.class.getResourceAsStream(resourcePath);

            if (resource == null) {
                byte[] body = "404 - Página não encontrada em Equestria".getBytes();
                exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
                exchange.sendResponseHeaders(404, body.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(body);
                }
                return;
            }

            String contentType = guessContentType(resourcePath);
            exchange.getResponseHeaders().set("Content-Type", contentType);

            byte[] bytes = resource.readAllBytes();
            resource.close();

            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }

        private String guessContentType(String resourcePath) {
            if (resourcePath.endsWith(".html")) return "text/html; charset=utf-8";
            if (resourcePath.endsWith(".css")) return "text/css; charset=utf-8";
            if (resourcePath.endsWith(".js")) return "application/javascript; charset=utf-8";
            if (resourcePath.endsWith(".png")) return "image/png";
            if (resourcePath.endsWith(".jpg") || resourcePath.endsWith(".jpeg")) return "image/jpeg";
            if (resourcePath.endsWith(".svg")) return "image/svg+xml";

            String guess = URLConnection.guessContentTypeFromName(resourcePath);
            return guess != null ? guess : "application/octet-stream";
        }
    }
}
