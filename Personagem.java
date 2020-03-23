public abstract class Personagem {
    private String imagem; // Identificador da imagem
    private int energia;
    protected boolean infectado;
    private Celula celula;
    protected int dano;

    public Personagem(int energiaInicial, String imagemInicial,int linInicial,int colInicial){
        this.imagem = imagemInicial;
        this.energia = energiaInicial;
        Jogo.getInstance().getCelula(linInicial, colInicial).setPersonagem(this);
    }

    public int getEnergia(){
        return energia;
    }

    public void incrementaEnergia(int valor){
        if (valor < 0) throw new IllegalArgumentException("Valor de energia invalido");
        if (getEnergia() + 50 > 100){
            energia = 100;
        }
        else{
            energia += valor;
        }
    }

    public void diminuiEnergia(int dano){
        energia -= dano;
        if (energia < 0){
            energia = 0;
        }
    }

    public int getDano(){
        return dano;
    }

    public void setDano(int novoDano){
        this.dano = novoDano;
    }

    public boolean infectado(){
        return infectado;
    }

    public void infecta(){
        infectado = true;
    }

    public void cura(){
        incrementaEnergia(50);
    }

    public boolean estaVivo(){
        return getEnergia() > 0;
    }

    public String getImage(){
        return imagem;
    }

    public void setImage(String imagem){
        this.imagem = imagem;
    }

    public Celula getCelula(){
        return celula;
    }

    public void setCelula(Celula celula){
        this.celula = celula;
    }

    // Define próximo movimento
    public abstract void atualizaPosicao();

    // Verifica possiveis atualizações de estado a cada passo
    public abstract void verificaEstado();

    // Define como o personagem influencia os vizinhos
    // Toda vez que chega em uma célula analisa os vizinhos
    // e influencia os mesmos
    public abstract void influenciaVizinhos();
}
