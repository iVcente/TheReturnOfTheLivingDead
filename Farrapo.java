public class Farrapo extends Personagem{ 
    private Personagem alvo;             
                                        
    public Farrapo(int linInicial, int colInicial){ 
        super(75, "Farrapo", linInicial, colInicial);
        alvo = null;
        this.infectado = true;
        this.dano = 40;
    }
    
    private Personagem defineAlvo(){
        for (int l = 0; l < Jogo.NLIN; l++){
            for (int c = 0; c < Jogo.NCOL; c++){
                Personagem p = Jogo.getInstance().getCelula(l, c).getPersonagem();
                if (p != null && p instanceof Sobrevivente && p.estaVivo()){ // p != null && p instanceof Personagem && !p.infectado()
                    alvo = p;
                    return p;
                }
            }
        }
        return null;
    }

    @Override
    public void atualizaPosicao(){
        if (alvo == null || alvo.infectado()){
            alvo = defineAlvo();
            return;
        }

        // Pega posicao atual do Farrapo
        int oldLin = this.getCelula().getLinha();
        int oldCol = this.getCelula().getColuna();

        // Pega a posicao do alvo
        int linAlvo = alvo.getCelula().getLinha();
        int colAlvo = alvo.getCelula().getColuna();

        // Calcula o deslocamento
        int lin = oldLin;
        int col = oldCol;
        if (lin < linAlvo) lin++;
        if (lin > linAlvo) lin--;
        if (col < colAlvo) col++;
        if (col > colAlvo) col--;

        // Verifica se não saiu dos limites do tabuleiro
        if (lin < 0) lin = 0;
        if (lin >= Jogo.NLIN) lin = Jogo.NLIN-1;
        if (col < 0) col = 0;
        if (col >= Jogo.NCOL) col = Jogo.NCOL-1;

        // Verifica se não quer ir para uma celula ocupada
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
                        if (p != null && p instanceof Sobrevivente && p.estaVivo()){
                            p.diminuiEnergia(this.dano);
                            System.out.println("----Farrapo atacando----");
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
            if (!alvo.estaVivo())
                defineAlvo();
        if (!this.estaVivo()){
            this.setImage("Farrapo morto");
            this.getCelula().setImageFromPersonagem();
            return;
        }
    }
}