import javax.swing.*;
import java.awt.*;
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

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
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

    public boolean atualizarProduto(String nomeAntigo, String novoNome, double novoPreco, int novaQuantidade) {
        Produto produto = buscarProduto(nomeAntigo);
        if (produto != null) {
            if (buscarProduto(novoNome) == null || novoNome.equalsIgnoreCase(nomeAntigo)) {
                produto.setNome(novoNome);
                produto.setPreco(novoPreco);
                produto.setQuantidade(novaQuantidade);
                return true; // Atualização bem-sucedida
            }
        }
        return false; // Falha na atualização
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
        frame = new JFrame("Peixaria Esperança");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        ImageIcon icon = new ImageIcon("src/fotos/fish_13319128.png"); // Altere para o caminho do seu ícone
        frame.setIconImage(icon.getImage());


        JTabbedPane tabbedPane = new JTabbedPane();

        // Aba de Adicionar Produtos
        JPanel adicionarPanel = new JPanel();
        adicionarPanel.setLayout(new FlowLayout());
        JTextField nomeField = new JTextField(10);
        JTextField precoField = new JTextField(10);
        JTextField quantidadeField = new JTextField(10);
        JButton adicionarButton = new JButton("Adicionar Produto");
        JTextArea areaTexto = new JTextArea(10, 30);
        areaTexto.setEditable(false);

        adicionarButton.addActionListener(e -> {
            try {
                String nome = nomeField.getText();
                double preco = Double.parseDouble(precoField.getText());
                int quantidade = Integer.parseInt(quantidadeField.getText());

                if (preco < 0 || quantidade < 0) {
                    throw new NumberFormatException("Preço e quantidade devem ser positivos.");
                }

                Produto produto = new Produto(nome, preco, quantidade);
                estoque.adicionarProduto(produto);
                atualizarExibicaoEstoque(areaTexto);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Entrada inválida: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
            nomeField.setText("");
            precoField.setText("");
            quantidadeField.setText("");
        });

        adicionarPanel.add(new JLabel("Nome:"));
        adicionarPanel.add(nomeField);
        adicionarPanel.add(new JLabel("Preço:"));
        adicionarPanel.add(precoField);
        adicionarPanel.add(new JLabel("Quantidade:"));
        adicionarPanel.add(quantidadeField);
        adicionarPanel.add(adicionarButton);
        adicionarPanel.add(areaTexto);
        tabbedPane.addTab("Adicionar Produto", adicionarPanel);

        // Aba de Vendas
        JPanel vendaPanel = new JPanel();
        vendaPanel.setLayout(new FlowLayout());
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

        vendaPanel.add(new JLabel("Nome:"));
        vendaPanel.add(vendaNomeField);
        vendaPanel.add(new JLabel("Quantidade:"));
        vendaPanel.add(vendaQuantidadeField);
        vendaPanel.add(vendaButton);
        vendaPanel.add(areaVendaTexto);
        vendaPanel.add(totalVendasArea);
        tabbedPane.addTab("Vender Produto", vendaPanel);

        // Aba de Alterar Produto
        JPanel alterarPanel = new JPanel();
        alterarPanel.setLayout(new FlowLayout());
        JTextField alterarNomeField = new JTextField(20);
        JTextField novoNomeField = new JTextField(22    );
        JTextField novoPrecoField = new JTextField(20);
        JTextField novaQuantidadeField = new JTextField(20);
        JButton alterarProdutoButton = new JButton("Alterar Produto");

        alterarProdutoButton.addActionListener(e -> {
            String nomeAntigo = alterarNomeField.getText();
            String novoNome = novoNomeField.getText();
            double novoPreco;
            int novaQuantidade;

            try {
                novoPreco = Double.parseDouble(novoPrecoField.getText());
                novaQuantidade = Integer.parseInt(novaQuantidadeField.getText());

                if (novoPreco < 0 || novaQuantidade < 0) {
                    throw new NumberFormatException("Preço e quantidade devem ser positivos.");
                }

                if (estoque.atualizarProduto(nomeAntigo, novoNome, novoPreco, novaQuantidade)) {
                    areaTexto.append("Produto alterado: " + nomeAntigo + " para " + novoNome + " - Novo Preço: R$ " + novoPreco + " - Nova Quantidade: " + novaQuantidade + ".\n");
                } else {
                    areaTexto.append("Falha ao alterar produto! Produto não encontrado ou novo nome já existe.\n");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Entrada inválida: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }

            alterarNomeField.setText("");
            novoNomeField.setText("");
            novoPrecoField.setText("");
            novaQuantidadeField.setText("");
        });

        alterarPanel.add(new JLabel("Nome Antigo:"));
        alterarPanel.add(alterarNomeField);
        alterarPanel.add(new JLabel("Novo Nome:"));
        alterarPanel.add(novoNomeField);
        alterarPanel.add(new JLabel("Novo Preço:"));
        alterarPanel.add(novoPrecoField);
        alterarPanel.add(new JLabel("Nova Quantidade:"));
        alterarPanel.add(novaQuantidadeField);
        alterarPanel.add(alterarProdutoButton);
        tabbedPane.addTab("Alterar Produto", alterarPanel);

        frame.add(tabbedPane);
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
