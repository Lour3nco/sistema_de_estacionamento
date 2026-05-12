package sistemdeEstacionar;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConexaoBD {

    private static final String URL = "jdbc:mysql://localhost:3306/estacionamento";
    private static final String USUARIO = "root";
    private static final String SENHA = "12345678";



    public static Connection obterConexao() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USUARIO, SENHA);
            System.out.println("✓ Conectado ao banco!");
            return conn;
        } catch (Exception e) {
            System.out.println("❌ ERRO DE CONEXÃO:");
            e.printStackTrace();
            return null;
        }
    }

 

    public static int inserirVeiculo(String placa, String modelo, String cor, String tipo) {
        String sql = "INSERT INTO veiculos (placa, modelo, cor, tipo) VALUES (?, ?, ?, ?)";

        try (Connection conn = obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, placa);
            stmt.setString(2, modelo);
            stmt.setString(3, cor);
            stmt.setString(4, tipo);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao inserir veículo: " + e.getMessage());
        }

        return -1;
    }

    public static Object[] buscarVeiculoPorPlaca(String placa) {
        String sql = "SELECT * FROM veiculos WHERE placa = ?";

        try (Connection conn = obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, placa);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Object[]{
                            rs.getInt("id"),
                            rs.getString("placa"),
                            rs.getString("modelo"),
                            rs.getString("cor"),
                            rs.getString("tipo")
                    };
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar veículo: " + e.getMessage());
        }

        return null;
    }

  
    public static List<String[]> listarVeiculos() {
        String sql = "SELECT * FROM veiculos";
        List<String[]> lista = new ArrayList<>();

        try (Connection conn = obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("placa"),
                        rs.getString("modelo"),
                        rs.getString("cor"),
                        rs.getString("tipo")
                });
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar veículos: " + e.getMessage());
        }

        return lista;
    }

 

    public static int obterVagaDisponivel() {
        String sql = "SELECT id FROM vagas WHERE ocupada = false LIMIT 1";

        try (Connection conn = obterConexao();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) return rs.getInt("id");

        } catch (SQLException e) {
            System.out.println("Erro ao obter vaga disponível: " + e.getMessage());
        }

        return -1;
    }

    public static void marcarVagaOcupada(int vagaId, boolean ocupada) {
        String sql = "UPDATE vagas SET ocupada = ? WHERE id = ?";

        try (Connection conn = obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, ocupada);
            stmt.setInt(2, vagaId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao marcar vaga: " + e.getMessage());
        }
    }

 

    public static int registrarEntrada(int veiculoId, int vagaId) {
        String sql = "INSERT INTO movimentacoes (veiculo_id, vaga_id, data_entrada) VALUES (?, ?, NOW())";

        try (Connection conn = obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, veiculoId);
            stmt.setInt(2, vagaId);
            stmt.executeUpdate();

            marcarVagaOcupada(vagaId, true);

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao registrar entrada: " + e.getMessage());
        }

        return -1;
    }

    public static double calcularValor(String placa) {
        String sql = "SELECT m.data_entrada FROM movimentacoes m " +
                     "JOIN veiculos v ON m.veiculo_id = v.id " +
                     "WHERE v.placa = ? AND m.data_saida IS NULL";

        try (Connection conn = obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, placa);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Timestamp entrada = rs.getTimestamp("data_entrada");
                    long tempo = System.currentTimeMillis() - entrada.getTime();
                    double horas = Math.ceil(tempo / (1000.0 * 60 * 60));
                    return horas * 5.0;
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro ao calcular valor: " + e.getMessage());
        }

        return 0;
    }

    public static boolean registrarSaida(int veiculoId) {
        String buscar   = "SELECT id, vaga_id, data_entrada FROM movimentacoes " +
                          "WHERE veiculo_id = ? AND data_saida IS NULL";
        String atualizar = "UPDATE movimentacoes SET data_saida = NOW(), valor_pago = ? WHERE id = ?";

        try (Connection conn = obterConexao();
             PreparedStatement stmtBusca = conn.prepareStatement(buscar)) {

            stmtBusca.setInt(1, veiculoId);

            try (ResultSet rs = stmtBusca.executeQuery()) {
                if (!rs.next()) return false;

                int movId  = rs.getInt("id");
                int vagaId = rs.getInt("vaga_id");
                Timestamp entrada = rs.getTimestamp("data_entrada");

                long tempo = System.currentTimeMillis() - entrada.getTime();
                double horas = Math.ceil(tempo / (1000.0 * 60 * 60));
                double valor = horas * 5.0;

                try (PreparedStatement stmtUpdate = conn.prepareStatement(atualizar)) {
                    stmtUpdate.setDouble(1, valor);
                    stmtUpdate.setInt(2, movId);
                    stmtUpdate.executeUpdate();
                }

                marcarVagaOcupada(vagaId, false);
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Erro ao registrar saída: " + e.getMessage());
            return false;
        }
    }

    
    public static List<String[]> listarHistorico() {
        String sql = "SELECT v.placa, v.tipo, m.data_entrada, m.data_saida, m.valor_pago " +
                     "FROM movimentacoes m JOIN veiculos v ON m.veiculo_id = v.id";
        List<String[]> lista = new ArrayList<>();

        try (Connection conn = obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new String[]{
                        rs.getString("placa"),
                        rs.getString("tipo"),
                        rs.getString("data_entrada"),
                        rs.getString("data_saida"),
                        rs.getString("valor_pago")
                });
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar histórico: " + e.getMessage());
        }

        return lista;
    }

    
    public static List<String[]> listarMovimentacoesAtivas() {
        String sql = "SELECT v.placa, va.numero FROM movimentacoes m " +
                     "JOIN veiculos v ON m.veiculo_id = v.id " +
                     "JOIN vagas va ON m.vaga_id = va.id " +
                     "WHERE m.data_saida IS NULL";
        List<String[]> lista = new ArrayList<>();

        try (Connection conn = obterConexao();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new String[]{
                        rs.getString("placa"),
                        rs.getString("numero")
                });
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar movimentações ativas: " + e.getMessage());
        }

        return lista;
    }
}