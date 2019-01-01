
package reversi_twoplayer;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Reversi_TwoPlayer extends JFrame {
   public static final int ROWS = 8;  
   public static final int COLS = 8;
 
  
   public static final int CELL_SIZE = 50; 
   public static final int CANVAS_WIDTH = CELL_SIZE * COLS;  
   public static final int CANVAS_HEIGHT = CELL_SIZE * ROWS;
   public static final int GRID_WIDTH = 4;                   
   public static final int GRID_WIDHT_HALF = GRID_WIDTH / 2; 
   
   public static final int CELL_PADDING = CELL_SIZE / 6;
   public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2; // width/height
   public static final int SYMBOL_STROKE_WIDTH = 4; // pen's stroke width
 
   public enum GameState {
      PLAYING, DRAW, RED_WON, BLUE_WON
   }
   private GameState currentState;  

   public enum Seed {
      EMPTY, RED, BLUE
   }
   private Seed currentPlayer;  
 
   private Seed[][] board   ; 
   private DrawCanvas canvas;
   private JLabel statusBar;  
 
   
   public Reversi_TwoPlayer() {
      canvas = new DrawCanvas(); 
      canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
 
      canvas.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {  
            int mouseX = e.getX();
            int mouseY = e.getY();
            
            int rowSelected = mouseY / CELL_SIZE;
            int colSelected = mouseX / CELL_SIZE;
 
            if (currentState == GameState.PLAYING) {
                if (rowSelected >= 0 && rowSelected < ROWS && colSelected >= 0 && colSelected < COLS && board[rowSelected][colSelected] == Seed.EMPTY ) {
                    
                    int count = 0,row = rowSelected-1,col = colSelected - 1,condition=0, condition1 = 0, condition2 = 0, condition3 = 0, condition4 = 0, condition5 = 0, condition6 = 0, condition7 = 0, condition8 = 0;

                    while( row >= 0 && board[row][colSelected] != currentPlayer ) { // to the north
                      row--;
                      count++;
                    }
                    if( row >= 0 && count > 0 ) {
                        condition1 = 1;
                    }
                    count = 0;
                    while( col >= 0 && board[rowSelected][col] != currentPlayer ) {   //to the left
                        col--;
                        count++;
                    }

                    if ( col >= 0 && count > 0 ) {
                       condition2 = 1;
                    }

                    row = rowSelected + 1;
                    count = 0;

                    while( row < ROWS && board[row][colSelected] != currentPlayer ) {  //to the south
                        row++;
                        count++;
                    } 
                    if ( row < ROWS && count > 0 ) {
                       condition3 = 1;
                    }
                    col = colSelected +1;
                    count = 0;
                    while( col < COLS && board[rowSelected][col] != currentPlayer ) {  //to the right
                        count++;
                        col++;
                    }
                    if( col < COLS && count > 0 ) {
                       condition4 = 1;
                    }

                    row = rowSelected-1;
                    col = colSelected-1;
                    count = 0;

                    while( col >=0 && row >= 0 && board[row][col] != currentPlayer ) {  //to the upper left diagonal
                        col--;
                        row--;
                        count++;
                    }
                    if( col >= 0 && row >= 0 && count > 0 ) {
                        condition5 = 1;
                    }
                    row = rowSelected+1;
                    col = colSelected+1;
                    count = 0;
                    while( col < COLS && row < ROWS && board[row][col] != currentPlayer ) {  //to the lower right diagonal
                        col++;
                        row++;
                        count++;
                    }
                    if( col < COLS && row < ROWS && count > 0) {
                       condition6 = 1;
                    }

                    row = rowSelected+1;
                    col = colSelected-1;
                    count = 0;
                    while( col >=0 && row < ROWS && board[row][col] != currentPlayer  ) {  //to the lower left diagonal
                        col--;
                        row++;
                        count++;
                    }
                    if( col >= 0 && row < ROWS && count > 0 ) {
                       condition7 = 1;
                    }

                    col = colSelected+1;
                    row = rowSelected-1;
                    count = 0;
                    while( col < COLS && row >= 0 && board[row][col] != currentPlayer) {  //to the upper right diagonal
                        col++;
                        row--;
                        count++;
                    }
                    if( col < COLS && row >= 0 && count > 0) {
                        condition8 = 1;
                    }
                    
                    Seed opponent = (currentPlayer==Seed.RED)? Seed.BLUE: Seed.RED;
                    if( rowSelected-1 >= 0 && board[rowSelected-1][colSelected] == opponent ||
                        rowSelected+1 < ROWS && board[rowSelected+1][colSelected] == opponent ||
                        colSelected-1 >= 0 && board[rowSelected][colSelected-1] == opponent ||
                        colSelected+1 < COLS && board[rowSelected][colSelected+1] == opponent  ) {
                        condition = 1;
                    }
                    
                    if( condition==1 && (condition1 == 1 ||condition2 == 1 ||condition3 == 1 ||condition4 == 1 ||condition5 == 1 ||condition6 == 1 ||condition7 == 1 ||condition8 == 1 )) {
                         board[rowSelected][colSelected] = currentPlayer; 
                        updateGame(currentPlayer, rowSelected, colSelected); 
                 
                        currentPlayer = (currentPlayer == Seed.RED) ? Seed.BLUE : Seed.RED; 
                    }
                }
            } else {       
               initGame(); 
            }
            
            repaint(); 
         }
      });
 
      statusBar = new JLabel("  ");
      statusBar.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 15));
      statusBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 4, 5));
 
      Container cp = getContentPane();
      cp.setLayout(new BorderLayout());
      cp.add(canvas, BorderLayout.CENTER);
      cp.add(statusBar, BorderLayout.PAGE_END); 
 
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      pack(); 
      setTitle("Reversi");
      setVisible(true);  
 
      board = new Seed[ROWS][COLS]; 
      initGame(); 
   }
 
   
   public void initGame() {
      for (int row = 0; row < ROWS; ++row) {
         for (int col = 0; col < COLS; ++col) {
            board[row][col] = Seed.EMPTY; 
         }
      }
      
      board[3][3] = Seed.BLUE;
      board[4][4] = Seed.BLUE;
      board[3][4] = Seed.RED;
      board[4][3] = Seed.RED;
      
      currentState = GameState.PLAYING; 
      currentPlayer = Seed.RED;       
   }
 
   
   public void updateGame(Seed theSeed, int rowSelected, int colSelected) {
       if(isFull()) {
           int red = 0, blue = 0;
           for( int i = 0; i < ROWS; i++ ) {
               for( int j = 0; j < COLS ; j++ ) {
                   if( board[i][j] == Seed.RED ) {
                       red++;
                   } else {
                       blue++;
                   }
               }
           }
           if( blue > red ) {
               currentState = GameState.BLUE_WON;
           } else if( red > blue ) {
               currentState = GameState.RED_WON;
           } else {
               currentState = GameState.DRAW;
           }
       } else {
           Seed opponent = (theSeed == Seed.BLUE) ? Seed.RED : Seed.BLUE;
           int row = rowSelected-1;
           while( row >= 0 && board[row][colSelected] != theSeed ) { // to the north
             row--;
           }
           if( row >= 0 ) {
               for( int i = row+1; i < rowSelected; i++ ) {
                   if( board[i][colSelected] == opponent ) {
                         board[i][colSelected] = theSeed;
                   }
               }
           }
           int col = colSelected - 1;
           while( col >= 0 && board[rowSelected][col] != theSeed ) {   //to the left
               col--;
           }
           
           if ( col >= 0 ) {
               for( int i = col+1; i < colSelected; i++ ) {
                   if( board[rowSelected][i] == opponent ) {
                        board[rowSelected][i] = theSeed;
                   }
               }
           }
           row = rowSelected + 1;
           while( row < ROWS && board[row][colSelected] != theSeed ) {  //to the south
               row++;
           } 
           if ( row < ROWS ) {
               for( int i = row-1; i > rowSelected; i-- ) {
                   if( board[i][colSelected] == opponent ) {
                        board[i][colSelected] = theSeed;
                   }
               }
           }
           col = colSelected +1;
           while( col < COLS && board[rowSelected][col] != theSeed ) {  //to the right
               col++;
           }
           if( col < COLS ) {
               for( int i = col-1; i > colSelected; i-- ) {
                   if( board[rowSelected][i] == opponent ) {
                        board[rowSelected][i] = theSeed;
                   }
               }
           }
           row = rowSelected-1;
           col = colSelected-1;
           while( col >=0 && row >= 0 && board[row][col] != theSeed ) {  //to the upper left diagonal
               col--;
               row--;
               
           }
           if( col >= 0 && row >= 0 ) {
               int i, j;
               for( i = col+1, j = row+1; i < colSelected && j < rowSelected; i++, j++ ) {
                   if( board[j][i] == opponent ) {
                        board[j][i] = theSeed;
                   }
               }
           }
           row = rowSelected+1;
           col = colSelected+1;
           while( col < COLS && row < ROWS && board[row][col] != theSeed ) {  //to the lower right diagonal
               col++;
               row++;
               
           }
           if( col < COLS && row < ROWS ) {
               int i, j;
               for( i = col-1, j = row-1; i > colSelected  && j > rowSelected; i--, j-- ) {
                   if( board[j][i] == opponent ) {
                        board[j][i] = theSeed;
                   }
               }
           }
           row = rowSelected+1;
           col = colSelected-1;
           while( col >=0 && row < ROWS && board[row][col] != theSeed ) {  //to the lower left diagonal
               col--;
               row++;
               
           }
           if( col >= 0 && row < ROWS ) {
               int i, j;
               for( i = col+1, j = row-1; i < colSelected && j > rowSelected; i++, j-- ) {
                   if( board[j][i] == opponent ) {
                        board[j][i] = theSeed;
                   }
               }
           }
           
           col = colSelected+1;
           row = rowSelected-1;
           
           while( col < COLS && row >= 0 && board[row][col] != theSeed ) {  //to the upper right diagonal
               col++;
               row--;
        
           }
           if( col < COLS && row >= 0 ) {
               int i, j;
               for( i = col-1, j = row+1; i > colSelected && j < rowSelected; i--, j++ ) {
                   if( board[j][i] == opponent ) {
                        board[j][i] = theSeed;
                   }
               }
           }
           
       }
   }

   public boolean isFull() {
      for (int row = 0; row < ROWS; ++row) {
         for (int col = 0; col < COLS; ++col) {
            if (board[row][col] == Seed.EMPTY) {
               return false; 
            }
         }
      }
      return true;  
   }
 
   
   class DrawCanvas extends JPanel {
      @Override
      public void paintComponent(Graphics g) {  
         super.paintComponent(g);    
         setBackground(Color.WHITE); 
 
         g.setColor(Color.LIGHT_GRAY);
         for (int row = 1; row < ROWS; ++row) {
            g.fillRoundRect(0, CELL_SIZE * row - GRID_WIDHT_HALF,
                  CANVAS_WIDTH-1, GRID_WIDTH, GRID_WIDTH, GRID_WIDTH);
         }
         for (int col = 1; col < COLS; ++col) {
            g.fillRoundRect(CELL_SIZE * col - GRID_WIDHT_HALF, 0,
                  GRID_WIDTH, CANVAS_HEIGHT-1, GRID_WIDTH, GRID_WIDTH);
         }
 
         Graphics2D g2d = (Graphics2D)g;
         g2d.setStroke(new BasicStroke(SYMBOL_STROKE_WIDTH, BasicStroke.CAP_ROUND,
               BasicStroke.JOIN_ROUND)); 
         for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
               int x1 = col * CELL_SIZE + CELL_PADDING;
               int y1 = row * CELL_SIZE + CELL_PADDING;
               if (board[row][col] == Seed.RED) {
                  g2d.setColor(Color.RED);
                  g2d.drawOval(x1, y1, SYMBOL_SIZE, SYMBOL_SIZE);
                  g2d.fillOval(x1, y1 ,SYMBOL_SIZE, SYMBOL_SIZE);
               } else if (board[row][col] == Seed.BLUE) {
                  g2d.setColor(Color.BLUE);
                  g2d.drawOval(x1, y1, SYMBOL_SIZE, SYMBOL_SIZE);
                  g2d.fillOval(x1, y1 ,SYMBOL_SIZE, SYMBOL_SIZE);
               }
            }
         }
 
         if (currentState == GameState.PLAYING) {
            statusBar.setForeground(Color.BLACK);
            if (currentPlayer == Seed.RED) {
               statusBar.setText("Red's Turn");
            } else {
               statusBar.setText("Blue's Turn");
            }
         } else if (currentState == GameState.DRAW) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("It's a Draw! Click to play again.");
         } else if (currentState == GameState.RED_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("'Red' Won! Click to play again.");
         } else if (currentState == GameState.BLUE_WON) {
            statusBar.setForeground(Color.RED);
            statusBar.setText("'Blue' Won! Click to play again.");
         }
      }
   }
 
   public static void main(String[] args) {
      
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            new Reversi_TwoPlayer(); 
         }
      });
    }
}
