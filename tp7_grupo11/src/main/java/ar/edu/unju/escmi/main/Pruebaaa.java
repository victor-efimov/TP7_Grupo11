package ar.edu.unju.escmi.main;

import ar.edu.unju.escmi.config.EmfSingleton;

public class Pruebaaa {
    public static void main(String[] args) {
        System.out.println(EmfSingleton.getInstance().getEmf().isOpen());
    }
}