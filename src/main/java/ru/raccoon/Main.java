package ru.raccoon;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    //счётчики "красивых" слов разной длины
    static AtomicInteger counter_length3 = new AtomicInteger();
    static AtomicInteger counter_length4 = new AtomicInteger();
    static AtomicInteger counter_length5 = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        //запускаем потоки с подсчётами

        Thread threadCase1 = new Thread(() -> {
            for (String str :
                    texts) {
                if (isCase_1(str)) {
                    if (str.length() == 3) {
                        counter_length3.getAndAdd(1);
                    }
                    if (str.length() == 4) {
                        counter_length4.getAndAdd(1);
                    }
                    if (str.length() == 5) {
                        counter_length5.getAndAdd(1);
                    }
                }
            }
        });
        threadCase1.start();

        Thread threadCase2 = new Thread(() -> {
            for (String str :
                    texts) {
                if (isCase_2(str)) {
                    if (str.length() == 3) {
                        counter_length3.getAndAdd(1);
                    }
                    if (str.length() == 4) {
                        counter_length4.getAndAdd(1);
                    }
                    if (str.length() == 5) {
                        counter_length5.getAndAdd(1);
                    }
                }
            }
        });
        threadCase2.start();

        Thread threadCase3 = new Thread(() -> {
            for (String str :
                    texts) {
                if (isCase_3(str)) {
                    if (str.length() == 3) {
                        counter_length3.getAndAdd(1);
                    }
                    if (str.length() == 4) {
                        counter_length4.getAndAdd(1);
                    }
                    if (str.length() == 5) {
                        counter_length5.getAndAdd(1);
                    }
                }
            }
        });
        threadCase3.start();

        //подождём, пока посчитаются "красивые" слова всех длин
        threadCase1.join();
        threadCase2.join();
        threadCase3.join();

        //результат
        System.out.println("Красивых слов с длиной 3: " + counter_length3 + " шт.");
        System.out.println("Красивых слов с длиной 4: " + counter_length4 + " шт.");
        System.out.println("Красивых слов с длиной 5: " + counter_length5 + " шт.");

    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    //метод проверки слова на "красивость"
    public static boolean isCase_1(String word) {

        //проверяем на вхождение в слово только одной буквы
        int counter = 1;
        for (int i = 1; i < word.length(); i++) {
            //если символы равны
            if (word.charAt(0) == word.charAt(i)) {
                //итерируем счётчик
                counter++;
                //и если все символы одинаковые
                if (counter == word.length()) {
                    //то слово "красивое"
                    return true;
                }
            }
        }
        return false;
    }

        public static boolean isCase_2(String word) {

            //проверяем - не палиндром ли
            StringBuilder originalWord = new StringBuilder(word);
            String invertedWord = originalWord.reverse().toString();
            //если палиндром, то слово "красивое"
            if (word.equals(invertedWord)) {
                return true;
            }
            return false;
        }

    public static boolean isCase_3(String word) {
        //проверяем на по-алфавитность
        int counter = 1;
        for (int i = 0; i < word.length() - 1; i++) {
            //если последующий символ больше или равен предыдущему
            if (word.charAt(i) <= word.charAt(i + 1)) {
                //итерируем счётчик
                counter++;
                //если условие выполняется для всех символов слова
                if (counter == word.length()) {
                    //то слово "красивое"
                    return true;
                }
            }
        }
        return false;
    }
}