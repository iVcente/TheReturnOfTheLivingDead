public class Zumbi extends Personagem{
    public Zumbi(int linInicial, int colInicial){
        super(50, "Zumbi", linInicial, colInicial);
        this.infectado = true;
        this.dano = 25;
    }
        
    @Override
    public void atualizaPosicao() {
        int dirLin = Jogo.getInstance().aleatorio(3)-1;
        int dirCol = Jogo.getInstance().aleatorio(3)-1;
        int oldLin = this.getCelula().getLinha();
        int oldCol = this.getCelula().getColuna();
        int lin = oldLin + dirLin;
        int col = oldCol + dirCol;
        if (lin < 0) lin = 0;
        if (lin >= Jogo.NLIN) lin = Jogo.NLIN-1;
        if (col < 0) col = 0;
        if (col >= Jogo.NCOL) col = Jogo.NCOL-1;
        if (Jogo.getInstance().getCelula(lin, col).getPersonagem() != null){
            return;
        }else{
            // Limpa celula atual
            Jogo.getInstance().getCelula(oldLin, oldCol).setPersonagem(null);
            // Coloca personagem na nova posição
            Jogo.getInstance().getCelula(lin, col).setPersonagem(this);
        }
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
                            p.diminuiEnergia(this.dano);
                            System.out.println("!!!!Zumbi atacando!!!!");
                            System.out.println("Energia do Sobrevivente: " + p.getEnergia());
                            p.verificaEstado();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void verificaEstado(){
        if (this.estaVivo())
            return;
        if (!this.estaVivo()){
            this.setImage("Zumbi morto");
            this.getCelula().setImageFromPersonagem();
        }
    }
}