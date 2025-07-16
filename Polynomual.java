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

        poly = poly.toLowerCase().replace("-", "+-").replace(" ", "");
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

            if (i != 0) {
                sen += nom.getCeoff() > 0 ? "+" : "";
            }

            if (nom.getCeoff() == 0) {
                continue;
            }

            sen += true ? nom.getCeoff() + "" : "(" + nom.getCeoff() + ")";

            sen += nomuals.get(i).getPow() == 0
                    ? "" : "x^" + (nom.getPow() > 0
                    ? nom.getPow() + "" : "(" + nom.getPow() + ")");
        }
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
        Map<Integer, Double> termMap = new java.util.HashMap<>();

        for (Nomual term : this.nomuals) {
            termMap.put(term.getPow(), termMap.getOrDefault(term.getPow(), 0.0) + term.getCeoff());
        }

        for (Nomual term : right.getNomuals()) {
            termMap.put(term.getPow(), termMap.getOrDefault(term.getPow(), 0.0) - term.getCeoff());
        }

        List<Nomual> resultNomuals = termMap.entrySet().stream()
                .filter(e -> e.getValue() != 0)
                .map(e -> new Nomual(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        return new Polynomual(resultNomuals);
    }

    public Polynomual multiply(Polynomual right) {
        List<Nomual> resultNomuals = new ArrayList<>();

        for (Nomual leftTerm : this.nomuals) {
            for (Nomual rightTerm : right.getNomuals()) {
                int newPow = leftTerm.getPow() + rightTerm.getPow();
                double newCoeff = leftTerm.getCeoff() * rightTerm.getCeoff();
                resultNomuals.add(new Nomual(newPow, newCoeff));
            }
        }

        Map<Integer, Double> combinedTerms = new java.util.HashMap<>();
        for (Nomual term : resultNomuals) {
            combinedTerms.put(
                    term.getPow(),
                    combinedTerms.getOrDefault(term.getPow(), 0.0) + term.getCeoff()
            );
        }

        List<Nomual> finalNomuals = combinedTerms.entrySet().stream()
                .filter(e -> e.getValue() != 0)
                .map(e -> new Nomual(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        return new Polynomual(finalNomuals);
    }

    public Polynomual devide(Polynomual right) throws ArithmeticException {
        List<Nomual> nomuals = this.nomuals;

        var rightNomuals = right.getNomuals();

        var jNomual = this.nomuals.stream();
        var rNomual = right.getNomuals();

//        Map<Integer, List<Nomual>> groupedNomuals = jNomual.collect(Collectors.groupingBy(Nomual::getCeoff));
//        Map<Integer, List<Nomual>> groupedNomuals = jNomual.collect(Collectors.groupingBy(n -> n.getPow()));
        nomuals.sort((a, b) -> b.getPow() - a.getPow());
        rNomual.sort((a, b) -> b.getPow() - a.getPow());

        List<Nomual> quotientList = new ArrayList<>();

        while (!nomuals.isEmpty() && nomuals.get(0).getPow() >= right.getNomuals().get(0).getPow()) {
            var leadingDividend = nomuals.get(0);
            var leadingDivisor = rNomual.get(0);

            if (leadingDivisor.getCeoff() == 0) {
                throw new ArithmeticException("Division by zero at pow with  in nomuals.");
            }

            int newPow = leadingDividend.getPow() - leadingDivisor.getPow();
            double newCoeff = leadingDividend.getCeoff() / leadingDivisor.getCeoff();

            var term = new Nomual(newPow, newCoeff);

            quotientList.add(term);

            var subtraction = new Polynomual(List.of(term)).multiply(right);

            var updateDivindend = new Polynomual(nomuals).remove(subtraction);

            nomuals = updateDivindend.getNomuals();
            nomuals.removeIf(n -> n.getCeoff() == 0);
            nomuals.sort((a, b) -> b.getPow() - a.getPow());
        }

        return new Polynomual(quotientList);

    }
}
