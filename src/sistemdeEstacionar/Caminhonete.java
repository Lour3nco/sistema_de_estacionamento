package sistemdeEstacionar;


public class Caminhonete extends Veiculo {
    private static final double TAXA_CAMINHONETE = 1.50; // 150% do valor do carro
    
    
    public Caminhonete(int id, String placa, String modelo, String cor) {
        super(id, placa, modelo, cor);
    }
    
    
    @Override
    public double calcularTarifa(double horas) {
        
        Carro carroEquivalente = new Carro(0, "", "", "");
        double tarifaCarro = carroEquivalente.calcularTarifa(horas);
        return tarifaCarro * TAXA_CAMINHONETE;
    }
    
    @Override
    public String getTipo() {
        return "Caminhonete";
    }
    
    @Override
    public String toString() {
        return "Caminhonete{" +
                "id=" + getId() +
                ", placa='" + getPlaca() + '\'' +
                ", modelo='" + getModelo() + '\'' +
                ", cor='" + getCor() + '\'' +
                '}';
    }
}
