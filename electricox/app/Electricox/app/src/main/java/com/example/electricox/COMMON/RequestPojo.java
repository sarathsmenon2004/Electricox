package com.example.electricox.COMMON;

public class RequestPojo
{
    String u_id,u_name,u_phone,u_address,u_email;

    String b_id,b_name,b_phone,b_address,b_email;

    String status , userType , PlugType;

    String bunk_id,bunk_name,bunk_hours,bunk_charging_rate,wifi_availability,restroom,waiting_area,nearBy_amenities,emergency_number,onsite_support,bunk_image,bunk_address,lat,longg;

    String booking_id;
    String vehicletype;
    String licenceNumber;
    String slot_number;
    String booking_time;

    String payment_id;

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getPlugType() {
        return PlugType;
    }

    public void setPlugType(String plugType) {
        PlugType = plugType;
    }

    private String feedbackId,description,subject,rating;

    public String getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(String feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    private String paymentId;

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentPrice() {
        return paymentPrice;
    }

    public void setPaymentPrice(String paymentPrice) {
        this.paymentPrice = paymentPrice;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    private String paymentPrice;
    private String cardNumber;
    private String cvv;
    private String expiryDate;
    private String accountNumber;
    private String paymentDate;


    String QR_code;

    public String getQR_code() {
        return QR_code;
    }

    public void setQR_code(String QR_code) {
        this.QR_code = QR_code;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getVehicletype() {
        return vehicletype;
    }

    public void setVehicletype(String vehicletype) {
        this.vehicletype = vehicletype;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public String getSlot_number() {
        return slot_number;
    }

    public void setSlot_number(String slot_number) {
        this.slot_number = slot_number;
    }

    public String getBooking_time() {
        return booking_time;
    }

    public void setBooking_time(String booking_time) {
        this.booking_time = booking_time;
    }

    public String getBooking_status() {
        return booking_status;
    }

    public void setBooking_status(String booking_status) {
        this.booking_status = booking_status;
    }

    public String getQr_status() {
        return qr_status;
    }

    public void setQr_status(String qr_status) {
        this.qr_status = qr_status;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    String booking_status;
    String qr_status;
    String payment_status;
    public String getBunk_id() {
        return bunk_id;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setBunk_id(String bunk_id) {
        this.bunk_id = bunk_id;
    }

    public String getBunk_name() {
        return bunk_name;
    }

    public void setBunk_name(String bunk_name) {
        this.bunk_name = bunk_name;
    }

    public String getBunk_hours() {
        return bunk_hours;
    }

    public void setBunk_hours(String bunk_hours) {
        this.bunk_hours = bunk_hours;
    }

    public String getBunk_charging_rate() {
        return bunk_charging_rate;
    }

    public void setBunk_charging_rate(String bunk_charging_rate) {
        this.bunk_charging_rate = bunk_charging_rate;
    }

    public String getWifi_availability() {
        return wifi_availability;
    }

    public void setWifi_availability(String wifi_availability) {
        this.wifi_availability = wifi_availability;
    }

    public String getRestroom() {
        return restroom;
    }

    public void setRestroom(String restroom) {
        this.restroom = restroom;
    }

    public String getWaiting_area() {
        return waiting_area;
    }

    public void setWaiting_area(String waiting_area) {
        this.waiting_area = waiting_area;
    }

    public String getNearBy_amenities() {
        return nearBy_amenities;
    }

    public void setNearBy_amenities(String nearBy_amenities) {
        this.nearBy_amenities = nearBy_amenities;
    }

    public String getEmergency_number() {
        return emergency_number;
    }

    public void setEmergency_number(String emergency_number) {
        this.emergency_number = emergency_number;
    }

    public String getOnsite_support() {
        return onsite_support;
    }

    public void setOnsite_support(String onsite_support) {
        this.onsite_support = onsite_support;
    }

    public String getBunk_image() {
        return bunk_image;
    }

    public void setBunk_image(String bunk_image) {
        this.bunk_image = bunk_image;
    }

    public String getBunk_address() {
        return bunk_address;
    }

    public void setBunk_address(String bunk_address) {
        this.bunk_address = bunk_address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongg() {
        return longg;
    }

    public void setLongg(String longg) {
        this.longg = longg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getU_name() {
        return u_name;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }

    public String getU_phone() {
        return u_phone;
    }

    public void setU_phone(String u_phone) {
        this.u_phone = u_phone;
    }

    public String getU_address() {
        return u_address;
    }

    public void setU_address(String u_address) {
        this.u_address = u_address;
    }

    public String getU_email() {
        return u_email;
    }

    public void setU_email(String u_email) {
        this.u_email = u_email;
    }

    public String getB_id() {
        return b_id;
    }

    public void setB_id(String b_id) {
        this.b_id = b_id;
    }

    public String getB_name() {
        return b_name;
    }

    public void setB_name(String b_name) {
        this.b_name = b_name;
    }

    public String getB_phone() {
        return b_phone;
    }

    public void setB_phone(String b_phone) {
        this.b_phone = b_phone;
    }

    public String getB_address() {
        return b_address;
    }

    public void setB_address(String b_address) {
        this.b_address = b_address;
    }

    public String getB_email() {
        return b_email;
    }

    public void setB_email(String b_email) {
        this.b_email = b_email;
    }

}
