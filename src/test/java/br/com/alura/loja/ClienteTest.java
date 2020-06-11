package br.com.alura.loja;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import br.com.alura.loja.builder.CarrinhoBuilder;
import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Produto;
import junit.framework.Assert;

public class ClienteTest {

	private WebTarget target;
	private Client client;
	private HttpServer server;

	@Before
	public void IniciaServidor() {
		this.server = new ServerFactory().start();
		this.client = ClientBuilder.newClient();
		this.target = client.target("http://localhost:8080");
	}

	@After
	public void TerminaServer() {
		this.server.stop();
	}

	@Test
	public void deveFuncionarTrazerUmCarrinho() {
		Carrinho carrinho = criaRequisicaoParaCarrinho();
		Assert.assertEquals("Rua Vergueiro 3185, 8 andar", carrinho.getRua());
	}

	private Carrinho criaRequisicaoParaCarrinho() {
		
		String carrinhoJson = target.path("/carrinhos/1").request().get(String.class);
		Carrinho carrinho = (Carrinho) new Gson().fromJson(carrinhoJson, Carrinho.class);
		return carrinho;
	}

	@Test
	public void deveCriarNovosCarrinhos() {
		Carrinho carrinho = new CarrinhoBuilder().criaCarrinho();
		String json = carrinho.toJSON();
		Entity<String> entity = Entity.entity(json, MediaType.APPLICATION_JSON);

		Response response = target.path("/carrinhos").request().post(entity);
		Assert.assertEquals(201, response.getStatus());

		String location = response.getHeaderString("Location");
		String conteudo = client.target(location).request().get(String.class);
		Assert.assertTrue(conteudo.contains("Microfone"));
	}

	@Test
	public void devePermitirAlterarQauntidadeDeProduto() {
		Carrinho carrinho = criaRequisicaoParaCarrinho();
		Produto produto = carrinho.getProdutos().get(1);
		produto.setQuantidade(produto.getQuantidade() + 1);
		String produtoJson = new Gson().toJson(produto);
		
		Response response = target
				.path("/carrinhos/" + carrinho.getId() + "/produtos/" + produto.getId() + "/quantidade/").request()
				.put(Entity.entity(produtoJson, APPLICATION_JSON));
		
		Assert.assertEquals(200, response.getStatus());
		
		 carrinho = criaRequisicaoParaCarrinho();
		 Assert.assertEquals(produto.getQuantidade(), carrinho.getProdutos().get(1).getQuantidade());
	}

}
