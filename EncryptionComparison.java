import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.*;
import java.util.Base64;

public class EncryptionComparison {
    public static void main(String[] args) throws Exception {
        String plainText = "Innovation distinguishes between a leader and a follower.";

        // ---------- AES (Symmetric) ----------
        System.out.println("AES Encryption (Symmetric)");
        KeyGenerator aesKeyGen = KeyGenerator.getInstance("AES");
        aesKeyGen.init(128);
        SecretKey aesKey = aesKeyGen.generateKey();
        Cipher aesCipher = Cipher.getInstance("AES");

        long startAES = System.nanoTime();
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] aesEncrypted = aesCipher.doFinal(plainText.getBytes());
        long endAES = System.nanoTime();

        aesCipher.init(Cipher.DECRYPT_MODE, aesKey);
        String aesDecrypted = new String(aesCipher.doFinal(aesEncrypted));

        System.out.println("Ciphertext (Base64): " + Base64.getEncoder().encodeToString(aesEncrypted));
        System.out.println("Decrypted Text: " + aesDecrypted);
        System.out.println("Execution Time: " + (endAES - startAES) / 1_000_000.0 + " ms");
        System.out.println("-----------------------------------------------------");

        // ---------- RSA (Asymmetric) ----------
        System.out.println("RSA Encryption (Asymmetric)");
        KeyPairGenerator rsaKeyGen = KeyPairGenerator.getInstance("RSA");
        rsaKeyGen.initialize(2048);
        KeyPair rsaPair = rsaKeyGen.generateKeyPair();
        PublicKey publicKey = rsaPair.getPublic();
        PrivateKey privateKey = rsaPair.getPrivate();
        Cipher rsaCipher = Cipher.getInstance("RSA");

        long startRSA = System.nanoTime();
        rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] rsaEncrypted = rsaCipher.doFinal(plainText.getBytes());
        long endRSA = System.nanoTime();

        rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
        String rsaDecrypted = new String(rsaCipher.doFinal(rsaEncrypted));

        System.out.println("Ciphertext (Base64): " + Base64.getEncoder().encodeToString(rsaEncrypted));
        System.out.println("Decrypted Text: " + rsaDecrypted);
        System.out.println("Execution Time: " + (endRSA - startRSA) / 1_000_000.0 + " ms");
        System.out.println("-----------------------------------------------------");

        // ---------- Comparison ----------
        System.out.println("\nPerformance Comparison:");
        System.out.printf("%-15s %-15s %-15s%n", "Algorithm", "Key Size", "Speed (ms)");
        System.out.printf("%-15s %-15s %-15.4f%n", "AES", "128-bit", (endAES - startAES) / 1_000_000.0);
        System.out.printf("%-15s %-15s %-15.4f%n", "RSA", "2048-bit", (endRSA - startRSA) / 1_000_000.0);

        System.out.println("\nObservation:");
        System.out.println("- AES is faster but requires secret key sharing.");
        System.out.println("- RSA is slower but offers stronger key management.");
        System.out.println("- Real systems often combine both (Hybrid Encryption).");
    }
}