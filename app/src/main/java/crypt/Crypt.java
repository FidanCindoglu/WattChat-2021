package crypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Crypt {
    crypt.Permutation permutationObject=new crypt.Permutation();
    crypt.Caesar caesarnObject=new crypt.Caesar();
    /*şifreleme metodunda gönderilecek bir mesaj metnine ilk olarak Permutasyon şifreleme ardından Sezar şifreleme
    işlemleri uygulanır. Şifre çözme metodunda ise tam tersi olarak önce Sezar ardından Permutasyon şifre çözme
     işlemleri uygulanır. */
    /* Şifreleme metodunda öncelikle şifrelenecek mesajdaki boşluk karakterleri ile '£' karakteri yer değiştirir.
    Ardından 1-9 arası rastgele iki sayı şifrelenecek metninbaşına ve sonuna yerleştirilir.
    * Şifrelenecek metnin uzunluğu, şifreleme işleminde kullanılacak olan aahtarın uzunluğunun katı olmadı gerektiğinden
    modu alınır ve eklenmesi gereken sayı oranında şifrelenecek metne ' (tırnak) karakteri eklenir. Ardından metin değeri
    Permutasyon şifreleme metoduna sokulur. metoddan çıkan şifreli metin,12 karakter kaydırma işlemi için Sezar şifreleme metoduna sokulur
     */
    public String encrypt(String data) {
        String permutationKey = "743682519";
        String encryptedText = "";

        try {
            if (data.length()!=0) {
                String unSpacedplainText = data.replaceAll("\\s+", "£");
                Random rand = new Random();
                int n = rand.nextInt(9)+1;
                int m = rand.nextInt(9)+1;
                unSpacedplainText =n+unSpacedplainText+m;
                int mod =(permutationKey.length()-(unSpacedplainText.length()%permutationKey.length()))%permutationKey();
                while (mod != 0 ) {
                    unSpacedplainText += "'";
                    mod-=1;
                }
                String encryptedText1=permutationObject.encryptedData(unSpacedplainText,permutationKey);
                encryptedText1=encryptedText1.toLowerCase();
                encryptedText=caesarnObject.encryption(encryptedText1, 12);
            }
        } catch (Exception e) {}
        return encryptedText;
    }

    private int permutationKey() {
        return 0;
    }


    /*Şifre çözme metoduda ise önce Sezar şifreleme yöntemi ile (alfabe sayısı - 12 ) karakter kaydırma işlemi
    uygulayarak şifreli metin karakterleri eski yerlerine geri getirdik. Ardından elde edilen şifreli metin, Permutasyon
    şifre çözme işlemi için çözücü anahtar değeri ile yeniden şifreleme metoduna gönderilir ve açık metin elde edilir.
    Açık metindeki £ karakterlerinin yerlerine boşluk karakteri eklenir. Metnin varsa eğer mod işlem, sırasında eklediğimiz
    '(tırnak) karakterleri çıkartılır. Açık metnin başında ve sonunda şifreleme işlemi sırasında eklediğimiz rastgele sayılar
    kalacaktır. Bu sayı değerleri de açık metinden çıkartılarak mesajın ilk halini elde edeceğiz.
     */
        public String decrypt (String data){
            String permutationDeKey = "863274159";
            String decryptedText = "";
            try {
                decryptedText = caesarnObject.encryption(data, caesarnObject.alphabet.length() - 12);
                decryptedText = permutationObject.encryptedData(decryptedText, permutationDeKey);
                decryptedText = decryptedText.replace("£", "");
                decryptedText = decryptedText.replace("'", "");
                decryptedText.trim();
                if (tryParseInt(decryptedText.substring(0, 1)) && tryParseInt(decryptedText.substring(decryptedText.length() - 1))) {
                    decryptedText = decryptedText.substring(1);
                    decryptedText = decryptedText.substring(0, decryptedText.length() - 1);
                } else
                    decryptedText = "";
            } catch (Exception e) {
                decryptedText = "";
            }
            return decryptedText;
        }
    /* kendisine gelen değerin sayı olup olmadığını kontrol eder.*/
    private static boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
    /* Kendisine gelen değerin özetini çıkarır. Bu metod Sign Up işleminde kullanıcı şifresini çıkartmak için kullanılır.*/
    public String md5Coder(String textToHash) {
        String generatedPassword=null;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes=md.digest(textToHash.getBytes());

            StringBuilder sb=new StringBuilder();
            for (int i=0; i<bytes.length; i++)
                sb.append(Integer.toString((bytes[i] & 0xff)+0x100, 16).substring(1));
            generatedPassword=sb.toString();
        } catch (NoSuchAlgorithmException e) {}
        return generatedPassword;
    }
}
