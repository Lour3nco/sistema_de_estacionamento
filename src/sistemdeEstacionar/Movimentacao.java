package sistemdeEstacionar;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


public class Movimentacao {
    private int id;
    private Veiculo veiculo;
    private Vaga vaga;
    private LocalDateTime dataEntrada;
    private LocalDateTime dataSaida;
    private double valorPago;
    
   
    public Movimentacao(int id, Veiculo veiculo, Vaga vaga, LocalDateTime dataEntrada) {
        this.id = id;
        this.veiculo = veiculo;
        this.vaga = vaga;
        this.dataEntrada = dataEntrada;
        this.dataSaida = null;
        this.valorPago = 0;
    }
    
   
    public Movimentacao(int id, Veiculo veiculo, Vaga vaga, LocalDateTime dataEntrada, 
                       LocalDateTime dataSaida, double valorPago) {
        this.id = id;
        this.veiculo = veiculo;
        this.vaga = vaga;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
        this.valorPago = valorPago;
    }
    
    // Getters
    public int getId() {
        return id;
    }
    
    public Veiculo getVeiculo() {
        return veiculo;
    }
    
    public Vaga getVaga() {
        return vaga;
    }
    
    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }
    
    public LocalDateTime getDataSaida() {
        return dataSaida;
    }
    
    public double getValorPago() {
        return valorPago;
    }
    
    // Setters
    public void setId(int id) {
        this.id = id;
    }
    
    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }
    
    public void setVaga(Vaga vaga) {
        this.vaga = vaga;
    }
    
    public void setDataEntrada(LocalDateTime dataEntrada) {
        this.dataEntrada = dataEntrada;
    }
    
    public void setDataSaida(LocalDateTime dataSaida) {
        this.dataSaida = dataSaida;
    }
    
    public void setValorPago(double valorPago) {
        this.valorPago = valorPago;
    }
    
    
    public double calcularTempoEstacionado() {
        if (dataSaida == null) {
            return 0;
        }
        long minutos = ChronoUnit.MINUTES.between(dataEntrada, dataSaida);
        return minutos / 60.0;
    }
    
   
    public double registrarSaida(LocalDateTime dataSaida) {
        this.dataSaida = dataSaida;
        double horas = calcularTempoEstacionado();
        this.valorPago = veiculo.calcularTarifa(horas);
        return this.valorPago;
    }
    
    
    public boolean estaEstacionado() {
        return dataSaida == null;
    }
    
    @Override
    public String toString() {
        return "Movimentacao{" +
                "id=" + id +
                ", veiculo=" + veiculo.getPlaca() +
                ", vaga=" + vaga.getNumero() +
                ", dataEntrada=" + dataEntrada +
                ", dataSaida=" + dataSaida +
                ", valorPago=" + valorPago +
                '}';
    }
}
