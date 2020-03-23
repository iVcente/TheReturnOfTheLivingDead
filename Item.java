public abstract class Item{
    private String imagem; // Identificador da imagem
    private Celula celula;

    public Item(String imagemInicial,int linInicial,int colInicial){
        this.imagem = imagemInicial;
        Jogo.getInstance().getCelula(linInicial, colInicial).setItem(this);
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

    public abstract int getDano();

    public abstract void removeErva();

    public abstract boolean removeArma();

    public abstract void verificaEstado();
    
    public abstract void influenciaVizinhos();
}