package br.packajo;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import ejb.ICarrinhoBean;
import ejb.IProdutoBean;
import ejb.Produto;

public class MainStateless {

	private ICarrinhoBean remoteCarrito;
	private IProdutoBean remoteEstoque;

	public static void main(String[] args) {
		MainStateless ms = new MainStateless();
		ms.getRemote();
		ms.remoteEstoque.povoaProdutoBean();
		ms.listarProdutos();
		// ms.addProduto((long) 1, 1);
		System.out.println("-----------");
		for (Produto p : ms.remoteCarrito.obterProdutos()) {
			System.out.println(p.getNome());
		}
		System.out.println(ms.remoteCarrito.obterPrecoCarrinho());
	}

	private void getRemote() {
		try {
			InitialContext ctx = new InitialContext(JNDIConfig.getConfigs());
			this.remoteCarrito = (ICarrinhoBean) ctx.lookup("ejb:/ejb-carrinho/CarrinhoBean!ejb.ICarrinhoBean");
			this.remoteEstoque = (IProdutoBean) ctx.lookup("ejb:/ejb-estoque/ProdutoBean!ejb.IProdutoBean");
		} catch (NamingException e) {
			e.printStackTrace();
		}

	}

	private void listarProdutos() {
		for (Produto p : this.remoteEstoque.obterProdutos()) {
			System.out.println(p.getNome());
		}
	}

	private void addProduto(Long id, int quantidade) {
		this.atualizarEstoque(id, quantidade);
		do {
			this.remoteCarrito.adicionarProduto(this.remoteEstoque.obterProduto(id));
			quantidade--;
		} while (quantidade != 0);

	}

	private void atualizarEstoque(Long id, int quantidade) {
		for (Produto p : this.remoteEstoque.obterProdutos()) {
			if (p.getId() == id) {
				p.setEstoque(p.getEstoque() - quantidade);
			}

		}
	}

	private boolean verificaEstoque(Long id, int quantidade) {
		return this.remoteEstoque.existeEstoque(id, quantidade);
	}

}
