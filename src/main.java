import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

class Produto {
    private String nome;
    private double preco;
    private int quantidade;

    public Produto(String nome, double preco, int quantidade) {
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}

class Estoque {
    private List<Produto> produtos;

    public Estoque() {
        produtos = new ArrayList<>();
    }

    public void adicionarProduto(Produto produto) {
        produtos.add(produto);
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public Produto buscarProduto(String nome) {
        for (Produto p : produtos) {
            if (p.getNome().equalsIgnoreCase(nome)) {
                return p;
            }
        }
        return null;
    }
}

class Venda {
    private List<Produto> produtosVendidos;
    private double total;

    public Venda() {
        produtosVendidos = new ArrayList<>();
        total = 0.0;
    }

    public void adicionarProduto(Produto produto, int quantidade) {
        if (produto.getQuantidade() >= quantidade) {
            produtosVendidos.add(produto);
            total += produto.getPreco() * quantidade;
            produto.setQuantidade(produto.getQuantidade() - quantidade);
        }
    }

    public double getTotal() {
        return total;
    }

    public List<Produto> getProdutosVendidos() {
        return produtosVendidos;
    }
}

 class AppVendasEstoque {
    private Estoque estoque;
    private double totalVendas = 0.0; // Contador total de vendas
    private JFrame frame;

    public AppVendasEstoque() {
        estoque = new Estoque();
        frame = new JFrame("App de Vendas e Estoque");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);
        frame.setLayout(new FlowLayout());

        ImageIcon icon = new ImageIcon("src/fotos/casa.png"); // Substitua pelo caminho da sua imagem
        frame.setIconImage(icon.getImage());

        // Campos para adicionar produtos
        JTextField nomeField = new JTextField(10);
        JTextField precoField = new JTextField(10);
        JTextField quantidadeField = new JTextField(10);
        JButton adicionarButton = new JButton("Adicionar Produto");
        JTextArea areaTexto = new JTextArea(10, 30);
        areaTexto.setEditable(false);

        adicionarButton.addActionListener(e -> {
            String nome = nomeField.getText();
            double preco = Double.parseDouble(precoField.getText());
            int quantidade = Integer.parseInt(quantidadeField.getText());

            Produto produto = new Produto(nome, preco, quantidade);
            estoque.adicionarProduto(produto);
            atualizarExibicaoEstoque(areaTexto);
            nomeField.setText("");
            precoField.setText("");
            quantidadeField.setText("");
        });

        // Campos para vendas
        JTextField vendaNomeField = new JTextField(10);
        JTextField vendaQuantidadeField = new JTextField(10);
        JButton vendaButton = new JButton("Vender Produto");
        JTextArea areaVendaTexto = new JTextArea(5, 30);
        areaVendaTexto.setEditable(false);
        JTextArea totalVendasArea = new JTextArea(1, 30);
        totalVendasArea.setEditable(false);

        vendaButton.addActionListener(e -> {
            String nome = vendaNomeField.getText();
            int quantidade = Integer.parseInt(vendaQuantidadeField.getText());
            Produto produto = estoque.buscarProduto(nome);

            if (produto != null) {
                Venda venda = new Venda();
                venda.adicionarProduto(produto, quantidade);
                double totalVenda = venda.getTotal();
                totalVendas += totalVenda; // Atualiza o total de vendas
                areaVendaTexto.append("Venda realizada: " + produto.getNome() + " - Total: R$ " + totalVenda + "\n");
                totalVendasArea.setText("Total de Vendas: R$ " + totalVendas); // Atualiza a exibição do total
                atualizarExibicaoEstoque(areaTexto); // Atualiza a exibição após a venda
            } else {
                areaVendaTexto.append("Produto não encontrado!\n");
            }
            vendaNomeField.setText("");
            vendaQuantidadeField.setText("");
        });

        // Layout
        frame.add(new JLabel("Adicionar Produto:"));
        frame.add(new JLabel("Nome:"));
        frame.add(nomeField);
        frame.add(new JLabel("Preço:"));
        frame.add(precoField);
        frame.add(new JLabel("Quantidade:"));
        frame.add(quantidadeField);
        frame.add(adicionarButton);
        frame.add(areaTexto);

        frame.add(new JLabel("Vender Produto:"));
        frame.add(new JLabel("Nome:"));
        frame.add(vendaNomeField);
        frame.add(new JLabel("Quantidade:"));
        frame.add(vendaQuantidadeField);
        frame.add(vendaButton);
        frame.add(areaVendaTexto);
        frame.add(totalVendasArea); // Adiciona a área de total de vendas

        frame.setVisible(true);
    }

    private void atualizarExibicaoEstoque(JTextArea areaTexto) {
        areaTexto.setText("Produtos no Estoque:\n");
        for (Produto produto : estoque.getProdutos()) {
            areaTexto.append(produto.getNome() + " - Qtd: " + produto.getQuantidade() + " - Preço: R$ " + produto.getPreco() + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AppVendasEstoque::new);
    }
}