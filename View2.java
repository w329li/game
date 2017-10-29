package wqp2;

public class View2 {


    public char[][] currentBoard;
    public Model model;

    public void viewNotify(){
        currentBoard = new char[model.height][model.width];
        for(int i = 0; i < model.height; i ++){
            for(int j = 0; j < model.width; j++){

                //System.out.println("view ==>");
                //System.out.println(i);
                //System.out.println(j);
                if (model.board[i][j] == null){
                    //System.out.println("NULL");
                    currentBoard[i][j] = '.';
                    continue;
                }
                currentBoard[i][j] = model.board[i][j].name;
                //System.out.println(model.board[i][j].name);
            }
        }
        show();

    }

    public void show(){
        for(int i = 0; i < model.height; i ++){
            for(int j = 0; j < model.width; j++){
                System.out.print(currentBoard[i][j]);
            }
            System.out.println("");
        }
    }

}
