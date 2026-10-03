package unnkk.math;

import java.math.BigInteger;

public class Fraction implements Comparable<Fraction>{
    private BigInteger numerator, denominator;
    private boolean autoReduce = true;

    public Fraction(BigInteger numerator, BigInteger denominator, boolean autoReduce){
        this.numerator = numerator;
        this.denominator = denominator;
        this.autoReduce = autoReduce;

        if (autoReduce) reduce();
    }

    public Fraction(BigInteger numerator, BigInteger denominator){
        this.numerator = numerator;
        this.denominator = denominator;

        reduce();
    }
    public Fraction(BigInteger value){
        this(value, BigInteger.ONE);
    }

    public Fraction(){
        this(BigInteger.ZERO, BigInteger.ONE);
    }

    public Fraction(String s) {
        s = s.trim();
        if(s.contains("/")){
            s = s.substring(1, s.length() - 1);
            String[] parts = s.split("/");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Неверный формат дроби: " + s);
            }

            numerator = new BigInteger(parts[0].trim());
            denominator = new BigInteger(parts[1].trim());

            if(this.denominator.equals(BigInteger.ZERO)) throw new ArithmeticException("Деление на ноль");
            reduce();
        }else{
            numerator = new BigInteger(s);
            denominator = BigInteger.ONE;
        }
    }

    public Fraction(Fraction fraction) {
        this.numerator = new BigInteger(fraction.numerator.toByteArray());
        this.denominator = new BigInteger(fraction.denominator.toByteArray());
        this.autoReduce = fraction.autoReduce;
    }


    //OPERATION AREA

    public Fraction plus(Fraction summand){
        BigInteger numerator = this.numerator.multiply(summand.denominator)
                .add(summand.numerator.multiply(this.denominator));
        Fraction result = new Fraction(numerator,
                this.denominator.multiply(summand.denominator),
                this.autoReduce || summand.autoReduce);

        if (result.autoReduce) result.reduce();

        return result;
    }

    public Fraction subtract(Fraction subtrahend){
        BigInteger numerator = this.numerator.multiply(subtrahend.denominator)
                .subtract(subtrahend.numerator.multiply(this.denominator));
        Fraction result = new Fraction(numerator,
                this.denominator.multiply(subtrahend.denominator),
                this.autoReduce || subtrahend.autoReduce);

        if (result.autoReduce) result.reduce();

        return result;
    }

    public Fraction multiply(Fraction multiplier){
        return new Fraction(this.numerator.multiply(multiplier.numerator),
            this.denominator.multiply(multiplier.denominator),
            this.autoReduce || multiplier.autoReduce);
    }
    
    public Fraction multiply(long multiplier){
        return new Fraction(this.numerator.multiply(BigInteger.valueOf(multiplier)), this.denominator, this.autoReduce);
    }
    
    public Fraction divide(Fraction divisor){
        return new Fraction(this.numerator.multiply(divisor.denominator),
            this.denominator.multiply(divisor.numerator),
            this.autoReduce || divisor.autoReduce);
    }
    
    public Fraction divide(long divisor){
        return new Fraction(this.numerator,
            this.denominator.multiply(BigInteger.valueOf(divisor)),
            this.autoReduce);
    }

    private void reduce() {
        BigInteger gcd = this.numerator.gcd(this.denominator);

        numerator = numerator.divide(gcd);
        denominator = denominator.divide(gcd);
        if (denominator.signum() < 0) {
            numerator = numerator.negate();
            denominator = denominator.negate();
        }
    }


    //OVERRIDE AREA

    @Override
    public String toString() {
        String str = "";

        if(!this.denominator.abs().equals(BigInteger.ONE)) return str + "(" +
                this.numerator + "/" + this.denominator +
                ")";
        else return str + this.numerator;
    }

    @Override
    public int compareTo(Fraction compared) {
        return this.numerator.multiply(compared.denominator)
                .compareTo(compared.numerator.multiply(this.denominator));
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj.getClass() == Fraction.class){
            Fraction o = (Fraction) obj;
            return o.numerator.multiply(this.denominator).equals(this.numerator.multiply(o.denominator));
        } else return false;
    }

    //GETTER/SETTER AREA

    //Getters

    public BigInteger getNumerator() {
        return numerator;
    }

    public BigInteger getDenominator() {
        return denominator;
    }

    public double getDouble(){
        return numerator.doubleValue() / denominator.doubleValue();
    }

    public double getAbs(){
        return java.lang.Math.abs(getDouble());
    }

    public Fraction negate() {
        Fraction result = new Fraction(this);
        result.numerator = result.numerator.negate();
        return result;
    }

    //Setters

    public void setAutoReduce(boolean autoReduce) {
        this.autoReduce = autoReduce;
    }
}
