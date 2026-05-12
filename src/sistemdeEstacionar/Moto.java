package sistemdeEstacionar;


public class Moto extends Veiculo {
    private static final double DESCONTO_MOTO = 0.50; 
    
    
    public Moto(int id, String placa, String modelo, String cor) {
        super(id, placa, modelo, cor);
    }
    
    
    @Override
    public double calcularTarifa(double horas) {
        
        Carro carroEquivalente = new Carro(0, "", "", "");
        double tarifaCarro = carroEquivalente.calcularTarifa(horas);
        return tarifaCarro * DESCONTO_MOTO;
    }
    
    @Override
    public String getTipo() {
        return "Moto";
    }
    
    @Override
    public String toString() {
        return "Moto{" +
                "id=" + getId() +
                ", placa='" + getPlaca() + '\'' +
                ", modelo='" + getModelo() + '\'' +
                ", cor='" + getCor() + '\'' +
                '}';
    }
}
