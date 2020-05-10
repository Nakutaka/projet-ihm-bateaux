package com.example.project.data.model.incident;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;

import com.example.project.data.model.Incident;
import com.example.project.data.model.Info;

@Entity
public class BasicIncident extends Incident implements Parcelable {
    private String level;
    private String comment;

    @Ignore
    public BasicIncident(int num, String level, String comment) {
        super(comment, level);
        this.num = num;
        this.info = null;
        this.level = level;
        this.comment = comment;
    }

    public BasicIncident(String reportId, int num, Info info, String level, String comment) {
        this.reportId = reportId;
        this.num = num;
        this.info = info;
        this.level = level;
        this.comment = comment;
    }

    private BasicIncident(Parcel in) {
        super(in);
        level = in.readString();
        comment = in.readString();
    }

    public static final Creator<BasicIncident> CREATOR = new Creator<BasicIncident>() {
        @Override
        public BasicIncident createFromParcel(Parcel in) {
            return new BasicIncident(in);
        }

        @Override
        public BasicIncident[] newArray(int size) {
            return new BasicIncident[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(level);
        dest.writeString(comment);
    }


    public String getLevel() {
        return level;
    }

    public String getComment() {
        return comment;
    }
}
