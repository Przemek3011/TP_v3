package checkers.client;

import checkers.Game.*;
import javax.swing.*;
import java.awt.*;
/**
 * class that creates visual interface for clients
 */
public class BoardGUI extends JFrame {
    private JTextField inputField;
    private JButton sendButton;
    private int numberOfPlayers;
    private JPanel boardPanel;         // Panel for the game board
    private JTextArea messagesArea;    // Single area for ALL messages (server + client)
    private Client client;
    private Board board;              

    public BoardGUI(Client client, int numberOfPlayers) {
        this.numberOfPlayers=numberOfPlayers;
        this.client = client;
        this.board = new Board();
        board.createBoard(numberOfPlayers);
        // Set up the frame
        setTitle("Checkers Game");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ---- Bottom panel for input (text field + send button) ----
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        sendButton = new JButton("Send");
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        // ---- Main center panel (holds the board in center, messages area on right) ----
        JPanel mainPanel = new JPanel(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);

        
        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(17, 25));
        initializeBoard();
        mainPanel.add(boardPanel, BorderLayout.CENTER);

        // Single text area on the right for all messages
        messagesArea = new JTextArea();
        messagesArea.setEditable(false);
        messagesArea.setLineWrap(true);
        messagesArea.setWrapStyleWord(true);
        JScrollPane messagesScrollPane = new JScrollPane(messagesArea);
        messagesScrollPane.setPreferredSize(new Dimension(300, 10)); 
        mainPanel.add(messagesScrollPane, BorderLayout.EAST);

        setVisible(true);

        // Send button action: send whatever is typed
        sendButton.addActionListener(e -> sendMessage());
        // Pressing ENTER in the inputField also triggers send
        inputField.addActionListener(e -> sendMessage());
    }

    /**
     * creating 1st board gui (not important)
     */
    private void initializeBoard() {
        int[][] boardData = board.getBoard(); // For example, a 17x25 int[][]
        for (int i = 0; i < boardData.length; i++) {
            for (int j = 0; j < boardData[i].length; j++) {
                JPanel cell = new JPanel();
                cell.setBackground(getColorForCell(boardData[i][j]));
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                boardPanel.add(cell);
            }
        }
    }

    /**
     * updating board 
     * @param newBoard updated board
     */
    public void updateBoard(int[][] newBoard) {
        boardPanel.removeAll();
        for (int i = 0; i < newBoard.length; i++) {
            for (int j = 0; j < newBoard[i].length; j++) {
                JPanel cell = new JPanel();
                cell.setBackground(getColorForCell(newBoard[i][j]));
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                boardPanel.add(cell);
            }
        }
        boardPanel.revalidate();
        boardPanel.repaint();
    }

   /**
    * send message to chat area
    * @param message message
    */
    public void appendMessage(String message) {
        messagesArea.append(message + "\n");
        messagesArea.setCaretPosition(messagesArea.getDocument().getLength());
    }

    /**
     * fill board with colors
     * @param value - value of piece on each cell
     * @return color 
     */
    private Color getColorForCell(int value) {
        final Color Light_RED=new Color(255,160,160);
        final Color Light_BLUE=new Color(51,204,255);
        final Color Light_GREEN=new Color(102,255,102);
        final Color Light_YELLOW=new Color(255,255,153);
        final Color Light_ORANGE=new Color(255,153,0);
        final Color Light_PINK=new Color(250,218,221);
        switch (value) {
            case 1: return Color.WHITE;  // Example: empty
            case 2: return Color.RED;    
            case 3: return Color.BLUE;   
            case 4: return Color.GREEN;  
            case 5: return Color.YELLOW; 
            case 6: return Color.ORANGE; 
            case 7: return Color.PINK; 
            case 8: return Light_RED;    
            case 9: return Light_BLUE;   
            case 10: return Light_GREEN;  
            case 11: return Light_YELLOW; 
            case 12: return Light_ORANGE; 
            case 13: return Light_PINK;  
            default: return Color.LIGHT_GRAY; 
        }
    }

    /**
     * send message 
     */
    private void sendMessage() {
        String message = inputField.getText().trim();
        if (!message.isEmpty()) {
            client.sendMessageToServer(message);
            inputField.setText("");
        }
    }
}
