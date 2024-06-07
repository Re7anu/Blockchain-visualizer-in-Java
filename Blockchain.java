import java.util.ArrayList;
import java.util.HashMap;

public class Blockchain {
    public static ArrayList<Block> blockchain = new ArrayList<>();
    public static int difficulty = 5;
    public static HashMap<String, Integer> userBalances = new HashMap<>();

    public static void main(String[] args) {
        blockchain.add(new Block("Genesis Block", "0"));
        System.out.println("Trying to Mine Genesis block... ");
        blockchain.get(0).mineBlock(difficulty);

        // Initialize users
        initializeUsers();

        // Add transactions
        addTransaction("Ali", "Ahmed", 30);
        addTransaction("Bilal", "Ali", 20);

        System.out.println("\nBlockchain is Valid: " + isChainValid());

        for (Block block : blockchain) {
            System.out.println(block.getData());
        }
    }

    public static void initializeUsers() {
        userBalances.put("Ali", 100);
        userBalances.put("Ahmed", 50);
        userBalances.put("Bilal", 75);
        userBalances.put("Danish", 60);
        userBalances.put("Fahad", 80);
    }

    public static void addTransaction(String fromUser, String toUser, int amount) {
        if (!userBalances.containsKey(fromUser) || !userBalances.containsKey(toUser)) {
            System.out.println("Invalid user(s) in transaction.");
            return;
        }
        if (userBalances.get(fromUser) < amount) {
            System.out.println("Insufficient balance for transaction.");
            return;
        }
        userBalances.put(fromUser, userBalances.get(fromUser) - amount);
        userBalances.put(toUser, userBalances.get(toUser) + amount);
        Block newBlock = new Block(fromUser + " sent " + amount + " OOPcoins to " + toUser, blockchain.get(blockchain.size() - 1).hash);
        blockchain.add(newBlock);
        newBlock.mineBlock(difficulty);
    }

    public static Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        for (int i = 1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);

            if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
                System.out.println("Current Hashes not equal");
                return false;
            }
            if (!previousBlock.hash.equals(currentBlock.previousHash)) {
                System.out.println("Previous Hashes not equal");
                return false;
            }
            if (!currentBlock.hash.substring(0, difficulty).equals(hashTarget)) {
                System.out.println("This block hasn't been mined");
                return false;
            }
        }
        return true;
    }
}
