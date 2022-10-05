package kz.optimabank.optima24.utility;

public class CardNumberUtils {

    public static String getMaskedCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() == 0) {
            return "";
        }
        if (cardNumber.length() != 19) {
            return cardNumber;
        }
        StringBuilder builder = new StringBuilder(cardNumber);
        builder.replace(4, 5, "-");
        builder.replace(7, 15, "xx-xxxx-");
        return builder.toString();
    }

    // проверка карты с помощью алгоритма Луны
    public static boolean isCardNumberCorrectWithLuhn(String cardNumber) {
        int digits = cardNumber.length();
        int sum = 0;
        boolean isSecond = false;
        for (int i = digits - 1; i >= 0; i--) {
            int d = cardNumber.charAt(i) - '0';
            if (isSecond)
                d = d * 2;

            sum += d / 10;
            sum += d % 10;

            isSecond = !isSecond;
        }
        return (sum % 10 == 0);
    }
}

