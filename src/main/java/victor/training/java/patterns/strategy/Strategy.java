package victor.training.java.patterns.strategy;

import java.time.LocalDate;

enum CountryEnum {
    RO, ES, FR, UK, CN
    ,IN
}

record Parcel(
        CountryEnum originCountry,
        double tobaccoValue,
        double regularValue,
        LocalDate date) {
}

class Plain {
    public static void main(String[] args) {
        CustomsService service = new CustomsService();
        System.out.println("Tax for (RO,100,100) = " + service.calculateCustomsTax(
                new Parcel(CountryEnum.valueOf("RO"), 100, 100, LocalDate.now())));
        System.out.println("Tax for (CN,100,100) = " + service.calculateCustomsTax(
                new Parcel(CountryEnum.valueOf("CN"), 100, 100, LocalDate.now())));
        System.out.println("Tax for (UK,100,100) = " + service.calculateCustomsTax(
                new Parcel(CountryEnum.valueOf("IN"), 100, 100, LocalDate.now())));
    }
}

//@Service
//@Data
//@ConfigurationProperties(prefix = "customs")
class CustomsService {
    //	private Map<String, Class<? extends TaxCalculator>> calculators; // configured in application.properties 😮
    //bl: parcel is a box that you can ship from one country to another by boat or plane
    //bl: the tax is calculated based on the country of origin of the parcel
    public double calculateCustomsTax(Parcel parcel) { // UGLY API we CANNOT change
        // a switch on a string is a code smell
        // despite the fact that that string appears to have a finite number of values
        // we have it as a String=> primitive obsession
//        switch (parcel.originCountry()) { // statement (does not return a value) this is a code smell
//            case UK:
//                return calculateUKTax(parcel);
//            case CN:
//                return calculateChinaTax(parcel);
//            case FR:
//            case ES: // other EU country codes...
//            case RO:
//                return calculateEUTax(parcel);
//            default: // this it's been with us since the 80s
//                // it's time to let go. it results in a runtime error
//                // that could have been a compile time error (EARLIER💖)
//                throw new IllegalArgumentException("Not a valid country ISO2 code: " + parcel.originCountry());
//        }
        double v = switch (parcel.originCountry()) {// switch expression (returns a value)
            case UK -> calculateUKTax(parcel);
            case CN -> calculateChinaTax(parcel); // other EU country codes...
            case FR, ES, RO -> calculateEUTax(parcel);
            // compilation failer if you DON'T cover all ENUM values, you must come with well-behaved enum, build failed if you add a new enum value and you don't cover it
//            default -> throw new IllegalArgumentException("Not a valid country ISO2 code: " + parcel.originCountry());
            //default a bad practice if you use switch as an expression on an ENUM

        };
        return v;
    }

    private static double calculateEUTax(Parcel parcel) {
        return parcel.tobaccoValue() / 3;
    }

    private static double calculateChinaTax(Parcel parcel) {
        return parcel.tobaccoValue() + parcel.regularValue();
    }

    private static double calculateUKTax(Parcel parcel) {
        // a colleague adds some more code here
        // a colleague adds some more code here
        // a colleague adds some more code here
        // a colleague adds some more code here
        // a colleague adds some more code here
        // a colleague adds some more code here
        // a colleague adds some more code here
        // a colleague adds some more code here
        // a colleague adds some more code here
        return parcel.tobaccoValue() / 2 + parcel.regularValue();
    }
}

