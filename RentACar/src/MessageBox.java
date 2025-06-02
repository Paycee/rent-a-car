import javax.swing.*;
import java.awt.*;

public class MessageBox extends JFrame{
    private JPanel messagePanel;
    private JTextArea messageArea;

    public MessageBox(String massage) {


        this.setSize(300, 300);
        this.setLayout(new FlowLayout());
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        messageArea.setEditable(false);
        messageArea.setCursor(null);
        messageArea.setOpaque(false);
        messageArea.setFocusable(false);

        messageArea.setText(massage);

        this.setContentPane(messagePanel);
    }
}
