package crypt;

public class Permutation {
    /* Şifrelenecek mesaj bloklara ayrılır ve şifreleme metoduna gönderilir.*/
    public String encryptedData(String data, String key) {
        String encryptedText="";
        while (data.length()!=0) {
            String block= null;
            block=data.substring(0, key.length());
            data=data.substring(key.length());
            encryptedText+=encrypt(block, key);
        }
        return encryptedText;
    }
    /* Anahtar değerinin sıralanmasına göre şifreli yeni blok değerleri elde edilir.*/
    private String encrypt(String data, String key) {
        int keyValue=0;
        String cipherText="";
        for (int i=0; i<data.length(); i++) {
            keyValue=Integer.parseInt(String.valueOf(key.charAt(i)));
            cipherText+=data.charAt(keyValue-1);
        }
        return cipherText;
    }
}
