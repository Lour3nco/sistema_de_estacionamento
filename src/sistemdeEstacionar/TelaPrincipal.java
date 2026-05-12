package sistemdeEstacionar;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;

public class TelaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

  
    private static final Color BG_DARK      = new Color(13, 15, 20);
    private static final Color BG_CARD      = new Color(22, 26, 35);
    private static final Color BG_INPUT     = new Color(30, 35, 47);
    private static final Color BG_HOVER     = new Color(38, 44, 58);
    private static final Color ACCENT       = new Color(255, 176, 32);
    private static final Color ACCENT_DIM   = new Color(180, 120, 10);
    private static final Color TEXT_PRIMARY = new Color(230, 232, 240);
    private static final Color TEXT_MUTED   = new Color(120, 128, 150);
    private static final Color BORDER_COLOR = new Color(40, 46, 62);
    private static final Color SUCCESS      = new Color(52, 199, 120);
    private static final Color DANGER       = new Color(255, 80, 80);

   
    private static final Font FONT_TITLE  = new Font("SansSerif", Font.BOLD, 22);
    private static final Font FONT_LABEL  = new Font("SansSerif", Font.PLAIN, 13);
    private static final Font FONT_SMALL  = new Font("SansSerif", Font.PLAIN, 11);
    private static final Font FONT_BUTTON = new Font("SansSerif", Font.BOLD, 13);
    private static final Font FONT_TABLE  = new Font("SansSerif", Font.PLAIN, 12);
    private static final Font FONT_HEADER = new Font("SansSerif", Font.BOLD, 12);

   
    private DefaultTableModel modeloVeiculos;
    private DefaultTableModel modeloAtivos;
    private DefaultTableModel modeloHistorico;

   
    private CardLayout cardLayout;
    private JPanel     cardPanel;

  
    public TelaPrincipal() {
        setTitle("ParkSystem — Gestão de Estacionamento");
        setSize(960, 680);
        setMinimumSize(new Dimension(800, 580));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_DARK);
        setLayout(new BorderLayout());

        add(criarSidebar(),  BorderLayout.WEST);
        add(criarConteudo(), BorderLayout.CENTER);

        setVisible(true);
    }

  
    private JPanel criarSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(BG_CARD);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER_COLOR));

        // Logo
        JPanel logoArea = new JPanel(new BorderLayout(12, 0));
        logoArea.setBackground(BG_CARD);
        logoArea.setBorder(new EmptyBorder(28, 20, 24, 20));
        logoArea.setMaximumSize(new Dimension(220, 88));

        JLabel iconLbl = new JLabel("🅿");
        iconLbl.setFont(new Font("SansSerif", Font.BOLD, 32));
        iconLbl.setForeground(ACCENT);

        JPanel textStack = new JPanel();
        textStack.setBackground(BG_CARD);
        textStack.setLayout(new BoxLayout(textStack, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("ParkSystem");
        title.setFont(FONT_TITLE);
        title.setForeground(TEXT_PRIMARY);
        JLabel sub = new JLabel("Gestão de estacionamento");
        sub.setFont(FONT_SMALL);
        sub.setForeground(TEXT_MUTED);
        textStack.add(title);
        textStack.add(Box.createVerticalStrut(2));
        textStack.add(sub);

        logoArea.add(iconLbl,   BorderLayout.WEST);
        logoArea.add(textStack, BorderLayout.CENTER);
        sidebar.add(logoArea);

        sidebar.add(criarSeparadorH());
        sidebar.add(Box.createVerticalStrut(8));

     
        String[][] menus = {
            {"🚗", "Cadastro",   "Cadastrar veículos"},
            {"📥", "Entrada",    "Registrar entrada"},
            {"📤", "Saída",      "Registrar saída"},
            {"📋", "Histórico",  "Ver movimentações"}
        };

        ButtonGroup group = new ButtonGroup();
        JToggleButton[] btns = new JToggleButton[menus.length];
        for (int i = 0; i < menus.length; i++) {
            final String card = menus[i][1];
            JToggleButton btn = criarBotaoMenu(menus[i][0], menus[i][1], menus[i][2]);
            group.add(btn);
            btns[i] = btn;
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(2));
            btn.addActionListener(e -> cardLayout.show(cardPanel, card));
        }
        btns[0].setSelected(true);

        sidebar.add(Box.createVerticalGlue());

       
        JLabel footer = new JLabel("v1.0  •  MySQL");
        footer.setFont(FONT_SMALL);
        footer.setForeground(TEXT_MUTED);
        footer.setBorder(new EmptyBorder(0, 20, 18, 0));
        footer.setAlignmentX(LEFT_ALIGNMENT);
        sidebar.add(footer);

        return sidebar;
    }

    private JToggleButton criarBotaoMenu(String emoji, String label, String descricao) {
        JToggleButton btn = new JToggleButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (isSelected()) {
                    g2.setColor(new Color(255, 176, 32, 28));
                    g2.fillRoundRect(6, 0, getWidth() - 12, getHeight(), 8, 8);
                    g2.setColor(ACCENT);
                    g2.fillRoundRect(2, 10, 3, getHeight() - 20, 3, 3);
                } else if (getModel().isRollover()) {
                    g2.setColor(BG_HOVER);
                    g2.fillRoundRect(6, 0, getWidth() - 12, getHeight(), 8, 8);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setLayout(new BorderLayout(10, 0));
        btn.setBorder(new EmptyBorder(10, 18, 10, 12));
        btn.setMaximumSize(new Dimension(220, 60));
        btn.setOpaque(false);
        btn.setBackground(new Color(0, 0, 0, 0));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel emojiLbl = new JLabel(emoji);
        emojiLbl.setFont(new Font("SansSerif", Font.PLAIN, 18));
        emojiLbl.setForeground(ACCENT);

        JPanel texts = new JPanel();
        texts.setOpaque(false);
        texts.setLayout(new BoxLayout(texts, BoxLayout.Y_AXIS));
        JLabel nameLbl = new JLabel(label);
        nameLbl.setFont(FONT_BUTTON);
        nameLbl.setForeground(TEXT_PRIMARY);
        JLabel descLbl = new JLabel(descricao);
        descLbl.setFont(FONT_SMALL);
        descLbl.setForeground(TEXT_MUTED);
        texts.add(nameLbl);
        texts.add(descLbl);

        btn.add(emojiLbl, BorderLayout.WEST);
        btn.add(texts,    BorderLayout.CENTER);
        return btn;
    }

 
    private JPanel criarConteudo() {
        cardLayout = new CardLayout();
        cardPanel  = new JPanel(cardLayout);
        cardPanel.setBackground(BG_DARK);
        cardPanel.add(criarAbaCadastro(),  "Cadastro");
        cardPanel.add(criarAbaEntrada(),   "Entrada");
        cardPanel.add(criarAbaSaida(),     "Saída");
        cardPanel.add(criarAbaHistorico(), "Histórico");
        return cardPanel;
    }

   

    private JPanel criarAbaCadastro() {
        JPanel page = paginaBase("Cadastro de Veículos", "Registre novos veículos no sistema");

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints g = gbc();

        JTextField txtPlaca  = campo("Ex: ABC-1234");
        JTextField txtModelo = campo("Ex: Toyota Corolla");
        JTextField txtCor    = campo("Ex: Prata");
        JComboBox<String> cbTipo = combo(new String[]{"carro", "moto", "caminhonete"});

        linha(form, g, 0, "Placa",          txtPlaca);
        linha(form, g, 1, "Modelo",         txtModelo);
        linha(form, g, 2, "Cor",            txtCor);
        linha(form, g, 3, "Tipo",           cbTipo);

        g.gridy = 4; g.gridx = 0; g.gridwidth = 3; g.insets = new Insets(14, 0, 14, 0);
        JPanel btnRow = rowPanel();
        JButton btnSalvar = botaoPrimario("＋  Cadastrar Veículo");
        JButton btnListar = botaoSecundario("↻  Atualizar Lista");
        btnRow.add(btnSalvar);
        btnRow.add(Box.createHorizontalStrut(10));
        btnRow.add(btnListar);
        form.add(btnRow, g);

        modeloVeiculos = modelo("ID", "Placa", "Modelo", "Cor", "Tipo");
        JTable tabela  = tabela(modeloVeiculos);
        g.gridy = 5; g.insets = new Insets(0, 0, 0, 0);
        g.weighty = 1.0; g.fill = GridBagConstraints.BOTH;
        form.add(scrollTabela(tabela), g);

        page.add(form, BorderLayout.CENTER);

        btnSalvar.addActionListener(e -> {
            String placa  = txtPlaca.getText().trim();
            String modelo = txtModelo.getText().trim();
            String cor    = txtCor.getText().trim();
            String tipo   = (String) cbTipo.getSelectedItem();
            if (placa.isEmpty() || modelo.isEmpty() || cor.isEmpty()) {
                msg("⚠️  Preencha todos os campos!"); return;
            }
            int id = ConexaoBD.inserirVeiculo(placa, modelo, cor, tipo);
            if (id > 0) {
                msg("✅  Veículo cadastrado! ID: " + id);
                txtPlaca.setText(""); txtModelo.setText(""); txtCor.setText("");
                atualizarTabelaVeiculos();
            } else {
                msg("❌  Erro ao cadastrar veículo.");
            }
        });
        btnListar.addActionListener(e -> atualizarTabelaVeiculos());
        return page;
    }

    private void atualizarTabelaVeiculos() {
        modeloVeiculos.setRowCount(0);
        List<String[]> lista = ConexaoBD.listarVeiculos();
        if (lista.isEmpty()) { msg("Nenhum veículo cadastrado."); return; }
        lista.forEach(modeloVeiculos::addRow);
    }

   

    private JPanel criarAbaEntrada() {
        JPanel page = paginaBase("Entrada de Veículo", "Registre a entrada e aloque uma vaga automaticamente");

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints g = gbc();

        JTextField txtPlaca = campo("Digite a placa do veículo");
        linha(form, g, 0, "Placa do veículo", txtPlaca);

        g.gridy = 1; g.gridx = 0; g.gridwidth = 3; g.insets = new Insets(14, 0, 14, 0);
        JPanel btnRow = rowPanel();
        JButton btnEntrada = botaoPrimario("📥  Registrar Entrada");
        JButton btnListar  = botaoSecundario("↻  Ver Estacionados");
        btnRow.add(btnEntrada);
        btnRow.add(Box.createHorizontalStrut(10));
        btnRow.add(btnListar);
        form.add(btnRow, g);

        modeloAtivos = modelo("Placa", "Nº Vaga");
        JTable tabela = tabela(modeloAtivos);
        g.gridy = 2; g.insets = new Insets(0, 0, 0, 0);
        g.weighty = 1.0; g.fill = GridBagConstraints.BOTH;
        form.add(scrollTabela(tabela), g);

        page.add(form, BorderLayout.CENTER);

        btnEntrada.addActionListener(e -> {
            String placa = txtPlaca.getText().trim();
            if (placa.isEmpty()) { msg("Digite a placa do veículo!"); return; }
            Object[] v = ConexaoBD.buscarVeiculoPorPlaca(placa);
            if (v == null) { msg("Veículo não encontrado! Cadastre-o primeiro."); return; }
            int vagaId = ConexaoBD.obterVagaDisponivel();
            if (vagaId == -1) { msg("Nenhuma vaga disponível!"); return; }
            int movId = ConexaoBD.registrarEntrada((int) v[0], vagaId);
            if (movId > 0) {
                msg("✅  Entrada registrada — Vaga " + vagaId);
                txtPlaca.setText("");
                atualizarTabelaAtivos();
            } else {
                msg("❌  Erro ao registrar entrada.");
            }
        });
        btnListar.addActionListener(e -> atualizarTabelaAtivos());
        return page;
    }

    private void atualizarTabelaAtivos() {
        modeloAtivos.setRowCount(0);
        List<String[]> lista = ConexaoBD.listarMovimentacoesAtivas();
        if (lista.isEmpty()) { msg("Nenhum veículo estacionado no momento."); return; }
        lista.forEach(modeloAtivos::addRow);
    }

 

    private JPanel criarAbaSaida() {
        JPanel page = paginaBase("Saída de Veículo", "Calcule o valor e libere a vaga");

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints g = gbc();

        JTextField txtPlaca = campo("Digite a placa do veículo");
        linha(form, g, 0, "Placa do veículo", txtPlaca);

        // Card de valor
        g.gridy = 1; g.gridx = 0; g.gridwidth = 3; g.insets = new Insets(14, 0, 8, 0);
        JPanel valorCard = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 14));
        valorCard.setBackground(BG_CARD);
        valorCard.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        JLabel labelValor = new JLabel("Valor estimado:");
        labelValor.setFont(FONT_LABEL);
        labelValor.setForeground(TEXT_MUTED);
        JLabel valorDisplay = new JLabel("—");
        valorDisplay.setFont(new Font("SansSerif", Font.BOLD, 26));
        valorDisplay.setForeground(ACCENT);
        valorCard.add(labelValor);
        valorCard.add(valorDisplay);
        form.add(valorCard, g);

        g.gridy = 2; g.insets = new Insets(8, 0, 14, 0);
        JPanel btnRow = rowPanel();
        JButton btnCalc  = botaoSecundario("🔍  Calcular Valor");
        JButton btnSaida = botaoPrimario("📤  Registrar Saída");
        btnRow.add(btnCalc);
        btnRow.add(Box.createHorizontalStrut(10));
        btnRow.add(btnSaida);
        form.add(btnRow, g);

        page.add(form, BorderLayout.CENTER);

        btnCalc.addActionListener(e -> {
            String placa = txtPlaca.getText().trim();
            if (placa.isEmpty()) { msg("Digite a placa do veículo!"); return; }
            double valor = ConexaoBD.calcularValor(placa);
            valorDisplay.setText(valor == 0 ? "Sem movimentação ativa"
                                            : String.format("R$ %.2f", valor));
        });

        btnSaida.addActionListener(e -> {
            String placa = txtPlaca.getText().trim();
            if (placa.isEmpty()) { msg("Digite a placa!"); return; }
            Object[] v = ConexaoBD.buscarVeiculoPorPlaca(placa);
            if (v == null) { msg("Veículo não encontrado!"); return; }
            boolean ok = ConexaoBD.registrarSaida((int) v[0]);
            if (ok) {
                msg("✅  Saída registrada com sucesso!");
                txtPlaca.setText("");
                valorDisplay.setText("—");
                atualizarTabelaAtivos();
                atualizarTabelaHistorico();
            } else {
                msg("❌  Verifique se o veículo está estacionado.");
            }
        });

        return page;
    }

   

    private JPanel criarAbaHistorico() {
        JPanel page = paginaBase("Histórico de Movimentações", "Todos os registros de entrada e saída");

        JPanel corpo = new JPanel(new BorderLayout(0, 14));
        corpo.setOpaque(false);

        modeloHistorico = modelo("Placa", "Tipo", "Entrada", "Saída", "Valor Pago");
        JTable t = tabela(modeloHistorico);
        corpo.add(scrollTabela(t), BorderLayout.CENTER);

        JPanel south = rowPanel();
        south.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        JButton btnAtualizar = botaoPrimario("↻  Atualizar Histórico");
        south.add(btnAtualizar);
        corpo.add(south, BorderLayout.SOUTH);

        page.add(corpo, BorderLayout.CENTER);
        btnAtualizar.addActionListener(e -> atualizarTabelaHistorico());
        return page;
    }

    private void atualizarTabelaHistorico() {
        modeloHistorico.setRowCount(0);
        List<String[]> lista = ConexaoBD.listarHistorico();
        if (lista.isEmpty()) { msg("Nenhum registro no histórico."); return; }
        lista.forEach(modeloHistorico::addRow);
    }

   

    private JPanel paginaBase(String titulo, String subtitulo) {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(BG_DARK);
        page.setBorder(new EmptyBorder(32, 36, 32, 36));

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(0, 0, 26, 0));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(FONT_TITLE);
        lblTitulo.setForeground(TEXT_PRIMARY);

        JLabel lblSub = new JLabel(subtitulo);
        lblSub.setFont(FONT_LABEL);
        lblSub.setForeground(TEXT_MUTED);

    
        JPanel linha = new JPanel();
        linha.setBackground(ACCENT);
        linha.setMaximumSize(new Dimension(40, 3));
        linha.setPreferredSize(new Dimension(40, 3));

        header.add(lblTitulo);
        header.add(Box.createVerticalStrut(4));
        header.add(lblSub);
        header.add(Box.createVerticalStrut(10));
        header.add(linha);

        page.add(header, BorderLayout.NORTH);
        return page;
    }

    private GridBagConstraints gbc() {
        GridBagConstraints g = new GridBagConstraints();
        g.insets  = new Insets(6, 0, 6, 12);
        g.fill    = GridBagConstraints.HORIZONTAL;
        g.anchor  = GridBagConstraints.WEST;
        g.weightx = 1.0;
        return g;
    }

    private void linha(JPanel p, GridBagConstraints g, int row, String label, JComponent campo) {
        g.gridy = row; g.gridx = 0; g.gridwidth = 1; g.weightx = 0;
        p.add(rotulo(label), g);
        g.gridx = 1; g.gridwidth = 2; g.weightx = 1.0;
        p.add(campo, g);
    }

    private JLabel rotulo(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(FONT_LABEL);
        l.setForeground(TEXT_MUTED);
        l.setBorder(new EmptyBorder(0, 0, 0, 18));
        l.setPreferredSize(new Dimension(140, 20));
        return l;
    }

    private JTextField campo(String placeholder) {
        JTextField f = new JTextField(20) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setColor(TEXT_MUTED);
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    g2.drawString(placeholder, 12, getHeight() / 2 + 5);
                }
            }
        };
        f.setBackground(BG_INPUT);
        f.setForeground(TEXT_PRIMARY);
        f.setCaretColor(ACCENT);
        f.setFont(FONT_LABEL);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            new EmptyBorder(9, 12, 9, 12)));
        f.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                f.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(ACCENT_DIM),
                    new EmptyBorder(9, 12, 9, 12)));
                f.repaint();
            }
            public void focusLost(FocusEvent e) {
                f.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR),
                    new EmptyBorder(9, 12, 9, 12)));
                f.repaint();
            }
        });
        return f;
    }

    private JComboBox<String> combo(String[] itens) {
        JComboBox<String> cb = new JComboBox<>(itens);
        cb.setBackground(BG_INPUT);
        cb.setForeground(TEXT_PRIMARY);
        cb.setFont(FONT_LABEL);
        cb.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        cb.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList<?> l, Object v,
                    int i, boolean sel, boolean foc) {
                super.getListCellRendererComponent(l, v, i, sel, foc);
                setBackground(sel ? BG_HOVER : BG_INPUT);
                setForeground(TEXT_PRIMARY);
                setBorder(new EmptyBorder(7, 12, 7, 12));
                return this;
            }
        });
        return cb;
    }

    private JButton botaoPrimario(String texto) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = getModel().isPressed()  ? ACCENT_DIM
                        : getModel().isRollover() ? ACCENT.brighter()
                        : ACCENT;
                g2.setColor(c);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BUTTON);
        btn.setForeground(new Color(15, 15, 15));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 22, 10, 22));
        return btn;
    }

    private JButton botaoSecundario(String texto) {
        JButton btn = new JButton(texto) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? BG_HOVER : BG_INPUT);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.setColor(BORDER_COLOR);
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BUTTON);
        btn.setForeground(TEXT_PRIMARY);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 22, 10, 22));
        return btn;
    }

    private DefaultTableModel modelo(String... colunas) {
        return new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
    }

    private JTable tabela(DefaultTableModel m) {
        JTable t = new JTable(m);
        t.setBackground(BG_INPUT);
        t.setForeground(TEXT_PRIMARY);
        t.setFont(FONT_TABLE);
        t.setRowHeight(36);
        t.setGridColor(BORDER_COLOR);
        t.setSelectionBackground(new Color(255, 176, 32, 35));
        t.setSelectionForeground(TEXT_PRIMARY);
        t.setShowVerticalLines(false);
        t.setIntercellSpacing(new Dimension(0, 1));
        t.setBorder(null);

        JTableHeader h = t.getTableHeader();
        h.setBackground(BG_CARD);
        h.setForeground(TEXT_MUTED);
        h.setFont(FONT_HEADER);
        h.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));
        h.setReorderingAllowed(false);
        h.setResizingAllowed(true);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                setBackground(sel ? new Color(255, 176, 32, 25) : (r % 2 == 0 ? BG_INPUT : BG_CARD));
                setForeground(TEXT_PRIMARY);
                setBorder(new EmptyBorder(0, 14, 0, 14));
                return this;
            }
        };
        for (int i = 0; i < m.getColumnCount(); i++) {
            t.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
        return t;
    }

    private JScrollPane scrollTabela(JTable t) {
        JScrollPane sp = new JScrollPane(t);
        sp.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        sp.getViewport().setBackground(BG_INPUT);
        sp.setBackground(BG_INPUT);
        return sp;
    }

    private JPanel rowPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.setOpaque(false);
        return p;
    }

    private JSeparator criarSeparadorH() {
        JSeparator s = new JSeparator();
        s.setForeground(BORDER_COLOR);
        s.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return s;
    }

    private void msg(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "ParkSystem", JOptionPane.PLAIN_MESSAGE);
    }



    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        UIManager.put("OptionPane.background",        BG_CARD);
        UIManager.put("Panel.background",             BG_CARD);
        UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
        UIManager.put("Button.background",            BG_INPUT);
        UIManager.put("Button.foreground",            TEXT_PRIMARY);
        UIManager.put("ScrollBar.thumb",              BG_HOVER);
        UIManager.put("ScrollBar.track",              BG_CARD);

        System.out.println("Iniciando ParkSystem...");
        ConexaoBD.obterConexao();
        SwingUtilities.invokeLater(TelaPrincipal::new);
    }

	public static Color getSuccess() {
		return SUCCESS;
	}

	public static Color getDanger() {
		return DANGER;
	}
}