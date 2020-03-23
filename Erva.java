public class Erva extends Item{
    public Erva(int linInicial, int colInicial){
        super("Erva", linInicial, colInicial);
    }

    @Override
    public void removeErva() {
        Jogo.getInstance().removeItem(this);
    }

    @Override
    public boolean removeArma() {
        return false;

    }

    @Override
    public int getDano(){
        return 0;
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
                    if (!( linha == l && coluna == c)){
                        // Recupera o personagem da célula vizinha
                        Personagem p = Jogo.getInstance().getCelula(l,c).getPersonagem();
                        if (p != null && p instanceof Sobrevivente && p.estaVivo()){
                            if (p.getEnergia() < 100){
                                p.cura();
                                p.verificaEstado();
                                removeErva();
                            }else{
                                return;
                            }
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