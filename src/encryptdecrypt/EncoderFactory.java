package encryptdecrypt;


public class EncoderFactory {
    private String type;

    public EncoderFactory(String type) {
        this.type = type;
    }

    public EncoderDecoder getInstance() {
        if ("unicode".equals(type)) {
            return new UnicodeEncoderDecoder();
        }
        return new ShiftEncoderDecoder();
    }
}

abstract class EncoderDecoder {

    abstract public String encode(String data, int offset);

    abstract public String decode(String data, int offset);

}


class ShiftEncoderDecoder extends EncoderDecoder {

    public String encode(String data, int offset) {
        char[] resultArray = new char[data.length()];

        for (int i = 0; i < data.length(); i++) {
            char current = data.charAt(i);
            if (Character.isLetter(current)  && Character.isLowerCase(current)) {
                resultArray[i] = (char) ( ((int) current - 97 + offset) % 26 + 97 );
            } else if (Character.isLetter(current) && Character.isUpperCase(current)) {
                resultArray[i] = (char) ( ((int) current - 65 + offset) % 26 + 65 );
            }
            else {
                resultArray[i] = current;
            }
        }

        return new String(resultArray);
    }

    public String decode(String data, int offset) {
        return encode(data, 26 - offset);
    }
}


class UnicodeEncoderDecoder extends EncoderDecoder {

    public String encode(String data, int offset) {
        char c;
        int n;
        final StringBuilder output = new StringBuilder();

        for (int i = 0; i < data.length(); i++) {
            c = data.charAt(i);
            n = (int) c + offset;
            output.append((char) n);
        }
        return output.toString();
    }

    public String decode(String data, int offset) {
        return encode(data, -offset);
    }
}
