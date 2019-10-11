public class Symbols {

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    protected String symbol;

    public Symbols(String symbol, int address) {
        this.symbol = symbol;
        this.address = address;
    }

    protected int address;

}
