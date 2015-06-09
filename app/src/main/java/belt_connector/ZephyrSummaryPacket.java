package belt_connector;


public class ZephyrSummaryPacket {

    private int sequenceNumber;

    private int timestampYear;

    private int timestampMonth;

    private int timestampDay;

    private int timestampMilliseconds;

    private int versionNumber;

    private int heartRate;

    private int respirationRate;

    private int skinTemperature;

    private int posture;

    private int activity;

    private int peakAcceleration;

    private int batteryVoltage;

    private int batteryLevel;

    private int breathingWaveAmplitude;

    private int breathingWaveNoise;

    private int breathingRateConfidence;

    private int ecgAmplitude;

    private int ecgNoise;

    private int heartRateConfidence;

    private int heartRateVariability;

    private int systemConfidence;

    private int gsr;

    private int rog;

    private int verticalAxisAccelerationMin;

    private int verticalAxisAccelerationPeak;

    private int lateralAxisAccelerationMin;

    private int lateralAxisAccelerationPeak;

    private int sagittalAxisAccelerationMin;

    private int sagittalAxisAccelerationPeak;

    private int deviceInternalTemp;

    private int statusInfo;

    private int linkQuality;

    private int rssi;

    private int txPower;

    private int estimatedCoreTemperature;

    private int auxiliaryChannel1;

    private int auxiliaryChannel2;

    private int auxiliaryChannel3;

    private int reserved;

