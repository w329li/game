package wqp2;
import java.util.*;
import java.awt.*;
import java.io.*;
import java.util.Random;



public class Model {
    public int width;
    public int height;
    public int numRightAns;
    public int numAns;
    public String[] ansArr;

    // 0 means no change
    // 1 dead wrong answer, infor store in msg
    // 2 dead loop
    // 3 dead break
    // 4 choose a right answer
    // 5 win
    public int gameState;
    public String msg;
    public String QuestionLine = "empty";
    public String dir = "wqp2/data/";

    public View view;

    public Cell[][] board;
    public Player player;

    public Model(View view, int gender, int level){
        this.view = view;
        view.model = this;
        heForSheMode(gender,level);
        //initModel1("wqp2/data/testMap1.txt");
    }

    public void heForSheMode(int gender, int level){
        String path = "m/map";
        String path2 = "m/ans";
        String path3 = "m/que";
        if(gender == 1){
            path = "f/map";
            path2 = "f/ans";
            path3 = "f/que";
        }
        String fileName = dir + path + Integer.toString(level) + ".txt";
        initModel1(fileName);
        String fileName2 = dir + path2 + Integer.toString(level) + ".txt";
        readAns(fileName2);
        if(gender == 1){
            player.pic = "female.png";
        }
        //only use on question
        String fileName3 = dir + path3 + Integer.toString(level) + ".png";
        view.que.pic = fileName3;

    }

    public void readAns(String str){
        randAns();
        BufferedReader br = null;
        FileReader fr = null;

        try{
            fr = new FileReader(str);
            br = new BufferedReader(fr);

            String line;
            br = new BufferedReader(new FileReader(str));

            line = br.readLine();
            int cidx = 0;
            int widx = 3;
            String[] arrS = line.split(";");
            ansArr = new String[5];
            for(int i = 0; i < height; i++){
                for(int j = 0; j < width;j++){
                    if(board[i][j] == null) continue;
                    if(board[i][j].name == '+'){
                        ansArr[board[i][j].id] = arrS[cidx];
                        cidx++;
                    }
                    if(board[i][j].name == '*'){
                        ansArr[board[i][j].id] = arrS[widx];
                        board[i][j].msg = arrS[widx+2];
                        widx++;
                    }
                }
            }
            view.que.ansArr = ansArr;
            view.dp.ansArr = ansArr;

        }catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void shuffleArray(int[] ar){
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--){
            int index = rnd.nextInt(i + 1);
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    public void randAns(){
        String[] picNames = {"A.png", "B.png", "C.png", "D.png", "E.png"};
        int[] arr = {0,1,2,3,4};
        shuffleArray(arr);
        Cell[] ans = {null, null, null, null, null};

        int k = 0;
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                if(board[i][j] == null){
                    continue;
                }
                if(board[i][j].name == '+' || board[i][j].name == '*'){
                    ans[k] = board[i][j];
                    k++;
                }
            }
        }
        for(int i = 0; i < 5; i++){
            ans[i].id = arr[i];
            ans[i].pic = picNames[arr[i]];
        }
    }


