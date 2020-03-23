public class Sobrevivente extends Personagem{

    public Sobrevivente(int linInicial, int colInicial){
        super(100, "Sobrevivente homem", linInicial, colInicial);
        this.infectado = false;
        this.dano = 10;
    }

    @Override
    public void cura(){
        super.cura();
        if (this.getEnergia() >= 50){
            this.setImage("Sobrevivente homem");
            this.getCelula().setImageFromPersonagem();   
        }
    }

    @Override
    public void atualizaPosicao(){
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
    public void influenciaVizinhos(){
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
                        if (p != null & p instanceof Personagem && p.infectado() && p.estaVivo()){
                            p.diminuiEnergia(this.dano);
                            System.out.println("####Sobrevivente atacando####");
                            System.out.println("Energia do inimigo: " + p.getEnergia());
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
        else{
            this.setImage("Sobrevivente morto");
            this.getCelula().setImageFromPersonagem();
            transformaEmZumbi();
        }                      
    }

    private void transformaEmZumbi(){
        int linhaAtual = this.getCelula().getLinha();
        int colunaAtual = this.getCelula().getColuna();
        Jogo.getInstance().removePersonagem(this);
        Jogo.getInstance().adicionaPersonagem(linhaAtual, colunaAtual);
    }
}