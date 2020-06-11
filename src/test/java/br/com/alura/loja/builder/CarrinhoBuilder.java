package br.com.alura.loja.builder;

import org.glassfish.grizzly.http.server.HttpServer;

import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Produto;

public class CarrinhoBuilder {
	public HttpServer server;

	public Carrinho criaCarrinho() {
		Carrinho carrinho = new Carrinho();
		carrinho.adiciona(new Produto(314,"Microfone",31,1));
		carrinho.setRua("Rua vergueiro,371");
		carrinho.setCidade("São Paulo");
		
		return carrinho;
	}
}