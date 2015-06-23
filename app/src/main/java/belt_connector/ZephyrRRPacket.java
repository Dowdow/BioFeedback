package belt_connector;

public class ZephyrRRPacket extends ZephyrPacket {

    private int sequenceNumber;

    private int timestampYear;

    private int timestampMonth;

    private int timestampDay;

    private int timestampMilliseconds;

    private int rToRSample0;

    private int rToRSample1;

    private int rToRSample2;

    private int rToRSample3;

    private int rToRSample4;

    private int rToRSample5;

    private int rToRSample6;

    private int rToRSample7;

    private int rToRSample8;

    private int rToRSample9;

    private int rToRSample10;

    private int rToRSample11;

    private int rToRSample12;

    private int rToRSample13;

    private int rToRSample14;

    private int rToRSample15;

    private int rToRSample16;

    private int rToRSample17;

    private int avgRToRSample;

    @Override
    public void initialize(byte[] bytes) {
        // 1 byte data
        setSequenceNumber(bytes[0]);
        setTimestampMonth(bytes[3]);
        setTimestampDay(bytes[4]);

        // 2 byte data
        setTimestampYear(twoBytesToInt(bytes[2], bytes[1]));
        setRToRSample0(twoBytesToInt(bytes[10], bytes[9]));
        setRToRSample1(twoBytesToInt(bytes[12], bytes[11]));
        setRToRSample2(twoBytesToInt(bytes[14], bytes[13]));
        setRToRSample3(twoBytesToInt(bytes[16], bytes[15]));
        setRToRSample4(twoBytesToInt(bytes[18], bytes[17]));
        setRToRSample5(twoBytesToInt(bytes[20], bytes[19]));
        setRToRSample6(twoBytesToInt(bytes[22], bytes[21]));
        setRToRSample7(twoBytesToInt(bytes[24], bytes[23]));
        setRToRSample8(twoBytesToInt(bytes[26], bytes[25]));
        setRToRSample9(twoBytesToInt(bytes[28], bytes[27]));
        setRToRSample10(twoBytesToInt(bytes[30], bytes[29]));
        setRToRSample11(twoBytesToInt(bytes[32], bytes[31]));
        setRToRSample12(twoBytesToInt(bytes[34], bytes[33]));
        setRToRSample13(twoBytesToInt(bytes[36], bytes[35]));
        setRToRSample14(twoBytesToInt(bytes[38], bytes[37]));
        setRToRSample15(twoBytesToInt(bytes[40], bytes[39]));
        setRToRSample16(twoBytesToInt(bytes[42], bytes[41]));
        setRToRSample17(twoBytesToInt(bytes[44], bytes[43]));

        // 4 byte data

        // Calculating average
        int sum = rToRSample0 + rToRSample1 + rToRSample2 + rToRSample3 + rToRSample4 + rToRSample5 + rToRSample6 + rToRSample7 + rToRSample8
                + rToRSample9 + rToRSample10 + rToRSample11 + rToRSample12 + rToRSample13 + rToRSample14 + rToRSample15 + rToRSample16 + rToRSample17;
        setAvgRToRSample(sum / 18);
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    private void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public int getTimestampYear() {
        return timestampYear;
    }

    private void setTimestampYear(int timestampYear) {
        this.timestampYear = timestampYear;
    }

    public int getTimestampMonth() {
        return timestampMonth;
    }

    private void setTimestampMonth(int timestampMonth) {
        this.timestampMonth = timestampMonth;
    }

    public int getTimestampDay() {
        return timestampDay;
    }

    private void setTimestampDay(int timestampDay) {
        this.timestampDay = timestampDay;
    }

    public int getTimestampMilliseconds() {
        return timestampMilliseconds;
    }

    private void setTimestampMilliseconds(int timestampMilliseconds) {
        this.timestampMilliseconds = timestampMilliseconds;
    }

    public int getRToRSample0() {
        return rToRSample0;
    }

    private void setRToRSample0(int rToRSample0) {
        this.rToRSample0 = rToRSample0;
    }

    public int getRToRSample1() {
        return rToRSample1;
    }

    private void setRToRSample1(int rToRSample1) {
        this.rToRSample1 = rToRSample1;
    }

    public int getRToRSample2() {
        return rToRSample2;
    }

    private void setRToRSample2(int rToRSample2) {
        this.rToRSample2 = rToRSample2;
    }

    public int getRToRSample3() {
        return rToRSample3;
    }

    private void setRToRSample3(int rToRSample3) {
        this.rToRSample3 = rToRSample3;
    }

    public int getRToRSample4() {
        return rToRSample4;
    }

    private void setRToRSample4(int rToRSample4) {
        this.rToRSample4 = rToRSample4;
    }

    public int getRToRSample5() {
        return rToRSample5;
    }

    private void setRToRSample5(int rToRSample5) {
        this.rToRSample5 = rToRSample5;
    }

    public int getRToRSample6() {
        return rToRSample6;
    }

    private void setRToRSample6(int rToRSample6) {
        this.rToRSample6 = rToRSample6;
    }

    public int getRToRSample7() {
        return rToRSample7;
    }

    private void setRToRSample7(int rToRSample7) {
        this.rToRSample7 = rToRSample7;
    }

    public int getRToRSample8() {
        return rToRSample8;
    }

    private void setRToRSample8(int rToRSample8) {
        this.rToRSample8 = rToRSample8;
    }

    public int getRToRSample9() {
        return rToRSample9;
    }

    private void setRToRSample9(int rToRSample9) {
        this.rToRSample9 = rToRSample9;
    }

    public int getRToRSample10() {
        return rToRSample10;
    }

    private void setRToRSample10(int rToRSample10) {
        this.rToRSample10 = rToRSample10;
    }

    public int getRToRSample11() {
        return rToRSample11;
    }

    private void setRToRSample11(int rToRSample11) {
        this.rToRSample11 = rToRSample11;
    }

    public int getRToRSample12() {
        return rToRSample12;
    }

    private void setRToRSample12(int rToRSample12) {
        this.rToRSample12 = rToRSample12;
    }

    public int getRToRSample13() {
        return rToRSample13;
    }

    private void setRToRSample13(int rToRSample13) {
        this.rToRSample13 = rToRSample13;
    }

    public int getRToRSample14() {
        return rToRSample14;
    }

    private void setRToRSample14(int rToRSample14) {
        this.rToRSample14 = rToRSample14;
    }

    public int getRToRSample15() {
        return rToRSample15;
    }

    private void setRToRSample15(int rToRSample15) {
        this.rToRSample15 = rToRSample15;
    }

    public int getRToRSample16() {
        return rToRSample16;
    }

    private void setRToRSample16(int rToRSample16) {
        this.rToRSample16 = rToRSample16;
    }

    public int getRToRSample17() {
        return rToRSample17;
    }

    private void setRToRSample17(int rToRSample17) {
        this.rToRSample17 = rToRSample17;
    }

    public int getAvgRToRSample() {
        return avgRToRSample;
    }

    private void setAvgRToRSample(int avgRToRSample) {
        this.avgRToRSample = avgRToRSample;
    }
}
