package sistemdeEstacionar;


public class Carro extends Veiculo {
    private static final double TARIFA_PRIMEIRA_HORA = 5.00;
    private static final double TARIFA_HORA_ADICIONAL = 3.00;
    
 
    public Carro(int id, String placa, String modelo, String cor) {
        super(id, placa, modelo, cor);
    }
    
    @Override
    public double calcularTarifa(double horas) {
        if (horas <= 0) {
            return 0;
        }
        
        if (horas <= 1) {
            return TARIFA_PRIMEIRA_HORA;
        }
        
        double horasAdicionais = horas - 1;
        return TARIFA_PRIMEIRA_HORA + (horasAdicionais * TARIFA_HORA_ADICIONAL);
    }
    
    @Override
    public String getTipo() {
        return "Carro";
    }
    
    @Override
    public String toString() {
        return "Carro{" +
                "id=" + getId() +
                ", placa='" + getPlaca() + '\'' +
                ", modelo='" + getModelo() + '\'' +
                ", cor='" + getCor() + '\'' +
                '}';
    }
}