    public void initModel1(String fileName){
        BufferedReader br = null;
        FileReader fr = null;


        try{
            fr = new FileReader(fileName);
            br = new BufferedReader(fr);

            String line;
            br = new BufferedReader(new FileReader(fileName));

            line = br.readLine();
            this.width = Integer.parseInt(line.split(" ")[0]);
            this.height= Integer.parseInt(line.split(" ")[1]);
            this.numRightAns= Integer.parseInt(line.split(" ")[2]);
            this.numAns = numRightAns + Integer.parseInt(line.split(" ")[3]);

            board = new Cell[height][width];
            int i = 0;
            while ((line = br.readLine()) != null) {
                int len = line.length();
                for(int j = 0;j < width;j++){
                    //char c = (char)(line[j]);
                    char c = line.charAt(j);
                    board[i][j] = genCell(c, i, j);
                    //if(board[i][j] != null) {
                    //}
                }
                i++;
            }
            view.viewNotify();

        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (br != null)
                    br.close();
                if (fr != null)
                    fr.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public Cell genCell(char c, int y, int x){
        Cell temp = null;
        switch(c){
            case '@':
                player = new Player(x,y,0);
                temp = player;
                break;
            case 'A':
                temp = new Wall(x,y,1);
                break;
            case '+':
                temp = new RAns(x,y,2);
                break;
            case '*':
                temp = new WAns(x,y,2);
                break;
            case '1':
                temp = new Monster(x,y,3,'1',0,1);
                break;
            case '2':
                temp = new Monster(x,y,4,'2',1,0);
                break;
            case '3':
                temp = new Monster(x,y,5,'3',-1,0);
                break;
            case '4':
                temp = new Monster(x,y,6,'4',0,-1);
                break;
            case '?':
                temp = new GreenPoint(x,y,7);
                break;
            case '&':
                GreenPoint temp2 = new GreenPoint(x,y,1);
                temp2.name = '&';
                temp2.passAble = 0;
                temp2.color = 8;
                temp = (Cell)temp2;
                break;
        }
        return temp;
    }

    public void moveTo(int x,int y){
        int ox = player.x;
        int oy = player.y;
        player.setPos(x, y);
        board[oy][ox] = player.preCell;

        player.preCell = board[y][x];
        board[y][x] = player;

        view.viewNotify();
    }

    public void move(int mx, int my){
        int initX = player.x;
        int initY = player.y;
        int x = player.x;
        int y = player.y;
        int nx, ny;
        while(true){
            nx = x + mx;
            ny = y + my;

            if(nx == -1){
                nx = width-1;
            }
            if(nx == width){
                nx = 0;
            }
            if(ny == -1){
                ny = height-1;
            }
            if(ny == height){
                ny = 0;
            }

            if(board[ny][nx] != null){

                if(nx == initX && ny == initY){
                    gameState = 2;
                    return;
                }
                int code = board[ny][nx].passIt(mx, my);
                if(code == 0){
                    moveTo(x,y);
                    return;
                }
                if(code == -1){
                    //send error message
                    gameState = 3;
                    //view.viewNotify();
                    return;
                }
                if(code == -2){
                    //msg = ((WAns)(board[ny][nx])).msg;
                    msg = Integer.toString(board[ny][nx].id) + ";b;r;k;" + board[ny][nx].msg;

                    gameState = 1;
                    return;
                }
                if(code == 2){
                    //pass a right answer
                    //got point
                    //checkWin
                    numRightAns--;
                    board[ny][nx] = null;
                    if(numRightAns == 0){
                        gameState = 5;
                        gameWin();
                        return;
                    }
                }
            }
            x = nx;
            y = ny;
            //add new
            //moveTo(x,y);
            if(x == initX && y == initY){
                gameState = 2;
                return;
            }
        }
    }

    public void gameWin(){
        //TODO
    }

}

class Cell {
    public int x;
    public int y;
    public int color;
    public char name;

    public String pic;

    public int id = -1;
    public String msg = "empty";

    public Cell(int x,int y, int color,char name){
        this.x = x;
        this.y = y;
        this.color = color;
        this.name = name;
    }

    public int passIt(int mx, int my){
        // 1 passable
        // 0 cannot pass
        // -1 Dead
        // 2 got point
        return 1;
    }

    public void setPos(int x ,int y){
        this.x = x;
        this.y = y;
    }
}

class Player extends Cell {

    public Cell preCell;

    public Player(int x,int y, int color){
        super(x,y,color,'@');
        this.preCell = null;
        pic = "male.png";
    }

    public int passIt(int mx, int my){
        return 0;
    }
}

class Wall extends Cell {

    public Wall(int x,int y, int color){
        super(x,y,color,'A');
        pic = "wall1.png";
    }

    public int passIt(int mx, int my){
        return 0;
    }
}

class RAns extends Cell {

    public RAns(int x,int y, int color){
        super(x,y,color,'+');
    }

    public int passIt(int mx, int my){
        return 2;
    }
}

class WAns extends Cell {

    public WAns(int x,int y, int color){
        super(x,y,color,'*');
    }

    public int passIt(int mx, int my){
        return -2;
    }
}

class Monster extends Cell{
    public int dx = -1;
    public int dy;


    public Monster(int x, int y, int color, char name, int dx, int dy){
        super(x,y,color,name);
        this.dx = dx;
        this.dy = dy;
        int ttnum = dx - 2 * dy;
        switch(ttnum){
            case 1:
                pic = "left.png";
                break;
            case -1:
                pic = "right.png";
                break;
            case 2:
                pic = "down.png";
                break;
            case -2:
                pic = "up.png";
                break;
        }
    }

    public int passIt(int mx, int my){
        if(mx == dx && my == dy){
            return -1;
        }
        return 0;
    }
}

class GreenPoint extends Cell{
    int passAble = 1;
    char greenWall = '&';
    String pic1 = "seed.png";
    String pic2 = "tree.png";

    public GreenPoint (int x, int y, int color){
        super(x,y,color,'?');
        pic = "seed.png";
    }

    public int passIt(int mx, int my){
        if(passAble == 1){
            passAble = 0;
            name = greenWall;
            pic = pic2;
            return 1;
        }
        return 0;
    }
}
