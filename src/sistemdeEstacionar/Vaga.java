package sistemdeEstacionar;


public class Vaga {
    private int id;
    private int numero;
    private boolean ocupada;
    
    
    public Vaga(int id, int numero, boolean ocupada) {
        this.id = id;
        this.numero = numero;
        this.ocupada = ocupada;
    }
    
    
    public int getId() {
        return id;
    }
    
    public int getNumero() {
        return numero;
    }
    
    public boolean isOcupada() {
        return ocupada;
    }
    
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setNumero(int numero) {
        this.numero = numero;
    }
    
    public void setOcupada(boolean ocupada) {
        this.ocupada = ocupada;
    }
    
    @Override
    public String toString() {
        return "Vaga{" +
                "id=" + id +
                ", numero=" + numero +
                ", ocupada=" + ocupada +
                '}';
    }
}


