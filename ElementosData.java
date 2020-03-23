import java.util.Arrays;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ElementosData{
    public static ObservableList<Integer> getQtdadeElementos(){
        List<Integer> qtdadeElementos = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        return FXCollections.observableList(qtdadeElementos);
    }
}