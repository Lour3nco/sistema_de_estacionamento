package sistemdeEstacionar;


public abstract class Veiculo {
    private int id;
    private String placa;
    private String modelo;
    private String cor;
    
    
    public Veiculo(int id, String placa, String modelo, String cor) {
        this.id = id;
        this.placa = placa;
        this.modelo = modelo;
        this.cor = cor;
    }
    
    // Getters
    public int getId() {
        return id;
    }
    
    public String getPlaca() {
        return placa;
    }
    
    public String getModelo() {
        return modelo;
    }
    
    public String getCor() {
        return cor;
    }
    
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setPlaca(String placa) {
        this.placa = placa;
    }
    
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
    
    public void setCor(String cor) {
        this.cor = cor;
    }
    
    
    public abstract double calcularTarifa(double horas);
    
 
    public abstract String getTipo();
    
    @Override
    public String toString() {
        return "Veículo{" +
                "id=" + id +
                ", placa='" + placa + '\'' +
                ", modelo='" + modelo + '\'' +
                ", cor='" + cor + '\'' +
                ", tipo='" + getTipo() + '\'' +
                '}';
    }
}
