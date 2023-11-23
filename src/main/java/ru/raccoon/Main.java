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

        Thread thread_length3 = new Thread(() -> {
            for (String str :
                    texts) {
                if (str.length() == 3 && isBeautyWord(str)) {
                    counter_length3.getAndAdd(1);
                }
            }
        });
        thread_length3.start();

        Thread thread_length4 = new Thread(() -> {
            for (String str :
                    texts) {
                if (str.length() == 4 && isBeautyWord(str)) {
                    counter_length4.getAndAdd(1);
                }
            }
        });
        thread_length4.start();

        Thread thread_length5 = new Thread(() -> {
            for (String str :
                    texts) {
                if (str.length() == 5 && isBeautyWord(str)) {
                    counter_length5.getAndAdd(1);
                }
            }
        });
        thread_length5.start();

        //подождём, пока посчитаются "красивые" слова всех длин
        thread_length3.join();
        thread_length4.join();
        thread_length5.join();

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
    public static boolean isBeautyWord(String word) {

        //проверяем на вхождение в слово только одной буквы (проверку выполняем первой, так как это ещё и палиндром)
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

        //проверяем, палиндром ли
        StringBuilder originalWord = new StringBuilder(word);
        String invertedWord = originalWord.reverse().toString();
        //если палиндром, то слово "красивое"
        if (word.equals(invertedWord)) {
            return true;
        }

        //проверяем на "по-алфавитность"
        counter = 1;
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
        //если нигде раньше не вернули результат, то слово "некрасивое"
        return false;
    }
}