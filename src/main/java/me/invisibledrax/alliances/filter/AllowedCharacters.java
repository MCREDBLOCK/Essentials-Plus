package me.invisibledrax.alliances.filter;

import java.util.ArrayList;

public class AllowedCharacters {

    private static ArrayList<Character> allowed = allowedCharacters();

    private static ArrayList<Character> allowedCharacters() {
        ArrayList<Character> list = new ArrayList<>();
        list.add('a');
        list.add('b');
        list.add('c');
        list.add('d');
        list.add('e');
        list.add('f');
        list.add('g');
        list.add('h');
        list.add('i');
        list.add('j');
        list.add('k');
        list.add('l');
        list.add('m');
        list.add('n');
        list.add('o');
        list.add('p');
        list.add('q');
        list.add('r');
        list.add('s');
        list.add('t');
        list.add('u');
        list.add('v');
        list.add('w');
        list.add('x');
        list.add('y');
        list.add('z');
        list.add('1');
        list.add('2');
        list.add('3');
        list.add('4');
        list.add('5');
        list.add('6');
        list.add('7');
        list.add('8');
        list.add('9');
        list.add('0');
        return list;
    }

    public static boolean allowed(String s) {
        for (Character c : s.toCharArray()) {
            if (!allowed.contains(c)) {
                return false;
            }
        }
        return true;
    }

}
