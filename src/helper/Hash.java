package helper;

public class Hash {
    /**
     * @param value    string
     * @param hashType MD5 OR SHA1
     * @return string hash of txt
     */
    public static String getHash(String value, String hashType) {
        String hash = null;
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance(hashType);
            byte[] array = md.digest(value.getBytes());
            StringBuilder stringBuffer = new StringBuilder();
            for (byte anArray : array) {
                stringBuffer.append(Integer.toHexString((anArray & 0xFF) | 0x100).substring(1, 3));
            }
            hash = stringBuffer.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash;
    }

    public static String md5(String value) {
        return Hash.getHash(value, "MD5");
    }

    public static String sha1(String value) {
        return Hash.getHash(value, "SHA1");
    }
}
