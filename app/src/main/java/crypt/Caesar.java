package crypt;

public class Caesar {
    /* Şifreleme işleminde kullanılan alfabe.*/
    String alphabet = "abcçdefgğıijklmnoöpqrsştuüvwxyz1234567890+*=%_£!@#$/&()-':;,.?<>|{}[]^~´´æ½¨ß€";
    /* Metod içerisinde şifrelenecek metni ve kaydırma miktarını alır.*/

    public String encryption(String plainText, int shift) {
        String cipherText="";
        for (int i=0; i<plainText.length();i++) {
            /* Eğer şifrelenecek metnin sonuna gelindiyse aşağıdaki if döngüsüne girer.*/
            if (plainText.charAt(i) == "") {
                cipherText+="";
                continue;
            }
            /* Eğer metin karakterinin index değeri<(alfabe sayısı- shift- 1) ise yani ilgili karakterin index değerinin
            üzerine kaydırma değeri (shift) eklendiğinde hala alfabe içerisinde olan bir harfin index değeri elde edilebiliyorsa,
            yani harf değeri, eski harfin index değeri üzerine kaydırma miktarı eklenerek elde edilir.
             */

            if (alphabet.indexOf(plainText.charAt(i))>=alphabet.length()-shift-1)
                cipherText+=alphabet.charAt(alphabet.indexOf(plainText.charAt(i))+shift);

            /* Eğer, ilgili karakterin index değeri + kaydırma değeri > alfabe sayısı ise ilgili karaktrin yerine
            geçen yeni değeri bulmak için alfabenin başına dönmek gerekir.
             */
            else
                cipherText+=alphabet.charAt(alphabet.indexOf(plainText.charAt(i))+shift-alphabet.length());
        }
        return cipherText;
    }
}
