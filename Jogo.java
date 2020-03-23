
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

public class Jogo extends Application {
    
    public static final int CELL_WIDTH = 20;
    public static final int CELL_HEIGHT = 20;
    public static final int NLIN = 15;
    public static final int NCOL = 15;
    private Button buttonAvanca, buttonRegras, buttonRetornaJogo, buttonRetornaJogoContexto;
    private Button buttonCarregaArquivo, buttonPartidaRapida, buttonIniciar;
    private Button buttonSalvaPartida, buttonReiniciaPartida, buttonIntroducao;
    private Label labelRegras, labelIntroducao;
    private Text textMenu;
    private ComboBox<Integer> comboBoxSobrevivente, comboBoxZumbi, comboBoxFarrapo;
    private ComboBox<Integer> comboBoxErva, comboBoxPistola, comboBoxEspingarda; 
    private Stage stageMenu, stageJogo, stageRegras, stageIntroducao;
    private GridPane gridMenu, gridMenuE, gridJogo, gridRegras, gridIntroducao;
    private Scene sceneMenu, sceneJogo, sceneRegras, sceneIntroducao;
    private int countSobreviventes, countZumbis, countFarrapos;
    private int countTurnos;
    private static final String REGRAS = 
        "Zumbi comum:\n" +
        " => Energia: 50 pontos\n"  +
        " => Dano: 25 pontos\n" +
        " => Movimenta-se aleatoriamente pelo mapa\n" +
        "Farrapo:\n" +
        " => Energia: 75 pontos\n"  +
        " => Dano: 40 pontos\n" +
        " => Movimenta-se perseguindo um alvo ate' mata-lo ou ser morto\n" +
        "Sobrevivente:\n" +
        " => Energia: 100 pontos\n"  +
        " => Dano: 10 pontos (dano inicial)\n" +
        " => Movimenta-se aleatoriamente pelo mapa\n" +
        " => Quando morre, se transforma em um Zumbi comum\n" +
        "Erva medicinal:\n" +
        " => Cura 50 pontos do Sobrevivente\n"  +
        "Pistola:\n" +
        " => Aumenta o dano causado pelo Sobrevivente para 25 pontos\n" +
        "Espingarda:\n" +
        " => Aumenta o dano causado pelo Sobrevivente para 50 pontos\n";

    public static Jogo jogo = null;

    private Random random;
    private Map<String, Image> imagens;
    private List<Celula> celulas;
    private List<Personagem> personagens;
    private List<Item> itens;

    public static Jogo getInstance(){ 
        return jogo;                        
    }                               
                                     
