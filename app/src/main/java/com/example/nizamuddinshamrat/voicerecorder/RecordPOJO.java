package com.example.nizamuddinshamrat.voicerecorder;

public class RecordPOJO {

    String recordName;
    String recordSize;
    String recordDuration;
    String recordPath;

    public RecordPOJO(String recordName, String recordSize, String recordDuration, String recordPath) {
        this.recordName = recordName;
        this.recordSize = recordSize;
        this.recordDuration = recordDuration;
        this.recordPath = recordPath;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public String getRecordPath() {
        return recordPath;
    }

    public String getRecordName() {
        return recordName;
    }

    public String getRecordSize() {
        return recordSize;
    }

    public String getRecordDuration() {
        return recordDuration;
    }


}
