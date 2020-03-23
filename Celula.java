import javafx.scene.image.ImageView;

public class Celula extends javafx.scene.control.Button {
    private Personagem personagem;
    private int linha;
    private int coluna;
    private Item item;

    public Celula(int linha, int coluna){
        this.linha = linha;
        this.coluna = coluna;
        personagem = null;
        item = null;
        setImageFromPersonagem();

    }

    public void setImageFromPersonagem(){
        if (personagem != null){
            String imagem = this.getPersonagem().getImage();
            ImageView iVaux = new ImageView(Jogo.getInstance().getImage(imagem));
            iVaux.setFitWidth(Jogo.CELL_WIDTH);
            iVaux.setFitHeight(Jogo.CELL_HEIGHT);
            setGraphic(iVaux);        
        }else{
            ImageView iVaux = new ImageView(Jogo.getInstance().getImage("Vazio"));
            iVaux.setFitWidth(Jogo.CELL_WIDTH);
            iVaux.setFitHeight(Jogo.CELL_HEIGHT);
            setGraphic(iVaux);        
        }
    }

    public void setImageFromItem(){
        if (item != null){
            String imagem = this.getItem().getImage();
            ImageView iVaux = new ImageView(Jogo.getInstance().getImage(imagem));
            iVaux.setFitWidth(Jogo.CELL_WIDTH);
            iVaux.setFitHeight(Jogo.CELL_HEIGHT);
            setGraphic(iVaux);        
        }else{
            ImageView iVaux = new ImageView(Jogo.getInstance().getImage("Vazio"));
            iVaux.setFitWidth(Jogo.CELL_WIDTH);
            iVaux.setFitHeight(Jogo.CELL_HEIGHT);
            setGraphic(iVaux);        
        }
    }

    public Personagem getPersonagem() {
        return personagem;
    }
    
    public Item getItem() {
        return item;
    }

    public void setPersonagem(Personagem personagem) {
        this.personagem = personagem;
        if (personagem != null){
            personagem.setCelula(this);
        }
        setImageFromPersonagem();
    }

    public void setItem(Item item) {
        this.item = item;
        if (item != null){
            item.setCelula(this);
        }
        setImageFromItem();
    }

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }

    public int getColuna() {
        return coluna;
    }

    public void setColuna(int coluna) {
        this.coluna = coluna;
    }
}