public class Pistola extends Item{
    protected int DANO = 25;
    
    public Pistola(int linInicial, int colInicial){
        super("Pistola", linInicial, colInicial);
    }

    @Override
    public int getDano(){
        return DANO;
    }

    @Override
    public void removeErva() {
        // Nao aumenta energia de nenhum personagem
    }

    @Override
    public boolean removeArma(){
        int oldLin = this.getCelula().getLinha();
        int oldCol = this.getCelula().getColuna();
        Jogo.getInstance().getCelula(oldLin, oldCol).setItem(null);
        if(Jogo.getInstance().removeItem(this))  
            return true;
        return false;
        
    }

    @Override
    public void influenciaVizinhos() {
        int linha = this.getCelula().getLinha();
        int coluna = this.getCelula().getColuna();
        for(int l = linha - 1;l <= linha + 1; l++){
            for(int c = coluna - 1; c <= coluna + 1; c++){
                // Se a posição é dentro do tabuleiro
                if (l >= 0 && l < Jogo.NLIN && c >= 0 && c < Jogo.NCOL){
                    // Se não é a propria celula
                    if (linha == l && coluna == c){
                        // Recupera o personagem da célula vizinha
                        Personagem p = Jogo.getInstance().getCelula(l,c).getPersonagem();
                        if (p != null & p instanceof Sobrevivente && p.estaVivo()){
                            if (DANO > p.getDano()){
                                p.setDano(this.DANO);
                                p.verificaEstado();
                                this.removeArma();
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void verificaEstado() {
        
    }
}