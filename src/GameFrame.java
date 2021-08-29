import javax.swing.JFrame;

public class GameFrame extends JFrame{
	GameFrame(){
		 this.add(new GamePanel());								
		 this.setTitle("Snake Game");							//To set the title of the Frame
		 this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//Exits the application on initiating "close" on the Frame
		 this.setResizable(false);								//To prevent the user from resizing the frame
		 this.pack();											//To make the Window size to fit is subcomponents preferred size and layouts 
		 this.setVisible(true);									//To show the Window
		 this.setLocationRelativeTo(null);						//To set the location of the Window on the center of the screen
		 
	}
}
