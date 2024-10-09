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
        System.out.println("Tax for (RO,100,100) = " + service.calculateCustomsTax(parse("RO")));
        System.out.println("Tax for (CN,100,100) = " + service.calculateCustomsTax(parse("CN")));
        System.out.println("Tax for (UK,100,100) = " + service.calculateCustomsTax(parse("In")));
//        IllegalArgumentException: No enum constant victor.training.java.patterns.strategy.CountryEnum.ID
//        But that exception occurs at the boundary when you pass.
//        When you interpret the data when you construct the structure that you want to work with, not in the core logic that you have.
//        System.out.println("Tax for (UK,100,100) = " + service.calculateCustomsTax(parse("ID")));

    }

    private static Parcel parse(String fromAJson) {
        return new Parcel(CountryEnum.valueOf(fromAJson.toUpperCase()), 100, 100, LocalDate.now());
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
        return switch (parcel.originCountry()) {// switch expression (returns a value) no break needed
            case UK -> new UKTaxCalculator().calculateTax(parcel);
            case CN,IN -> new ChinaTaxCalculator().calculateTax(parcel);
            case FR, ES, RO -> new EUTaxCalculator().calculateTax(parcel);
            // compilation failer if you DON'T cover all ENUM values, you must come with well-behaved enum, build failed if you add a new enum value and you don't cover it
//            default -> throw new IllegalArgumentException("Not a valid country ISO2 code: " + parcel.originCountry());
            //default a bad practice if you use switch as an expression on an ENUM
        };
    }
}

class EUTaxCalculator{
    public double calculateTax(Parcel parcel) {
        return parcel.tobaccoValue() / 3;
    }
}
class ChinaTaxCalculator{
    public double calculateTax(Parcel parcel) {
        return parcel.tobaccoValue() + parcel.regularValue();
    }
}

class UKTaxCalculator {
    public double calculateTax(Parcel parcel) {
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

