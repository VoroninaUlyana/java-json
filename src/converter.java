public class converter {
    public int letterToNumber(char letter) {
        char lowerCase = Character.toLowerCase(letter);
        if (lowerCase >= 'a' && lowerCase <= 'z') {
            return lowerCase - 'a' + 1;
        }
        return -1;
    }
    public boolean isEnglishLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }
    public String formatNumber(int number) {
        String numberStr = String.valueOf(number);
        if (number < 10) {
            return numberStr + " ";
        }
        return numberStr;
    }
    public int getFormattedNumberLength(int number) {
        if (number < 10) {
            return 2;
        }
        return String.valueOf(number).length();
    }
}