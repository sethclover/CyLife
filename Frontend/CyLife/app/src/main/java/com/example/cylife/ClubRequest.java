package com.example.cylife;

public class ClubRequest {
    private int requestId;
    private int studentId;
    private String clubName;
    private String description;
    private String clubEmail;
    private String status;

    public ClubRequest(int requestId, int studentId, String clubName, String description, String clubEmail, String status) {
        this.requestId = requestId;
        this.studentId = studentId;
        this.clubName = clubName;
        this.description = description;
        this.clubEmail = clubEmail;
        this.status = status;
    }

    public int getRequestId() {
        return requestId;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getClubName() {
        return clubName;
    }

    public String getDescription() {
        return description;
    }

    public String getClubEmail() {
        return clubEmail;
    }

    public String getStatus() {
        return status;
    }
}
