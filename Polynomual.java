/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package polynomual;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Aslan
 */
public class Polynomual {

    private List<Nomual> nomuals;

    public Polynomual(List<Nomual> nomuals) {
        this.nomuals = nomuals;
    }

    public Polynomual(String poly) {
        nomuals = new ArrayList<Nomual>();

        poly = poly.toLowerCase().replace("-", "+-");
        var perAdds = poly.split("\\+");

        for (var peradd : perAdds) {
            var perPows = peradd.split("\\^");

            int ceoff = Integer.parseInt(perPows[0].replaceAll("[()x]", ""));

            int pow = 0;

            if (perPows.length > 1) {
                pow = Integer.parseInt(perPows[1].replace("[()]", ""));
            }

            nomuals.add(new Nomual(pow, ceoff));
        }
    }

    public List<Nomual> getNomuals() {
        return this.nomuals;
    }

    public int getDegree() {
        return nomuals.size();
//        return (int) (Arrays.stream(coeffs).filter(x -> x != 0).count());
    }

    @Override
    public String toString() {
        var sen = "";

        for (int i = 0; i < this.nomuals.size(); i++) {
            var nom = nomuals.get(i);

            if (nom.getCeoff() == 0) {
                continue;
            }

            sen += nom.getCeoff() > 0 ? nom.getCeoff() + "" : "(" + nom.getCeoff() + ")";

            sen += nomuals.get(i).getPow() == 0
                    ? "" : "x^" + (nom.getPow() > 0
                    ? nom.getPow() + "" : "(" + nom.getPow() + ")");

            sen += "+";
        }
        sen = sen.substring(0, sen.length() - 1);
        return sen;
    }

    public double evaluate(double x) {
        var result = 0;

        for (int i = 0; i < this.nomuals.size(); i++) {
            result += this.nomuals.get(i).getCeoff() * Math.pow(x, this.nomuals.get(i).getPow());
        }
        return result;
    }

    public Polynomual add(Polynomual right) {
        var nomuals = new ArrayList<Nomual>();

        var rightNomuals = right.getNomuals();

        var jNomual = Stream.concat(this.nomuals.stream(), rightNomuals.stream());

//        Map<Integer, List<Nomual>> groupedNomuals = jNomual.collect(Collectors.groupingBy(Nomual::getCeoff));
        Map<Integer, List<Nomual>> groupedNomuals = jNomual.collect(Collectors.groupingBy(n -> n.getPow()));

        for (var groupedNomual : groupedNomuals.entrySet()) {
            var ggNomuals = groupedNomual.getValue();

            var resultNomual = new Nomual(groupedNomual.getKey(), 0);

            for (var ggNomual : ggNomuals) {
                resultNomual.setCeoff(resultNomual.getCeoff() + ggNomual.getCeoff());
            }

            nomuals.add(resultNomual);
        }

        return new Polynomual(nomuals);
    }

    public Polynomual remove(Polynomual right) {
        var nomuals = new ArrayList<Nomual>();

        var rightNomuals = right.getNomuals();

        var jNomual = Stream.concat(this.nomuals.stream(), rightNomuals.stream());

//        Map<Integer, List<Nomual>> groupedNomuals = jNomual.collect(Collectors.groupingBy(Nomual::getCeoff));
        Map<Integer, List<Nomual>> groupedNomuals = jNomual.collect(Collectors.groupingBy(n -> n.getPow()));

        for (var groupedNomual : groupedNomuals.entrySet()) {
            var ggNomuals = groupedNomual.getValue();

            var resultNomual = new Nomual(groupedNomual.getKey(), 0);

            for (var ggNomual : ggNomuals) {
                resultNomual.setCeoff(resultNomual.getCeoff() - ggNomual.getCeoff());
            }

            nomuals.add(resultNomual);
        }

        return new Polynomual(nomuals);
    }

    public Polynomual multiply(Polynomual right) {
        var nomuals = new ArrayList<Nomual>();

        var rightNomuals = right.getNomuals();

        var jNomual = Stream.concat(this.nomuals.stream(), rightNomuals.stream());

//        Map<Integer, List<Nomual>> groupedNomuals = jNomual.collect(Collectors.groupingBy(Nomual::getCeoff));
        Map<Integer, List<Nomual>> groupedNomuals = jNomual.collect(Collectors.groupingBy(n -> n.getPow()));

        for (var groupedNomual : groupedNomuals.entrySet()) {
            var ggNomuals = groupedNomual.getValue();

            var resultNomual = new Nomual(0, 1);

            for (var ggNomual : ggNomuals) {
                resultNomual.setCeoff(resultNomual.getCeoff() * ggNomual.getCeoff());

                resultNomual.setPow(resultNomual.getPow() + ggNomual.getPow());
            }

            nomuals.add(resultNomual);
        }

        return new Polynomual(nomuals);
    }

    public Polynomual devide(Polynomual right) throws ArithmeticException {
        var nomuals = new ArrayList<Nomual>();

        var rightNomuals = right.getNomuals();

        var jNomual = Stream.concat(this.nomuals.stream(), rightNomuals.stream());

//        Map<Integer, List<Nomual>> groupedNomuals = jNomual.collect(Collectors.groupingBy(Nomual::getCeoff));
        Map<Integer, List<Nomual>> groupedNomuals = jNomual.collect(Collectors.groupingBy(n -> n.getPow()));

        for (var groupedNomual : groupedNomuals.entrySet()) {
            var ggNomuals = groupedNomual.getValue();

            var resultNomual = new Nomual(0, 1);

            for (var ggNomual : ggNomuals) {
                if (ggNomual.getCeoff() == 0 || resultNomual.getCeoff() == 0) {
                    throw new ArithmeticException("Division by zero at pow with " + ggNomual.getPow() + " in nomuals.");
                }
                resultNomual.setCeoff(resultNomual.getCeoff() / ggNomual.getCeoff());

                if (ggNomuals.indexOf(ggNomual) == 0) {
                    resultNomual.setPow(ggNomual.getPow());
                } else {
                    resultNomual.setPow(resultNomual.getPow() - ggNomual.getPow());
                }
            }

            nomuals.add(resultNomual);
        }

        return new Polynomual(nomuals);
    }
}
