/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package polynomual;

import java.util.Arrays;

/**
 *
 * @author Aslan
 */
public class main {

    public static void main(String[] args) {
        var poly = new Polynomual("5x^3+3x^4-6X^2");
        var poly2 = new Polynomual("5x^3+3x^4-6X^2");

        System.out.println("Polynomual:");
        System.out.println(poly.toString());

        System.out.println("\nEvaluate with 2:");
        System.out.println(poly.evaluate(2));

        System.out.println("\nDegree:");
        System.out.println(poly.getDegree());

        System.out.println("\nAdd poly2:");
        System.out.println(poly.add(poly2).toString());

        System.out.println("\nRemove poly2:");
        System.out.println(poly.remove(poly2).toString());

        System.out.println("\nMultiply poly2:");
        System.out.println(poly.multiply(poly2).toString());

        System.out.println("\nDevide poly2:");
        try {
            System.out.println(poly.devide(poly2).toString());
        } catch (ArithmeticException e) {
            System.err.println("Cannot divide by zero: " + e.getMessage());
        }

//        System.out.println("\nCoeffs:");
//        System.out.println(Arrays.toString(poly.getCoeffs()));
    }
}
