package belt_connector;

public class ZephyrSummaryPacket extends ZephyrPacket {

    private int sequenceNumber;

    private int timestampYear;

    private int timestampMonth;

    private int timestampDay;

    private int timestampMilliseconds;

    private int versionNumber;

    private int heartRate;

    private float respirationRate;

    private float skinTemperature;

    private int posture;

    private float activity;

    private float peakAcceleration;

    private float batteryVoltage;

    private int batteryLevel;

    private int breathingWaveAmplitude;

    private int breathingWaveNoise;

    private int breathingRateConfidence;

    private float ecgAmplitude;

    private float ecgNoise;

    private int heartRateConfidence;

    private int heartRateVariability;

    private int systemConfidence;

    private int gsr;

    private int rog;

    private float verticalAxisAccelerationMin;

    private float verticalAxisAccelerationPeak;

    private float lateralAxisAccelerationMin;

    private float lateralAxisAccelerationPeak;

    private float sagittalAxisAccelerationMin;

    private float sagittalAxisAccelerationPeak;

    private float deviceInternalTemp;

    private int statusInfo;

    private int linkQuality;

    private int rssi;

    private int txPower;

    private float estimatedCoreTemperature;

    private int auxiliaryChannel1;

    private int auxiliaryChannel2;

    private int auxiliaryChannel3;

    private int reserved;

    @Override
    public void initialize(byte[] bytes) {
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
        setTimestampYear(twoBytesToInt(bytes[2], bytes[1]));
        setHeartRate(twoBytesToInt(bytes[11], bytes[10]));
        setRespirationRate(twoBytesToFloat(bytes[13], bytes[12]) / 10);
        setSkinTemperature(twoBytesToFloat(bytes[15], bytes[14]) / 10);
        setPosture(twoBytesToInt(bytes[17], bytes[17]));
        setActivity(twoBytesToFloat(bytes[19], bytes[18]) / 100);
        setPeakAcceleration(twoBytesToFloat(bytes[21], bytes[20]) / 100);
        setBatteryVoltage(twoBytesToFloat(bytes[23], bytes[22]) / 1000);
        setBreathingWaveAmplitude(twoBytesToInt(bytes[26], bytes[25]));
        setBreathingWaveNoise(twoBytesToInt(bytes[28], bytes[27]));
        setEcgAmplitude(twoBytesToFloat(bytes[31], bytes[30]) / 1000000);
        setEcgNoise(twoBytesToFloat(bytes[33], bytes[32]) / 1000000);
        setHeartRateVariability(twoBytesToInt(bytes[36], bytes[35]));
        setGsr(twoBytesToInt(bytes[39], bytes[38]));
        setRog(twoBytesToInt(bytes[41], bytes[40]));
        setVerticalAxisAccelerationMin(twoBytesToFloat(bytes[43], bytes[42]) / 100);
        setVerticalAxisAccelerationPeak(twoBytesToFloat(bytes[45], bytes[44]) / 100);
        setLateralAxisAccelerationMin(twoBytesToFloat(bytes[47], bytes[46]) / 100);
        setLateralAxisAccelerationPeak(twoBytesToFloat(bytes[49], bytes[48]) / 100);
        setSagittalAxisAccelerationMin(twoBytesToFloat(bytes[51], bytes[50]) / 100);
        setSagittalAxisAccelerationPeak(twoBytesToFloat(bytes[53], bytes[52]) / 100);
        setDeviceInternalTemp(twoBytesToFloat(bytes[55], bytes[54]) / 10);
        setStatusInfo(twoBytesToInt(bytes[57], bytes[56]));
        setEstimatedCoreTemperature(twoBytesToFloat(bytes[62], bytes[61]) / 10);
        setAuxiliaryChannel1(twoBytesToInt(bytes[64], bytes[63]));
        setAuxiliaryChannel2(twoBytesToInt(bytes[66], bytes[65]));
        setAuxiliaryChannel3(twoBytesToInt(bytes[68], bytes[67]));
        setReserved(twoBytesToInt(bytes[70], bytes[69]));

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

    public float getRespirationRate() {
        return respirationRate;
    }

    private void setRespirationRate(float respirationRate) {
        this.respirationRate = respirationRate;
    }

    public float getSkinTemperature() {
        return skinTemperature;
    }

    private void setSkinTemperature(float skinTemperature) {
        this.skinTemperature = skinTemperature;
    }

    public int getPosture() {
        return posture;
    }

    private void setPosture(int posture) {
        this.posture = posture;
    }

    public float getActivity() {
        return activity;
    }

    private void setActivity(float activity) {
        this.activity = activity;
    }

    public float getPeakAcceleration() {
        return peakAcceleration;
    }

    private void setPeakAcceleration(float peakAcceleration) {
        this.peakAcceleration = peakAcceleration;
    }

    public float getBatteryVoltage() {
        return batteryVoltage;
    }

    private void setBatteryVoltage(float batteryVoltage) {
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

    public float getEcgAmplitude() {
        return ecgAmplitude;
    }

    private void setEcgAmplitude(float ecgAmplitude) {
        this.ecgAmplitude = ecgAmplitude;
    }

    public float getEcgNoise() {
        return ecgNoise;
    }

    private void setEcgNoise(float ecgNoise) {
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

    public float getVerticalAxisAccelerationMin() {
        return verticalAxisAccelerationMin;
    }

    private void setVerticalAxisAccelerationMin(float verticalAxisAccelerationMin) {
        this.verticalAxisAccelerationMin = verticalAxisAccelerationMin;
    }

    public float getVerticalAxisAccelerationPeak() {
        return verticalAxisAccelerationPeak;
    }

    private void setVerticalAxisAccelerationPeak(float verticalAxisAccelerationPeak) {
        this.verticalAxisAccelerationPeak = verticalAxisAccelerationPeak;
    }

    public float getLateralAxisAccelerationMin() {
        return lateralAxisAccelerationMin;
    }

    private void setLateralAxisAccelerationMin(float lateralAxisAccelerationMin) {
        this.lateralAxisAccelerationMin = lateralAxisAccelerationMin;
    }

    public float getLateralAxisAccelerationPeak() {
        return lateralAxisAccelerationPeak;
    }

    private void setLateralAxisAccelerationPeak(float lateralAxisAccelerationPeak) {
        this.lateralAxisAccelerationPeak = lateralAxisAccelerationPeak;
    }

    public float getSagittalAxisAccelerationMin() {
        return sagittalAxisAccelerationMin;
    }

    private void setSagittalAxisAccelerationMin(float sagittalAxisAccelerationMin) {
        this.sagittalAxisAccelerationMin = sagittalAxisAccelerationMin;
    }

    public float getSagittalAxisAccelerationPeak() {
        return sagittalAxisAccelerationPeak;
    }

    private void setSagittalAxisAccelerationPeak(float sagittalAxisAccelerationPeak) {
        this.sagittalAxisAccelerationPeak = sagittalAxisAccelerationPeak;
    }

    public float getDeviceInternalTemp() {
        return deviceInternalTemp;
    }

    private void setDeviceInternalTemp(float deviceInternalTemp) {
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

    public float getEstimatedCoreTemperature() {
        return estimatedCoreTemperature;
    }

    private void setEstimatedCoreTemperature(float estimatedCoreTemperature) {
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
