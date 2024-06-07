import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class BlockchainGUI extends JFrame {
    private JTextArea blockchainArea;
    private JTextField newBlockData;
    private JButton addBlockButton;
    private JComboBox<String> fromUserBox;
    private JComboBox<String> toUserBox;
    private JTextField amountField;
    private JButton transactionButton;
    private JButton validateButton;

    public BlockchainGUI() {
        setTitle("Blockchain Visualizer");
        setSize(1200, 800); // Increased frame size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set Segoe UI font for the UI components
        Font segoeUIFont = new Font("Segoe UI", Font.PLAIN, 14);
        UIManager.put("Label.font", segoeUIFont);
        UIManager.put("Button.font", segoeUIFont);
        UIManager.put("TextField.font", segoeUIFont);
        UIManager.put("TextArea.font", segoeUIFont);
        UIManager.put("ComboBox.font", segoeUIFont);

        blockchainArea = new JTextArea();
        blockchainArea.setEditable(false);
        blockchainArea.setBackground(new Color(245, 245, 245)); // Light grey background for text area
        JScrollPane scrollPane = new JScrollPane(blockchainArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(245, 245, 245));

        newBlockData = new JTextField();
        addBlockButton = new JButton("Add Block");

        addBlockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String data = newBlockData.getText();
                if (!data.isEmpty()) {
                    addNewBlock(data);
                    newBlockData.setText("");
                }
            }
        });

        JPanel blockPanel = new JPanel(new BorderLayout());
        blockPanel.setBackground(new Color(173, 216, 230)); // Light blue background for panel
        blockPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        blockPanel.add(new JLabel("Enter new block data:"), BorderLayout.WEST);
        blockPanel.add(newBlockData, BorderLayout.CENTER);
        blockPanel.add(addBlockButton, BorderLayout.EAST);

        fromUserBox = new JComboBox<>(Blockchain.userBalances.keySet().toArray(new String[0]));
        toUserBox = new JComboBox<>(Blockchain.userBalances.keySet().toArray(new String[0]));
        amountField = new JTextField();
        transactionButton = new JButton("Add Transaction");

        transactionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fromUser = (String) fromUserBox.getSelectedItem();
                String toUser = (String) toUserBox.getSelectedItem();
                int amount;
                try {
                    amount = Integer.parseInt(amountField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid amount.");
                    return;
                }
                addTransaction(fromUser, toUser, amount);
                amountField.setText("");
            }
        });

        JPanel transactionPanel = new JPanel(new GridLayout(3, 2));
        transactionPanel.setBackground(new Color(224, 255, 255)); // Light cyan background for panel
        transactionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        transactionPanel.add(new JLabel("From:"));
        transactionPanel.add(fromUserBox);
        transactionPanel.add(new JLabel("To:"));
        transactionPanel.add(toUserBox);
        transactionPanel.add(new JLabel("Amount:"));
        transactionPanel.add(amountField);

        JPanel transactionContainer = new JPanel(new BorderLayout());
        transactionContainer.setBackground(new Color(175, 238, 238)); // Pale turquoise background for container
        transactionContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        transactionContainer.add(transactionPanel, BorderLayout.CENTER);
        transactionContainer.add(transactionButton, BorderLayout.SOUTH);

        validateButton = new JButton("Validate Blockchain");
        validateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validateBlockchain();
            }
        });
        validateButton.setBackground(new Color(144, 238, 144)); // Light green background for button
        validateButton.setOpaque(true);
        validateButton.setBorderPainted(false);

        JPanel validatePanel = new JPanel(new BorderLayout());
        validatePanel.setBackground(new Color(240, 255, 240)); // Honeydew background for panel
        validatePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        validatePanel.add(validateButton, BorderLayout.CENTER);

        add(scrollPane, BorderLayout.CENTER);
        add(blockPanel, BorderLayout.NORTH);
        add(transactionContainer, BorderLayout.WEST);
        add(validatePanel, BorderLayout.SOUTH);

        Blockchain.initializeUsers();
        fromUserBox.setModel(new DefaultComboBoxModel<>(Blockchain.userBalances.keySet().toArray(new String[0])));
        toUserBox.setModel(new DefaultComboBoxModel<>(Blockchain.userBalances.keySet().toArray(new String[0])));

        displayBlockchain();
    }

    private void addNewBlock(String data) {
        Block newBlock = new Block(data, Blockchain.blockchain.get(Blockchain.blockchain.size() - 1).hash);
        Blockchain.blockchain.add(newBlock);
        newBlock.mineBlock(Blockchain.difficulty);
        displayBlockchain();
    }

    private void addTransaction(String fromUser, String toUser, int amount) {
        Blockchain.addTransaction(fromUser, toUser, amount);
        displayBlockchain();
    }

    private void displayBlockchain() {
        blockchainArea.setText("");
        for (Block block : Blockchain.blockchain) {
            blockchainArea.append("Block:\n");
            blockchainArea.append("Index: " + Blockchain.blockchain.indexOf(block) + "\n");
            blockchainArea.append("Timestamp: " + new Date(block.getTimeStamp()) + "\n");
            blockchainArea.append("Data: " + block.getData() + "\n");
            blockchainArea.append("Hash: " + block.getHash() + "\n");
            blockchainArea.append("Previous Hash: " + block.getPreviousHash() + "\n\n");
        }
    }

    private void validateBlockchain() {
        boolean isValid = Blockchain.isChainValid();
        JOptionPane.showMessageDialog(this, "Blockchain is " + (isValid ? "valid" : "invalid"));
    }

    public static void main(String[] args) {
        Blockchain.blockchain.add(new Block("Genesis Block", "0"));
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BlockchainGUI().setVisible(true);
            }
        });
    }
}
