package br.com.alura.loja;

import java.io.IOException;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;



public class Loja {
	public static void main(String[] args) throws IOException {
		HttpServer server = new ServerFactory().start();
		System.out.println("Servidor Rodando");
		System.in.read();
		server.stop();
	}


}