    // Initialise l'objet
    public void initialize(byte[] bytes) {
        float x = bytes[54] & (bytes[55] << 8);

        System.out.println("======================");
        System.out.println("DIT : " + x);
        System.out.println("======================");

        // 1 byte data
        setSequenceNumber(bytes[0]);
        setTimestampMonth(bytes[3]);
        setTimestampDay(bytes[4]);
        setVersionNumber(bytes[9]);
        setBatteryLevel(bytes[24]);
        setBreathingRateConfidence(bytes[29]);
        setHeartRateConfidence(bytes[34]);
        setSystemConfidence(bytes[37]);
        setLinkQuality(bytes[58]);
        setRssi(bytes[59]);
        setTxPower(bytes[60]);

        // 2 byte data

        // 4 byte data
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

    public int getVersionNumber() {
        return versionNumber;
    }

    private void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public int getHeartRate() {
        return heartRate;
    }

    private void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public int getRespirationRate() {
        return respirationRate;
    }

    private void setRespirationRate(int respirationRate) {
        this.respirationRate = respirationRate;
    }

    public int getSkinTemperature() {
        return skinTemperature;
    }

    private void setSkinTemperature(int skinTemperature) {
        this.skinTemperature = skinTemperature;
    }

    public int getPosture() {
        return posture;
    }

    private void setPosture(int posture) {
        this.posture = posture;
    }

    public int getActivity() {
        return activity;
    }

    private void setActivity(int activity) {
        this.activity = activity;
    }

    public int getPeakAcceleration() {
        return peakAcceleration;
    }

    private void setPeakAcceleration(int peakAcceleration) {
        this.peakAcceleration = peakAcceleration;
    }

    public int getBatteryVoltage() {
        return batteryVoltage;
    }

    private void setBatteryVoltage(int batteryVoltage) {
        this.batteryVoltage = batteryVoltage;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    private void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    public int getBreathingWaveAmplitude() {
        return breathingWaveAmplitude;
    }

    private void setBreathingWaveAmplitude(int breathingWaveAmplitude) {
        this.breathingWaveAmplitude = breathingWaveAmplitude;
    }

    public int getBreathingWaveNoise() {
        return breathingWaveNoise;
    }

    private void setBreathingWaveNoise(int breathingWaveNoise) {
        this.breathingWaveNoise = breathingWaveNoise;
    }

    public int getBreathingRateConfidence() {
        return breathingRateConfidence;
    }

    private void setBreathingRateConfidence(int breathingRateConfidence) {
        this.breathingRateConfidence = breathingRateConfidence;
    }

    public int getEcgAmplitude() {
        return ecgAmplitude;
    }

    private void setEcgAmplitude(int ecgAmplitude) {
        this.ecgAmplitude = ecgAmplitude;
    }

    public int getEcgNoise() {
        return ecgNoise;
    }

    private void setEcgNoise(int ecgNoise) {
        this.ecgNoise = ecgNoise;
    }

    public int getHeartRateConfidence() {
        return heartRateConfidence;
    }

    private void setHeartRateConfidence(int heartRateConfidence) {
        this.heartRateConfidence = heartRateConfidence;
    }

    public int getHeartRateVariability() {
        return heartRateVariability;
    }

    private void setHeartRateVariability(int heartRateVariability) {
        this.heartRateVariability = heartRateVariability;
    }

    public int getSystemConfidence() {
        return systemConfidence;
    }

    private void setSystemConfidence(int systemConfidence) {
        this.systemConfidence = systemConfidence;
    }

    public int getGsr() {
        return gsr;
    }

    private void setGsr(int gsr) {
        this.gsr = gsr;
    }

    public int getRog() {
        return rog;
    }

    private void setRog(int rog) {
        this.rog = rog;
    }

    public int getVerticalAxisAccelerationMin() {
        return verticalAxisAccelerationMin;
    }

    private void setVerticalAxisAccelerationMin(int verticalAxisAccelerationMin) {
        this.verticalAxisAccelerationMin = verticalAxisAccelerationMin;
    }

    public int getVerticalAxisAccelerationPeak() {
        return verticalAxisAccelerationPeak;
    }

    private void setVerticalAxisAccelerationPeak(int verticalAxisAccelerationPeak) {
        this.verticalAxisAccelerationPeak = verticalAxisAccelerationPeak;
    }

    public int getLateralAxisAccelerationMin() {
        return lateralAxisAccelerationMin;
    }

    private void setLateralAxisAccelerationMin(int lateralAxisAccelerationMin) {
        this.lateralAxisAccelerationMin = lateralAxisAccelerationMin;
    }

    public int getLateralAxisAccelerationPeak() {
        return lateralAxisAccelerationPeak;
    }

    private void setLateralAxisAccelerationPeak(int lateralAxisAccelerationPeak) {
        this.lateralAxisAccelerationPeak = lateralAxisAccelerationPeak;
    }

    public int getSagittalAxisAccelerationMin() {
        return sagittalAxisAccelerationMin;
    }

    private void setSagittalAxisAccelerationMin(int sagittalAxisAccelerationMin) {
        this.sagittalAxisAccelerationMin = sagittalAxisAccelerationMin;
    }

    public int getSagittalAxisAccelerationPeak() {
        return sagittalAxisAccelerationPeak;
    }

    private void setSagittalAxisAccelerationPeak(int sagittalAxisAccelerationPeak) {
        this.sagittalAxisAccelerationPeak = sagittalAxisAccelerationPeak;
    }

    public int getDeviceInternalTemp() {
        return deviceInternalTemp;
    }

    private void setDeviceInternalTemp(int deviceInternalTemp) {
        this.deviceInternalTemp = deviceInternalTemp;
    }

    public int getStatusInfo() {
        return statusInfo;
    }

    private void setStatusInfo(int statusInfo) {
        this.statusInfo = statusInfo;
    }

    public int getLinkQuality() {
        return linkQuality;
    }

    private void setLinkQuality(int linkQuality) {
        this.linkQuality = linkQuality;
    }

    public int getRssi() {
        return rssi;
    }

    private void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public int getTxPower() {
        return txPower;
    }

    private void setTxPower(int txPower) {
        this.txPower = txPower;
    }

    public int getEstimatedCoreTemperature() {
        return estimatedCoreTemperature;
    }

    private void setEstimatedCoreTemperature(int estimatedCoreTemperature) {
        this.estimatedCoreTemperature = estimatedCoreTemperature;
    }

    public int getAuxiliaryChannel1() {
        return auxiliaryChannel1;
    }

    private void setAuxiliaryChannel1(int auxiliaryChannel1) {
        this.auxiliaryChannel1 = auxiliaryChannel1;
    }

    public int getAuxiliaryChannel2() {
        return auxiliaryChannel2;
    }

    private void setAuxiliaryChannel2(int auxiliaryChannel2) {
        this.auxiliaryChannel2 = auxiliaryChannel2;
    }

    public int getAuxiliaryChannel3() {
        return auxiliaryChannel3;
    }

    private void setAuxiliaryChannel3(int auxiliaryChannel3) {
        this.auxiliaryChannel3 = auxiliaryChannel3;
    }

    public int getReserved() {
        return reserved;
    }

    private void setReserved(int reserved) {
        this.reserved = reserved;
    }
}
