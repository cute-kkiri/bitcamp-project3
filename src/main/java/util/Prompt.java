package util;

import java.util.Scanner;

public class Prompt {

    static Scanner keyboardScanner = new Scanner(System.in);

    public static String input(String format, Object... args) {
        String promptTitle = String.format(format + " ", args);
        System.out.print(promptTitle);

        return keyboardScanner.nextLine();
    }

    public static int inputInt(String format, Object... args) {
        return Integer.parseInt(input(format, args));
    }

    public static void close() {
        keyboardScanner.close();
    }
}