    public Jogo(){
        jogo = this;
        random = new Random();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Retorna um número aleatorio a partir do gerador unico
    public int aleatorio(int limite){
        return random.nextInt(limite);
    }

    // Retorna a celula de uma certa linha,coluna
    public Celula getCelula(int nLin,int nCol){
        int pos = (nLin*NCOL)+nCol;
        return celulas.get(pos);
    }

    private void loadImagens() {
        imagens = new HashMap<>();

        // Armazena as imagens dos personagens
        Image aux = new Image("file:Imagens\\Zumbi.png"); // file:Imagens\\Zumbi.png
        imagens.put("Zumbi", aux);
        
        aux = new Image("file:Imagens\\ZumbiMorto.png");
        imagens.put("Zumbi morto", aux);
        
        aux = new Image("file:Imagens\\Farrapo.png");
        imagens.put("Farrapo", aux);
        
        aux = new Image("file:Imagens\\FarrapoMorto.png");
        imagens.put("Farrapo morto", aux);
        
        aux = new Image("file:Imagens\\Sobrevivente.png");
        imagens.put("Sobrevivente homem", aux);

        aux = new Image("file:Imagens\\SobreviventeHomemMorrendo.png");
        imagens.put("Sobrevivente homem morrendo", aux);

        aux = new Image("file:Imagens\\RIP3.png");
        imagens.put("Sobrevivente morto", aux);

        aux = new Image("file:Imagens\\Gun1.png");
        imagens.put("Pistola", aux);

        aux = new Image("file:Imagens\\Shotgun1.png");
        imagens.put("Espingarda", aux);

        aux = new Image("file:Imagens\\leaves.png");
        imagens.put("Erva", aux);
        
        aux = new Image("file:Imagens\\Back.png");
        imagens.put("Vazio", aux);

        // Armazena a imagem da celula ula
        imagens.put("Null", null);
    }

    public Image getImage(String id){
        return imagens.get(id);
    }

    public boolean removePersonagem(Personagem p){
        if(personagens.remove(p))
            return true;
        else    
            return false;
    }

    public boolean removeItem(Item item){
        if(itens.remove(item))
            return true;
        else    
            return false;
    }

    public boolean adicionaItem(int linInicial, int colInicial, Item item){
        if (item instanceof Pistola){
            itens.add(new Pistola(linInicial, colInicial));
            return true;
        }
        else if (item instanceof Espingarda){
            itens.add(new Espingarda(linInicial, colInicial));
            return true;
        }
        else if (item instanceof Erva){
            itens.add(new Erva(linInicial, colInicial));
            return true;
        }
        else
            return false;
    }

    public boolean adicionaPersonagem(int linInicial, int colInicial){
        if (personagens.add(new Zumbi(linInicial, colInicial))){
            return true;
        }
        else
            return false;
    }

    @Override
    public void start(Stage primaryStage) {
        
        // Carrega imagens
        loadImagens();

        // Cria a lista de personagens
        personagens = new ArrayList<>(NLIN*NCOL);
        itens = new ArrayList<>(NLIN*NCOL);

        // Define os botoes
        buttonAvanca = new Button("Proximo Turno");
        buttonAvanca.setOnAction(e->jogo.avancaSimulacao());
        buttonRegras = new Button("Regras");
        buttonRegras.setOnAction(e->trataButtonRegras(e));
        buttonRetornaJogo = new Button("Retornar ao Jogo");
        buttonRetornaJogo.setOnAction(e->trataButtonRetornaJogo(e));
        buttonSalvaPartida = new Button("Salvar Partida");
        buttonSalvaPartida.setOnAction(e->trataButtonSalvaPartida(e));
        buttonReiniciaPartida = new Button("Reinicia Partida");
        buttonReiniciaPartida.setOnAction(e->trataButtonReiniciaPartida(e));
        buttonRetornaJogoContexto = new Button("Retornar ao Jogo");
        buttonRetornaJogoContexto.setOnAction(e->trataButtonRetornaJogoContexto(e));
        // Define os labels
        labelRegras = new Label(REGRAS);
        labelIntroducao = new Label(INTRODUCAO);

        /*
         * Configurando os stages
         */

        // Stage principal (Menu)
        stageMenu = primaryStage;
        stageMenu.setTitle("The Return of the Living Dead");
        // Stage do jogo em si
        stageJogo = new Stage();
        stageJogo.setTitle("The Return of the Living Dead");
        // Stage das regras
        stageRegras = new Stage();
        stageRegras.setTitle("Regras do Jogo");
        // Stage da Introducao
        stageIntroducao = new Stage();
        stageIntroducao.setTitle("Contexto do Jogo");

        /*
         * Confugurando os grids
         */
        // Grid do menu
        gridMenu = new GridPane();
        gridMenu.setAlignment(Pos.CENTER);
        gridMenu.setHgap(20);
        gridMenu.setVgap(10);
        gridMenu.setPadding(new Insets(25, 25, 25, 25));
        //gridMenu.setGridLinesVisible(true);
            // Adiciona elementos ao grid
            // Adicionandoo titulo
            textMenu = new Text("Defina o numero de elementos do jogo: ");
            textMenu.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
            gridMenu.add(textMenu, 0, 0, 2, 1);

            // Define grid da esquerda
            gridMenuE = new GridPane();
            gridMenuE.setAlignment(Pos.BASELINE_LEFT);
            gridMenuE.setHgap(4);
            gridMenuE.setVgap(6);
            // Campo do Sobrevivente
            gridMenuE.add(new Label ("Sobreviventes: "), 0, 1);
            comboBoxSobrevivente = new ComboBox<Integer>(ElementosData.getQtdadeElementos());
            gridMenuE.add(comboBoxSobrevivente, 1, 1);
            // Campo do Zumbi
            gridMenuE.add(new Label ("Zumbis:"), 0, 2);
            comboBoxZumbi = new ComboBox<Integer>(ElementosData.getQtdadeElementos());
            gridMenuE.add(comboBoxZumbi, 1, 2);
            // Campo do Farrapo
            gridMenuE.add(new Label ("Farrapos:"), 0, 3);
            comboBoxFarrapo = new ComboBox<Integer>(ElementosData.getQtdadeElementos());
            gridMenuE.add(comboBoxFarrapo, 1, 3);
            // Campo da Erva
            gridMenuE.add(new Label ("Ervas:"), 0, 4);
            comboBoxErva = new ComboBox<Integer>(ElementosData.getQtdadeElementos());
            gridMenuE.add(comboBoxErva, 1, 4);
            // Campo da Pistola
            gridMenuE.add(new Label ("Pistolas:"), 0, 5);
            comboBoxPistola = new ComboBox<Integer>(ElementosData.getQtdadeElementos());
            gridMenuE.add(comboBoxPistola, 1, 5);
            // Campo da Espingarda
            gridMenuE.add(new Label ("Espingardas:"), 0, 6);
            comboBoxEspingarda = new ComboBox<Integer>(ElementosData.getQtdadeElementos());
            gridMenuE.add(comboBoxEspingarda, 1, 6);

            // Botoes 
            // Botao para carregar partida a partir de um arquivo
            buttonCarregaArquivo = new Button();
            buttonCarregaArquivo.setText("Carregar Partida");
            buttonCarregaArquivo.setOnAction(e->this.trataButtonCarregaArquivo(e));
            // Botao para iniciar partida com valores aleatorios
            buttonPartidaRapida = new Button();
            buttonPartidaRapida.setText("Partida Rapida");
            buttonPartidaRapida.setOnAction(e->this.trataButtonPartidaRapida(e));
            // Botao para iniciar partida com valores escolhidos
            buttonIniciar = new Button();
            buttonIniciar.setText("Iniciar Partida com Valores Selecionados");
            buttonIniciar.setOnAction(e->this.trataButtonIniciar(e));
            // Botao para exibir a historia do jogo
            buttonIntroducao = new Button();
            buttonIntroducao.setText("Contexto do Jogo");
            buttonIntroducao.setOnAction(e->this.trataButtonIntroducao(e));
            // Agrupa os botoes em um HBox
            HBox hbBotoes = new HBox(30);
            hbBotoes.setAlignment(Pos.BOTTOM_RIGHT);
            hbBotoes.getChildren().add(buttonIniciar);
            hbBotoes.getChildren().add(buttonPartidaRapida);
            hbBotoes.getChildren().add(buttonCarregaArquivo);
            hbBotoes.getChildren().add(buttonIntroducao);
            //Adiciona  o HBox dentro do grid
            gridMenu.add(hbBotoes, 0, 7);
            //Adiciona o sub grid dentro do grid principal do menu
            gridMenu.add(gridMenuE, 0, 2);




        // Grid do jogo
        gridJogo = new GridPane();
        gridJogo.setAlignment(Pos.CENTER);
        gridJogo.setHgap(10);
        gridJogo.setVgap(10);
        gridJogo.setPadding(new Insets(25, 25, 25, 25));
        //Grid das regras
        gridRegras = new GridPane();
        gridRegras.setAlignment(Pos.CENTER);
        gridRegras.setHgap(10);
        gridRegras.setVgap(10);
        gridRegras.setPadding(new Insets(25, 25, 25, 25)); // Aparentemente nao e' obrigatorio
        gridRegras.add(labelRegras, 0, 0);
        gridRegras.add(buttonRetornaJogo, 1, 1);

        // Grid do contexto
        gridIntroducao = new GridPane();
        gridIntroducao.setAlignment(Pos.CENTER);
        gridIntroducao.setHgap(10);
        gridIntroducao.setVgap(10);
        gridIntroducao.setPadding(new Insets(25, 25, 25, 25)); // Aparentemente nao e' obrigatorio
        gridIntroducao.add(labelIntroducao, 0, 0);
        gridIntroducao.add(buttonRetornaJogoContexto, 1, 1);

        /*
         * Define scenes
         */
        // Define a scene do menu
        sceneMenu = new Scene(gridMenu, 800, 400);
        
        // Define a scene do jogo
        HBox hbJogo = new HBox(10);
        hbJogo.setAlignment(Pos.CENTER);
        hbJogo.setPadding(new Insets(25, 25, 25, 25));
        hbJogo.getChildren().add(gridJogo);
        hbJogo.getChildren().add(buttonAvanca);
        hbJogo.getChildren().add(buttonRegras);  
        hbJogo.getChildren().add(buttonSalvaPartida);
        hbJogo.getChildren().add(buttonReiniciaPartida);

        sceneJogo = new Scene(hbJogo);

        // Define a scene das regras
        sceneRegras = new Scene(gridRegras, 600,500);

        // Define a scene do contexto
        sceneIntroducao = new Scene(gridIntroducao, 1150, 500);
        

        // Define a scene para para o stage
        stageJogo.setScene(sceneJogo);
        stageRegras.setScene(sceneRegras);
        stageIntroducao.setScene(sceneIntroducao);
        
        stageMenu.setScene(sceneMenu);
        stageMenu.show();

        // Monta o "tabuleiro"
        celulas = new ArrayList<>(NLIN*NCOL);
        for (int lin = 0; lin < NLIN; lin++) {
            for (int col = 0; col < NCOL; col++) {
                Celula cel = new Celula(lin,col);
                cel.setOnAction(e->cliqueNaCelula(e));
                celulas.add(cel);
                gridJogo.add(cel, col, lin);
            }
        }

        //countSobreviventes = comboBoxSobrevivente.getSelectionModel().getSelectedItem();
        //countZumbis = comboBoxZumbi.getSelectionModel().getSelectedItem();
        //countFarrapos = comboBoxFarrapo.getSelectionModel().getSelectedItem();
        //countTurnos = 0;
    }

    public void setQtdadeSobreviventes(int qtdadeSobreviventes){
        // Cria Sobreviventes em posicoes aleatorias
        for(int i=0;i<qtdadeSobreviventes;i++){
            boolean posOk = false;
            while(!posOk){
                int lin = random.nextInt(NLIN);
                int col = random.nextInt(NCOL);
                if (this.getCelula(lin, col).getPersonagem() == null){
                    personagens.add(new Sobrevivente(lin,col));
                    posOk = true;
                }
            }
        }
    }
    
    public void setQtdadeZumbis(int qtdadeZumbis){
        // Cria Zumbis em posições aleatórias
        for(int i=0;i<qtdadeZumbis;i++){
            boolean posOk = false;
            while(!posOk){
                int lin = random.nextInt(NLIN);
                int col = random.nextInt(NCOL);
                if (this.getCelula(lin, col).getPersonagem() == null){
                    personagens.add(new Zumbi(lin,col));
                    posOk = true;
                }
            }
        }
    }

    public void setQtdadeFarrapos(int qtdadeFarrapos){
        // Cria Farrapos em posições aleatórias
        for(int i=0;i<qtdadeFarrapos;i++){
            boolean posOk = false;
            while(!posOk){
                int lin = random.nextInt(NLIN);
                int col = random.nextInt(NCOL);
                if (this.getCelula(lin, col).getPersonagem() == null){
                    personagens.add(new Farrapo(lin,col));
                    posOk = true;
                }
            }
        }
    }

    public void setQtdadeErvas(int qtdadeErvas){
        // Cria Ervas em posições aleatórias
        for(int i=0;i<qtdadeErvas;i++){
            boolean posOk = false;
            while(!posOk){
                int lin = random.nextInt(NLIN);
                int col = random.nextInt(NCOL);
                if (this.getCelula(lin, col).getItem() == null){
                    itens.add(new Erva(lin,col));
                    posOk = true;
                }
            }
        }
    }

    public void setQtdadePistolas(int qtdadePistolas){
        // Cria Pistolas em posições aleatórias
        for(int i=0;i<qtdadePistolas;i++){
            boolean posOk = false;
            while(!posOk){
                int lin = random.nextInt(NLIN);
                int col = random.nextInt(NCOL);
                if (this.getCelula(lin, col).getItem() == null){
                    itens.add(new Pistola(lin,col));
                    posOk = true;
                }
            }
        }
    }

    public void setQtdadeEspingardas(int qtdadeEspingardas){
        // Cria Espingardas em posições aleatórias
        for(int i=0;i<qtdadeEspingardas;i++){
            boolean posOk = false;
            while(!posOk){
                int lin = random.nextInt(NLIN);
                int col = random.nextInt(NCOL);
                if (this.getCelula(lin, col).getItem() == null){
                    itens.add(new Espingarda(lin,col));
                    posOk = true;
                }
            }
        }
    }

    public void trataButtonIntroducao(ActionEvent e){
        stageIntroducao.showAndWait();
    }

    public void trataButtonIniciar(ActionEvent e){
        setQtdadeSobreviventes(comboBoxSobrevivente.getSelectionModel().getSelectedItem());
        setQtdadeZumbis(comboBoxZumbi.getSelectionModel().getSelectedItem());
        setQtdadeFarrapos(comboBoxFarrapo.getSelectionModel().getSelectedItem());
        setQtdadeErvas(comboBoxErva.getSelectionModel().getSelectedItem());
        setQtdadePistolas(comboBoxPistola.getSelectionModel().getSelectedItem());
        setQtdadeEspingardas(comboBoxEspingarda.getSelectionModel().getSelectedItem());
        stageMenu.close();
        stageJogo.showAndWait();
    }

    public void trataButtonPartidaRapida(ActionEvent e){
        setQtdadeSobreviventes(aleatorio(9));
        setQtdadeZumbis(aleatorio(9));
        setQtdadeFarrapos(aleatorio(9));
        setQtdadeErvas(aleatorio(9));
        setQtdadePistolas(aleatorio(9));
        setQtdadeEspingardas(aleatorio(9));
        stageMenu.close();
        stageJogo.showAndWait();
    }

    public void trataButtonReiniciaPartida(ActionEvent e){
        stageJogo.close();
        personagens.clear();
        itens.clear();
        celulas.clear();
        start(stageJogo);
        carregaPartidaPersonagens();
        carregaPartidaItens();
        
        stageMenu.close();
        stageJogo.showAndWait();
    }

    public void trataButtonSalvaPartida(ActionEvent e){
        salvaPartidaPersonagens();
        salvaPartidaItens();
    }

    public void trataButtonCarregaArquivo(ActionEvent e){
        carregaPartidaPersonagens();
        carregaPartidaItens();
        stageMenu.close();
        stageJogo.showAndWait();
    }

    public void trataButtonRegras(ActionEvent e){
        stageRegras.showAndWait();
    }
    
    public void trataButtonRetornaJogo(ActionEvent e){
        stageRegras.close();
    }

    public void trataButtonRetornaJogoContexto(ActionEvent e){
        stageIntroducao.close();
    }

    public void avancaSimulacao(){
        salvaPartidaPersonagens();
        salvaPartidaItens();
        System.out.println("Quantidade de Sobreviventes: "+ countSobreviventes);
        System.out.println("Quantidade de Zumbis: " + countZumbis);
        System.out.println("Quantidade de Farrapos: " + countFarrapos);
        System.out.println("Numero de turnos: " + countTurnos);
        // Avança um passo em todos os personagens
        personagens.forEach(p->{
            if (p.estaVivo()){
                p.atualizaPosicao();
                p.verificaEstado();
                p.influenciaVizinhos();
            }
        }
        );

        itens.forEach(i->{
                i.influenciaVizinhos();
        });
        long zumbisVivos = personagens
                    .stream()
                    .filter(p->(p instanceof Zumbi || p instanceof Farrapo))
                    .filter(p->p.estaVivo())
                    .count();
        if (zumbisVivos == 0){
            Alert msgBox = new Alert(AlertType.INFORMATION);
            msgBox.setHeaderText("Fim de Jogo");
            msgBox.setContentText("Todos os zumbis foram eliminados! A raca humana sobrevive por mais um dia!");
            msgBox.showAndWait();
            System.exit(0);
        }

        long humanosVivos = personagens
                    .stream()
                    .filter(p->!(p instanceof Zumbi))
                    .filter(p->!(p instanceof Farrapo))
                    .filter(p->p.estaVivo())
                    .count();
        if (humanosVivos == 0){
            Alert msgBox = new Alert(AlertType.INFORMATION);
            msgBox.setHeaderText("Fim de Jogo");
            msgBox.setContentText("Todos os humanos morreram! Os Zumbis devoraram as pobres almas!");
            msgBox.showAndWait();
            System.exit(0);
        }
        countTurnos++;
    }

    public void cliqueNaCelula(ActionEvent e){
        Celula c = (Celula)e.getSource();
        System.out.println("Celula: linha = "+c.getLinha()+" coluna = "+c.getColuna());
        Personagem p = c.getPersonagem();
        Item i = c.getItem();
        if (p instanceof Personagem){
            System.out.println("Personagem : " + p.getClass());
            System.out.println("Energia do personagem: " + p.getEnergia());
            System.out.println("Dano do personagem: " + p.getDano());
        }
        else if(i instanceof Item){
            System.out.println("Item : " + i.getClass());
            System.out.println("Dano: " + i.getDano());
        }
    }

    public void salvaPartidaPersonagens(){
        String fName = "ProgressoPersonagens.dat";
        String currDir = Paths.get("").toAbsolutePath().toString();
        String nameComplete = currDir+"\\"+fName;
        Path path = Paths.get(nameComplete);
        // defaultCharset retorna a codificação padrão de textos (usualmente UTF-8)
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(path, StandardCharsets.UTF_8))){
          for(Personagem p:personagens){
                String linha = p.getClass()+","+
                               p.getCelula().getLinha() +","+
                               p.getCelula().getColuna();
                writer.println(linha);
            }
        }catch (IOException x){
          System.err.format("Erro de E/S: %s%n", x);
       }
    }

    public void salvaPartidaItens(){
        String fName = "ProgressoItens.dat";
        String currDir = Paths.get("").toAbsolutePath().toString();
        String nameComplete = currDir+"\\"+fName;
        Path path = Paths.get(nameComplete);
        // defaultCharset retorna a codificação padrão de textos (usualmente UTF-8)
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(path, StandardCharsets.UTF_8))){
            for(Item i:itens){
                String linha = i.getClass()+","+
                               i.getCelula().getLinha() +","+
                               i.getCelula().getColuna();
                writer.println(linha);
            }
        }catch (IOException x){
          System.err.format("Erro de E/S: %s%n", x);
       }
    }

    public void carregaPartidaPersonagens(){
        String fName = "ProgressoPersonagens.dat";
        String currDir = Paths.get("").toAbsolutePath().toString();
        String nameComplete = currDir+"\\"+fName;
        Path path = Paths.get(nameComplete);
        try (Scanner sc = new Scanner(Files.newBufferedReader(path, StandardCharsets.UTF_8))){
           while (sc.hasNext()){
               String linha = sc.nextLine();
               String dados[] = linha.split(",");
               String personagem = dados[0];
               int linhaP = Integer.parseInt(dados[1]);
               int colunaP = Integer.parseInt(dados[2]); 
               adicionaPersonagemArquivo(personagem, linhaP, colunaP);
           }
        }catch (IOException x){
            System.err.format("Erro de E/S: %s%n", x);
        }
    }

    public void carregaPartidaItens(){
        String fName = "ProgressoItens.dat";
        String currDir = Paths.get("").toAbsolutePath().toString();
        String nameComplete = currDir+"\\"+fName;
        Path path = Paths.get(nameComplete);
        try (Scanner sc = new Scanner(Files.newBufferedReader(path, StandardCharsets.UTF_8))){
           while (sc.hasNext()){
               String linha = sc.nextLine();
               String dados[] = linha.split(",");
               String item = dados[0];
               int linhaI = Integer.parseInt(dados[1]);
               int colunaI = Integer.parseInt(dados[2]); 
               adicionaItemArquivo(item, linhaI, colunaI);
           }
        }catch (IOException x){
            System.err.format("Erro de E/S: %s%n", x);
        }
    }

    public void adicionaPersonagemArquivo(String p, int lin, int col){
        if (p.equals("class Sobrevivente"))
            personagens.add(new Sobrevivente(lin, col));
        else if (p.equals("class Zumbi"))
            personagens.add(new Zumbi(lin, col));
        else if (p.equals("class Farrapo"))
            personagens.add(new Farrapo(lin, col));
    }

    public void adicionaItemArquivo(String i, int lin, int col){
        if (i.equals("class Erva"))
            itens.add(new Erva(lin, col));
        else if (i.equals("class Pistola"))
            itens.add(new Pistola(lin,col));
        else if(i.equals("class Espingarda"))
            itens.add(new Espingarda(lin,col));
    }

    private static final String INTRODUCAO = 
    "Em uma pequena cidade do interior do Rio Grande do Sul, ha relatos de mortos estarem se erguendo, tanto dos arredores da \n" +
    "regiao quanto de suas covas no cemiterio, sabe-se que a cidadezinha foi batizada de acordo com a lingua dos indigenas locais, portanto, seguindo os registros historicos,\n" +
    "sabemos que ha diversos aterramentos indigenas ali por perto, nao somente isso, foram deixados resquicios da Guerra dos Farrapos na area "+
    "(a cidade foi o epicentro do evento),\n " +
    "soldados guerreiros ja ha muito mortos levantam-se de seu descanso eterno iniciando sua jornada na tentativa de conter sua fome insaciavel por cerebros humanos.\n"+
    "Os humanos que ainda estao vivos se equiparam com armas e utensilios de combate corpo a corpo que possuiam em suas residencias, ha armas de fogo espalhadas pela cidade, \n"+
    "novas e antigas, reliquias de um passado nao tao distante assim. O povo local tambem conta com ervas medicinais utilizadas pelos povos indigenas que moravam ali.\n"+
    "Por algum motivo desconhecido, alem dos zumbis comuns, ha um zumbi mais mortal, \"o Farrapo\" - ele leva esse nome por carregar vestimentas dos soldados ja mortos, \n"+
    "cre-se que esses zumbis se tornaram mais perigosos devido o contato com as antigas zonas de mineracao da cidade, havendo ate lagos contaminados de metais e minerais na regiao\n"+
    "e de algum modo evoluiram.\n"+
    "Ele e' mais perigoso e apos ser atraido a um humano, a unica forma de para-lo e' explodindo seus miolos.\n"+
    "Apos os humanos serem mortos pelos zumbis eles tambem se tornam zumbis, o que faz com que os vivos estejam perdendo as esperancas de escapar deste pesadelo,\n"+
    "o trabalho em equipe, aliado com uma boa dose de sorte pode ser a chave para terminar este episodio aterrorizante e colocar os mortos para descansarem por uma ultima vez.";

}
