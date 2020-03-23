public class Espingarda extends Item{
    private int DANO = 50;

    public Espingarda(int linInicial, int colInicial){
        super("Espingarda", linInicial, colInicial);
    }

    @Override
    public int getDano(){
        return DANO;
    }

    @Override
    public void removeErva() {
        // Nao e' implementado

    }

    @Override
    public boolean removeArma() {
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
                    // Se não é a propria celulaks
                    if (!( linha == l && coluna == c)){
                        // Recupera o personagem da célula vizinha
                        Personagem p = Jogo.getInstance().getCelula(l,c).getPersonagem();
                        if (p != null & p instanceof Sobrevivente && p.estaVivo()){
                            if (DANO > p.getDano()){
                                p.setDano(this.DANO);
                                p.verificaEstado(); 
                                this.removeArma();
                            }else   
                                return;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void verificaEstado() {
        // TODO Auto-generated method stub

    }
}